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
 *   <li>{@link gallery.PictureCollection#getSubCollections <em>Sub Collections</em>}</li>
 *   <li>{@link gallery.PictureCollection#getSuperCollection <em>Super Collection</em>}</li>
 *   <li>{@link gallery.PictureCollection#getPictures <em>Pictures</em>}</li>
 *   <li>{@link gallery.PictureCollection#getLibrary <em>Library</em>}</li>
 * </ul>
 * </p>
 *
 * @see gallery.GalleryPackage#getPictureCollection()
 * @model
 * @generated
 */
public interface PictureCollection extends PathElement {
	/**
	 * Returns the value of the '<em><b>Sub Collections</b></em>' containment reference list.
	 * The list contents are of type {@link gallery.PictureCollection}.
	 * It is bidirectional and its opposite is '{@link gallery.PictureCollection#getSuperCollection <em>Super Collection</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sub Collections</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sub Collections</em>' containment reference list.
	 * @see gallery.GalleryPackage#getPictureCollection_SubCollections()
	 * @see gallery.PictureCollection#getSuperCollection
	 * @model opposite="superCollection" containment="true"
	 * @generated
	 */
	EList<PictureCollection> getSubCollections();

	/**
	 * Returns the value of the '<em><b>Super Collection</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link gallery.PictureCollection#getSubCollections <em>Sub Collections</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Super Collection</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Super Collection</em>' container reference.
	 * @see #setSuperCollection(PictureCollection)
	 * @see gallery.GalleryPackage#getPictureCollection_SuperCollection()
	 * @see gallery.PictureCollection#getSubCollections
	 * @model opposite="subCollections" transient="false"
	 * @generated
	 */
	PictureCollection getSuperCollection();

	/**
	 * Sets the value of the '{@link gallery.PictureCollection#getSuperCollection <em>Super Collection</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Super Collection</em>' container reference.
	 * @see #getSuperCollection()
	 * @generated
	 */
	void setSuperCollection(PictureCollection value);

	/**
	 * Returns the value of the '<em><b>Pictures</b></em>' containment reference list.
	 * The list contents are of type {@link gallery.Picture}.
	 * It is bidirectional and its opposite is '{@link gallery.Picture#getCollection <em>Collection</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Pictures</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Pictures</em>' containment reference list.
	 * @see gallery.GalleryPackage#getPictureCollection_Pictures()
	 * @see gallery.Picture#getCollection
	 * @model opposite="collection" containment="true"
	 * @generated
	 */
	EList<Picture> getPictures();

	/**
	 * Returns the value of the '<em><b>Library</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link gallery.PictureLibrary#getBaseCollection <em>Base Collection</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Library</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Library</em>' container reference.
	 * @see #setLibrary(PictureLibrary)
	 * @see gallery.GalleryPackage#getPictureCollection_Library()
	 * @see gallery.PictureLibrary#getBaseCollection
	 * @model opposite="baseCollection" transient="false"
	 * @generated
	 */
	PictureLibrary getLibrary();

	/**
	 * Sets the value of the '{@link gallery.PictureCollection#getLibrary <em>Library</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Library</em>' container reference.
	 * @see #getLibrary()
	 * @generated
	 */
	void setLibrary(PictureLibrary value);

} // PictureCollection
