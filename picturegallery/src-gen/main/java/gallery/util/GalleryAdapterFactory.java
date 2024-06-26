/**
 */
package gallery.util;

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

import gallery.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see gallery.GalleryPackage
 * @generated
 */
public class GalleryAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static GalleryPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GalleryAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = GalleryPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected GallerySwitch<Adapter> modelSwitch =
		new GallerySwitch<Adapter>() {
			@Override
			public Adapter casePictureCollection(PictureCollection object) {
				return createPictureCollectionAdapter();
			}
			@Override
			public Adapter casePicture(Picture object) {
				return createPictureAdapter();
			}
			@Override
			public Adapter caseRealPicture(RealPicture object) {
				return createRealPictureAdapter();
			}
			@Override
			public Adapter caseLinkedPicture(LinkedPicture object) {
				return createLinkedPictureAdapter();
			}
			@Override
			public Adapter casePathElement(PathElement object) {
				return createPathElementAdapter();
			}
			@Override
			public Adapter casePictureLibrary(PictureLibrary object) {
				return createPictureLibraryAdapter();
			}
			@Override
			public Adapter caseMetadata(Metadata object) {
				return createMetadataAdapter();
			}
			@Override
			public Adapter caseRealPictureCollection(RealPictureCollection object) {
				return createRealPictureCollectionAdapter();
			}
			@Override
			public Adapter caseLinkedPictureCollection(LinkedPictureCollection object) {
				return createLinkedPictureCollectionAdapter();
			}
			@Override
			public Adapter caseDeletedPicture(DeletedPicture object) {
				return createDeletedPictureAdapter();
			}
			@Override
			public Adapter casePictureWithHash(PictureWithHash object) {
				return createPictureWithHashAdapter();
			}
			@Override
			public Adapter caseTag(Tag object) {
				return createTagAdapter();
			}
			@Override
			public Adapter caseTagCategory(TagCategory object) {
				return createTagCategoryAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link gallery.PictureCollection <em>Picture Collection</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gallery.PictureCollection
	 * @generated
	 */
	public Adapter createPictureCollectionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gallery.Picture <em>Picture</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gallery.Picture
	 * @generated
	 */
	public Adapter createPictureAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gallery.RealPicture <em>Real Picture</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gallery.RealPicture
	 * @generated
	 */
	public Adapter createRealPictureAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gallery.LinkedPicture <em>Linked Picture</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gallery.LinkedPicture
	 * @generated
	 */
	public Adapter createLinkedPictureAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gallery.PathElement <em>Path Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gallery.PathElement
	 * @generated
	 */
	public Adapter createPathElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gallery.PictureLibrary <em>Picture Library</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gallery.PictureLibrary
	 * @generated
	 */
	public Adapter createPictureLibraryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gallery.Metadata <em>Metadata</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gallery.Metadata
	 * @generated
	 */
	public Adapter createMetadataAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gallery.RealPictureCollection <em>Real Picture Collection</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gallery.RealPictureCollection
	 * @generated
	 */
	public Adapter createRealPictureCollectionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gallery.LinkedPictureCollection <em>Linked Picture Collection</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gallery.LinkedPictureCollection
	 * @generated
	 */
	public Adapter createLinkedPictureCollectionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gallery.DeletedPicture <em>Deleted Picture</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gallery.DeletedPicture
	 * @generated
	 */
	public Adapter createDeletedPictureAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gallery.PictureWithHash <em>Picture With Hash</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gallery.PictureWithHash
	 * @generated
	 */
	public Adapter createPictureWithHashAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gallery.Tag <em>Tag</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gallery.Tag
	 * @generated
	 */
	public Adapter createTagAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gallery.TagCategory <em>Tag Category</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gallery.TagCategory
	 * @generated
	 */
	public Adapter createTagCategoryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //GalleryAdapterFactory
