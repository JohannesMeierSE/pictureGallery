package picturegallery.state;

import gallery.PictureCollection;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.Region;
import javafx.util.Callback;
import picturegallery.Logic;
import picturegallery.MainApp;
import picturegallery.action.RenameCollectionAction;
import picturegallery.persistency.ObservablePictureCollection;
import picturegallery.persistency.PictureCollectionTreeTableCell;

public class CollectionState extends State {
	// TODO: die Library sollte hier als Member stehen, nicht in der MainApp! (oder??)
	protected final TreeTableView<PictureCollection> table;

	public CollectionState() {
		super();
		table = new TreeTableView<>();
		table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		TreeTableColumn<PictureCollection, PictureCollection> nameCol = new TreeTableColumn<>("Collection name");
		nameCol.setEditable(false);
		nameCol.setPrefWidth(250.0);
		nameCol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<PictureCollection, PictureCollection>, ObservableValue<PictureCollection>>() {
			@Override
			public ObservableValue<PictureCollection> call(CellDataFeatures<PictureCollection, PictureCollection> param) {
				return new ObservablePictureCollection(param.getValue().getValue());
			}
		});
		nameCol.setCellFactory(new Callback<TreeTableColumn<PictureCollection, PictureCollection>, TreeTableCell<PictureCollection, PictureCollection>>() {
			@Override
			public TreeTableCell<PictureCollection, PictureCollection> call(TreeTableColumn<PictureCollection, PictureCollection> param) {
				return new PictureCollectionTreeTableCell() {
					@Override
					protected String toText(PictureCollection item) {
						return item.getName();
					}
				};
			}
		});
		table.getColumns().add(nameCol);

		TreeTableColumn<PictureCollection, PictureCollection> sizeCol = new TreeTableColumn<>("Pictures");
		nameCol.setEditable(false);
		sizeCol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<PictureCollection, PictureCollection>, ObservableValue<PictureCollection>>() {
			@Override
			public ObservableValue<PictureCollection> call(CellDataFeatures<PictureCollection, PictureCollection> param) {
				return new ObservablePictureCollection(param.getValue().getValue());
			}
		});
		sizeCol.setCellFactory(new Callback<TreeTableColumn<PictureCollection, PictureCollection>, TreeTableCell<PictureCollection, PictureCollection>>() {
			@Override
			public TreeTableCell<PictureCollection, PictureCollection> call(TreeTableColumn<PictureCollection, PictureCollection> param) {
				return new PictureCollectionTreeTableCell() {
					@Override
					protected String toText(PictureCollection item) {
						return item.getPictures().size() + " pictures";
					}
				};
			}
		});
		table.getColumns().add(sizeCol);

		TreeItem<PictureCollection> rootItem = new TreeItem<PictureCollection>(MainApp.get().getBaseCollection());
		rootItem.setExpanded(true);
		Logic.handleTreeItem(rootItem, true);
		table.setShowRoot(true);
		table.setRoot(rootItem);
	}

	@Override
	public Region getRootNode() {
		return table;
	}

	@Override
	public void onInit() {
		registerAction(new RenameCollectionAction());
	}

	@Override
	public void onClose() {
		table.setRoot(null);
	}

	@Override
	public void onEntry(State previousState) {
		// empty
	}

	@Override
	public void onExit(State nextState) {
		// empty
	}

	/**
	 * Returns the currently selected picture collection (or null).
	 * @return
	 */
	public PictureCollection getSelection() {
		TreeItem<PictureCollection> selectedItem = table.getSelectionModel().getSelectedItem();
		if (selectedItem == null) {
			return null;
		}
		return selectedItem.getValue();
	}
}
