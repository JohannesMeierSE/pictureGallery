/**
 */
package gallery.impl;

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

import gallery.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class GalleryFactoryImpl extends EFactoryImpl implements GalleryFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static GalleryFactory init() {
		try {
			GalleryFactory theGalleryFactory = (GalleryFactory)EPackage.Registry.INSTANCE.getEFactory(GalleryPackage.eNS_URI);
			if (theGalleryFactory != null) {
				return theGalleryFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new GalleryFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GalleryFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case GalleryPackage.REAL_PICTURE: return createRealPicture();
			case GalleryPackage.LINKED_PICTURE: return createLinkedPicture();
			case GalleryPackage.PICTURE_LIBRARY: return createPictureLibrary();
			case GalleryPackage.METADATA: return createMetadata();
			case GalleryPackage.REAL_PICTURE_COLLECTION: return createRealPictureCollection();
			case GalleryPackage.LINKED_PICTURE_COLLECTION: return createLinkedPictureCollection();
			case GalleryPackage.DELETED_PICTURE: return createDeletedPicture();
			case GalleryPackage.TAG: return createTag();
			case GalleryPackage.TAG_CATEGORY: return createTagCategory();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public RealPicture createRealPicture() {
		RealPictureImpl realPicture = new RealPictureImpl();
		return realPicture;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LinkedPicture createLinkedPicture() {
		LinkedPictureImpl linkedPicture = new LinkedPictureImpl();
		return linkedPicture;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PictureLibrary createPictureLibrary() {
		PictureLibraryImpl pictureLibrary = new PictureLibraryImpl();
		return pictureLibrary;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Metadata createMetadata() {
		MetadataImpl metadata = new MetadataImpl();
		return metadata;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public RealPictureCollection createRealPictureCollection() {
		RealPictureCollectionImpl realPictureCollection = new RealPictureCollectionImpl();
		return realPictureCollection;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LinkedPictureCollection createLinkedPictureCollection() {
		LinkedPictureCollectionImpl linkedPictureCollection = new LinkedPictureCollectionImpl();
		return linkedPictureCollection;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DeletedPicture createDeletedPicture() {
		DeletedPictureImpl deletedPicture = new DeletedPictureImpl();
		return deletedPicture;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Tag createTag() {
		TagImpl tag = new TagImpl();
		return tag;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public TagCategory createTagCategory() {
		TagCategoryImpl tagCategory = new TagCategoryImpl();
		return tagCategory;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public GalleryPackage getGalleryPackage() {
		return (GalleryPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static GalleryPackage getPackage() {
		return GalleryPackage.eINSTANCE;
	}

} //GalleryFactoryImpl
