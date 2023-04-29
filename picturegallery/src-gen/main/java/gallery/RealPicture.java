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
 * A representation of the model object '<em><b>Real Picture</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gallery.RealPicture#getLinkedBy <em>Linked By</em>}</li>
 *   <li>{@link gallery.RealPicture#getMetadata <em>Metadata</em>}</li>
 * </ul>
 * </p>
 *
 * @see gallery.GalleryPackage#getRealPicture()
 * @model
 * @generated
 */
public interface RealPicture extends Picture, PictureWithHash {
	/**
	 * Returns the value of the '<em><b>Linked By</b></em>' reference list.
	 * The list contents are of type {@link gallery.LinkedPicture}.
	 * It is bidirectional and its opposite is '{@link gallery.LinkedPicture#getRealPicture <em>Real Picture</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Linked By</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Linked By</em>' reference list.
	 * @see gallery.GalleryPackage#getRealPicture_LinkedBy()
	 * @see gallery.LinkedPicture#getRealPicture
	 * @model opposite="realPicture"
	 * @generated
	 */
	EList<LinkedPicture> getLinkedBy();

	/**
	 * Returns the value of the '<em><b>Metadata</b></em>' containment reference.
	 * It is bidirectional and its opposite is '{@link gallery.Metadata#getPicture <em>Picture</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Metadata</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Metadata</em>' containment reference.
	 * @see #setMetadata(Metadata)
	 * @see gallery.GalleryPackage#getRealPicture_Metadata()
	 * @see gallery.Metadata#getPicture
	 * @model opposite="picture" containment="true"
	 * @generated
	 */
	Metadata getMetadata();

	/**
	 * Sets the value of the '{@link gallery.RealPicture#getMetadata <em>Metadata</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Metadata</em>' containment reference.
	 * @see #getMetadata()
	 * @generated
	 */
	void setMetadata(Metadata value);

} // RealPicture
