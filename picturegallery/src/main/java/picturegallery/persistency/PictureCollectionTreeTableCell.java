package picturegallery.persistency;

import gallery.PictureCollection;
import javafx.scene.control.TreeTableCell;

public abstract class PictureCollectionTreeTableCell extends TreeTableCell<PictureCollection, PictureCollection> {
	@Override
	protected void updateItem(PictureCollection item, boolean empty) {
		super.updateItem(item, empty);
		if (empty) {
			setGraphic(null);
			setText(null);
		} else {
			setText(toText(item));
		}
	}

	protected abstract String toText(PictureCollection item);
}
