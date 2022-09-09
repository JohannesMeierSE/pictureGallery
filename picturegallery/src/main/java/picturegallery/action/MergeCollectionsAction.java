package picturegallery.action;

import gallery.GalleryPackage;
import gallery.LinkedPicture;
import gallery.LinkedPictureCollection;
import gallery.Picture;
import gallery.PictureCollection;
import gallery.RealPicture;
import gallery.RealPictureCollection;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import javafx.scene.input.KeyCode;
import picturegallery.Logic;
import picturegallery.MainApp;
import picturegallery.state.CollectionState;
import picturegallery.state.State;

public class MergeCollectionsAction extends Action {

	@Override
	public void run(State currentState) {
		// calculate the collection to move
		if (!(currentState instanceof CollectionState)) {
			throw new IllegalStateException();
		}
		CollectionState state = (CollectionState) currentState;
		PictureCollection selection = state.getSelection();
		if (selection == null) {
			return;
		}
		if (!(selection instanceof RealPictureCollection)) {
			return;
		}
		RealPictureCollection collectionToDelete = (RealPictureCollection) selection;
		if (MainApp.get().getBaseCollection().equals(collectionToDelete)) {
			return; // do not merge the base collection
		}

		// select the target real collection
		List<PictureCollection> ignoredCollections = new ArrayList<>();
		ignoredCollections.add(collectionToDelete);
		ignoredCollections.addAll(Logic.getAllSuperCollections(collectionToDelete));
		ignoredCollections.addAll(Logic.getAllSubCollections(collectionToDelete));
		RealPictureCollection target = (RealPictureCollection) Logic.selectCollection(state, true, true, false, ignoredCollections);
		if (target == null) {
			return;
		}


		// do the long-running merging in another thread!
		MainApp.get().switchToWaitingState();

		Logic.runNotOnUiThread(new Runnable() {
			@Override
			public void run() {
				MainApp.get().mergeCollections(collectionToDelete, target);

				MainApp.get().switchCloseWaitingState();

				// jump to the target collection
				Logic.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						state.jumpToCollection(target);
					}
				});
			}
		});
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.M;
	}

	@Override
	public boolean requiresCtrl() {
		return true;
	}

	@Override
	public String getDescription() {
		// sub-collections are moved/merged accordingly
		return "merges the current real collection with/into another real collection (within the library)";
	}
}
