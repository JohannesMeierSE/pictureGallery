/**
 */
package gallery.impl;

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

import gallery.GalleryPackage;
import gallery.Picture;
import gallery.PictureCollection;
import gallery.RealPictureCollection;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Picture Collection</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gallery.impl.PictureCollectionImpl#getSuperCollection <em>Super Collection</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class PictureCollectionImpl extends PathElementImpl implements PictureCollection {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PictureCollectionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return GalleryPackage.Literals.PICTURE_COLLECTION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EList<PictureCollection> getSubCollections() {
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RealPictureCollection getSuperCollection() {
		if (eContainerFeatureID() != GalleryPackage.PICTURE_COLLECTION__SUPER_COLLECTION) return null;
		return (RealPictureCollection)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSuperCollection(RealPictureCollection newSuperCollection, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newSuperCollection, GalleryPackage.PICTURE_COLLECTION__SUPER_COLLECTION, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSuperCollection(RealPictureCollection newSuperCollection) {
		if (newSuperCollection != eInternalContainer() || (eContainerFeatureID() != GalleryPackage.PICTURE_COLLECTION__SUPER_COLLECTION && newSuperCollection != null)) {
			if (EcoreUtil.isAncestor(this, newSuperCollection))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newSuperCollection != null)
				msgs = ((InternalEObject)newSuperCollection).eInverseAdd(this, GalleryPackage.REAL_PICTURE_COLLECTION__SUB_COLLECTIONS, RealPictureCollection.class, msgs);
			msgs = basicSetSuperCollection(newSuperCollection, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GalleryPackage.PICTURE_COLLECTION__SUPER_COLLECTION, newSuperCollection, newSuperCollection));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EList<Picture> getPictures() {
		throw new UnsupportedOperationException();
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
			case GalleryPackage.PICTURE_COLLECTION__SUPER_COLLECTION:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetSuperCollection((RealPictureCollection)otherEnd, msgs);
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
			case GalleryPackage.PICTURE_COLLECTION__SUPER_COLLECTION:
				return basicSetSuperCollection(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
		switch (eContainerFeatureID()) {
			case GalleryPackage.PICTURE_COLLECTION__SUPER_COLLECTION:
				return eInternalContainer().eInverseRemove(this, GalleryPackage.REAL_PICTURE_COLLECTION__SUB_COLLECTIONS, RealPictureCollection.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case GalleryPackage.PICTURE_COLLECTION__SUPER_COLLECTION:
				return getSuperCollection();
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
			case GalleryPackage.PICTURE_COLLECTION__SUPER_COLLECTION:
				setSuperCollection((RealPictureCollection)newValue);
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
			case GalleryPackage.PICTURE_COLLECTION__SUPER_COLLECTION:
				setSuperCollection((RealPictureCollection)null);
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
			case GalleryPackage.PICTURE_COLLECTION__SUPER_COLLECTION:
				return getSuperCollection() != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case GalleryPackage.PICTURE_COLLECTION___GET_PICTURES:
				return getPictures();
			case GalleryPackage.PICTURE_COLLECTION___GET_SUB_COLLECTIONS:
				return getSubCollections();
		}
		return super.eInvoke(operationID, arguments);
	}

	/**
	 * user-defined code!
	 */
	@Override
	public String getFullPath() {
		if (getSuperCollection() == null) {
			return ((RealPictureCollection) this).getLibrary().getBasePath() + File.separator + getName();
		} else {
			return getSuperCollection().getFullPath() + File.separator + getName();
		}
	}

	/**
	 * user-defined code!
	 */
	@Override
	public String getRelativePath() {
		if (getSuperCollection() == null) {
			return getName();
		} else {
			return getSuperCollection().getRelativePath() + File.separator + getName();
		}
	}

	/**
	 * user-defined code!
	 */
	@Override
	public String getRelativePathWithoutBase() {
		if (getSuperCollection() == null) {
			return "";
		} else {
			String relativePath = getSuperCollection().getRelativePathWithoutBase();
			if (relativePath == null || relativePath.isEmpty()) {
				return getName();
			} else {
				return relativePath + File.separator + getName();
			}
		}
	}

} //PictureCollectionImpl
