/**
 */
package gallery;

import org.eclipse.emf.common.util.EList;

/*-
 * BEGIN-LICENSE
 * picturegallery
 * %%
 * Copyright (C) 2016 - 2023 Johannes Meier
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
 * A representation of the model object '<em><b>Picture</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link gallery.Picture#getFileExtension <em>File Extension</em>}</li>
 *   <li>{@link gallery.Picture#getCollection <em>Collection</em>}</li>
 *   <li>{@link gallery.Picture#getTags <em>Tags</em>}</li>
 * </ul>
 *
 * @see gallery.GalleryPackage#getPicture()
 * @model abstract="true"
 * @generated
 */
public interface Picture extends PathElement {
	/**
	 * Returns the value of the '<em><b>Collection</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link gallery.RealPictureCollection#getPictures <em>Pictures</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Collection</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Collection</em>' container reference.
	 * @see #setCollection(RealPictureCollection)
	 * @see gallery.GalleryPackage#getPicture_Collection()
	 * @see gallery.RealPictureCollection#getPictures
	 * @model opposite="pictures" required="true" transient="false"
	 * @generated
	 */
	RealPictureCollection getCollection();

	/**
	 * Sets the value of the '{@link gallery.Picture#getCollection <em>Collection</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Collection</em>' container reference.
	 * @see #getCollection()
	 * @generated
	 */
	void setCollection(RealPictureCollection value);

	/**
	 * Returns the value of the '<em><b>Tags</b></em>' containment reference list.
	 * The list contents are of type {@link gallery.Tag}.
	 * It is bidirectional and its opposite is '{@link gallery.Tag#getPicture <em>Picture</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Tags</em>' containment reference list.
	 * @see gallery.GalleryPackage#getPicture_Tags()
	 * @see gallery.Tag#getPicture
	 * @model opposite="picture" containment="true"
	 * @generated
	 */
	EList<Tag> getTags();

	/**
	 * Returns the value of the '<em><b>File Extension</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>File Extension</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>File Extension</em>' attribute.
	 * @see #setFileExtension(String)
	 * @see gallery.GalleryPackage#getPicture_FileExtension()
	 * @model required="true"
	 * @generated
	 */
	String getFileExtension();

	/**
	 * Sets the value of the '{@link gallery.Picture#getFileExtension <em>File Extension</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>File Extension</em>' attribute.
	 * @see #getFileExtension()
	 * @generated
	 */
	void setFileExtension(String value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	Metadata getMetadata();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	String getHash();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	String getHashFast();

} // Picture
