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


	public final static double detailRationXDefault = 0.5;
	public final static double detailRationYDefault = 0.5;
	public final static double zoomDefault = 1.0;

	void setZoom(double newZoom);
	void setDetailX(double newX);
	void setDetailY(double newY);
}
