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

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Tag</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link gallery.Tag#getValue <em>Value</em>}</li>
 *   <li>{@link gallery.Tag#getPicture <em>Picture</em>}</li>
 *   <li>{@link gallery.Tag#getCategory <em>Category</em>}</li>
 * </ul>
 *
 * @see gallery.GalleryPackage#getTag()
 * @model
 * @generated
 */
public interface Tag extends EObject {
	/**
	 * Returns the value of the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #setValue(String)
	 * @see gallery.GalleryPackage#getTag_Value()
	 * @model
	 * @generated
	 */
	String getValue();

	/**
	 * Sets the value of the '{@link gallery.Tag#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(String value);

	/**
	 * Returns the value of the '<em><b>Picture</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link gallery.Picture#getTags <em>Tags</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Picture</em>' container reference.
	 * @see #setPicture(Picture)
	 * @see gallery.GalleryPackage#getTag_Picture()
	 * @see gallery.Picture#getTags
	 * @model opposite="tags" required="true" transient="false"
	 * @generated
	 */
	Picture getPicture();

	/**
	 * Sets the value of the '{@link gallery.Tag#getPicture <em>Picture</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Picture</em>' container reference.
	 * @see #getPicture()
	 * @generated
	 */
	void setPicture(Picture value);

	/**
	 * Returns the value of the '<em><b>Category</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link gallery.TagCategory#getTagInstances <em>Tag Instances</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Category</em>' reference.
	 * @see #setCategory(TagCategory)
	 * @see gallery.GalleryPackage#getTag_Category()
	 * @see gallery.TagCategory#getTagInstances
	 * @model opposite="tagInstances" required="true"
	 * @generated
	 */
	TagCategory getCategory();

	/**
	 * Sets the value of the '{@link gallery.Tag#getCategory <em>Category</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Category</em>' reference.
	 * @see #getCategory()
	 * @generated
	 */
	void setCategory(TagCategory value);

} // Tag
