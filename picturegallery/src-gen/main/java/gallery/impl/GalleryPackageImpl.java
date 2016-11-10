/**
 */
package gallery.impl;

import gallery.GalleryFactory;
import gallery.GalleryPackage;
import gallery.LinkedPicture;
import gallery.Metadata;
import gallery.PathElement;
import gallery.Picture;
import gallery.PictureCollection;
import gallery.PictureLibrary;
import gallery.RealPicture;

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
		GalleryPackageImpl theGalleryPackage = (GalleryPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof GalleryPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new GalleryPackageImpl());

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
	public EClass getPictureCollection() {
		return pictureCollectionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPictureCollection_SubCollections() {
		return (EReference)pictureCollectionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPictureCollection_SuperCollection() {
		return (EReference)pictureCollectionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPictureCollection_Pictures() {
		return (EReference)pictureCollectionEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPictureCollection_Library() {
		return (EReference)pictureCollectionEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPicture() {
		return pictureEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPicture_Collection() {
		return (EReference)pictureEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPicture_FileExtension() {
		return (EAttribute)pictureEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getPicture__GetMetadata() {
		return pictureEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRealPicture() {
		return realPictureEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRealPicture_LinkedBy() {
		return (EReference)realPictureEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRealPicture_Metadata() {
		return (EReference)realPictureEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getLinkedPicture() {
		return linkedPictureEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLinkedPicture_RealPicture() {
		return (EReference)linkedPictureEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPathElement() {
		return pathElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPathElement_Name() {
		return (EAttribute)pathElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getPathElement__GetFullPath() {
		return pathElementEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPictureLibrary() {
		return pictureLibraryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPictureLibrary_BasePath() {
		return (EAttribute)pictureLibraryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPictureLibrary_BaseCollection() {
		return (EReference)pictureLibraryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPictureLibrary_Name() {
		return (EAttribute)pictureLibraryEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMetadata() {
		return metadataEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMetadata_Picture() {
		return (EReference)metadataEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
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
		createEReference(pictureCollectionEClass, PICTURE_COLLECTION__SUB_COLLECTIONS);
		createEReference(pictureCollectionEClass, PICTURE_COLLECTION__SUPER_COLLECTION);
		createEReference(pictureCollectionEClass, PICTURE_COLLECTION__PICTURES);
		createEReference(pictureCollectionEClass, PICTURE_COLLECTION__LIBRARY);

		pictureEClass = createEClass(PICTURE);
		createEReference(pictureEClass, PICTURE__COLLECTION);
		createEAttribute(pictureEClass, PICTURE__FILE_EXTENSION);
		createEOperation(pictureEClass, PICTURE___GET_METADATA);

		realPictureEClass = createEClass(REAL_PICTURE);
		createEReference(realPictureEClass, REAL_PICTURE__LINKED_BY);
		createEReference(realPictureEClass, REAL_PICTURE__METADATA);

		linkedPictureEClass = createEClass(LINKED_PICTURE);
		createEReference(linkedPictureEClass, LINKED_PICTURE__REAL_PICTURE);

		pathElementEClass = createEClass(PATH_ELEMENT);
		createEAttribute(pathElementEClass, PATH_ELEMENT__NAME);
		createEOperation(pathElementEClass, PATH_ELEMENT___GET_FULL_PATH);

		pictureLibraryEClass = createEClass(PICTURE_LIBRARY);
		createEAttribute(pictureLibraryEClass, PICTURE_LIBRARY__BASE_PATH);
		createEReference(pictureLibraryEClass, PICTURE_LIBRARY__BASE_COLLECTION);
		createEAttribute(pictureLibraryEClass, PICTURE_LIBRARY__NAME);

		metadataEClass = createEClass(METADATA);
		createEReference(metadataEClass, METADATA__PICTURE);
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
		linkedPictureEClass.getESuperTypes().add(this.getPicture());

		// Initialize classes, features, and operations; add parameters
		initEClass(pictureCollectionEClass, PictureCollection.class, "PictureCollection", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getPictureCollection_SubCollections(), this.getPictureCollection(), this.getPictureCollection_SuperCollection(), "subCollections", null, 0, -1, PictureCollection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPictureCollection_SuperCollection(), this.getPictureCollection(), this.getPictureCollection_SubCollections(), "superCollection", null, 0, 1, PictureCollection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPictureCollection_Pictures(), this.getPicture(), this.getPicture_Collection(), "pictures", null, 0, -1, PictureCollection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPictureCollection_Library(), this.getPictureLibrary(), this.getPictureLibrary_BaseCollection(), "library", null, 0, 1, PictureCollection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(pictureEClass, Picture.class, "Picture", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getPicture_Collection(), this.getPictureCollection(), this.getPictureCollection_Pictures(), "collection", null, 1, 1, Picture.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPicture_FileExtension(), ecorePackage.getEString(), "fileExtension", null, 1, 1, Picture.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getPicture__GetMetadata(), this.getMetadata(), "getMetadata", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(realPictureEClass, RealPicture.class, "RealPicture", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getRealPicture_LinkedBy(), this.getLinkedPicture(), this.getLinkedPicture_RealPicture(), "linkedBy", null, 0, -1, RealPicture.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRealPicture_Metadata(), this.getMetadata(), this.getMetadata_Picture(), "metadata", null, 0, 1, RealPicture.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(linkedPictureEClass, LinkedPicture.class, "LinkedPicture", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getLinkedPicture_RealPicture(), this.getRealPicture(), this.getRealPicture_LinkedBy(), "realPicture", null, 1, 1, LinkedPicture.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(pathElementEClass, PathElement.class, "PathElement", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPathElement_Name(), ecorePackage.getEString(), "name", null, 1, 1, PathElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getPathElement__GetFullPath(), ecorePackage.getEString(), "getFullPath", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(pictureLibraryEClass, PictureLibrary.class, "PictureLibrary", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPictureLibrary_BasePath(), ecorePackage.getEString(), "basePath", null, 1, 1, PictureLibrary.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPictureLibrary_BaseCollection(), this.getPictureCollection(), this.getPictureCollection_Library(), "baseCollection", null, 1, 1, PictureLibrary.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPictureLibrary_Name(), ecorePackage.getEString(), "name", null, 0, 1, PictureLibrary.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(metadataEClass, Metadata.class, "Metadata", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getMetadata_Picture(), this.getRealPicture(), this.getRealPicture_Metadata(), "picture", null, 1, 1, Metadata.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} //GalleryPackageImpl
