/**
 */
package gallery;

/*-
 * BEGIN-LICENSE
 * picturegallery
 * %%
 * Copyright (C) 2016 - 2024 Johannes Meier
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

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Real Picture Collection</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gallery.RealPictureCollection#getLinkedBy <em>Linked By</em>}</li>
 *   <li>{@link gallery.RealPictureCollection#getLibrary <em>Library</em>}</li>
 *   <li>{@link gallery.RealPictureCollection#getPictures <em>Pictures</em>}</li>
 *   <li>{@link gallery.RealPictureCollection#getSubCollections <em>Sub Collections</em>}</li>
 * </ul>
 * </p>
 *
 * @see gallery.GalleryPackage#getRealPictureCollection()
 * @model
 * @generated
 */
public interface RealPictureCollection extends PictureCollection {
	/**
	 * Returns the value of the '<em><b>Linked By</b></em>' reference list.
	 * The list contents are of type {@link gallery.LinkedPictureCollection}.
	 * It is bidirectional and its opposite is '{@link gallery.LinkedPictureCollection#getRealCollection <em>Real Collection</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Linked By</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Linked By</em>' reference list.
	 * @see gallery.GalleryPackage#getRealPictureCollection_LinkedBy()
	 * @see gallery.LinkedPictureCollection#getRealCollection
	 * @model opposite="realCollection"
	 * @generated
	 */
	EList<LinkedPictureCollection> getLinkedBy();

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
	 * @see gallery.GalleryPackage#getRealPictureCollection_Library()
	 * @see gallery.PictureLibrary#getBaseCollection
	 * @model opposite="baseCollection" transient="false"
	 * @generated
	 */
	PictureLibrary getLibrary();

	/**
	 * Sets the value of the '{@link gallery.RealPictureCollection#getLibrary <em>Library</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Library</em>' container reference.
	 * @see #getLibrary()
	 * @generated
	 */
	void setLibrary(PictureLibrary value);

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
	 * @see gallery.GalleryPackage#getRealPictureCollection_Pictures()
	 * @see gallery.Picture#getCollection
	 * @model opposite="collection" containment="true"
	 * @generated
	 */
	EList<Picture> getPictures();

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
	 * @see gallery.GalleryPackage#getRealPictureCollection_SubCollections()
	 * @see gallery.PictureCollection#getSuperCollection
	 * @model opposite="superCollection" containment="true"
	 * @generated
	 */
	EList<PictureCollection> getSubCollections();

} // RealPictureCollection
