package picturegallery.state;

import gallery.GalleryPackage;
import gallery.Picture;
import gallery.PictureCollection;
import gallery.RealPictureCollection;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javax.ws.rs.NotSupportedException;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;

import picturegallery.Logic;
import picturegallery.MainApp;
import picturegallery.action.ExitSingleCollectionStateAction;
import picturegallery.action.JumpLeftAction;
import picturegallery.action.JumpRightAction;
import picturegallery.action.RenamePictureAction;

public class SingleCollectionState extends PictureSwitchingState {
	public final SimpleObjectProperty<PictureCollection> currentCollection;
	public final SimpleObjectProperty<RealPictureCollection> movetoCollection = new SimpleObjectProperty<>();
	public final SimpleObjectProperty<RealPictureCollection> linktoCollection = new SimpleObjectProperty<>();

	private final Adapter adapterCurrentCollection;
	private final ImageView iv;
	private final StackPane root;
	private final VBox vBox;
	private final Label labelCollectionPath;
	private final Label labelIndex;
	private final Label labelPictureName;
	private final Label labelMeta;

	public SingleCollectionState() {
		super();

		adapterCurrentCollection = new AdapterImpl() {
			@Override
			public void notifyChanged(Notification msg) {
				if (msg.getEventType() == Notification.REMOVING_ADAPTER || msg.getEventType() == Notification.RESOLVE) {
					return;
				}
				Logic.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// changes of the name of this collection
						if (msg.getFeature() == GalleryPackage.eINSTANCE.getPathElement_Name()) {
							updateCollectionLabel();
							return;
						}
						// add or remove pictures
						if (msg.getFeature() != GalleryPackage.eINSTANCE.getRealPictureCollection_Pictures()) {
							return;
						}
						switch (msg.getEventType()) {
						case Notification.ADD:
							picturesToShow.add((Picture) msg.getNewValue());
							break;
						case Notification.ADD_MANY:
							throw new NotSupportedException(msg.getNewValue().toString());
						case Notification.REMOVE:
							picturesToShow.remove(msg.getOldValue());
							break;
						case Notification.REMOVE_MANY:
							throw new NotSupportedException(msg.getOldValue().toString());
						case Notification.MOVE:
							// nothing to do, because this case is handled by the SpecialSortedList in PictureSwitchingState!
							break;
						}
					}
				});
			}
		};

		currentCollection = new SimpleObjectProperty<>();
		currentCollection.addListener(new ChangeListener<PictureCollection>() {
			@Override
			public void changed(ObservableValue<? extends PictureCollection> observable,
					PictureCollection oldValue, PictureCollection newValue) {
				if (oldValue != null) {
					oldValue.eAdapters().remove(adapterCurrentCollection);
					picturesToShow.removeAll(oldValue.getPictures());
				}
				if (newValue != null) {
					newValue.eAdapters().add(adapterCurrentCollection);
					picturesToShow.addAll(newValue.getPictures());
				}
				updateCollectionLabel();
			}
		});

		// Stack Pane
		root = new StackPane();
		root.setStyle("-fx-background-color: #000000;");

		// image
		iv = new ImageView();
		iv.setPreserveRatio(true);
		iv.setSmooth(true);
		// https://stackoverflow.com/questions/15003897/is-there-any-way-to-force-javafx-to-release-video-memory
		iv.setCache(false);
		// https://stackoverflow.com/questions/12630296/resizing-images-to-fit-the-parent-node
		iv.fitWidthProperty().bind(root.widthProperty());
		iv.fitHeightProperty().bind(root.heightProperty());
		root.getChildren().add(iv);

    	vBox = new VBox();
    	vBox.visibleProperty().bind(MainApp.get().labelsVisible);

    	labelCollectionPath = new Label("Collection name");
    	handleLabel(labelCollectionPath);
    	labelIndex = new Label("index");
    	handleLabel(labelIndex);
    	labelPictureName = new Label("picture name");
    	handleLabel(labelPictureName);
    	labelMeta= new Label("meta data");
    	handleLabel(labelMeta);

    	root.getChildren().add(vBox);
	}

    private void handleLabel(Label label) {
    	// https://assylias.wordpress.com/2013/12/08/383/
		label.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4);"
				+ "-fx-text-fill: white;");
    	vBox.getChildren().add(label);
	}

	@Override
	public Region getRootNode() {
    	return root;
	}

	@Override
	public PictureCollection getCurrentCollection() {
		return currentCollection.get();
	}

	@Override
	protected String getCollectionDescription() {
		return currentCollection.get().getRelativePath();
	}

	@Override
	protected void setLabelIndex(String newText) {
		labelIndex.setText(newText);
	}

	@Override
	protected void setLabelMeta(String newText) {
		labelMeta.setText(newText);
	}

	@Override
	protected void setLabelPictureName(String newText) {
		labelPictureName.setText(newText);
	}

	@Override
	protected void setLabelCollectionPath(String newText) {
		labelCollectionPath.setText(newText);
	}

	@Override
	protected ImageView getImage() {
		return iv;
	}

	public void setCurrentCollection(PictureCollection currentCollection) {
		if (currentCollection == null) {
			throw new IllegalArgumentException();
		}

		this.currentCollection.set(currentCollection);
		setMovetoCollection(null);
		setLinktoCollection(null);
		indexCurrentCollection = -1;

		if (isVisible()) {
			showInitialPicture();
		}
	}

	@Override
	public void onInit() {
		super.onInit();
		registerAction(new JumpRightAction());
		registerAction(new JumpLeftAction());
		registerAction(new ExitSingleCollectionStateAction());
		registerAction(new RenamePictureAction());
	}

	public RealPictureCollection getLinktoCollection() {
		return linktoCollection.get();
	}
	public void setLinktoCollection(RealPictureCollection linktoCollection) {
		this.linktoCollection.set(linktoCollection);
	}

	public RealPictureCollection getMovetoCollection() {
		return movetoCollection.get();
	}
	public void setMovetoCollection(RealPictureCollection movetoCollection) {
		this.movetoCollection.set(movetoCollection);
	}
}
