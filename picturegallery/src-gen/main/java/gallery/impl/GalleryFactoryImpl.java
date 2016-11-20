/**
 */
package gallery.impl;

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
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RealPicture createRealPicture() {
		RealPictureImpl realPicture = new RealPictureImpl();
		return realPicture;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LinkedPicture createLinkedPicture() {
		LinkedPictureImpl linkedPicture = new LinkedPictureImpl();
		return linkedPicture;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PictureLibrary createPictureLibrary() {
		PictureLibraryImpl pictureLibrary = new PictureLibraryImpl();
		return pictureLibrary;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Metadata createMetadata() {
		MetadataImpl metadata = new MetadataImpl();
		return metadata;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RealPictureCollection createRealPictureCollection() {
		RealPictureCollectionImpl realPictureCollection = new RealPictureCollectionImpl();
		return realPictureCollection;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LinkedPictureCollection createLinkedPictureCollection() {
		LinkedPictureCollectionImpl linkedPictureCollection = new LinkedPictureCollectionImpl();
		return linkedPictureCollection;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
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
