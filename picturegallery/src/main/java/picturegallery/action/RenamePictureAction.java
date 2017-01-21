package picturegallery.action;

import gallery.GalleryPackage;
import gallery.LinkedPicture;
import gallery.Picture;
import gallery.RealPicture;

import java.io.File;

import javafx.scene.input.KeyCode;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.MoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import picturegallery.Logic;
import picturegallery.MainApp;
import picturegallery.state.PictureSwitchingState;
import picturegallery.state.State;

/**
 * Renames the currently selected picture.
 * If the renaming influences the order of pictures which are currently shown,
 * than the index remains unchanged => another picture will be shown instead!
 * @author Johannes Meier
 */
public class RenamePictureAction extends Action {

	@Override
	public void run(State currentState) {
		if (!(currentState instanceof PictureSwitchingState)) {
			throw new IllegalStateException();
		}
		PictureSwitchingState state = (PictureSwitchingState) currentState;
		Picture currentPicture = state.getCurrentPicture();
		if (currentPicture == null) {
			return;
		}

		String newName = Logic.askForString("Rename Picture",
				"Select a new name for the picture " + currentPicture.getRelativePath() + "!",
				"New name: ", false, currentPicture.getName());
		if (newName == null || newName.isEmpty()) {
			return;
		}
		if (newName.equals(currentPicture.getName())) {
			return; // same name like before => nothing to do!
		}
		// check for uniqueness
		if (!Logic.isPictureNameUnique(currentPicture, newName)) {
			System.err.println("The new name " + newName + " is not unique!");
			return; // the new name is not unique!
		}

		if (currentPicture instanceof LinkedPicture) {

			// rename in file system and EMF model
			renameModel(currentPicture, newName);

		} else {

			RealPicture realPicture = (RealPicture) currentPicture;
			// remove all links linking on the renamed real picture
			for (LinkedPicture link : realPicture.getLinkedBy()) {
				Logic.deleteSymlinkPicture(link);
			}

			// rename in file system and EMF model
			renameModel(currentPicture, newName);

			// create all deleted links again
			for (LinkedPicture link : realPicture.getLinkedBy()) {
				Logic.createSymlinkPicture(link);
			}
		}
	}

	private void renameModel(Picture picture, String newName) {
		// rename in file system
		// http://www.java-examples.com/rename-file-or-directory
		File oldFile = new File(picture.getFullPath());
		oldFile.renameTo(new File(oldFile.getParent() + File.separator + newName + "." + picture.getFileExtension()));

		// rename in EMF model via commands
		EditingDomain domain = MainApp.get().getModelDomain();
		Command set = SetCommand.create(domain, picture, GalleryPackage.eINSTANCE.getPathElement_Name(), newName);
		domain.getCommandStack().execute(set);

		// sort the pictures within the parent collection => by moving the wrong element to the correct position
		domain.getCommandStack().execute(MoveCommand.create(domain,
				picture.getCollection(), GalleryPackage.eINSTANCE.getRealPictureCollection_Pictures(),
				picture, Logic.getIndexForPictureAtWrongPositionMove(
						picture.getCollection().getPictures(), picture)));
		// TODO: check in SinglePictureState and TempState!!
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.R;
	}

	@Override
	public String getDescription() {
		return "rename the currently selected picture (both, real and linked pictures)";
	}
}
