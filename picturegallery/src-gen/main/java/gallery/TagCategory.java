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

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Tag Category</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link gallery.TagCategory#getName <em>Name</em>}</li>
 *   <li>{@link gallery.TagCategory#getLibrary <em>Library</em>}</li>
 *   <li>{@link gallery.TagCategory#getTagInstances <em>Tag Instances</em>}</li>
 * </ul>
 *
 * @see gallery.GalleryPackage#getTagCategory()
 * @model
 * @generated
 */
public interface TagCategory extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see gallery.GalleryPackage#getTagCategory_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link gallery.TagCategory#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Library</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link gallery.PictureLibrary#getTagCategories <em>Tag Categories</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Library</em>' container reference.
	 * @see #setLibrary(PictureLibrary)
	 * @see gallery.GalleryPackage#getTagCategory_Library()
	 * @see gallery.PictureLibrary#getTagCategories
	 * @model opposite="tagCategories" required="true" transient="false"
	 * @generated
	 */
	PictureLibrary getLibrary();

	/**
	 * Sets the value of the '{@link gallery.TagCategory#getLibrary <em>Library</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Library</em>' container reference.
	 * @see #getLibrary()
	 * @generated
	 */
	void setLibrary(PictureLibrary value);

	/**
	 * Returns the value of the '<em><b>Tag Instances</b></em>' reference list.
	 * The list contents are of type {@link gallery.Tag}.
	 * It is bidirectional and its opposite is '{@link gallery.Tag#getCategory <em>Category</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Tag Instances</em>' reference list.
	 * @see gallery.GalleryPackage#getTagCategory_TagInstances()
	 * @see gallery.Tag#getCategory
	 * @model opposite="category"
	 * @generated
	 */
	EList<Tag> getTagInstances();

} // TagCategory
