package picturegallery.persistency;

import gallery.RealPicture;

import java.io.File;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import picturegallery.Logic;
import picturegallery.persistency.ObjectCache.CallBack;

public class MediaRenderBaseImpl implements MediaRenderBase {
	private final ObjectCache<RealPicture, Image> cache;
	private final Pane parentNode;
	private final double width;
	private final double height;

	private Canvas canvas;
	private ImageView image;
	private MediaView media;

	private Node shownNode;

	public MediaRenderBaseImpl(ObjectCache<RealPicture, Image> cache, Pane parentNode) {
		super();
		if (cache == null) {
			throw new IllegalArgumentException();
		}
		this.cache = cache;

		if (parentNode == null) {
			throw new IllegalArgumentException();
		}
		this.parentNode = parentNode;

		width = -1;
		height = -1;
	}

	public MediaRenderBaseImpl(ObjectCache<RealPicture, Image> cache, double width, double height) {
		super();
		if (cache == null) {
			throw new IllegalArgumentException();
		}
		this.cache = cache;

		parentNode = null;
		this.width = width;
		this.height = height;
	}

	@Override
	public Canvas getCanvas() {
		if (canvas == null) {
			canvas = new Canvas();
			canvas.setCache(false);

			if (parentNode != null) {
				canvas.widthProperty().bind(parentNode.widthProperty());
				canvas.heightProperty().bind(parentNode.heightProperty());
			} else {
				canvas.setWidth(width);
				canvas.setHeight(height);
			}

			parentNode.getChildren().add(canvas);
		}
		return canvas;
	}

	@Override
	public ImageView getImageView() {
		if (image == null) {
			image = new ImageView();
			image.setPreserveRatio(true);
			image.setSmooth(true);
			// https://stackoverflow.com/questions/15003897/is-there-any-way-to-force-javafx-to-release-video-memory
			image.setCache(false);

			if (parentNode != null) {
				image.fitWidthProperty().bind(parentNode.widthProperty());
				image.fitHeightProperty().bind(parentNode.heightProperty());
			} else {
				image.setFitWidth(width);
				image.setFitHeight(height);
			}

			parentNode.getChildren().add(image);
		}
		return image;
	}

	@Override
	public MediaView getMediaView() {
		if (media == null) {
			media = new MediaView();
			media.setCache(false);
			media.setPreserveRatio(true);
			media.setSmooth(true);

			if (parentNode != null) {
				media.fitWidthProperty().bind(parentNode.widthProperty());
				media.fitHeightProperty().bind(parentNode.heightProperty());
			} else {
				media.setFitWidth(width);
				media.setFitHeight(height);
			}

			parentNode.getChildren().add(media);
		}
		return media;
	}

	@Override
	public void showCanvas() {
		canvas.setVisible(true);
		image.setVisible(false);
		media.setVisible(false);
	}

	@Override
	public void showImageView() {
		canvas.setVisible(false);
		image.setVisible(true);
		media.setVisible(false);
	}

	@Override
	public void showMediaView() {
		canvas.setVisible(false);
		image.setVisible(false);
		media.setVisible(true);
	}

	@Override
	public Node getShownNode() {
		return shownNode;
	}

	@Override
	public void renderPicture(RealPicture realPicture) {
		PictureProvider provider = new PictureProvider() {
			@Override
			public RealPicture get() {
				return realPicture;
			}
		};
		renderPicture(provider);
	}

	@Override
	public void renderPicture(PictureProvider provider) {
		RealPicture realPicture = provider.get();
		if (realPicture == null) {
			// nothing to render => draw black image
			renderPicture(provider, getImageView(), cache);
			return;
		}

		String extension = realPicture.getFileExtension();
		if (extension == null || extension.isEmpty()) {
			throw new IllegalArgumentException(realPicture.getFullPath());
		}
		extension = extension.toLowerCase();
		if (extension.equals("gif")) {
			// Gif
			renderPicture(provider, getImageView(), cache);
		} else if (extension.equals("mp4")) {
			// video
			renderPicture(provider, getMediaView());
		} else {
			// image
			renderPicture(provider, getCanvas(), cache);
		}
	}

