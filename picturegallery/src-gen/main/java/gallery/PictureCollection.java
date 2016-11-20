/**
 */
package gallery;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Picture Collection</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gallery.PictureCollection#getSuperCollection <em>Super Collection</em>}</li>
 * </ul>
 * </p>
 *
 * @see gallery.GalleryPackage#getPictureCollection()
 * @model abstract="true"
 * @generated
 */
public interface PictureCollection extends PathElement {
	/**
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sub Collections</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	EList<PictureCollection> getSubCollections();

	/**
	 * Returns the value of the '<em><b>Super Collection</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link gallery.RealPictureCollection#getSubCollections <em>Sub Collections</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Super Collection</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Super Collection</em>' container reference.
	 * @see #setSuperCollection(RealPictureCollection)
	 * @see gallery.GalleryPackage#getPictureCollection_SuperCollection()
	 * @see gallery.RealPictureCollection#getSubCollections
	 * @model opposite="subCollections" transient="false"
	 * @generated
	 */
	RealPictureCollection getSuperCollection();

	/**
	 * Sets the value of the '{@link gallery.PictureCollection#getSuperCollection <em>Super Collection</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Super Collection</em>' container reference.
	 * @see #getSuperCollection()
	 * @generated
	 */
	void setSuperCollection(RealPictureCollection value);

	/**
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Pictures</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	EList<Picture> getPictures();

} // PictureCollection
