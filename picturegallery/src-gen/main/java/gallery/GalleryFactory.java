/**
 */
package gallery;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see gallery.GalleryPackage
 * @generated
 */
public interface GalleryFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	GalleryFactory eINSTANCE = gallery.impl.GalleryFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Real Picture</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Real Picture</em>'.
	 * @generated
	 */
	RealPicture createRealPicture();

	/**
	 * Returns a new object of class '<em>Linked Picture</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Linked Picture</em>'.
	 * @generated
	 */
	LinkedPicture createLinkedPicture();

	/**
	 * Returns a new object of class '<em>Picture Library</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Picture Library</em>'.
	 * @generated
	 */
	PictureLibrary createPictureLibrary();

	/**
	 * Returns a new object of class '<em>Metadata</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Metadata</em>'.
	 * @generated
	 */
	Metadata createMetadata();

	/**
	 * Returns a new object of class '<em>Real Picture Collection</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Real Picture Collection</em>'.
	 * @generated
	 */
	RealPictureCollection createRealPictureCollection();

	/**
	 * Returns a new object of class '<em>Linked Picture Collection</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Linked Picture Collection</em>'.
	 * @generated
	 */
	LinkedPictureCollection createLinkedPictureCollection();

	/**
	 * Returns a new object of class '<em>Deleted Picture</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Deleted Picture</em>'.
	 * @generated
	 */
	DeletedPicture createDeletedPicture();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	GalleryPackage getGalleryPackage();

} //GalleryFactory
