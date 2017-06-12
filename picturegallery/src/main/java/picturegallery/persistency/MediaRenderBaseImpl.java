package picturegallery.persistency;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaView;

public class MediaRenderBaseImpl implements MediaRenderBase {
	private final Pane parentNode;

	private Canvas canvas;
	private ImageView image;
	private MediaView media;

	public MediaRenderBaseImpl(Pane parentNode) {
		super();
		if (parentNode == null) {
			throw new IllegalArgumentException();
		}
		this.parentNode = parentNode;
	}

	@Override
	public Canvas getCanvas() {
		if (canvas == null) {
			canvas = new Canvas();
			canvas.setCache(false);

			canvas.widthProperty().bind(parentNode.widthProperty());
			canvas.heightProperty().bind(parentNode.heightProperty());

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

			image.fitWidthProperty().bind(parentNode.widthProperty());
			image.fitHeightProperty().bind(parentNode.heightProperty());

			parentNode.getChildren().add(image);
		}
		return image;
	}

	@Override
	public MediaView getMediaView() {
		if (media == null) {
			media = new MediaView();
			media.setCache(false);
//			media.setMediaPlayer(value);
			media.setPreserveRatio(true);
			media.setSmooth(true);

			media.fitWidthProperty().bind(parentNode.widthProperty());
			media.fitHeightProperty().bind(parentNode.heightProperty());

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
}
