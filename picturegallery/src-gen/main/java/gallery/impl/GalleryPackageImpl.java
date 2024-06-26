/**
 */
package gallery.impl;

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

import gallery.DeletedPicture;
import gallery.GalleryFactory;
import gallery.GalleryPackage;
import gallery.LinkedPicture;
import gallery.LinkedPictureCollection;
import gallery.Metadata;
import gallery.PathElement;
import gallery.Picture;
import gallery.PictureCollection;
import gallery.PictureLibrary;
import gallery.PictureWithHash;
import gallery.RealPicture;

import gallery.RealPictureCollection;
import gallery.Tag;
import gallery.TagCategory;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class GalleryPackageImpl extends EPackageImpl implements GalleryPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass pictureCollectionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass pictureEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass realPictureEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass linkedPictureEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass pathElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass pictureLibraryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass metadataEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass realPictureCollectionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass linkedPictureCollectionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass deletedPictureEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass pictureWithHashEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass tagEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass tagCategoryEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see gallery.GalleryPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private GalleryPackageImpl() {
		super(eNS_URI, GalleryFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 *
	 * <p>This method is used to initialize {@link GalleryPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static GalleryPackage init() {
		if (isInited) return (GalleryPackage)EPackage.Registry.INSTANCE.getEPackage(GalleryPackage.eNS_URI);

		// Obtain or create and register package
		Object registeredGalleryPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
		GalleryPackageImpl theGalleryPackage = registeredGalleryPackage instanceof GalleryPackageImpl ? (GalleryPackageImpl)registeredGalleryPackage : new GalleryPackageImpl();

		isInited = true;

		// Create package meta-data objects
		theGalleryPackage.createPackageContents();

		// Initialize created meta-data
		theGalleryPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theGalleryPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(GalleryPackage.eNS_URI, theGalleryPackage);
		return theGalleryPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPictureCollection() {
		return pictureCollectionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPictureCollection_SuperCollection() {
		return (EReference)pictureCollectionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getPictureCollection__GetPictures() {
		return pictureCollectionEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getPictureCollection__GetSubCollections() {
		return pictureCollectionEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPicture() {
		return pictureEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPicture_Collection() {
		return (EReference)pictureEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPicture_Tags() {
		return (EReference)pictureEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPicture_FileExtension() {
		return (EAttribute)pictureEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getPicture__GetMetadata() {
		return pictureEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getPicture__GetHash() {
		return pictureEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getPicture__GetHashFast() {
		return pictureEClass.getEOperations().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getRealPicture() {
		return realPictureEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getRealPicture_LinkedBy() {
		return (EReference)realPictureEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getRealPicture_Metadata() {
		return (EReference)realPictureEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getLinkedPicture() {
		return linkedPictureEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getLinkedPicture_RealPicture() {
		return (EReference)linkedPictureEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPathElement() {
		return pathElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPathElement_Name() {
		return (EAttribute)pathElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getPathElement__GetFullPath() {
		return pathElementEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getPathElement__GetRelativePath() {
		return pathElementEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getPathElement__GetRelativePathWithoutBase() {
		return pathElementEClass.getEOperations().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPictureLibrary() {
		return pictureLibraryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPictureLibrary_BasePath() {
		return (EAttribute)pictureLibraryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPictureLibrary_BaseCollection() {
		return (EReference)pictureLibraryEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPictureLibrary_DeletedPictures() {
		return (EReference)pictureLibraryEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPictureLibrary_TagCategories() {
		return (EReference)pictureLibraryEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPictureLibrary_Name() {
		return (EAttribute)pictureLibraryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getMetadata() {
		return metadataEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getMetadata_Picture() {
		return (EReference)metadataEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMetadata_Size() {
		return (EAttribute)metadataEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMetadata_Landscape() {
		return (EAttribute)metadataEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMetadata_Created() {
		return (EAttribute)metadataEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMetadata_Height() {
		return (EAttribute)metadataEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMetadata_Width() {
		return (EAttribute)metadataEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMetadata_Camera() {
		return (EAttribute)metadataEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getRealPictureCollection() {
		return realPictureCollectionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getRealPictureCollection_LinkedBy() {
		return (EReference)realPictureCollectionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getRealPictureCollection_Library() {
		return (EReference)realPictureCollectionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getRealPictureCollection_Pictures() {
		return (EReference)realPictureCollectionEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getRealPictureCollection_SubCollections() {
		return (EReference)realPictureCollectionEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getLinkedPictureCollection() {
		return linkedPictureCollectionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getLinkedPictureCollection_RealCollection() {
		return (EReference)linkedPictureCollectionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getDeletedPicture() {
		return deletedPictureEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDeletedPicture_RelativePath() {
		return (EAttribute)deletedPictureEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getDeletedPicture_Library() {
		return (EReference)deletedPictureEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPictureWithHash() {
		return pictureWithHashEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPictureWithHash_Hash() {
		return (EAttribute)pictureWithHashEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPictureWithHash_HashFast() {
		return (EAttribute)pictureWithHashEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getTag() {
		return tagEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTag_Value() {
		return (EAttribute)tagEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getTag_Picture() {
		return (EReference)tagEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getTag_Category() {
		return (EReference)tagEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getTagCategory() {
		return tagCategoryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTagCategory_Name() {
		return (EAttribute)tagCategoryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getTagCategory_Library() {
		return (EReference)tagCategoryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getTagCategory_TagInstances() {
		return (EReference)tagCategoryEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public GalleryFactory getGalleryFactory() {
		return (GalleryFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		pictureCollectionEClass = createEClass(PICTURE_COLLECTION);
		createEReference(pictureCollectionEClass, PICTURE_COLLECTION__SUPER_COLLECTION);
		createEOperation(pictureCollectionEClass, PICTURE_COLLECTION___GET_PICTURES);
		createEOperation(pictureCollectionEClass, PICTURE_COLLECTION___GET_SUB_COLLECTIONS);

		pictureEClass = createEClass(PICTURE);
		createEAttribute(pictureEClass, PICTURE__FILE_EXTENSION);
		createEReference(pictureEClass, PICTURE__COLLECTION);
		createEReference(pictureEClass, PICTURE__TAGS);
		createEOperation(pictureEClass, PICTURE___GET_METADATA);
		createEOperation(pictureEClass, PICTURE___GET_HASH);
		createEOperation(pictureEClass, PICTURE___GET_HASH_FAST);

		realPictureEClass = createEClass(REAL_PICTURE);
		createEReference(realPictureEClass, REAL_PICTURE__LINKED_BY);
		createEReference(realPictureEClass, REAL_PICTURE__METADATA);

		linkedPictureEClass = createEClass(LINKED_PICTURE);
		createEReference(linkedPictureEClass, LINKED_PICTURE__REAL_PICTURE);

		pathElementEClass = createEClass(PATH_ELEMENT);
		createEAttribute(pathElementEClass, PATH_ELEMENT__NAME);
		createEOperation(pathElementEClass, PATH_ELEMENT___GET_FULL_PATH);
		createEOperation(pathElementEClass, PATH_ELEMENT___GET_RELATIVE_PATH);
		createEOperation(pathElementEClass, PATH_ELEMENT___GET_RELATIVE_PATH_WITHOUT_BASE);

		pictureLibraryEClass = createEClass(PICTURE_LIBRARY);
		createEAttribute(pictureLibraryEClass, PICTURE_LIBRARY__BASE_PATH);
		createEAttribute(pictureLibraryEClass, PICTURE_LIBRARY__NAME);
		createEReference(pictureLibraryEClass, PICTURE_LIBRARY__BASE_COLLECTION);
		createEReference(pictureLibraryEClass, PICTURE_LIBRARY__DELETED_PICTURES);
		createEReference(pictureLibraryEClass, PICTURE_LIBRARY__TAG_CATEGORIES);

		metadataEClass = createEClass(METADATA);
		createEReference(metadataEClass, METADATA__PICTURE);
		createEAttribute(metadataEClass, METADATA__SIZE);
		createEAttribute(metadataEClass, METADATA__LANDSCAPE);
		createEAttribute(metadataEClass, METADATA__CREATED);
		createEAttribute(metadataEClass, METADATA__HEIGHT);
		createEAttribute(metadataEClass, METADATA__WIDTH);
		createEAttribute(metadataEClass, METADATA__CAMERA);

		realPictureCollectionEClass = createEClass(REAL_PICTURE_COLLECTION);
		createEReference(realPictureCollectionEClass, REAL_PICTURE_COLLECTION__LINKED_BY);
		createEReference(realPictureCollectionEClass, REAL_PICTURE_COLLECTION__LIBRARY);
		createEReference(realPictureCollectionEClass, REAL_PICTURE_COLLECTION__PICTURES);
		createEReference(realPictureCollectionEClass, REAL_PICTURE_COLLECTION__SUB_COLLECTIONS);

		linkedPictureCollectionEClass = createEClass(LINKED_PICTURE_COLLECTION);
		createEReference(linkedPictureCollectionEClass, LINKED_PICTURE_COLLECTION__REAL_COLLECTION);

		deletedPictureEClass = createEClass(DELETED_PICTURE);
		createEAttribute(deletedPictureEClass, DELETED_PICTURE__RELATIVE_PATH);
		createEReference(deletedPictureEClass, DELETED_PICTURE__LIBRARY);

		pictureWithHashEClass = createEClass(PICTURE_WITH_HASH);
		createEAttribute(pictureWithHashEClass, PICTURE_WITH_HASH__HASH);
		createEAttribute(pictureWithHashEClass, PICTURE_WITH_HASH__HASH_FAST);

		tagEClass = createEClass(TAG);
		createEAttribute(tagEClass, TAG__VALUE);
		createEReference(tagEClass, TAG__PICTURE);
		createEReference(tagEClass, TAG__CATEGORY);

		tagCategoryEClass = createEClass(TAG_CATEGORY);
		createEAttribute(tagCategoryEClass, TAG_CATEGORY__NAME);
		createEReference(tagCategoryEClass, TAG_CATEGORY__LIBRARY);
		createEReference(tagCategoryEClass, TAG_CATEGORY__TAG_INSTANCES);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		pictureCollectionEClass.getESuperTypes().add(this.getPathElement());
		pictureEClass.getESuperTypes().add(this.getPathElement());
		realPictureEClass.getESuperTypes().add(this.getPicture());
		realPictureEClass.getESuperTypes().add(this.getPictureWithHash());
		linkedPictureEClass.getESuperTypes().add(this.getPicture());
		realPictureCollectionEClass.getESuperTypes().add(this.getPictureCollection());
		linkedPictureCollectionEClass.getESuperTypes().add(this.getPictureCollection());
		deletedPictureEClass.getESuperTypes().add(this.getPictureWithHash());

		// Initialize classes, features, and operations; add parameters
		initEClass(pictureCollectionEClass, PictureCollection.class, "PictureCollection", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getPictureCollection_SuperCollection(), this.getRealPictureCollection(), this.getRealPictureCollection_SubCollections(), "superCollection", null, 0, 1, PictureCollection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getPictureCollection__GetPictures(), this.getPicture(), "getPictures", 0, -1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getPictureCollection__GetSubCollections(), this.getPictureCollection(), "getSubCollections", 0, -1, IS_UNIQUE, IS_ORDERED);

		initEClass(pictureEClass, Picture.class, "Picture", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPicture_FileExtension(), ecorePackage.getEString(), "fileExtension", null, 1, 1, Picture.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPicture_Collection(), this.getRealPictureCollection(), this.getRealPictureCollection_Pictures(), "collection", null, 1, 1, Picture.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPicture_Tags(), this.getTag(), this.getTag_Picture(), "tags", null, 0, -1, Picture.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getPicture__GetMetadata(), this.getMetadata(), "getMetadata", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getPicture__GetHash(), ecorePackage.getEString(), "getHash", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getPicture__GetHashFast(), ecorePackage.getEString(), "getHashFast", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(realPictureEClass, RealPicture.class, "RealPicture", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getRealPicture_LinkedBy(), this.getLinkedPicture(), this.getLinkedPicture_RealPicture(), "linkedBy", null, 0, -1, RealPicture.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRealPicture_Metadata(), this.getMetadata(), this.getMetadata_Picture(), "metadata", null, 0, 1, RealPicture.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(linkedPictureEClass, LinkedPicture.class, "LinkedPicture", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getLinkedPicture_RealPicture(), this.getRealPicture(), this.getRealPicture_LinkedBy(), "realPicture", null, 1, 1, LinkedPicture.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(pathElementEClass, PathElement.class, "PathElement", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPathElement_Name(), ecorePackage.getEString(), "name", null, 1, 1, PathElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getPathElement__GetFullPath(), ecorePackage.getEString(), "getFullPath", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getPathElement__GetRelativePath(), ecorePackage.getEString(), "getRelativePath", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getPathElement__GetRelativePathWithoutBase(), ecorePackage.getEString(), "getRelativePathWithoutBase", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(pictureLibraryEClass, PictureLibrary.class, "PictureLibrary", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPictureLibrary_BasePath(), ecorePackage.getEString(), "basePath", null, 1, 1, PictureLibrary.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPictureLibrary_Name(), ecorePackage.getEString(), "name", null, 0, 1, PictureLibrary.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPictureLibrary_BaseCollection(), this.getRealPictureCollection(), this.getRealPictureCollection_Library(), "baseCollection", null, 1, 1, PictureLibrary.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPictureLibrary_DeletedPictures(), this.getDeletedPicture(), this.getDeletedPicture_Library(), "deletedPictures", null, 0, -1, PictureLibrary.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPictureLibrary_TagCategories(), this.getTagCategory(), this.getTagCategory_Library(), "tagCategories", null, 0, -1, PictureLibrary.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(metadataEClass, Metadata.class, "Metadata", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getMetadata_Picture(), this.getRealPicture(), this.getRealPicture_Metadata(), "picture", null, 1, 1, Metadata.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMetadata_Size(), ecorePackage.getEInt(), "size", "-1", 0, 1, Metadata.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMetadata_Landscape(), ecorePackage.getEBoolean(), "landscape", "true", 0, 1, Metadata.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMetadata_Created(), ecorePackage.getEDate(), "created", null, 0, 1, Metadata.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMetadata_Height(), ecorePackage.getEInt(), "height", "-1", 0, 1, Metadata.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMetadata_Width(), ecorePackage.getEInt(), "width", "-1", 0, 1, Metadata.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMetadata_Camera(), ecorePackage.getEString(), "camera", null, 0, 1, Metadata.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(realPictureCollectionEClass, RealPictureCollection.class, "RealPictureCollection", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getRealPictureCollection_LinkedBy(), this.getLinkedPictureCollection(), this.getLinkedPictureCollection_RealCollection(), "linkedBy", null, 0, -1, RealPictureCollection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRealPictureCollection_Library(), this.getPictureLibrary(), this.getPictureLibrary_BaseCollection(), "library", null, 0, 1, RealPictureCollection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRealPictureCollection_Pictures(), this.getPicture(), this.getPicture_Collection(), "pictures", null, 0, -1, RealPictureCollection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRealPictureCollection_SubCollections(), this.getPictureCollection(), this.getPictureCollection_SuperCollection(), "subCollections", null, 0, -1, RealPictureCollection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(linkedPictureCollectionEClass, LinkedPictureCollection.class, "LinkedPictureCollection", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getLinkedPictureCollection_RealCollection(), this.getRealPictureCollection(), this.getRealPictureCollection_LinkedBy(), "realCollection", null, 1, 1, LinkedPictureCollection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(deletedPictureEClass, DeletedPicture.class, "DeletedPicture", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDeletedPicture_RelativePath(), ecorePackage.getEString(), "relativePath", null, 0, 1, DeletedPicture.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDeletedPicture_Library(), this.getPictureLibrary(), this.getPictureLibrary_DeletedPictures(), "library", null, 1, 1, DeletedPicture.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(pictureWithHashEClass, PictureWithHash.class, "PictureWithHash", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPictureWithHash_Hash(), ecorePackage.getEString(), "hash", null, 0, 1, PictureWithHash.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPictureWithHash_HashFast(), ecorePackage.getEString(), "hashFast", null, 0, 1, PictureWithHash.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(tagEClass, Tag.class, "Tag", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getTag_Value(), ecorePackage.getEString(), "value", null, 0, 1, Tag.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getTag_Picture(), this.getPicture(), this.getPicture_Tags(), "picture", null, 1, 1, Tag.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getTag_Category(), this.getTagCategory(), this.getTagCategory_TagInstances(), "category", null, 1, 1, Tag.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(tagCategoryEClass, TagCategory.class, "TagCategory", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getTagCategory_Name(), ecorePackage.getEString(), "name", null, 0, 1, TagCategory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getTagCategory_Library(), this.getPictureLibrary(), this.getPictureLibrary_TagCategories(), "library", null, 1, 1, TagCategory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getTagCategory_TagInstances(), this.getTag(), this.getTag_Category(), "tagInstances", null, 0, -1, TagCategory.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} //GalleryPackageImpl
