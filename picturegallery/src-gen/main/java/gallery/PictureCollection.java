/**
 */
package gallery;

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

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Picture Collection</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gallery.PictureCollection#getSuperCollection <em>Super Collection</em>}</li>
 * </ul>
 * </p>
 *
 * @see gallery.GalleryPackage#getPictureCollection()
 * @model abstract="true"
 * @generated
 */
public interface PictureCollection extends PathElement {
	/**
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sub Collections</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	EList<PictureCollection> getSubCollections();

	/**
	 * Returns the value of the '<em><b>Super Collection</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link gallery.RealPictureCollection#getSubCollections <em>Sub Collections</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Super Collection</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Super Collection</em>' container reference.
	 * @see #setSuperCollection(RealPictureCollection)
	 * @see gallery.GalleryPackage#getPictureCollection_SuperCollection()
	 * @see gallery.RealPictureCollection#getSubCollections
	 * @model opposite="subCollections" transient="false"
	 * @generated
	 */
	RealPictureCollection getSuperCollection();

	/**
	 * Sets the value of the '{@link gallery.PictureCollection#getSuperCollection <em>Super Collection</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Super Collection</em>' container reference.
	 * @see #getSuperCollection()
	 * @generated
	 */
	void setSuperCollection(RealPictureCollection value);

	/**
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Pictures</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	EList<Picture> getPictures();

} // PictureCollection
