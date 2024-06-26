package picturegallery;

/*-
 * BEGIN-LICENSE
 * picturegallery
 * %%
 * Copyright (C) 2016 - 2024 Johannes Meier
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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import gallery.GalleryPackage;
import gallery.PictureLibrary;
import gallery.RealPictureCollection;

import java.io.File;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.junit.Test;

public class SimpleTest {

	@Test
	public void test() {
		String baseDir = "/home/johannes/Documents/photo/2000";
		String emfModelPath = baseDir + "/model.xmi";
		assertTrue(new File(emfModelPath).exists());

		// http://www.vogella.com/tutorials/EclipseEMFPersistence/article.html
		GalleryPackage.eINSTANCE.eClass(); // init the EMF stuff
		ResourceSet rset = new ResourceSetImpl();
		rset.getResourceFactoryRegistry().getExtensionToFactoryMap().putIfAbsent("xmi", new XMIResourceFactoryImpl());
		URI uri = URI.createFileURI(emfModelPath);
		assertNotNull(uri);
		Resource modelResource = rset.getResource(uri, true);
		assertNotNull(modelResource);

		// initialize the EMF model
		RealPictureCollection baseCollection;
		if (modelResource == null || modelResource.getContents().isEmpty()) {
			modelResource = rset.createResource(uri);
			baseCollection = Logic.createEmptyLibrary(baseDir);
			modelResource.getContents().add(baseCollection.getLibrary());
		} else {
			baseCollection = ((PictureLibrary) modelResource.getContents().get(0)).getBaseCollection();
		}
		assertNotNull(baseCollection);
	}

}
