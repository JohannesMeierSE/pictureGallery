package picturegallery.state;

import gallery.LinkedPicture;
import gallery.LinkedPictureCollection;
import gallery.Picture;
import gallery.PictureCollection;
import gallery.RealPicture;
import gallery.RealPictureCollection;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.Region;
import javafx.util.Callback;
import picturegallery.MainApp;
import picturegallery.action.ClearLinkCollectionsAction;
import picturegallery.action.CreateNewCollection;
import picturegallery.action.LinkCollectionsAction;
import picturegallery.action.RenameCollectionAction;
import picturegallery.action.SearchIdenticalAction;
import picturegallery.action.SearchIdenticalAndReplaceAction;
import picturegallery.action.ShowSingleCollectionAction;
import picturegallery.persistency.ObservablePictureCollection;
import picturegallery.persistency.PictureCollectionTreeTableCell;
import picturegallery.persistency.SubCollectionCallback;
import picturegallery.ui.RecursiveTreeItem;

public class CollectionState extends State {
	protected final TreeTableView<PictureCollection> table;
	private SimpleObjectProperty<RealPictureCollection> collectionWithNewLinks = new SimpleObjectProperty<>();
	private final SingleCollectionState singleState;

	public CollectionState() {
		super();
		singleState = new SingleCollectionState(this);
		singleState.onInit();

		table = new TreeTableView<>();
		table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		TreeTableColumn<PictureCollection, PictureCollection> nameCol = new TreeTableColumn<>("Collection name");
		nameCol.setEditable(false);
		nameCol.setPrefWidth(250.0);
		nameCol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<PictureCollection, PictureCollection>, ObservableValue<PictureCollection>>() {
			@Override
			public ObservableValue<PictureCollection> call(CellDataFeatures<PictureCollection, PictureCollection> param) {
				// Performanz-Optimierung möglich: nicht bei jeder sondern nur bei den relevanten Änderungen benachrichtigen lassen!
				List<ObservableValue<? extends PictureCollection>> otherValues = new ArrayList<>();
				otherValues.add(collectionWithNewLinks);
				otherValues.add(singleState.currentCollection);
				otherValues.add(singleState.movetoCollection);
				otherValues.add(singleState.linktoCollection);
				return new ObservablePictureCollection(param.getValue().getValue(), otherValues);
			}
		});
		nameCol.setCellFactory(new Callback<TreeTableColumn<PictureCollection, PictureCollection>, TreeTableCell<PictureCollection, PictureCollection>>() {
			@Override
			public TreeTableCell<PictureCollection, PictureCollection> call(TreeTableColumn<PictureCollection, PictureCollection> param) {
				return new PictureCollectionTreeTableCell() {
					@Override
					protected String toText(PictureCollection item) {
						String textToShow = item.getName();
						if (item == singleState.currentCollection.get()) {
							textToShow = textToShow + " [currently shown]";
						}
						if (item == singleState.movetoCollection.get()) {
							textToShow = textToShow + " [currently moving into]";
						}
						if (item == singleState.linktoCollection.get()) {
							textToShow = textToShow + " [currently linking into]";
						}
						if (collectionWithNewLinks.get() == item) {
							textToShow = textToShow + " [target of linking-collections-action]";
						}
						return textToShow;
					}
				};
			}
		});
		table.getColumns().add(nameCol);

