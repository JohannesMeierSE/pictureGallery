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


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Linked Picture Collection</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gallery.LinkedPictureCollection#getRealCollection <em>Real Collection</em>}</li>
 * </ul>
 * </p>
 *
 * @see gallery.GalleryPackage#getLinkedPictureCollection()
 * @model
 * @generated
 */
public interface LinkedPictureCollection extends PictureCollection {
	/**
	 * Returns the value of the '<em><b>Real Collection</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link gallery.RealPictureCollection#getLinkedBy <em>Linked By</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Real Collection</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Real Collection</em>' reference.
	 * @see #setRealCollection(RealPictureCollection)
	 * @see gallery.GalleryPackage#getLinkedPictureCollection_RealCollection()
	 * @see gallery.RealPictureCollection#getLinkedBy
	 * @model opposite="linkedBy" required="true"
	 * @generated
	 */
	RealPictureCollection getRealCollection();

	/**
	 * Sets the value of the '{@link gallery.LinkedPictureCollection#getRealCollection <em>Real Collection</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Real Collection</em>' reference.
	 * @see #getRealCollection()
	 * @generated
	 */
	void setRealCollection(RealPictureCollection value);

} // LinkedPictureCollection
