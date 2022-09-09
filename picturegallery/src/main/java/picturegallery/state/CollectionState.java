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
import javafx.scene.Node;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import picturegallery.Logic;
import picturegallery.MainApp;
import picturegallery.action.ClearAndSetLinkCollectionsAction;
import picturegallery.action.ClearLinkCollectionsAction;
import picturegallery.action.CreateNewCollection;
import picturegallery.action.DebuggerCollectionAction;
import picturegallery.action.DiffCollectionDeleteAction;
import picturegallery.action.ExportLinkedPicturesAction;
import picturegallery.action.FixPictureNumbersAction;
import picturegallery.action.JumpRelatedCollectionAction;
import picturegallery.action.LinkCollectionsAction;
import picturegallery.action.MoveCollectionAction;
import picturegallery.action.OpenCloseCollectionsOfSameLevelAction;
import picturegallery.action.RenameCollectionAction;
import picturegallery.action.SearchIdenticalAction;
import picturegallery.action.SearchIdenticalAndCollectAction;
import picturegallery.action.SearchIdenticalAndReplaceAction;
import picturegallery.action.SearchIdenticalDeletedAction;
import picturegallery.action.ShowSeveralCollectionsAction;
import picturegallery.action.ShowSingleCollectionAction;
import picturegallery.filter.CompositeCollectionFilter;
import picturegallery.persistency.ObservablePictureCollection;
import picturegallery.persistency.PictureCollectionTreeTableCell;
import picturegallery.persistency.SubCollectionCallback;
import picturegallery.ui.RecursiveTreeItem;
import picturegallery.ui.RecursiveTreeItem.PositionCalculator;

/**
 * This state shows real and linked collections in form of a tree and allows to manage collections.
 *
 * @author Johannes Meier
 */
public class CollectionState extends State {
	private final VBox vbox;
	protected final TreeTableView<PictureCollection> table;
	private SimpleObjectProperty<RealPictureCollection> collectionWithNewLinks = new SimpleObjectProperty<>();
	private final CompositeCollectionFilter collectionFilter;

	private final SingleCollectionState singleState;
	private final MultiCollectionState multiState;

