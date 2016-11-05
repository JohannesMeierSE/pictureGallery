/**
 */
package gallery;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Picture Library</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gallery.PictureLibrary#getBasePath <em>Base Path</em>}</li>
 *   <li>{@link gallery.PictureLibrary#getBaseCollection <em>Base Collection</em>}</li>
 *   <li>{@link gallery.PictureLibrary#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see gallery.GalleryPackage#getPictureLibrary()
 * @model
 * @generated
 */
public interface PictureLibrary extends EObject {
	/**
	 * Returns the value of the '<em><b>Base Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Base Path</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Path</em>' attribute.
	 * @see #setBasePath(String)
	 * @see gallery.GalleryPackage#getPictureLibrary_BasePath()
	 * @model required="true"
	 * @generated
	 */
	String getBasePath();

	/**
	 * Sets the value of the '{@link gallery.PictureLibrary#getBasePath <em>Base Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Path</em>' attribute.
	 * @see #getBasePath()
	 * @generated
	 */
	void setBasePath(String value);

	/**
	 * Returns the value of the '<em><b>Base Collection</b></em>' containment reference.
	 * It is bidirectional and its opposite is '{@link gallery.PictureCollection#getLibrary <em>Library</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Base Collection</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Collection</em>' containment reference.
	 * @see #setBaseCollection(PictureCollection)
	 * @see gallery.GalleryPackage#getPictureLibrary_BaseCollection()
	 * @see gallery.PictureCollection#getLibrary
	 * @model opposite="library" containment="true" required="true"
	 * @generated
	 */
	PictureCollection getBaseCollection();

	/**
	 * Sets the value of the '{@link gallery.PictureLibrary#getBaseCollection <em>Base Collection</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Collection</em>' containment reference.
	 * @see #getBaseCollection()
	 * @generated
	 */
	void setBaseCollection(PictureCollection value);

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see gallery.GalleryPackage#getPictureLibrary_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link gallery.PictureLibrary#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

} // PictureLibrary
