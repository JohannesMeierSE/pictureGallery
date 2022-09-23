/**
 */
package gallery;

/*-
 * BEGIN-LICENSE
 * picturegallery
 * %%
 * Copyright (C) 2016 - 2022 Johannes Meier
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * END-LICENSE
 */


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
