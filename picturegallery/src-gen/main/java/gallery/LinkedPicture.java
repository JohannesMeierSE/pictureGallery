/**
 */
package gallery;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Linked Picture</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gallery.LinkedPicture#getRealPicture <em>Real Picture</em>}</li>
 * </ul>
 * </p>
 *
 * @see gallery.GalleryPackage#getLinkedPicture()
 * @model
 * @generated
 */
public interface LinkedPicture extends Picture {
	/**
	 * Returns the value of the '<em><b>Real Picture</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link gallery.RealPicture#getLinkedBy <em>Linked By</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Real Picture</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Real Picture</em>' reference.
	 * @see #setRealPicture(RealPicture)
	 * @see gallery.GalleryPackage#getLinkedPicture_RealPicture()
	 * @see gallery.RealPicture#getLinkedBy
	 * @model opposite="linkedBy" required="true"
	 * @generated
	 */
	RealPicture getRealPicture();

	/**
	 * Sets the value of the '{@link gallery.LinkedPicture#getRealPicture <em>Real Picture</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Real Picture</em>' reference.
	 * @see #getRealPicture()
	 * @generated
	 */
	void setRealPicture(RealPicture value);

} // LinkedPicture
