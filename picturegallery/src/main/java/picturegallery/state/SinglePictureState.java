package picturegallery.state;

/*-
 * BEGIN-LICENSE
 * picturegallery
 * %%
 * Copyright (C) 2016 - 2022 Johannes Meier
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * END-LICENSE
 */

import gallery.RealPictureCollection;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import picturegallery.MainApp;
import picturegallery.action.DetailsDownAction;
import picturegallery.action.DetailsLeftAction;
import picturegallery.action.DetailsResetAction;
import picturegallery.action.DetailsRightAction;
import picturegallery.action.DetailsUpAction;
import picturegallery.action.ExitCurrentStateAction;
import picturegallery.action.JumpLeftAction;
import picturegallery.action.JumpParticularPictureAction;
import picturegallery.action.JumpRightAction;
import picturegallery.action.RenamePictureAction;
import picturegallery.action.SetTagPictureAction;
import picturegallery.action.ZoomDecreaseAction;
import picturegallery.action.ZoomIncreaseAction;
import picturegallery.action.ZoomResetAction;
import picturegallery.persistency.MediaRenderBase;
import picturegallery.persistency.MediaRenderBaseImpl;

public abstract class SinglePictureState extends SinglePictureSwitchingState {
	public final SimpleObjectProperty<RealPictureCollection> movetoCollection = new SimpleObjectProperty<>();
	public final SimpleObjectProperty<RealPictureCollection> linktoCollection = new SimpleObjectProperty<>();

	public final SimpleDoubleProperty detailRatioX = new SimpleDoubleProperty(MediaRenderBase.detailRationXDefault); // horizontal shift: 0 .. 1 (0.5 == centered)
	public final SimpleDoubleProperty detailRatioY = new SimpleDoubleProperty(MediaRenderBase.detailRationYDefault); // vertical shift: 0 .. 1 (0.5 == centered)
	public final SimpleDoubleProperty zoom = new SimpleDoubleProperty(MediaRenderBase.zoomDefault); // 1.0 == no zoom

	private final MediaRenderBase mediaBase;
	private final StackPane root;
	private final VBox vBox;
	private final Label labelCollectionPath;
	private final Label labelIndex;
	private final Label labelPictureName;
	private final Label labelMeta;
	private final Label labelTags;

	public SinglePictureState(State parentState) {
		super(parentState);

		// react on changes of the settings
		detailRatioX.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				mediaBase.setDetailX((double) newValue);
			}
		});
		detailRatioY.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				mediaBase.setDetailY((double) newValue);
			}
		});
		zoom.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				mediaBase.setZoom((double) newValue);
			}
		});

		// Stack Pane
		root = new StackPane();
		root.setStyle("-fx-background-color: #000000;");

		// image
		mediaBase = new MediaRenderBaseImpl(MainApp.get().getImageCache(), root);

    	vBox = new VBox();
    	vBox.visibleProperty().bind(MainApp.get().labelsVisible);

    	labelCollectionPath = new Label("Collection name");
    	handleLabel(labelCollectionPath);
    	labelIndex = new Label("index");
    	handleLabel(labelIndex);
    	labelPictureName = new Label("picture name");
    	handleLabel(labelPictureName);
    	labelMeta = new Label("meta data");
    	handleLabel(labelMeta);
    	labelTags = new Label("tags: key-value paris");
    	handleLabel(labelTags);

    	root.getChildren().add(vBox);
    	vBox.toFront();
	}

    private void handleLabel(Label label) {
		MainApp.styleLabel(label);
    	vBox.getChildren().add(label);
	}

	@Override
	public Region getRootNode() {
    	return root;
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
	protected void setLabelTags(String newText) {
		labelTags.setText(newText);
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
	protected MediaRenderBase getImage() {
		return mediaBase;
	}

	@Override
	public void onInit() {
		super.onInit();
		registerAction(new JumpRightAction());
		registerAction(new JumpLeftAction());
		registerAction(new JumpParticularPictureAction());
		registerAction(new ExitCurrentStateAction(false));
		registerAction(new RenamePictureAction());
		registerAction(new ZoomIncreaseAction());
		registerAction(new ZoomDecreaseAction());
		registerAction(new ZoomResetAction());
		registerAction(new DetailsRightAction());
		registerAction(new DetailsLeftAction());
		registerAction(new DetailsUpAction());
		registerAction(new DetailsDownAction());
		registerAction(new DetailsResetAction());
		registerAction(new SetTagPictureAction());
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

	public double getCurrentImageWidth() {
		return mediaBase.getCurrentImageWidth();
	}
	public double getCurrentImageHeight() {
		return mediaBase.getCurrentImageHeight();
	}
	public double getCurrentNodeHeight() {
		return mediaBase.getCurrentNodeHeight();
	}
	public double getCurrentNodeWidth() {
		return mediaBase.getCurrentNodeWidth();
	}
}
