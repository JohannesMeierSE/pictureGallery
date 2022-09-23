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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see gallery.GalleryFactory
 * @model kind="package"
 * @generated
 */
public interface GalleryPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "gallery";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://gallery/1.0";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "gallery";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	GalleryPackage eINSTANCE = gallery.impl.GalleryPackageImpl.init();

	/**
	 * The meta object id for the '{@link gallery.impl.PathElementImpl <em>Path Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gallery.impl.PathElementImpl
	 * @see gallery.impl.GalleryPackageImpl#getPathElement()
	 * @generated
	 */
	int PATH_ELEMENT = 4;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATH_ELEMENT__NAME = 0;

	/**
	 * The number of structural features of the '<em>Path Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATH_ELEMENT_FEATURE_COUNT = 1;

	/**
	 * The operation id for the '<em>Get Full Path</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATH_ELEMENT___GET_FULL_PATH = 0;

	/**
	 * The operation id for the '<em>Get Relative Path</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATH_ELEMENT___GET_RELATIVE_PATH = 1;

	/**
	 * The operation id for the '<em>Get Relative Path Without Base</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATH_ELEMENT___GET_RELATIVE_PATH_WITHOUT_BASE = 2;

	/**
	 * The number of operations of the '<em>Path Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATH_ELEMENT_OPERATION_COUNT = 3;

	/**
	 * The meta object id for the '{@link gallery.impl.PictureCollectionImpl <em>Picture Collection</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gallery.impl.PictureCollectionImpl
	 * @see gallery.impl.GalleryPackageImpl#getPictureCollection()
	 * @generated
	 */
	int PICTURE_COLLECTION = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PICTURE_COLLECTION__NAME = PATH_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Super Collection</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PICTURE_COLLECTION__SUPER_COLLECTION = PATH_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Picture Collection</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PICTURE_COLLECTION_FEATURE_COUNT = PATH_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Full Path</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PICTURE_COLLECTION___GET_FULL_PATH = PATH_ELEMENT___GET_FULL_PATH;

	/**
	 * The operation id for the '<em>Get Relative Path</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PICTURE_COLLECTION___GET_RELATIVE_PATH = PATH_ELEMENT___GET_RELATIVE_PATH;

	/**
	 * The operation id for the '<em>Get Relative Path Without Base</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PICTURE_COLLECTION___GET_RELATIVE_PATH_WITHOUT_BASE = PATH_ELEMENT___GET_RELATIVE_PATH_WITHOUT_BASE;

	/**
	 * The operation id for the '<em>Get Pictures</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PICTURE_COLLECTION___GET_PICTURES = PATH_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * The operation id for the '<em>Get Sub Collections</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PICTURE_COLLECTION___GET_SUB_COLLECTIONS = PATH_ELEMENT_OPERATION_COUNT + 1;

	/**
	 * The number of operations of the '<em>Picture Collection</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PICTURE_COLLECTION_OPERATION_COUNT = PATH_ELEMENT_OPERATION_COUNT + 2;

	/**
	 * The meta object id for the '{@link gallery.impl.PictureImpl <em>Picture</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gallery.impl.PictureImpl
	 * @see gallery.impl.GalleryPackageImpl#getPicture()
	 * @generated
	 */
	int PICTURE = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PICTURE__NAME = PATH_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>File Extension</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PICTURE__FILE_EXTENSION = PATH_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Collection</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PICTURE__COLLECTION = PATH_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Picture</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PICTURE_FEATURE_COUNT = PATH_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The operation id for the '<em>Get Full Path</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PICTURE___GET_FULL_PATH = PATH_ELEMENT___GET_FULL_PATH;

	/**
	 * The operation id for the '<em>Get Relative Path</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PICTURE___GET_RELATIVE_PATH = PATH_ELEMENT___GET_RELATIVE_PATH;

	/**
	 * The operation id for the '<em>Get Relative Path Without Base</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PICTURE___GET_RELATIVE_PATH_WITHOUT_BASE = PATH_ELEMENT___GET_RELATIVE_PATH_WITHOUT_BASE;

	/**
	 * The operation id for the '<em>Get Metadata</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PICTURE___GET_METADATA = PATH_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * The operation id for the '<em>Get Hash</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PICTURE___GET_HASH = PATH_ELEMENT_OPERATION_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Hash Fast</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PICTURE___GET_HASH_FAST = PATH_ELEMENT_OPERATION_COUNT + 2;

	/**
	 * The number of operations of the '<em>Picture</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PICTURE_OPERATION_COUNT = PATH_ELEMENT_OPERATION_COUNT + 3;

	/**
	 * The meta object id for the '{@link gallery.impl.RealPictureImpl <em>Real Picture</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gallery.impl.RealPictureImpl
	 * @see gallery.impl.GalleryPackageImpl#getRealPicture()
	 * @generated
	 */
	int REAL_PICTURE = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REAL_PICTURE__NAME = PICTURE__NAME;

	/**
	 * The feature id for the '<em><b>File Extension</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REAL_PICTURE__FILE_EXTENSION = PICTURE__FILE_EXTENSION;

	/**
	 * The feature id for the '<em><b>Collection</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REAL_PICTURE__COLLECTION = PICTURE__COLLECTION;

	/**
	 * The feature id for the '<em><b>Hash</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REAL_PICTURE__HASH = PICTURE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Hash Fast</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REAL_PICTURE__HASH_FAST = PICTURE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Linked By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REAL_PICTURE__LINKED_BY = PICTURE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Metadata</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REAL_PICTURE__METADATA = PICTURE_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Real Picture</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REAL_PICTURE_FEATURE_COUNT = PICTURE_FEATURE_COUNT + 4;

	/**
	 * The operation id for the '<em>Get Full Path</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REAL_PICTURE___GET_FULL_PATH = PICTURE___GET_FULL_PATH;

	/**
	 * The operation id for the '<em>Get Relative Path</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REAL_PICTURE___GET_RELATIVE_PATH = PICTURE___GET_RELATIVE_PATH;

	/**
	 * The operation id for the '<em>Get Relative Path Without Base</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REAL_PICTURE___GET_RELATIVE_PATH_WITHOUT_BASE = PICTURE___GET_RELATIVE_PATH_WITHOUT_BASE;

	/**
	 * The operation id for the '<em>Get Metadata</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REAL_PICTURE___GET_METADATA = PICTURE___GET_METADATA;

	/**
	 * The operation id for the '<em>Get Hash</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REAL_PICTURE___GET_HASH = PICTURE___GET_HASH;

	/**
	 * The operation id for the '<em>Get Hash Fast</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REAL_PICTURE___GET_HASH_FAST = PICTURE___GET_HASH_FAST;

	/**
	 * The number of operations of the '<em>Real Picture</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REAL_PICTURE_OPERATION_COUNT = PICTURE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link gallery.impl.LinkedPictureImpl <em>Linked Picture</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gallery.impl.LinkedPictureImpl
	 * @see gallery.impl.GalleryPackageImpl#getLinkedPicture()
	 * @generated
	 */
	int LINKED_PICTURE = 3;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINKED_PICTURE__NAME = PICTURE__NAME;

	/**
	 * The feature id for the '<em><b>File Extension</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINKED_PICTURE__FILE_EXTENSION = PICTURE__FILE_EXTENSION;

	/**
	 * The feature id for the '<em><b>Collection</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINKED_PICTURE__COLLECTION = PICTURE__COLLECTION;

	/**
	 * The feature id for the '<em><b>Real Picture</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINKED_PICTURE__REAL_PICTURE = PICTURE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Linked Picture</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINKED_PICTURE_FEATURE_COUNT = PICTURE_FEATURE_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Full Path</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINKED_PICTURE___GET_FULL_PATH = PICTURE___GET_FULL_PATH;

	/**
	 * The operation id for the '<em>Get Relative Path</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINKED_PICTURE___GET_RELATIVE_PATH = PICTURE___GET_RELATIVE_PATH;

	/**
	 * The operation id for the '<em>Get Relative Path Without Base</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINKED_PICTURE___GET_RELATIVE_PATH_WITHOUT_BASE = PICTURE___GET_RELATIVE_PATH_WITHOUT_BASE;

	/**
	 * The operation id for the '<em>Get Metadata</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINKED_PICTURE___GET_METADATA = PICTURE___GET_METADATA;

	/**
	 * The operation id for the '<em>Get Hash</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINKED_PICTURE___GET_HASH = PICTURE___GET_HASH;

	/**
	 * The operation id for the '<em>Get Hash Fast</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINKED_PICTURE___GET_HASH_FAST = PICTURE___GET_HASH_FAST;

	/**
	 * The number of operations of the '<em>Linked Picture</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINKED_PICTURE_OPERATION_COUNT = PICTURE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link gallery.impl.PictureLibraryImpl <em>Picture Library</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gallery.impl.PictureLibraryImpl
	 * @see gallery.impl.GalleryPackageImpl#getPictureLibrary()
	 * @generated
	 */
	int PICTURE_LIBRARY = 5;

	/**
	 * The feature id for the '<em><b>Base Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PICTURE_LIBRARY__BASE_PATH = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PICTURE_LIBRARY__NAME = 1;

	/**
	 * The feature id for the '<em><b>Base Collection</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PICTURE_LIBRARY__BASE_COLLECTION = 2;

	/**
	 * The feature id for the '<em><b>Deleted Pictures</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PICTURE_LIBRARY__DELETED_PICTURES = 3;

	/**
	 * The number of structural features of the '<em>Picture Library</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PICTURE_LIBRARY_FEATURE_COUNT = 4;

	/**
	 * The number of operations of the '<em>Picture Library</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PICTURE_LIBRARY_OPERATION_COUNT = 0;


	/**
	 * The meta object id for the '{@link gallery.impl.MetadataImpl <em>Metadata</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gallery.impl.MetadataImpl
	 * @see gallery.impl.GalleryPackageImpl#getMetadata()
	 * @generated
	 */
	int METADATA = 6;

	/**
	 * The feature id for the '<em><b>Picture</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METADATA__PICTURE = 0;

	/**
	 * The feature id for the '<em><b>Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METADATA__SIZE = 1;

	/**
	 * The feature id for the '<em><b>Landscape</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METADATA__LANDSCAPE = 2;

	/**
	 * The feature id for the '<em><b>Created</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METADATA__CREATED = 3;

	/**
	 * The feature id for the '<em><b>Height</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METADATA__HEIGHT = 4;

	/**
	 * The feature id for the '<em><b>Width</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METADATA__WIDTH = 5;

	/**
	 * The feature id for the '<em><b>Camera</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METADATA__CAMERA = 6;

	/**
	 * The number of structural features of the '<em>Metadata</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METADATA_FEATURE_COUNT = 7;

	/**
	 * The number of operations of the '<em>Metadata</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METADATA_OPERATION_COUNT = 0;


	/**
	 * The meta object id for the '{@link gallery.impl.RealPictureCollectionImpl <em>Real Picture Collection</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gallery.impl.RealPictureCollectionImpl
	 * @see gallery.impl.GalleryPackageImpl#getRealPictureCollection()
	 * @generated
	 */
	int REAL_PICTURE_COLLECTION = 7;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REAL_PICTURE_COLLECTION__NAME = PICTURE_COLLECTION__NAME;

	/**
	 * The feature id for the '<em><b>Super Collection</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REAL_PICTURE_COLLECTION__SUPER_COLLECTION = PICTURE_COLLECTION__SUPER_COLLECTION;

	/**
	 * The feature id for the '<em><b>Linked By</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REAL_PICTURE_COLLECTION__LINKED_BY = PICTURE_COLLECTION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Library</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REAL_PICTURE_COLLECTION__LIBRARY = PICTURE_COLLECTION_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Pictures</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REAL_PICTURE_COLLECTION__PICTURES = PICTURE_COLLECTION_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Sub Collections</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REAL_PICTURE_COLLECTION__SUB_COLLECTIONS = PICTURE_COLLECTION_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Real Picture Collection</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REAL_PICTURE_COLLECTION_FEATURE_COUNT = PICTURE_COLLECTION_FEATURE_COUNT + 4;

	/**
	 * The operation id for the '<em>Get Full Path</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REAL_PICTURE_COLLECTION___GET_FULL_PATH = PICTURE_COLLECTION___GET_FULL_PATH;

	/**
	 * The operation id for the '<em>Get Relative Path</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REAL_PICTURE_COLLECTION___GET_RELATIVE_PATH = PICTURE_COLLECTION___GET_RELATIVE_PATH;

	/**
	 * The operation id for the '<em>Get Relative Path Without Base</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REAL_PICTURE_COLLECTION___GET_RELATIVE_PATH_WITHOUT_BASE = PICTURE_COLLECTION___GET_RELATIVE_PATH_WITHOUT_BASE;

	/**
	 * The operation id for the '<em>Get Pictures</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REAL_PICTURE_COLLECTION___GET_PICTURES = PICTURE_COLLECTION___GET_PICTURES;

	/**
	 * The operation id for the '<em>Get Sub Collections</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REAL_PICTURE_COLLECTION___GET_SUB_COLLECTIONS = PICTURE_COLLECTION___GET_SUB_COLLECTIONS;

	/**
	 * The number of operations of the '<em>Real Picture Collection</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REAL_PICTURE_COLLECTION_OPERATION_COUNT = PICTURE_COLLECTION_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link gallery.impl.LinkedPictureCollectionImpl <em>Linked Picture Collection</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gallery.impl.LinkedPictureCollectionImpl
	 * @see gallery.impl.GalleryPackageImpl#getLinkedPictureCollection()
	 * @generated
	 */
	int LINKED_PICTURE_COLLECTION = 8;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINKED_PICTURE_COLLECTION__NAME = PICTURE_COLLECTION__NAME;

	/**
	 * The feature id for the '<em><b>Super Collection</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINKED_PICTURE_COLLECTION__SUPER_COLLECTION = PICTURE_COLLECTION__SUPER_COLLECTION;

	/**
	 * The feature id for the '<em><b>Real Collection</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINKED_PICTURE_COLLECTION__REAL_COLLECTION = PICTURE_COLLECTION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Linked Picture Collection</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINKED_PICTURE_COLLECTION_FEATURE_COUNT = PICTURE_COLLECTION_FEATURE_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Full Path</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINKED_PICTURE_COLLECTION___GET_FULL_PATH = PICTURE_COLLECTION___GET_FULL_PATH;

	/**
	 * The operation id for the '<em>Get Relative Path</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINKED_PICTURE_COLLECTION___GET_RELATIVE_PATH = PICTURE_COLLECTION___GET_RELATIVE_PATH;

	/**
	 * The operation id for the '<em>Get Relative Path Without Base</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINKED_PICTURE_COLLECTION___GET_RELATIVE_PATH_WITHOUT_BASE = PICTURE_COLLECTION___GET_RELATIVE_PATH_WITHOUT_BASE;

	/**
	 * The operation id for the '<em>Get Pictures</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINKED_PICTURE_COLLECTION___GET_PICTURES = PICTURE_COLLECTION___GET_PICTURES;

	/**
	 * The operation id for the '<em>Get Sub Collections</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINKED_PICTURE_COLLECTION___GET_SUB_COLLECTIONS = PICTURE_COLLECTION___GET_SUB_COLLECTIONS;

	/**
	 * The number of operations of the '<em>Linked Picture Collection</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINKED_PICTURE_COLLECTION_OPERATION_COUNT = PICTURE_COLLECTION_OPERATION_COUNT + 0;


	/**
	 * The meta object id for the '{@link gallery.impl.PictureWithHashImpl <em>Picture With Hash</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gallery.impl.PictureWithHashImpl
	 * @see gallery.impl.GalleryPackageImpl#getPictureWithHash()
	 * @generated
	 */
	int PICTURE_WITH_HASH = 10;

	/**
	 * The feature id for the '<em><b>Hash</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PICTURE_WITH_HASH__HASH = 0;

	/**
	 * The feature id for the '<em><b>Hash Fast</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PICTURE_WITH_HASH__HASH_FAST = 1;

	/**
	 * The number of structural features of the '<em>Picture With Hash</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PICTURE_WITH_HASH_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Picture With Hash</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PICTURE_WITH_HASH_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link gallery.impl.DeletedPictureImpl <em>Deleted Picture</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gallery.impl.DeletedPictureImpl
	 * @see gallery.impl.GalleryPackageImpl#getDeletedPicture()
	 * @generated
	 */
	int DELETED_PICTURE = 9;

	/**
	 * The feature id for the '<em><b>Hash</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELETED_PICTURE__HASH = PICTURE_WITH_HASH__HASH;

	/**
	 * The feature id for the '<em><b>Hash Fast</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELETED_PICTURE__HASH_FAST = PICTURE_WITH_HASH__HASH_FAST;

	/**
	 * The feature id for the '<em><b>Relative Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELETED_PICTURE__RELATIVE_PATH = PICTURE_WITH_HASH_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Library</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELETED_PICTURE__LIBRARY = PICTURE_WITH_HASH_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Deleted Picture</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELETED_PICTURE_FEATURE_COUNT = PICTURE_WITH_HASH_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Deleted Picture</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELETED_PICTURE_OPERATION_COUNT = PICTURE_WITH_HASH_OPERATION_COUNT + 0;


	/**
	 * Returns the meta object for class '{@link gallery.PictureCollection <em>Picture Collection</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Picture Collection</em>'.
	 * @see gallery.PictureCollection
	 * @generated
	 */
	EClass getPictureCollection();

	/**
	 * Returns the meta object for the container reference '{@link gallery.PictureCollection#getSuperCollection <em>Super Collection</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Super Collection</em>'.
	 * @see gallery.PictureCollection#getSuperCollection()
	 * @see #getPictureCollection()
	 * @generated
	 */
	EReference getPictureCollection_SuperCollection();

	/**
	 * Returns the meta object for the '{@link gallery.PictureCollection#getPictures() <em>Get Pictures</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Pictures</em>' operation.
	 * @see gallery.PictureCollection#getPictures()
	 * @generated
	 */
	EOperation getPictureCollection__GetPictures();

	/**
	 * Returns the meta object for the '{@link gallery.PictureCollection#getSubCollections() <em>Get Sub Collections</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Sub Collections</em>' operation.
	 * @see gallery.PictureCollection#getSubCollections()
	 * @generated
	 */
	EOperation getPictureCollection__GetSubCollections();

	/**
	 * Returns the meta object for class '{@link gallery.Picture <em>Picture</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Picture</em>'.
	 * @see gallery.Picture
	 * @generated
	 */
	EClass getPicture();

	/**
	 * Returns the meta object for the container reference '{@link gallery.Picture#getCollection <em>Collection</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Collection</em>'.
	 * @see gallery.Picture#getCollection()
	 * @see #getPicture()
	 * @generated
	 */
	EReference getPicture_Collection();

	/**
	 * Returns the meta object for the attribute '{@link gallery.Picture#getFileExtension <em>File Extension</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>File Extension</em>'.
	 * @see gallery.Picture#getFileExtension()
	 * @see #getPicture()
	 * @generated
	 */
	EAttribute getPicture_FileExtension();

	/**
	 * Returns the meta object for the '{@link gallery.Picture#getMetadata() <em>Get Metadata</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Metadata</em>' operation.
	 * @see gallery.Picture#getMetadata()
	 * @generated
	 */
	EOperation getPicture__GetMetadata();

	/**
	 * Returns the meta object for the '{@link gallery.Picture#getHash() <em>Get Hash</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Hash</em>' operation.
	 * @see gallery.Picture#getHash()
	 * @generated
	 */
	EOperation getPicture__GetHash();

	/**
	 * Returns the meta object for the '{@link gallery.Picture#getHashFast() <em>Get Hash Fast</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Hash Fast</em>' operation.
	 * @see gallery.Picture#getHashFast()
	 * @generated
	 */
	EOperation getPicture__GetHashFast();

	/**
	 * Returns the meta object for class '{@link gallery.RealPicture <em>Real Picture</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Real Picture</em>'.
	 * @see gallery.RealPicture
	 * @generated
	 */
	EClass getRealPicture();

	/**
	 * Returns the meta object for the reference list '{@link gallery.RealPicture#getLinkedBy <em>Linked By</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Linked By</em>'.
	 * @see gallery.RealPicture#getLinkedBy()
	 * @see #getRealPicture()
	 * @generated
	 */
	EReference getRealPicture_LinkedBy();

	/**
	 * Returns the meta object for the containment reference '{@link gallery.RealPicture#getMetadata <em>Metadata</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Metadata</em>'.
	 * @see gallery.RealPicture#getMetadata()
	 * @see #getRealPicture()
	 * @generated
	 */
	EReference getRealPicture_Metadata();

	/**
	 * Returns the meta object for class '{@link gallery.LinkedPicture <em>Linked Picture</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Linked Picture</em>'.
	 * @see gallery.LinkedPicture
	 * @generated
	 */
	EClass getLinkedPicture();

	/**
	 * Returns the meta object for the reference '{@link gallery.LinkedPicture#getRealPicture <em>Real Picture</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Real Picture</em>'.
	 * @see gallery.LinkedPicture#getRealPicture()
	 * @see #getLinkedPicture()
	 * @generated
	 */
	EReference getLinkedPicture_RealPicture();

	/**
	 * Returns the meta object for class '{@link gallery.PathElement <em>Path Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Path Element</em>'.
	 * @see gallery.PathElement
	 * @generated
	 */
	EClass getPathElement();

	/**
	 * Returns the meta object for the attribute '{@link gallery.PathElement#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see gallery.PathElement#getName()
	 * @see #getPathElement()
	 * @generated
	 */
	EAttribute getPathElement_Name();

	/**
	 * Returns the meta object for the '{@link gallery.PathElement#getFullPath() <em>Get Full Path</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Full Path</em>' operation.
	 * @see gallery.PathElement#getFullPath()
	 * @generated
	 */
	EOperation getPathElement__GetFullPath();

	/**
	 * Returns the meta object for the '{@link gallery.PathElement#getRelativePath() <em>Get Relative Path</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Relative Path</em>' operation.
	 * @see gallery.PathElement#getRelativePath()
	 * @generated
	 */
	EOperation getPathElement__GetRelativePath();

	/**
	 * Returns the meta object for the '{@link gallery.PathElement#getRelativePathWithoutBase() <em>Get Relative Path Without Base</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Relative Path Without Base</em>' operation.
	 * @see gallery.PathElement#getRelativePathWithoutBase()
	 * @generated
	 */
	EOperation getPathElement__GetRelativePathWithoutBase();

	/**
	 * Returns the meta object for class '{@link gallery.PictureLibrary <em>Picture Library</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Picture Library</em>'.
	 * @see gallery.PictureLibrary
	 * @generated
	 */
	EClass getPictureLibrary();

	/**
	 * Returns the meta object for the attribute '{@link gallery.PictureLibrary#getBasePath <em>Base Path</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Base Path</em>'.
	 * @see gallery.PictureLibrary#getBasePath()
	 * @see #getPictureLibrary()
	 * @generated
	 */
	EAttribute getPictureLibrary_BasePath();

	/**
	 * Returns the meta object for the containment reference '{@link gallery.PictureLibrary#getBaseCollection <em>Base Collection</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Base Collection</em>'.
	 * @see gallery.PictureLibrary#getBaseCollection()
	 * @see #getPictureLibrary()
	 * @generated
	 */
	EReference getPictureLibrary_BaseCollection();

	/**
	 * Returns the meta object for the containment reference list '{@link gallery.PictureLibrary#getDeletedPictures <em>Deleted Pictures</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Deleted Pictures</em>'.
	 * @see gallery.PictureLibrary#getDeletedPictures()
	 * @see #getPictureLibrary()
	 * @generated
	 */
	EReference getPictureLibrary_DeletedPictures();

	/**
	 * Returns the meta object for the attribute '{@link gallery.PictureLibrary#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see gallery.PictureLibrary#getName()
	 * @see #getPictureLibrary()
	 * @generated
	 */
	EAttribute getPictureLibrary_Name();

	/**
	 * Returns the meta object for class '{@link gallery.Metadata <em>Metadata</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Metadata</em>'.
	 * @see gallery.Metadata
	 * @generated
	 */
	EClass getMetadata();

	/**
	 * Returns the meta object for the container reference '{@link gallery.Metadata#getPicture <em>Picture</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Picture</em>'.
	 * @see gallery.Metadata#getPicture()
	 * @see #getMetadata()
	 * @generated
	 */
	EReference getMetadata_Picture();

	/**
	 * Returns the meta object for the attribute '{@link gallery.Metadata#getSize <em>Size</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Size</em>'.
	 * @see gallery.Metadata#getSize()
	 * @see #getMetadata()
	 * @generated
	 */
	EAttribute getMetadata_Size();

	/**
	 * Returns the meta object for the attribute '{@link gallery.Metadata#isLandscape <em>Landscape</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Landscape</em>'.
	 * @see gallery.Metadata#isLandscape()
	 * @see #getMetadata()
	 * @generated
	 */
	EAttribute getMetadata_Landscape();

	/**
	 * Returns the meta object for the attribute '{@link gallery.Metadata#getCreated <em>Created</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Created</em>'.
	 * @see gallery.Metadata#getCreated()
	 * @see #getMetadata()
	 * @generated
	 */
	EAttribute getMetadata_Created();

	/**
	 * Returns the meta object for the attribute '{@link gallery.Metadata#getHeight <em>Height</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Height</em>'.
	 * @see gallery.Metadata#getHeight()
	 * @see #getMetadata()
	 * @generated
	 */
	EAttribute getMetadata_Height();

	/**
	 * Returns the meta object for the attribute '{@link gallery.Metadata#getWidth <em>Width</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Width</em>'.
	 * @see gallery.Metadata#getWidth()
	 * @see #getMetadata()
	 * @generated
	 */
	EAttribute getMetadata_Width();

	/**
	 * Returns the meta object for the attribute '{@link gallery.Metadata#getCamera <em>Camera</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Camera</em>'.
	 * @see gallery.Metadata#getCamera()
	 * @see #getMetadata()
	 * @generated
	 */
	EAttribute getMetadata_Camera();

	/**
	 * Returns the meta object for class '{@link gallery.RealPictureCollection <em>Real Picture Collection</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Real Picture Collection</em>'.
	 * @see gallery.RealPictureCollection
	 * @generated
	 */
	EClass getRealPictureCollection();

	/**
	 * Returns the meta object for the reference list '{@link gallery.RealPictureCollection#getLinkedBy <em>Linked By</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Linked By</em>'.
	 * @see gallery.RealPictureCollection#getLinkedBy()
	 * @see #getRealPictureCollection()
	 * @generated
	 */
	EReference getRealPictureCollection_LinkedBy();

	/**
	 * Returns the meta object for the container reference '{@link gallery.RealPictureCollection#getLibrary <em>Library</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Library</em>'.
	 * @see gallery.RealPictureCollection#getLibrary()
	 * @see #getRealPictureCollection()
	 * @generated
	 */
	EReference getRealPictureCollection_Library();

	/**
	 * Returns the meta object for the containment reference list '{@link gallery.RealPictureCollection#getPictures <em>Pictures</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Pictures</em>'.
	 * @see gallery.RealPictureCollection#getPictures()
	 * @see #getRealPictureCollection()
	 * @generated
	 */
	EReference getRealPictureCollection_Pictures();

	/**
	 * Returns the meta object for the containment reference list '{@link gallery.RealPictureCollection#getSubCollections <em>Sub Collections</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Sub Collections</em>'.
	 * @see gallery.RealPictureCollection#getSubCollections()
	 * @see #getRealPictureCollection()
	 * @generated
	 */
	EReference getRealPictureCollection_SubCollections();

	/**
	 * Returns the meta object for class '{@link gallery.LinkedPictureCollection <em>Linked Picture Collection</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Linked Picture Collection</em>'.
	 * @see gallery.LinkedPictureCollection
	 * @generated
	 */
	EClass getLinkedPictureCollection();

	/**
	 * Returns the meta object for the reference '{@link gallery.LinkedPictureCollection#getRealCollection <em>Real Collection</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Real Collection</em>'.
	 * @see gallery.LinkedPictureCollection#getRealCollection()
	 * @see #getLinkedPictureCollection()
	 * @generated
	 */
	EReference getLinkedPictureCollection_RealCollection();

	/**
	 * Returns the meta object for class '{@link gallery.DeletedPicture <em>Deleted Picture</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Deleted Picture</em>'.
	 * @see gallery.DeletedPicture
	 * @generated
	 */
	EClass getDeletedPicture();

	/**
	 * Returns the meta object for the attribute '{@link gallery.DeletedPicture#getRelativePath <em>Relative Path</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Relative Path</em>'.
	 * @see gallery.DeletedPicture#getRelativePath()
	 * @see #getDeletedPicture()
	 * @generated
	 */
	EAttribute getDeletedPicture_RelativePath();

	/**
	 * Returns the meta object for the container reference '{@link gallery.DeletedPicture#getLibrary <em>Library</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Library</em>'.
	 * @see gallery.DeletedPicture#getLibrary()
	 * @see #getDeletedPicture()
	 * @generated
	 */
	EReference getDeletedPicture_Library();

	/**
	 * Returns the meta object for class '{@link gallery.PictureWithHash <em>Picture With Hash</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Picture With Hash</em>'.
	 * @see gallery.PictureWithHash
	 * @generated
	 */
	EClass getPictureWithHash();

	/**
	 * Returns the meta object for the attribute '{@link gallery.PictureWithHash#getHash <em>Hash</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Hash</em>'.
	 * @see gallery.PictureWithHash#getHash()
	 * @see #getPictureWithHash()
	 * @generated
	 */
	EAttribute getPictureWithHash_Hash();

	/**
	 * Returns the meta object for the attribute '{@link gallery.PictureWithHash#getHashFast <em>Hash Fast</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Hash Fast</em>'.
	 * @see gallery.PictureWithHash#getHashFast()
	 * @see #getPictureWithHash()
	 * @generated
	 */
	EAttribute getPictureWithHash_HashFast();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	GalleryFactory getGalleryFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link gallery.impl.PictureCollectionImpl <em>Picture Collection</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gallery.impl.PictureCollectionImpl
		 * @see gallery.impl.GalleryPackageImpl#getPictureCollection()
		 * @generated
		 */
		EClass PICTURE_COLLECTION = eINSTANCE.getPictureCollection();

		/**
		 * The meta object literal for the '<em><b>Super Collection</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PICTURE_COLLECTION__SUPER_COLLECTION = eINSTANCE.getPictureCollection_SuperCollection();

		/**
		 * The meta object literal for the '<em><b>Get Pictures</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation PICTURE_COLLECTION___GET_PICTURES = eINSTANCE.getPictureCollection__GetPictures();

		/**
		 * The meta object literal for the '<em><b>Get Sub Collections</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation PICTURE_COLLECTION___GET_SUB_COLLECTIONS = eINSTANCE.getPictureCollection__GetSubCollections();

		/**
		 * The meta object literal for the '{@link gallery.impl.PictureImpl <em>Picture</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gallery.impl.PictureImpl
		 * @see gallery.impl.GalleryPackageImpl#getPicture()
		 * @generated
		 */
		EClass PICTURE = eINSTANCE.getPicture();

		/**
		 * The meta object literal for the '<em><b>Collection</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PICTURE__COLLECTION = eINSTANCE.getPicture_Collection();

		/**
		 * The meta object literal for the '<em><b>File Extension</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PICTURE__FILE_EXTENSION = eINSTANCE.getPicture_FileExtension();

		/**
		 * The meta object literal for the '<em><b>Get Metadata</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation PICTURE___GET_METADATA = eINSTANCE.getPicture__GetMetadata();

		/**
		 * The meta object literal for the '<em><b>Get Hash</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation PICTURE___GET_HASH = eINSTANCE.getPicture__GetHash();

		/**
		 * The meta object literal for the '<em><b>Get Hash Fast</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation PICTURE___GET_HASH_FAST = eINSTANCE.getPicture__GetHashFast();

		/**
		 * The meta object literal for the '{@link gallery.impl.RealPictureImpl <em>Real Picture</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gallery.impl.RealPictureImpl
		 * @see gallery.impl.GalleryPackageImpl#getRealPicture()
		 * @generated
		 */
		EClass REAL_PICTURE = eINSTANCE.getRealPicture();

		/**
		 * The meta object literal for the '<em><b>Linked By</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REAL_PICTURE__LINKED_BY = eINSTANCE.getRealPicture_LinkedBy();

		/**
		 * The meta object literal for the '<em><b>Metadata</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REAL_PICTURE__METADATA = eINSTANCE.getRealPicture_Metadata();

		/**
		 * The meta object literal for the '{@link gallery.impl.LinkedPictureImpl <em>Linked Picture</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gallery.impl.LinkedPictureImpl
		 * @see gallery.impl.GalleryPackageImpl#getLinkedPicture()
		 * @generated
		 */
		EClass LINKED_PICTURE = eINSTANCE.getLinkedPicture();

		/**
		 * The meta object literal for the '<em><b>Real Picture</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LINKED_PICTURE__REAL_PICTURE = eINSTANCE.getLinkedPicture_RealPicture();

		/**
		 * The meta object literal for the '{@link gallery.impl.PathElementImpl <em>Path Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gallery.impl.PathElementImpl
		 * @see gallery.impl.GalleryPackageImpl#getPathElement()
		 * @generated
		 */
		EClass PATH_ELEMENT = eINSTANCE.getPathElement();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PATH_ELEMENT__NAME = eINSTANCE.getPathElement_Name();

		/**
		 * The meta object literal for the '<em><b>Get Full Path</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation PATH_ELEMENT___GET_FULL_PATH = eINSTANCE.getPathElement__GetFullPath();

		/**
		 * The meta object literal for the '<em><b>Get Relative Path</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation PATH_ELEMENT___GET_RELATIVE_PATH = eINSTANCE.getPathElement__GetRelativePath();

		/**
		 * The meta object literal for the '<em><b>Get Relative Path Without Base</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation PATH_ELEMENT___GET_RELATIVE_PATH_WITHOUT_BASE = eINSTANCE.getPathElement__GetRelativePathWithoutBase();

		/**
		 * The meta object literal for the '{@link gallery.impl.PictureLibraryImpl <em>Picture Library</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gallery.impl.PictureLibraryImpl
		 * @see gallery.impl.GalleryPackageImpl#getPictureLibrary()
		 * @generated
		 */
		EClass PICTURE_LIBRARY = eINSTANCE.getPictureLibrary();

		/**
		 * The meta object literal for the '<em><b>Base Path</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PICTURE_LIBRARY__BASE_PATH = eINSTANCE.getPictureLibrary_BasePath();

		/**
		 * The meta object literal for the '<em><b>Base Collection</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PICTURE_LIBRARY__BASE_COLLECTION = eINSTANCE.getPictureLibrary_BaseCollection();

		/**
		 * The meta object literal for the '<em><b>Deleted Pictures</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PICTURE_LIBRARY__DELETED_PICTURES = eINSTANCE.getPictureLibrary_DeletedPictures();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PICTURE_LIBRARY__NAME = eINSTANCE.getPictureLibrary_Name();

		/**
		 * The meta object literal for the '{@link gallery.impl.MetadataImpl <em>Metadata</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gallery.impl.MetadataImpl
		 * @see gallery.impl.GalleryPackageImpl#getMetadata()
		 * @generated
		 */
		EClass METADATA = eINSTANCE.getMetadata();

		/**
		 * The meta object literal for the '<em><b>Picture</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference METADATA__PICTURE = eINSTANCE.getMetadata_Picture();

		/**
		 * The meta object literal for the '<em><b>Size</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute METADATA__SIZE = eINSTANCE.getMetadata_Size();

		/**
		 * The meta object literal for the '<em><b>Landscape</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute METADATA__LANDSCAPE = eINSTANCE.getMetadata_Landscape();

		/**
		 * The meta object literal for the '<em><b>Created</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute METADATA__CREATED = eINSTANCE.getMetadata_Created();

		/**
		 * The meta object literal for the '<em><b>Height</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute METADATA__HEIGHT = eINSTANCE.getMetadata_Height();

		/**
		 * The meta object literal for the '<em><b>Width</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute METADATA__WIDTH = eINSTANCE.getMetadata_Width();

		/**
		 * The meta object literal for the '<em><b>Camera</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute METADATA__CAMERA = eINSTANCE.getMetadata_Camera();

		/**
		 * The meta object literal for the '{@link gallery.impl.RealPictureCollectionImpl <em>Real Picture Collection</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gallery.impl.RealPictureCollectionImpl
		 * @see gallery.impl.GalleryPackageImpl#getRealPictureCollection()
		 * @generated
		 */
		EClass REAL_PICTURE_COLLECTION = eINSTANCE.getRealPictureCollection();

		/**
		 * The meta object literal for the '<em><b>Linked By</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REAL_PICTURE_COLLECTION__LINKED_BY = eINSTANCE.getRealPictureCollection_LinkedBy();

		/**
		 * The meta object literal for the '<em><b>Library</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REAL_PICTURE_COLLECTION__LIBRARY = eINSTANCE.getRealPictureCollection_Library();

		/**
		 * The meta object literal for the '<em><b>Pictures</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REAL_PICTURE_COLLECTION__PICTURES = eINSTANCE.getRealPictureCollection_Pictures();

		/**
		 * The meta object literal for the '<em><b>Sub Collections</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REAL_PICTURE_COLLECTION__SUB_COLLECTIONS = eINSTANCE.getRealPictureCollection_SubCollections();

		/**
		 * The meta object literal for the '{@link gallery.impl.LinkedPictureCollectionImpl <em>Linked Picture Collection</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gallery.impl.LinkedPictureCollectionImpl
		 * @see gallery.impl.GalleryPackageImpl#getLinkedPictureCollection()
		 * @generated
		 */
		EClass LINKED_PICTURE_COLLECTION = eINSTANCE.getLinkedPictureCollection();

		/**
		 * The meta object literal for the '<em><b>Real Collection</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LINKED_PICTURE_COLLECTION__REAL_COLLECTION = eINSTANCE.getLinkedPictureCollection_RealCollection();

		/**
		 * The meta object literal for the '{@link gallery.impl.DeletedPictureImpl <em>Deleted Picture</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gallery.impl.DeletedPictureImpl
		 * @see gallery.impl.GalleryPackageImpl#getDeletedPicture()
		 * @generated
		 */
		EClass DELETED_PICTURE = eINSTANCE.getDeletedPicture();

		/**
		 * The meta object literal for the '<em><b>Relative Path</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DELETED_PICTURE__RELATIVE_PATH = eINSTANCE.getDeletedPicture_RelativePath();

		/**
		 * The meta object literal for the '<em><b>Library</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DELETED_PICTURE__LIBRARY = eINSTANCE.getDeletedPicture_Library();

		/**
		 * The meta object literal for the '{@link gallery.impl.PictureWithHashImpl <em>Picture With Hash</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gallery.impl.PictureWithHashImpl
		 * @see gallery.impl.GalleryPackageImpl#getPictureWithHash()
		 * @generated
		 */
		EClass PICTURE_WITH_HASH = eINSTANCE.getPictureWithHash();

		/**
		 * The meta object literal for the '<em><b>Hash</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PICTURE_WITH_HASH__HASH = eINSTANCE.getPictureWithHash_Hash();

		/**
		 * The meta object literal for the '<em><b>Hash Fast</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PICTURE_WITH_HASH__HASH_FAST = eINSTANCE.getPictureWithHash_HashFast();

	}

} //GalleryPackage
