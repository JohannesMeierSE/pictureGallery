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
import gallery.LinkedPictureCollection;
import gallery.Picture;
import gallery.PictureCollection;
import gallery.RealPictureCollection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Linked Picture Collection</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gallery.impl.LinkedPictureCollectionImpl#getRealCollection <em>Real Collection</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LinkedPictureCollectionImpl extends PictureCollectionImpl implements LinkedPictureCollection {
	/**
	 * The cached value of the '{@link #getRealCollection() <em>Real Collection</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRealCollection()
	 * @generated
	 * @ordered
	 */
	protected RealPictureCollection realCollection;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LinkedPictureCollectionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return GalleryPackage.Literals.LINKED_PICTURE_COLLECTION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RealPictureCollection getRealCollection() {
		if (realCollection != null && realCollection.eIsProxy()) {
			InternalEObject oldRealCollection = (InternalEObject)realCollection;
			realCollection = (RealPictureCollection)eResolveProxy(oldRealCollection);
			if (realCollection != oldRealCollection) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, GalleryPackage.LINKED_PICTURE_COLLECTION__REAL_COLLECTION, oldRealCollection, realCollection));
			}
		}
		return realCollection;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RealPictureCollection basicGetRealCollection() {
		return realCollection;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetRealCollection(RealPictureCollection newRealCollection, NotificationChain msgs) {
		RealPictureCollection oldRealCollection = realCollection;
		realCollection = newRealCollection;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, GalleryPackage.LINKED_PICTURE_COLLECTION__REAL_COLLECTION, oldRealCollection, newRealCollection);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRealCollection(RealPictureCollection newRealCollection) {
		if (newRealCollection != realCollection) {
			NotificationChain msgs = null;
			if (realCollection != null)
				msgs = ((InternalEObject)realCollection).eInverseRemove(this, GalleryPackage.REAL_PICTURE_COLLECTION__LINKED_BY, RealPictureCollection.class, msgs);
			if (newRealCollection != null)
				msgs = ((InternalEObject)newRealCollection).eInverseAdd(this, GalleryPackage.REAL_PICTURE_COLLECTION__LINKED_BY, RealPictureCollection.class, msgs);
			msgs = basicSetRealCollection(newRealCollection, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GalleryPackage.LINKED_PICTURE_COLLECTION__REAL_COLLECTION, newRealCollection, newRealCollection));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case GalleryPackage.LINKED_PICTURE_COLLECTION__REAL_COLLECTION:
				if (realCollection != null)
					msgs = ((InternalEObject)realCollection).eInverseRemove(this, GalleryPackage.REAL_PICTURE_COLLECTION__LINKED_BY, RealPictureCollection.class, msgs);
				return basicSetRealCollection((RealPictureCollection)otherEnd, msgs);
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
			case GalleryPackage.LINKED_PICTURE_COLLECTION__REAL_COLLECTION:
				return basicSetRealCollection(null, msgs);
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
			case GalleryPackage.LINKED_PICTURE_COLLECTION__REAL_COLLECTION:
				if (resolve) return getRealCollection();
				return basicGetRealCollection();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case GalleryPackage.LINKED_PICTURE_COLLECTION__REAL_COLLECTION:
				setRealCollection((RealPictureCollection)newValue);
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
			case GalleryPackage.LINKED_PICTURE_COLLECTION__REAL_COLLECTION:
				setRealCollection((RealPictureCollection)null);
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
			case GalleryPackage.LINKED_PICTURE_COLLECTION__REAL_COLLECTION:
				return realCollection != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * user-defined code!
	 */
	public EList<PictureCollection> getSubCollections() {
		return getRealCollection().getSubCollections();
	}

	/**
	 * user-defined code!
	 */
	public EList<Picture> getPictures() {
		return getRealCollection().getPictures();
	}
} //LinkedPictureCollectionImpl
