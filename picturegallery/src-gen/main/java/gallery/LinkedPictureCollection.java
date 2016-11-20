/**
 */
package gallery;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Linked Picture Collection</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gallery.LinkedPictureCollection#getRealCollection <em>Real Collection</em>}</li>
 * </ul>
 * </p>
 *
 * @see gallery.GalleryPackage#getLinkedPictureCollection()
 * @model
 * @generated
 */
public interface LinkedPictureCollection extends PictureCollection {
	/**
	 * Returns the value of the '<em><b>Real Collection</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link gallery.RealPictureCollection#getLinkedBy <em>Linked By</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Real Collection</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Real Collection</em>' reference.
	 * @see #setRealCollection(RealPictureCollection)
	 * @see gallery.GalleryPackage#getLinkedPictureCollection_RealCollection()
	 * @see gallery.RealPictureCollection#getLinkedBy
	 * @model opposite="linkedBy" required="true"
	 * @generated
	 */
	RealPictureCollection getRealCollection();

	/**
	 * Sets the value of the '{@link gallery.LinkedPictureCollection#getRealCollection <em>Real Collection</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Real Collection</em>' reference.
	 * @see #getRealCollection()
	 * @generated
	 */
	void setRealCollection(RealPictureCollection value);

} // LinkedPictureCollection
