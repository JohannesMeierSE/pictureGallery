package picturegallery.persistency;

import gallery.RealPicture;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaView;

public interface MediaRenderBase {
	public Canvas getCanvas();
	public ImageView getImageView();
	public MediaView getMediaView();

	public Node getShownNode();

	public interface PictureProvider {
		public RealPicture get();
	}
	public void renderPicture(PictureProvider provider);
	public void renderPicture(RealPicture picture);

	public void showCanvas();
	public void showImageView();
	public void showMediaView();
}
