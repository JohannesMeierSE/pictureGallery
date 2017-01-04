package picturegallery.state;

import gallery.PictureCollection;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.Region;
import javafx.util.Callback;
import picturegallery.Logic;
import picturegallery.MainApp;

public class CollectionState extends State {
	// TODO: die Library sollte hier als Member stehen, nicht in der MainApp! (oder??)
	protected final TreeTableView<PictureCollection> table;

	public CollectionState() {
		super();
		table = new TreeTableView<>();

		TreeTableColumn<PictureCollection, String> nameCol = new TreeTableColumn<>("Collection name");
		nameCol.setEditable(false);
		nameCol.setPrefWidth(250.0);
		nameCol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<PictureCollection,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<PictureCollection, String> param) {
				return new ReadOnlyStringWrapper(param.getValue().getValue().getName());
			}
		});

		TreeTableColumn<PictureCollection, Integer> sizeCol = new TreeTableColumn<>("Pictures");
		nameCol.setEditable(false);
		sizeCol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<PictureCollection,Integer>, ObservableValue<Integer>>() {
			@Override
			public ObservableValue<Integer> call(CellDataFeatures<PictureCollection, Integer> param) {
				return new ReadOnlyObjectWrapper<Integer>(param.getValue().getValue().getPictures().size());
			}
		});

		table.getColumns().addAll(nameCol, sizeCol);
		table.setShowRoot(true);

		TreeItem<PictureCollection> rootItem = new TreeItem<PictureCollection>(MainApp.get().getBaseCollection());
		rootItem.setExpanded(true);
		Logic.handleTreeItem(rootItem, true);
		table.setRoot(rootItem);
	}

	@Override
	public Region getRootNode() {
		return table;
	}

	@Override
	public void onInit() {
		// TODO: later, register actions
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
}
