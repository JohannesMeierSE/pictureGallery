/**
 */
package gallery.impl;

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

import gallery.DeletedPicture;
import gallery.GalleryPackage;
import gallery.PictureLibrary;
import gallery.RealPictureCollection;
import gallery.TagCategory;
import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Picture Library</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link gallery.impl.PictureLibraryImpl#getBasePath <em>Base Path</em>}</li>
 *   <li>{@link gallery.impl.PictureLibraryImpl#getName <em>Name</em>}</li>
 *   <li>{@link gallery.impl.PictureLibraryImpl#getBaseCollection <em>Base Collection</em>}</li>
 *   <li>{@link gallery.impl.PictureLibraryImpl#getDeletedPictures <em>Deleted Pictures</em>}</li>
 *   <li>{@link gallery.impl.PictureLibraryImpl#getTagCategories <em>Tag Categories</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PictureLibraryImpl extends MinimalEObjectImpl.Container implements PictureLibrary {
	/**
	 * The default value of the '{@link #getBasePath() <em>Base Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBasePath()
	 * @generated
	 * @ordered
	 */
	protected static final String BASE_PATH_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getBasePath() <em>Base Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBasePath()
	 * @generated
	 * @ordered
	 */
	protected String basePath = BASE_PATH_EDEFAULT;

	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getBaseCollection() <em>Base Collection</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseCollection()
	 * @generated
	 * @ordered
	 */
	protected RealPictureCollection baseCollection;

	/**
	 * The cached value of the '{@link #getDeletedPictures() <em>Deleted Pictures</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDeletedPictures()
	 * @generated
	 * @ordered
	 */
	protected EList<DeletedPicture> deletedPictures;

	/**
	 * The cached value of the '{@link #getTagCategories() <em>Tag Categories</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTagCategories()
	 * @generated
	 * @ordered
	 */
	protected EList<TagCategory> tagCategories;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PictureLibraryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return GalleryPackage.Literals.PICTURE_LIBRARY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getBasePath() {
		return basePath;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBasePath(String newBasePath) {
		String oldBasePath = basePath;
		basePath = newBasePath;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GalleryPackage.PICTURE_LIBRARY__BASE_PATH, oldBasePath, basePath));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public RealPictureCollection getBaseCollection() {
		return baseCollection;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetBaseCollection(RealPictureCollection newBaseCollection, NotificationChain msgs) {
		RealPictureCollection oldBaseCollection = baseCollection;
		baseCollection = newBaseCollection;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, GalleryPackage.PICTURE_LIBRARY__BASE_COLLECTION, oldBaseCollection, newBaseCollection);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBaseCollection(RealPictureCollection newBaseCollection) {
		if (newBaseCollection != baseCollection) {
			NotificationChain msgs = null;
			if (baseCollection != null)
				msgs = ((InternalEObject)baseCollection).eInverseRemove(this, GalleryPackage.REAL_PICTURE_COLLECTION__LIBRARY, RealPictureCollection.class, msgs);
			if (newBaseCollection != null)
				msgs = ((InternalEObject)newBaseCollection).eInverseAdd(this, GalleryPackage.REAL_PICTURE_COLLECTION__LIBRARY, RealPictureCollection.class, msgs);
			msgs = basicSetBaseCollection(newBaseCollection, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GalleryPackage.PICTURE_LIBRARY__BASE_COLLECTION, newBaseCollection, newBaseCollection));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<DeletedPicture> getDeletedPictures() {
		if (deletedPictures == null) {
			deletedPictures = new EObjectContainmentWithInverseEList<DeletedPicture>(DeletedPicture.class, this, GalleryPackage.PICTURE_LIBRARY__DELETED_PICTURES, GalleryPackage.DELETED_PICTURE__LIBRARY);
		}
		return deletedPictures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<TagCategory> getTagCategories() {
		if (tagCategories == null) {
			tagCategories = new EObjectContainmentWithInverseEList<TagCategory>(TagCategory.class, this, GalleryPackage.PICTURE_LIBRARY__TAG_CATEGORIES, GalleryPackage.TAG_CATEGORY__LIBRARY);
		}
		return tagCategories;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GalleryPackage.PICTURE_LIBRARY__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case GalleryPackage.PICTURE_LIBRARY__BASE_COLLECTION:
				if (baseCollection != null)
					msgs = ((InternalEObject)baseCollection).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - GalleryPackage.PICTURE_LIBRARY__BASE_COLLECTION, null, msgs);
				return basicSetBaseCollection((RealPictureCollection)otherEnd, msgs);
			case GalleryPackage.PICTURE_LIBRARY__DELETED_PICTURES:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getDeletedPictures()).basicAdd(otherEnd, msgs);
			case GalleryPackage.PICTURE_LIBRARY__TAG_CATEGORIES:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getTagCategories()).basicAdd(otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case GalleryPackage.PICTURE_LIBRARY__BASE_COLLECTION:
				return basicSetBaseCollection(null, msgs);
			case GalleryPackage.PICTURE_LIBRARY__DELETED_PICTURES:
				return ((InternalEList<?>)getDeletedPictures()).basicRemove(otherEnd, msgs);
			case GalleryPackage.PICTURE_LIBRARY__TAG_CATEGORIES:
				return ((InternalEList<?>)getTagCategories()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case GalleryPackage.PICTURE_LIBRARY__BASE_PATH:
				return getBasePath();
			case GalleryPackage.PICTURE_LIBRARY__NAME:
				return getName();
			case GalleryPackage.PICTURE_LIBRARY__BASE_COLLECTION:
				return getBaseCollection();
			case GalleryPackage.PICTURE_LIBRARY__DELETED_PICTURES:
				return getDeletedPictures();
			case GalleryPackage.PICTURE_LIBRARY__TAG_CATEGORIES:
				return getTagCategories();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case GalleryPackage.PICTURE_LIBRARY__BASE_PATH:
				setBasePath((String)newValue);
				return;
			case GalleryPackage.PICTURE_LIBRARY__NAME:
				setName((String)newValue);
				return;
			case GalleryPackage.PICTURE_LIBRARY__BASE_COLLECTION:
				setBaseCollection((RealPictureCollection)newValue);
				return;
			case GalleryPackage.PICTURE_LIBRARY__DELETED_PICTURES:
				getDeletedPictures().clear();
				getDeletedPictures().addAll((Collection<? extends DeletedPicture>)newValue);
				return;
			case GalleryPackage.PICTURE_LIBRARY__TAG_CATEGORIES:
				getTagCategories().clear();
				getTagCategories().addAll((Collection<? extends TagCategory>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case GalleryPackage.PICTURE_LIBRARY__BASE_PATH:
				setBasePath(BASE_PATH_EDEFAULT);
				return;
			case GalleryPackage.PICTURE_LIBRARY__NAME:
				setName(NAME_EDEFAULT);
				return;
			case GalleryPackage.PICTURE_LIBRARY__BASE_COLLECTION:
				setBaseCollection((RealPictureCollection)null);
				return;
			case GalleryPackage.PICTURE_LIBRARY__DELETED_PICTURES:
				getDeletedPictures().clear();
				return;
			case GalleryPackage.PICTURE_LIBRARY__TAG_CATEGORIES:
				getTagCategories().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case GalleryPackage.PICTURE_LIBRARY__BASE_PATH:
				return BASE_PATH_EDEFAULT == null ? basePath != null : !BASE_PATH_EDEFAULT.equals(basePath);
			case GalleryPackage.PICTURE_LIBRARY__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case GalleryPackage.PICTURE_LIBRARY__BASE_COLLECTION:
				return baseCollection != null;
			case GalleryPackage.PICTURE_LIBRARY__DELETED_PICTURES:
				return deletedPictures != null && !deletedPictures.isEmpty();
			case GalleryPackage.PICTURE_LIBRARY__TAG_CATEGORIES:
				return tagCategories != null && !tagCategories.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (basePath: ");
		result.append(basePath);
		result.append(", name: ");
		result.append(name);
		result.append(')');
		return result.toString();
	}

} //PictureLibraryImpl