	public static void renderPicture(PictureProvider provider, MediaView image) {
		RealPicture realCurrentPicture = provider.get();
		if (realCurrentPicture == null) {
			throw new IllegalArgumentException();
		} else {
			// https://stackoverflow.com/questions/24043420/why-does-platform-runlater-not-check-if-it-currently-is-on-the-javafx-thread
			if (Platform.isFxApplicationThread()) {
				renderMedia(realCurrentPicture, image);
			} else {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						renderMedia(realCurrentPicture, image);
					}
				});
			}
		}
	}
	/**
	 * 
	 * @param provider for the feature, that this request is out-dated after loading the picture!
	 * @param image
	 */
	public static void renderPicture(PictureProvider provider, ImageView image, ObjectCache<RealPicture, Image> cache) {
		RealPicture realCurrentPicture = provider.get();
		if (realCurrentPicture == null) {
			// show no picture
			Logic.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					image.setImage(null);
				}
			});
		} else {
			cache.request(realCurrentPicture, new CallBack<RealPicture, Image>() {
				@Override
				public void loaded(RealPicture key, Image value) {
					// https://stackoverflow.com/questions/26554814/javafx-updating-gui
					// https://stackoverflow.com/questions/24043420/why-does-platform-runlater-not-check-if-it-currently-is-on-the-javafx-thread
					if (Platform.isFxApplicationThread()) {
						renderImage(image, value, realCurrentPicture);
					} else {
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								if (key.equals(provider.get())) {
									renderImage(image, value, realCurrentPicture);
								} else {
									// ignore the result, because another picture should be shown
								}
							}
						});
					}
				}
			});
		}
	}
	/**
	 * 
	 * @param provider for the feature, that this request is out-dated after loading the picture!
	 * @param image
	 */
	public static void renderPicture(PictureProvider provider, Canvas image, ObjectCache<RealPicture, Image> cache) {
		RealPicture realCurrentPicture = provider.get();
		if (realCurrentPicture == null) {
			// show no picture
			Logic.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					renderCanvas(image, null, null);
				}
			});
		} else {
			cache.request(realCurrentPicture, new CallBack<RealPicture, Image>() {
				@Override
				public void loaded(RealPicture key, Image value) {
					// https://stackoverflow.com/questions/26554814/javafx-updating-gui
					// https://stackoverflow.com/questions/24043420/why-does-platform-runlater-not-check-if-it-currently-is-on-the-javafx-thread
					if (Platform.isFxApplicationThread()) {
						renderCanvas(image, value, realCurrentPicture);
					} else {
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								if (key.equals(provider.get())) {
									renderCanvas(image, value, realCurrentPicture);
								} else {
									// ignore the result, because another picture should be shown
								}
							}
						});
					}
				}
			});
		}
	}

	private static void renderMedia(RealPicture picture, MediaView image) {
		String picturePath = picture.getFullPath();
		// TODO: geeignet cachen!?
		String link = new File(picturePath).toURI().toString();
		Media media = null;
		try {
			media = new Media(link);
			final Media mediaFinal = media;
			media.setOnError(() -> {
				System.out.println("error:");
				if (mediaFinal.getError() != null) {
					mediaFinal.getError().printStackTrace();
				} else {
					System.err.println("error is null!");
				}
			});
		} catch (Throwable e) {
			System.err.println("loading " + picturePath + " failed because of:");
			e.printStackTrace();
		}
		MediaPlayer player = new MediaPlayer(media);
		image.setMediaPlayer(player);
//		player.play();
		player.setAutoPlay(true);
	}

	private static void renderImage(ImageView image, Image value, RealPicture picture) {
		gallery.Metadata metadata = picture.getMetadata();
		double rotate = 0.0;
		if (metadata != null) {
			if (!metadata.isLandscape()) {
				rotate = 90.0;
			}
		}
		image.setRotate(rotate);
		image.setImage(value);
	}

	private static void renderCanvas(Canvas image, Image value, RealPicture picture) {
		// draw the image in rotated way
		// https://stackoverflow.com/questions/18260421/how-to-draw-image-rotated-on-javafx-canvas
		GraphicsContext gc = image.getGraphicsContext2D();
		double availableHeight = image.getHeight();
		double availableWidth = image.getWidth();

		// print black rectangle => clear previous picture
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, availableWidth, availableHeight);

		// print image (if available)
		if (value != null) {
			double imageHeight = value.getHeight();
			double imageWidth = value.getWidth();

			double factor = 1.0;
			if (availableWidth / imageWidth * imageHeight > availableHeight) {
				// height is the limit
				factor = availableHeight / imageHeight;
			} else {
				// width is the limit
				factor = availableWidth / imageWidth;
			}
			imageHeight = imageHeight * factor;
			imageWidth = imageWidth * factor;

			gc.save(); // saves the current state on stack, including the current transform

			// get the rotation angle
			if (picture != null && picture.getMetadata() != null && !picture.getMetadata().isLandscape()) {
				double rotate = 90.0;

				Rotate r = new Rotate(rotate, availableWidth / 2.0, availableHeight / 2.0);
				gc.transform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
//				gc.rotate(rotate);

				double scale = availableHeight / availableWidth;
//				scale = 0.7; // Näherung
//				scale = imageHeight / availableHeight; // eigentlich gedacht...
				scale = imageHeight / imageWidth; // dies scheint korrekt zu sein!
				Scale s = new Scale(scale, scale, availableWidth / 2.0, availableHeight / 2.0);
				gc.transform(s.getMxx(), s.getMyx(), s.getMxy(), s.getMyy(), s.getTx(), s.getTy());
//				gc.scale(scale, scale); // factors for x, y dimension

//				double help = availableHeight;
//				availableHeight = availableWidth;
//				availableWidth = help;
//
//				double help2 = imageHeight;
//				imageHeight = imageWidth;
//				imageWidth = help2;
				
			}

			gc.drawImage(value, (availableWidth - imageWidth) / 2.0, (availableHeight - imageHeight) / 2.0,
					imageWidth, imageHeight); // top-left x, y, width, height
			
			gc.restore(); // back to original state (before rotation)
		}
	}

}
