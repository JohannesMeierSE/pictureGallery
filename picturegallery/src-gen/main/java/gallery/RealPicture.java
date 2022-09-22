/**
 */
package gallery;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Real Picture</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gallery.RealPicture#getLinkedBy <em>Linked By</em>}</li>
 *   <li>{@link gallery.RealPicture#getMetadata <em>Metadata</em>}</li>
 * </ul>
 * </p>
 *
 * @see gallery.GalleryPackage#getRealPicture()
 * @model
 * @generated
 */
public interface RealPicture extends Picture, PictureWithHash {
	/**
	 * Returns the value of the '<em><b>Linked By</b></em>' reference list.
	 * The list contents are of type {@link gallery.LinkedPicture}.
	 * It is bidirectional and its opposite is '{@link gallery.LinkedPicture#getRealPicture <em>Real Picture</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Linked By</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Linked By</em>' reference list.
	 * @see gallery.GalleryPackage#getRealPicture_LinkedBy()
	 * @see gallery.LinkedPicture#getRealPicture
	 * @model opposite="realPicture"
	 * @generated
	 */
	EList<LinkedPicture> getLinkedBy();

	/**
	 * Returns the value of the '<em><b>Metadata</b></em>' containment reference.
	 * It is bidirectional and its opposite is '{@link gallery.Metadata#getPicture <em>Picture</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Metadata</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Metadata</em>' containment reference.
	 * @see #setMetadata(Metadata)
	 * @see gallery.GalleryPackage#getRealPicture_Metadata()
	 * @see gallery.Metadata#getPicture
	 * @model opposite="picture" containment="true"
	 * @generated
	 */
	Metadata getMetadata();

	/**
	 * Sets the value of the '{@link gallery.RealPicture#getMetadata <em>Metadata</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Metadata</em>' containment reference.
	 * @see #getMetadata()
	 * @generated
	 */
	void setMetadata(Metadata value);

} // RealPicture
