package picturegallery.persistency;

import gallery.RealPicture;
import javafx.scene.Node;

public interface MediaRenderBase {
	public Node getShownNode();

	public interface PictureProvider {
		public RealPicture get();
	}
	public void renderPicture(PictureProvider provider);
	public void renderPicture(RealPicture picture);
}
