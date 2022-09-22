package picturegallery.persistency;

import gallery.PictureCollection;
import javafx.scene.control.Label;
import javafx.scene.control.TreeTableCell;
import picturegallery.state.CollectionState;

public abstract class PictureCollectionTreeTableCell extends TreeTableCell<PictureCollection, PictureCollection> {
	private final Label label;
	private final CollectionState state;

	public PictureCollectionTreeTableCell(CollectionState state) {
		super();
		this.state = state;
		label = new Label();
	}

	@Override
	protected void updateItem(PictureCollection item, boolean empty) {
		super.updateItem(item, empty);
		if (empty) {
			setGraphic(null);
			setText(null);
		} else {
			setText(null);
			label.setText(toText(item));
			label.setDisable(!state.isCollectionEnabled(item));
			setGraphic(label);
		}
	}

	protected abstract String toText(PictureCollection item);
}
