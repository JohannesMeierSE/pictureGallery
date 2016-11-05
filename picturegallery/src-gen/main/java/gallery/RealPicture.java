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
 * </ul>
 * </p>
 *
 * @see gallery.GalleryPackage#getRealPicture()
 * @model
 * @generated
 */
public interface RealPicture extends Picture {
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

} // RealPicture