		TreeTableColumn<PictureCollection, PictureCollection> sizeRealPicturesCol = new TreeTableColumn<>("Real Pictures");
		sizeRealPicturesCol.setEditable(false);
		sizeRealPicturesCol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<PictureCollection, PictureCollection>, ObservableValue<PictureCollection>>() {
			@Override
			public ObservableValue<PictureCollection> call(CellDataFeatures<PictureCollection, PictureCollection> param) {
				return new ObservablePictureCollection(param.getValue().getValue());
			}
		});
		sizeRealPicturesCol.setCellFactory(new Callback<TreeTableColumn<PictureCollection, PictureCollection>, TreeTableCell<PictureCollection, PictureCollection>>() {
			@Override
			public TreeTableCell<PictureCollection, PictureCollection> call(TreeTableColumn<PictureCollection, PictureCollection> param) {
				return new PictureCollectionTreeTableCell() {
					@Override
					protected String toText(PictureCollection item) {
						int count = 0;
						for (Picture pic : item.getPictures()) {
							if (pic instanceof RealPicture) {
								count++;
							}
						}
						if (count == 0) {
							return "";
						}
						return count + "";
					}
				};
			}
		});
		table.getColumns().add(sizeRealPicturesCol);

		TreeTableColumn<PictureCollection, PictureCollection> sizeLinkedPicturesCol = new TreeTableColumn<>("Linked Pictures");
		sizeLinkedPicturesCol.setEditable(false);
		sizeLinkedPicturesCol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<PictureCollection, PictureCollection>, ObservableValue<PictureCollection>>() {
			@Override
			public ObservableValue<PictureCollection> call(CellDataFeatures<PictureCollection, PictureCollection> param) {
				return new ObservablePictureCollection(param.getValue().getValue());
			}
		});
		sizeLinkedPicturesCol.setCellFactory(new Callback<TreeTableColumn<PictureCollection, PictureCollection>, TreeTableCell<PictureCollection, PictureCollection>>() {
			@Override
			public TreeTableCell<PictureCollection, PictureCollection> call(TreeTableColumn<PictureCollection, PictureCollection> param) {
				return new PictureCollectionTreeTableCell() {
					@Override
					protected String toText(PictureCollection item) {
						int count = 0;
						for (Picture pic : item.getPictures()) {
							if (pic instanceof LinkedPicture) {
								count++;
							}
						}
						if (count == 0) {
							return "";
						}
						return count + "";
					}
				};
			}
		});
		table.getColumns().add(sizeLinkedPicturesCol);

		TreeTableColumn<PictureCollection, PictureCollection> linkCol = new TreeTableColumn<>("Links");
		linkCol.setEditable(false);
		linkCol.setPrefWidth(300.0);
		linkCol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<PictureCollection, PictureCollection>, ObservableValue<PictureCollection>>() {
			@Override
			public ObservableValue<PictureCollection> call(CellDataFeatures<PictureCollection, PictureCollection> param) {
				return new ObservablePictureCollection(param.getValue().getValue());
			}
		});
		linkCol.setCellFactory(new Callback<TreeTableColumn<PictureCollection, PictureCollection>, TreeTableCell<PictureCollection, PictureCollection>>() {
			@Override
			public TreeTableCell<PictureCollection, PictureCollection> call(TreeTableColumn<PictureCollection, PictureCollection> param) {
				return new PictureCollectionTreeTableCell() {
					@Override
					protected String toText(PictureCollection item) {
						if (item instanceof LinkedPictureCollection) {
							return "=> " + ((LinkedPictureCollection) item).getRealCollection().getRelativePath();
						} else {
							String message = "";
							for (LinkedPictureCollection link : ((RealPictureCollection) item).getLinkedBy()) {
								message = message + "<= " + link.getRelativePath() + "\n";
							}
							return message.trim();
						}
					}
				};
			}
		});
		table.getColumns().add(linkCol);

		TreeItem<PictureCollection> rootItem =
				new RecursiveTreeItem<PictureCollection>(MainApp.get().getBaseCollection(), new SubCollectionCallback());
		rootItem.setExpanded(true);
		table.setShowRoot(true);
		table.setRoot(rootItem);
	}

	@Override
	public Region getRootNode() {
		return table;
	}

	@Override
	public void onInit() {
		registerAction(new ShowSingleCollectionAction());
		registerAction(new RenameCollectionAction());
		registerAction(new CreateNewCollection());
		registerAction(new LinkCollectionsAction());
		registerAction(new ClearLinkCollectionsAction());
		registerAction(new SearchIdenticalAction());
		registerAction(new SearchIdenticalAndReplaceAction());
	}

	@Override
	public void onClose() {
		table.setRoot(null);
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

	public RealPictureCollection getCollectionWithNewLinks() {
		return collectionWithNewLinks.get();
	}

	public void setCollectionWithNewLinks(RealPictureCollection collectionWithNewLinks) {
		this.collectionWithNewLinks.set(collectionWithNewLinks);
	}

	public SingleCollectionState getSingleState() {
		return singleState;
	}
}
