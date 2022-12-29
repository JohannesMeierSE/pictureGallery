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

import gallery.Picture;
import gallery.PictureCollection;
import gallery.RealPictureCollection;
import javafx.scene.layout.Region;
import picturegallery.persistency.MediaRenderBase;

public class TempCollectionState extends SinglePictureSwitchingState {

	public TempCollectionState(State parentState) {
		super(parentState);
	}

	@Override
	public SinglePictureSwitchingState getParentStateHierarchy() {
		return (SinglePictureSwitchingState) parentState;
	}

	@Override
	public PictureCollection getCurrentCollection() {
		return getParentStateHierarchy().getCurrentCollection();
	}

	@Override
	protected String getCollectionDescription() {
		return "temp collection within (" + getParentStateHierarchy().getCollectionDescription() + ")";
	}

	@Override
	protected void setLabelIndex(String newText) {
		getParentStateHierarchy().setLabelIndex(newText);
	}

	@Override
	protected void setLabelMeta(String newText) {
		getParentStateHierarchy().setLabelMeta(newText);
	}

	@Override
	protected void setLabelTags(String newText) {
		getParentStateHierarchy().setLabelTags(newText);
	}

	@Override
	protected void setLabelPictureName(String newText) {
		getParentStateHierarchy().setLabelPictureName(newText);
	}

	@Override
	protected void setLabelCollectionPath(String newText) {
		getParentStateHierarchy().setLabelCollectionPath(newText);
	}

	@Override
	protected MediaRenderBase getImage() {
		return getParentStateHierarchy().getImage();
	}

	@Override
	public Region getRootNode() {
		return getParentStateHierarchy().getRootNode();
	}

	@Override
	public void onExit(State nextState) {
		super.onExit(nextState);
		if (tempState != null && nextState == tempState) {
			// keep the pictures, if the "next deeper temp level" will be reached
		} else {
			clearPictures();
		}
	}

	@Override
	public void onClose() {
		super.onClose();
		clearPictures();
	}

	public void addPicture(Picture picture) {
		picturesToShow.add(picture);
	}

	public void removePicture(Picture picture) {
		picturesToShow.remove(picture);
	}

	public void clearPictures() {
		picturesToShow.clear();
	}

	@Override
	public RealPictureCollection getMovetoCollection() {
		return getParentStateHierarchy().getMovetoCollection();
	}

	@Override
	public void setMovetoCollection(RealPictureCollection movetoCollection) {
		getParentStateHierarchy().setMovetoCollection(movetoCollection);
	}

	@Override
	public RealPictureCollection getLinktoCollection() {
		return getParentStateHierarchy().getLinktoCollection();
	}

	@Override
	public void setLinktoCollection(RealPictureCollection linktoCollection) {
		getParentStateHierarchy().setLinktoCollection(linktoCollection);
	}
}