	public CollectionState() {
		super();
		singleState = new SingleCollectionState();
		singleState.setNextAfterClosed(this);
		singleState.onInit();

		multiState = new MultiCollectionState();
		multiState.setNextAfterClosed(this);
		multiState.onInit();

		collectionFilter = new CompositeCollectionFilter(this, null); // no parent filter available!
		collectionFilter.setAcceptMinimum(0);

		table = new TreeTableView<>();
		table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		Node filterNode = collectionFilter.getUI();
		vbox = new VBox(10.0, filterNode, table);
		VBox.setVgrow(table, Priority.ALWAYS);
		VBox.setVgrow(filterNode, Priority.NEVER);

		TreeTableColumn<PictureCollection, PictureCollection> nameCol = new TreeTableColumn<>("Collection name");
		setupColumn(nameCol);
		nameCol.setPrefWidth(300.0);
		nameCol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<PictureCollection, PictureCollection>, ObservableValue<PictureCollection>>() {
			@Override
			public ObservableValue<PictureCollection> call(CellDataFeatures<PictureCollection, PictureCollection> param) {
				// Performanz-Optimierung möglich: nicht bei jeder sondern nur bei den relevanten Änderungen benachrichtigen lassen!
				List<ObservableValue<? extends PictureCollection>> otherValues = new ArrayList<>();
				otherValues.add(collectionWithNewLinks);
				otherValues.add(singleState.currentCollection);
				otherValues.add(singleState.movetoCollection);
				otherValues.add(singleState.linktoCollection);
				return new ObservablePictureCollection(param.getValue().getValue(), otherValues, collectionFilter);
			}
		});
		nameCol.setCellFactory(new Callback<TreeTableColumn<PictureCollection, PictureCollection>, TreeTableCell<PictureCollection, PictureCollection>>() {
			@Override
			public TreeTableCell<PictureCollection, PictureCollection> call(TreeTableColumn<PictureCollection, PictureCollection> param) {
				return new PictureCollectionTreeTableCell(CollectionState.this) {
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
		setupColumn(sizeRealPicturesCol);
		sizeRealPicturesCol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<PictureCollection, PictureCollection>, ObservableValue<PictureCollection>>() {
			@Override
			public ObservableValue<PictureCollection> call(CellDataFeatures<PictureCollection, PictureCollection> param) {
				return new ObservablePictureCollection(param.getValue().getValue());
			}
		});
		sizeRealPicturesCol.setCellFactory(new Callback<TreeTableColumn<PictureCollection, PictureCollection>, TreeTableCell<PictureCollection, PictureCollection>>() {
			@Override
			public TreeTableCell<PictureCollection, PictureCollection> call(TreeTableColumn<PictureCollection, PictureCollection> param) {
				return new PictureCollectionTreeTableCell(CollectionState.this) {
					@Override
					protected String toText(PictureCollection item) {
						int count = 0;
						/* TODO Exception in thread "JavaFX Application Thread" java.util.ConcurrentModificationException
						 * durch WaitingState behandelt, aber auch vollständig und funktionierend?
						 * oder muss jeder Zugriff aus Modell doch synchronisiert werden??
						 */
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
		setupColumn(sizeLinkedPicturesCol);
		sizeLinkedPicturesCol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<PictureCollection, PictureCollection>, ObservableValue<PictureCollection>>() {
			@Override
			public ObservableValue<PictureCollection> call(CellDataFeatures<PictureCollection, PictureCollection> param) {
				return new ObservablePictureCollection(param.getValue().getValue());
			}
		});
		sizeLinkedPicturesCol.setCellFactory(new Callback<TreeTableColumn<PictureCollection, PictureCollection>, TreeTableCell<PictureCollection, PictureCollection>>() {
			@Override
			public TreeTableCell<PictureCollection, PictureCollection> call(TreeTableColumn<PictureCollection, PictureCollection> param) {
				return new PictureCollectionTreeTableCell(CollectionState.this) {
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
		setupColumn(linkCol);
		linkCol.setPrefWidth(400.0);
		linkCol.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<PictureCollection, PictureCollection>, ObservableValue<PictureCollection>>() {
			@Override
			public ObservableValue<PictureCollection> call(CellDataFeatures<PictureCollection, PictureCollection> param) {
				return new ObservablePictureCollection(param.getValue().getValue());
			}
		});
		linkCol.setCellFactory(new Callback<TreeTableColumn<PictureCollection, PictureCollection>, TreeTableCell<PictureCollection, PictureCollection>>() {
			@Override
			public TreeTableCell<PictureCollection, PictureCollection> call(TreeTableColumn<PictureCollection, PictureCollection> param) {
				return new PictureCollectionTreeTableCell(CollectionState.this) {
					@Override
					protected String toText(PictureCollection item) {
						if (item instanceof LinkedPictureCollection) {
							return "=> " + ((LinkedPictureCollection) item).getRealCollection().getRelativePathWithoutBase();
						} else {
							String message = "";
							for (LinkedPictureCollection link : ((RealPictureCollection) item).getLinkedBy()) {
								message = message + "<= " + link.getRelativePathWithoutBase() + "\n";
							}
							return message.trim();
						}
					}
				};
			}
		});
		table.getColumns().add(linkCol);

		RecursiveTreeItem<PictureCollection> rootItem =
				new RecursiveTreeItem<PictureCollection>(
						MainApp.get().getBaseCollection(),
						new SubCollectionCallback(),
						new PositionCalculator<PictureCollection>() {
							@Override
							public int calculate(List<TreeItem<PictureCollection>> items, PictureCollection itemToAdd) {
								int result = 0;
								while (result < items.size()
										&& Logic.getComparable(items.get(result).getValue()).compareTo(Logic.getComparable(itemToAdd)) < 0) {
									// TODO: hier scheint bei neuen Werten ein Fehler zu sein ?!
									result++;
								}
								return result;
							}
						});
		rootItem.setExpanded(true);
		table.setShowRoot(true);
		table.setRoot(rootItem);
	}

	private void setupColumn(TreeTableColumn<PictureCollection, PictureCollection> column) {
		column.setEditable(false);
		column.setSortable(false);
	}

	@Override
	public Region getRootNode() {
		return vbox;
	}

	@Override
	public void onInit() {
		super.onInit();
		registerAction(new ShowSingleCollectionAction());
		registerAction(new ShowSeveralCollectionsAction());
		registerAction(new RenameCollectionAction());
		registerAction(new CreateNewCollection());
		registerAction(new MoveCollectionAction());
		registerAction(new LinkCollectionsAction());
		registerAction(new ClearLinkCollectionsAction());
		registerAction(new ClearAndSetLinkCollectionsAction());
		registerAction(new SearchIdenticalAction());
		registerAction(new SearchIdenticalAndReplaceAction());
		registerAction(new SearchIdenticalAndCollectAction());
		registerAction(new SearchIdenticalDeletedAction());
		registerAction(new DiffCollectionDeleteAction());
		registerAction(new ExportLinkedPicturesAction());
		registerAction(new JumpRelatedCollectionAction());
		registerAction(new FixPictureNumbersAction());
		registerAction(new DebuggerCollectionAction());
		registerAction(new OpenCloseCollectionsOfSameLevelAction());
	}

	@Override
	public void onClose() {
		super.onClose();
		table.setRoot(null);
	}

	@Override
	public void onEntry(State previousState) {
		super.onEntry(previousState);
		table.requestFocus();
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
		PictureCollection collection = selectedItem.getValue();
		if (collection == null) {
			return null;
		}
		if (isCollectionEnabled(collection)) {
			return collection;
		} else {
			return null;
		}
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
	public MultiCollectionState getMultiState() {
		return multiState;
	}

	public boolean isCollectionEnabled(PictureCollection collection) {
		if (collection == null) {
			return false;
		}

		// use the collection filter!
		if (collectionFilter.ignore.get()) {
			// handle the case, when the filter should be ignored
			return true;
		}
		return collectionFilter.isUsable(collection);
	}

	public TreeItem<PictureCollection> getCollectionItem(PictureCollection collection) {
		if (collection == null) {
			return null;
		}
		TreeItem<PictureCollection> item = ((RecursiveTreeItem<PictureCollection>) table.getRoot()).getTreeItemOfElement(collection);
		return item;
	}

	public void jumpToCollection(PictureCollection jumpTo) {
		if (jumpTo == null) {
			throw new IllegalArgumentException();
		}
		// search for the TreeItem of the collection
		TreeItem<PictureCollection> item = getCollectionItem(jumpTo);
		if (item == null) {
			throw new IllegalArgumentException(jumpTo.getRelativePath());
		}

		// scroll and select it
		int row = table.getRow(item);
		table.scrollTo(row);
		table.getSelectionModel().select(row);
	}
}
