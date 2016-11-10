/**
 */
package gallery;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Metadata</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gallery.Metadata#getPicture <em>Picture</em>}</li>
 * </ul>
 * </p>
 *
 * @see gallery.GalleryPackage#getMetadata()
 * @model
 * @generated
 */
public interface Metadata extends EObject {
	/**
	 * Returns the value of the '<em><b>Picture</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link gallery.RealPicture#getMetadata <em>Metadata</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Picture</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Picture</em>' container reference.
	 * @see #setPicture(RealPicture)
	 * @see gallery.GalleryPackage#getMetadata_Picture()
	 * @see gallery.RealPicture#getMetadata
	 * @model opposite="metadata" required="true" transient="false"
	 * @generated
	 */
	RealPicture getPicture();

	/**
	 * Sets the value of the '{@link gallery.Metadata#getPicture <em>Picture</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Picture</em>' container reference.
	 * @see #getPicture()
	 * @generated
	 */
	void setPicture(RealPicture value);

} // Metadata
