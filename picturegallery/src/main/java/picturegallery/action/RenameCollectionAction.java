package picturegallery.action;

import gallery.GalleryPackage;
import gallery.LinkedPicture;
import gallery.LinkedPictureCollection;
import gallery.PictureCollection;
import gallery.RealPictureCollection;
import gallery.util.GalleryAdapterFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javafx.scene.input.KeyCode;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;

import picturegallery.Logic;
import picturegallery.MainApp;
import picturegallery.state.CollectionState;
import picturegallery.state.PictureSwitchingState;
import picturegallery.state.State;

public class RenameCollectionAction extends Action {

	@Override
	public void run(State currentState) {
		if (!(currentState instanceof CollectionState)) {
			throw new IllegalStateException();
		}
		CollectionState state = (CollectionState) currentState;

		RealPictureCollection baseCollection = MainApp.get().getBaseCollection();

//		PictureCollection collectionToRename = Logic.selectCollection(
//				currentState, true, true, true, Collections.singletonList(baseCollection));
		PictureCollection collectionToRename = state.getSelection();

		if (collectionToRename == null) {
			return;
		}
		if (collectionToRename == baseCollection) {
			// sollte eigentlich gar nicht möglich sein!
			return;
		}
		String newName = Logic.askForString("Rename collection",
				"Select a new name for the collection " + collectionToRename.getName() + "!",
				"New name: ", true, collectionToRename.getName());
		if (newName.equals(collectionToRename.getName())) {
			return; // same name like before => nothing to do!
		}
		// check for uniqueness
		if (!Logic.isCollectionNameUnique(collectionToRename.getSuperCollection(), newName)) {
			System.err.println("The new name " + newName + " is not unique!");
			return; // the new name is not unique!
		}

		if (collectionToRename instanceof RealPictureCollection) {
			// after testing all pre-conditions, start with the renaming itself ...
			List<LinkedPicture> linksToReGenerate = Logic.findLinksOnPicturesIn(collectionToRename);
			// remove all links linking on pictures contained (recursively) in the collection to rename
			for (LinkedPicture link : linksToReGenerate) {
				Logic.deleteSymlinkPicture(link);
			}
			// remove all links on the collection to rename
			RealPictureCollection realCollectionToRename = (RealPictureCollection) collectionToRename;
			for (LinkedPictureCollection link : realCollectionToRename.getLinkedBy()) {
				Logic.deleteSymlinkCollection(link);
			}
			// rename in file system
			// http://www.java-examples.com/rename-file-or-directory
			File oldFile = new File(collectionToRename.getFullPath());
			oldFile.renameTo(new File(oldFile.getParent() + File.separator + newName));
			// rename in EMF model
			renameModel(collectionToRename, newName);
			// create all deleted links again
			for (LinkedPicture link : linksToReGenerate) {
				Logic.createSymlinkPicture(link);
			}
			// create all deleted links on the renamed collection again
			for (LinkedPictureCollection link : realCollectionToRename.getLinkedBy()) {
				Logic.createSymlinkCollection(link);
			}
		} else {
			// rename in file system
			// http://www.java-examples.com/rename-file-or-directory
			File oldFile = new File(collectionToRename.getFullPath());
			oldFile.renameTo(new File(oldFile.getParent() + File.separator + newName));
			renameModel(collectionToRename, newName);
		}
		// sort the collections within the parent collection
		Logic.sortSubCollections(collectionToRename.getSuperCollection(), false);

		if (currentState instanceof PictureSwitchingState) {
			((PictureSwitchingState) currentState).updateCollectionLabel();
		}
	}

	private void renameModel(PictureCollection collectionToRename, String newName) {
		// simple version
//		collectionToRename.setName(newName);

		// version with support for notification
		// http://www.vogella.com/tutorials/EclipseEMFPersistence/article.html
		ResourceSet rset = new ResourceSetImpl();
		rset.getResourceFactoryRegistry().getExtensionToFactoryMap().putIfAbsent("xmi", new XMIResourceFactoryImpl());
		RealPictureCollection baseCollection = MainApp.get().getBaseCollection();
		URI uri = URI.createFileURI(baseCollection.getFullPath() + "/model.xmi");
		System.out.println(uri.toFileString());
		Resource res = rset.createResource(uri);
		res.getContents().add(baseCollection);

		EditingDomain domain = new AdapterFactoryEditingDomain(new GalleryAdapterFactory(), new BasicCommandStack(), rset);
		Command set = SetCommand.create(domain, collectionToRename, GalleryPackage.eINSTANCE.getPathElement_Name(), newName);
		domain.getCommandStack().execute(set);

		try {
			res.save(null); // wird überschrieben
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.R;
	}

	@Override
	public String getDescription() {
		return "rename the currently selected collection (both, real and linked collections)";
	}
}
