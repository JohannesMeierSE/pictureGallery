/**
 */
package gallery;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Deleted Picture</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gallery.DeletedPicture#getRelativePath <em>Relative Path</em>}</li>
 *   <li>{@link gallery.DeletedPicture#getLibrary <em>Library</em>}</li>
 * </ul>
 * </p>
 *
 * @see gallery.GalleryPackage#getDeletedPicture()
 * @model
 * @generated
 */
public interface DeletedPicture extends PictureWithHash {
	/**
	 * Returns the value of the '<em><b>Relative Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Relative Path</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Relative Path</em>' attribute.
	 * @see #setRelativePath(String)
	 * @see gallery.GalleryPackage#getDeletedPicture_RelativePath()
	 * @model
	 * @generated
	 */
	String getRelativePath();

	/**
	 * Sets the value of the '{@link gallery.DeletedPicture#getRelativePath <em>Relative Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Relative Path</em>' attribute.
	 * @see #getRelativePath()
	 * @generated
	 */
	void setRelativePath(String value);

	/**
	 * Returns the value of the '<em><b>Library</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link gallery.PictureLibrary#getDeletedPictures <em>Deleted Pictures</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Library</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Library</em>' container reference.
	 * @see #setLibrary(PictureLibrary)
	 * @see gallery.GalleryPackage#getDeletedPicture_Library()
	 * @see gallery.PictureLibrary#getDeletedPictures
	 * @model opposite="deletedPictures" required="true" transient="false"
	 * @generated
	 */
	PictureLibrary getLibrary();

	/**
	 * Sets the value of the '{@link gallery.DeletedPicture#getLibrary <em>Library</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Library</em>' container reference.
	 * @see #getLibrary()
	 * @generated
	 */
	void setLibrary(PictureLibrary value);

} // DeletedPicture
