package picturegallery.persistency;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaView;

public interface MediaRenderBase {
	public Canvas getCanvas();
	public ImageView getImageView();
	public MediaView getMediaView();

	public void showCanvas();
	public void showImageView();
	public void showMediaView();
}
