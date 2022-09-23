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
