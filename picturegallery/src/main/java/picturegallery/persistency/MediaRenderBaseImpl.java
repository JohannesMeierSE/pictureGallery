package picturegallery.persistency;

import gallery.RealPicture;

import java.io.File;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
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

	private double currentImageWidth;
	private double currentImageHeight;

	// used to set the width/height of the "node/canvas" to print on ...
	private final double width;
	private final double height;

	// used to calculate the part of the image to show on the currently used "node/canvas"
	protected double zoom = MediaRenderBase.zoomDefault;
	protected double detailX = MediaRenderBase.detailRationXDefault;
	protected double detailY = MediaRenderBase.detailRationYDefault;

	private Canvas canvas;
	private ImageView image;
	private MediaView media;

	private Node shownNode;
	private PictureProvider currentProvider;

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

		init();
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

		init();
	}

	private void init() {
		showImageView();

		if (parentNode != null) {
			/* repaint the canvas, if the size of the canvas was changed!
			 * image and media will be resized automatically
			 */
			InvalidationListener listener = new InvalidationListener() {
				@Override
				public void invalidated(Observable observable) {
					if (shownNode != null && shownNode == canvas && currentProvider != null) {
						renderPictureCanvas();
					}
				}
			};
			parentNode.widthProperty().addListener(listener);
			parentNode.heightProperty().addListener(listener);
		}
	}

	@Override
	public double getCurrentImageWidth() {
		return currentImageWidth;
	}

	@Override
	public double getCurrentImageHeight() {
		return currentImageHeight;
	}

	@Override
	public double getCurrentNodeWidth() {
		Node node = getShownNode();
		if (node == null) {
			return 0.0;
		}
		if (node instanceof ImageView) {
			return ((ImageView) node).fitWidthProperty().get();
		}
		if (node instanceof Canvas) {
			return ((Canvas) node).getWidth();
		}
		if (node instanceof MediaView) {
			return ((MediaView) node).fitWidthProperty().get();
		}
		throw new IllegalStateException("unknown node: " + node);
	}

	@Override
	public double getCurrentNodeHeight() {
		Node node = getShownNode();
		if (node == null) {
			return 0.0;
		}
		if (node instanceof ImageView) {
			return ((ImageView) node).fitHeightProperty().get();
		}
		if (node instanceof Canvas) {
			return ((Canvas) node).getHeight();
		}
		if (node instanceof MediaView) {
			return ((MediaView) node).fitHeightProperty().get();
		}
		throw new IllegalStateException("unknown node: " + node);
	}

	@Override
	public void setZoom(double zoom) {
		this.zoom = zoom;
		repaint();
	}

	@Override
	public void setDetailX(double detailX) {
		this.detailX = detailX;
		repaint();
	}

	@Override
	public void setDetailY(double detailY) {
		this.detailY = detailY;
		repaint();
	}

	protected void repaint() {
		renderPicture(currentProvider);
	}

	private Canvas getCanvas() {
		if (canvas == null) {
			canvas = new Canvas();
			canvas.setCache(false);

			if (parentNode != null) {
				canvas.widthProperty().bind(parentNode.widthProperty());
				canvas.heightProperty().bind(parentNode.heightProperty());

				parentNode.getChildren().add(canvas);
				canvas.toBack();
			} else {
				canvas.setWidth(width);
				canvas.setHeight(height);
			}
		}
		return canvas;
	}

	private ImageView getImageView() {
		if (image == null) {
			image = new ImageView();
			image.setPreserveRatio(true);
			image.setSmooth(true);
			// https://stackoverflow.com/questions/15003897/is-there-any-way-to-force-javafx-to-release-video-memory
			image.setCache(false);

			if (parentNode != null) {
				image.fitWidthProperty().bind(parentNode.widthProperty());
				image.fitHeightProperty().bind(parentNode.heightProperty());

				parentNode.getChildren().add(image);
				image.toBack();
			} else {
				image.setFitWidth(width);
				image.setFitHeight(height);
			}
		}
		return image;
	}

	private MediaView getMediaView() {
		if (media == null) {
			media = new MediaView();
			media.setCache(false);
			media.setPreserveRatio(true);
			media.setSmooth(true);

			if (parentNode != null) {
				media.fitWidthProperty().bind(parentNode.widthProperty());
				media.fitHeightProperty().bind(parentNode.heightProperty());

				parentNode.getChildren().add(media);
				media.toBack();
			} else {
				media.setFitWidth(width);
				media.setFitHeight(height);
			}
		}
		return media;
	}

	private void showCanvas() {
		getCanvas();
		canvas.setVisible(true);
		shownNode = canvas;

		if (image != null) {
			image.setVisible(false);
		}
		if (media != null) {
			media.setVisible(false);
		}
	}

	private void showImageView() {
		getImageView();
		image.setVisible(true);
		shownNode = image;

		if (canvas != null) {
			canvas.setVisible(false);
		}
		if (media != null) {
			media.setVisible(false);
		}
	}

	private void showMediaView() {
		getMediaView();
		media.setVisible(true);
		shownNode = media;

		if (canvas != null) {
			canvas.setVisible(false);
		}
		if (image != null) {
			image.setVisible(false);
		}
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
		currentProvider = provider;
		RealPicture realPicture = currentProvider.get();
		if (realPicture == null) {
			// nothing to render => draw black image
			renderPictureImage();
			return;
		}

		String extension = realPicture.getFileExtension();
		if (extension == null || extension.isEmpty()) {
			throw new IllegalArgumentException(realPicture.getFullPath());
		}
		extension = extension.toLowerCase();
		if (extension.equals("gif")) {
			// Gif
			renderPictureImage();
		} else if (extension.equals("mp4")) {
			// video
			renderPictureMedia();
		} else {
			// image
			renderPictureCanvas();
		}
	}

	private void renderPictureMedia() {
		RealPicture realCurrentPicture = currentProvider.get();
		if (realCurrentPicture == null) {
			throw new IllegalArgumentException();
		} else {
			// https://stackoverflow.com/questions/24043420/why-does-platform-runlater-not-check-if-it-currently-is-on-the-javafx-thread
			if (Platform.isFxApplicationThread()) {
				renderMedia(realCurrentPicture);
			} else {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						renderMedia(realCurrentPicture);
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
	private void renderPictureImage() {
		RealPicture realCurrentPicture = currentProvider.get();
		if (realCurrentPicture == null) {
			// show no picture
			Logic.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					getImageView().setImage(null);
					showImageView();
				}
			});
		} else {
			cache.request(realCurrentPicture, new CallBack<RealPicture, Image>() {
				@Override
				public void loaded(RealPicture key, Image value) {
					// https://stackoverflow.com/questions/26554814/javafx-updating-gui
					// https://stackoverflow.com/questions/24043420/why-does-platform-runlater-not-check-if-it-currently-is-on-the-javafx-thread
					if (Platform.isFxApplicationThread()) {
						renderImage(value, realCurrentPicture);
					} else {
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								if (key.equals(currentProvider.get())) {
									renderImage(value, realCurrentPicture);
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
	private void renderPictureCanvas() {
		RealPicture realCurrentPicture = currentProvider.get();
		if (realCurrentPicture == null) {
			// show no picture
			Logic.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					renderCanvas(null, null);
				}
			});
		} else {
			cache.request(realCurrentPicture, new CallBack<RealPicture, Image>() {
				@Override
				public void loaded(RealPicture key, Image value) {
					// https://stackoverflow.com/questions/26554814/javafx-updating-gui
					// https://stackoverflow.com/questions/24043420/why-does-platform-runlater-not-check-if-it-currently-is-on-the-javafx-thread
					if (Platform.isFxApplicationThread()) {
						renderCanvas(value, realCurrentPicture);
					} else {
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								if (key.equals(currentProvider.get())) {
									renderCanvas(value, realCurrentPicture);
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

	private void renderMedia(RealPicture picture) {
		currentImageHeight = 0.0;
		currentImageWidth = 0.0;

		String picturePath = picture.getFullPath();
		// TODO: geeignet cachen!?
		// TODO: Zoom + Details realisieren
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
		getMediaView().setMediaPlayer(player);
//		player.play();
		player.setAutoPlay(true);
		showMediaView();
	}

	private void renderImage(Image value, RealPicture picture) {
		currentImageHeight = 0.0;
		currentImageWidth = 0.0;

		gallery.Metadata metadata = picture.getMetadata();
		double rotate = 0.0;
		if (metadata != null) {
			if (!metadata.isLandscape()) {
				rotate = 90.0;
			}
		}
		ImageView localImage = getImageView();
		localImage.setRotate(rotate);
		localImage.setImage(value);
//		TODO: Zoom + Details realisieren
//		localImage.setX(value);
//		localImage.setViewport(value);
		showImageView();
	}

	private void renderCanvas(Image value, RealPicture picture) {
		Canvas localImage = getCanvas();
		// draw the image in rotated way
		// https://stackoverflow.com/questions/18260421/how-to-draw-image-rotated-on-javafx-canvas
		GraphicsContext gc = localImage.getGraphicsContext2D();
		final double availableHeight = localImage.getHeight();
		final double availableWidth = localImage.getWidth();

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
			// zoom / scale
			factor *= zoom;
			// update the wanted image size
			imageHeight = imageHeight * factor;
			imageWidth = imageWidth * factor;
			currentImageHeight = imageHeight;
			currentImageWidth = imageWidth;

			gc.save(); // saves the current state on stack, including the current transform

			// get the rotation angle
			if (picture != null && picture.getMetadata() != null && picture.getMetadata().isLandscape() == false) {
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

//			without change of the detail x/y
//			double x = (availableWidth - imageWidth) / 2.0;
//			double y = (availableHeight - imageHeight) / 2.0;
			double x = (availableWidth - imageWidth) * detailX;
			double y = (availableHeight - imageHeight) * detailY;

			gc.drawImage(value, x, y, imageWidth, imageHeight); // top-left x, y, width, height

			gc.restore(); // back to original state (before rotation)
		} else {
			currentImageHeight = 0.0;
			currentImageWidth = 0.0;
		}
		showCanvas();
	}
}
