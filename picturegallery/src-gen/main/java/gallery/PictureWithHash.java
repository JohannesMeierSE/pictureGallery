/**
 */
package gallery;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Picture With Hash</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gallery.PictureWithHash#getHash <em>Hash</em>}</li>
 *   <li>{@link gallery.PictureWithHash#getHashFast <em>Hash Fast</em>}</li>
 * </ul>
 * </p>
 *
 * @see gallery.GalleryPackage#getPictureWithHash()
 * @model abstract="true"
 * @generated
 */
public interface PictureWithHash extends EObject {
	/**
	 * Returns the value of the '<em><b>Hash</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Hash</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Hash</em>' attribute.
	 * @see #setHash(String)
	 * @see gallery.GalleryPackage#getPictureWithHash_Hash()
	 * @model
	 * @generated
	 */
	String getHash();

	/**
	 * Sets the value of the '{@link gallery.PictureWithHash#getHash <em>Hash</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Hash</em>' attribute.
	 * @see #getHash()
	 * @generated
	 */
	void setHash(String value);

	/**
	 * Returns the value of the '<em><b>Hash Fast</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Hash Fast</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Hash Fast</em>' attribute.
	 * @see #setHashFast(String)
	 * @see gallery.GalleryPackage#getPictureWithHash_HashFast()
	 * @model
	 * @generated
	 */
	String getHashFast();

	/**
	 * Sets the value of the '{@link gallery.PictureWithHash#getHashFast <em>Hash Fast</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Hash Fast</em>' attribute.
	 * @see #getHashFast()
	 * @generated
	 */
	void setHashFast(String value);

} // PictureWithHash
