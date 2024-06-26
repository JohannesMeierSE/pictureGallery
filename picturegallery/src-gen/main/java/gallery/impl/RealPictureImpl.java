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
import gallery.LinkedPicture;
import gallery.Metadata;
import gallery.PictureWithHash;
import gallery.RealPicture;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Real Picture</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gallery.impl.RealPictureImpl#getHash <em>Hash</em>}</li>
 *   <li>{@link gallery.impl.RealPictureImpl#getHashFast <em>Hash Fast</em>}</li>
 *   <li>{@link gallery.impl.RealPictureImpl#getLinkedBy <em>Linked By</em>}</li>
 *   <li>{@link gallery.impl.RealPictureImpl#getMetadata <em>Metadata</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RealPictureImpl extends PictureImpl implements RealPicture {
	/**
	 * The default value of the '{@link #getHash() <em>Hash</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHash()
	 * @generated
	 * @ordered
	 */
	protected static final String HASH_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getHash() <em>Hash</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHash()
	 * @generated
	 * @ordered
	 */
	protected String hash = HASH_EDEFAULT;

	/**
	 * The default value of the '{@link #getHashFast() <em>Hash Fast</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHashFast()
	 * @generated
	 * @ordered
	 */
	protected static final String HASH_FAST_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getHashFast() <em>Hash Fast</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHashFast()
	 * @generated
	 * @ordered
	 */
	protected String hashFast = HASH_FAST_EDEFAULT;

	/**
	 * The cached value of the '{@link #getLinkedBy() <em>Linked By</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLinkedBy()
	 * @generated
	 * @ordered
	 */
	protected EList<LinkedPicture> linkedBy;

	/**
	 * The cached value of the '{@link #getMetadata() <em>Metadata</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMetadata()
	 * @generated
	 * @ordered
	 */
	protected Metadata metadata;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RealPictureImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return GalleryPackage.Literals.REAL_PICTURE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<LinkedPicture> getLinkedBy() {
		if (linkedBy == null) {
			linkedBy = new EObjectWithInverseResolvingEList<LinkedPicture>(LinkedPicture.class, this, GalleryPackage.REAL_PICTURE__LINKED_BY, GalleryPackage.LINKED_PICTURE__REAL_PICTURE);
		}
		return linkedBy;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Metadata getMetadata() {
		return metadata;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMetadata(Metadata newMetadata, NotificationChain msgs) {
		Metadata oldMetadata = metadata;
		metadata = newMetadata;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, GalleryPackage.REAL_PICTURE__METADATA, oldMetadata, newMetadata);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMetadata(Metadata newMetadata) {
		if (newMetadata != metadata) {
			NotificationChain msgs = null;
			if (metadata != null)
				msgs = ((InternalEObject)metadata).eInverseRemove(this, GalleryPackage.METADATA__PICTURE, Metadata.class, msgs);
			if (newMetadata != null)
				msgs = ((InternalEObject)newMetadata).eInverseAdd(this, GalleryPackage.METADATA__PICTURE, Metadata.class, msgs);
			msgs = basicSetMetadata(newMetadata, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GalleryPackage.REAL_PICTURE__METADATA, newMetadata, newMetadata));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getHash() {
		return hash;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setHash(String newHash) {
		String oldHash = hash;
		hash = newHash;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GalleryPackage.REAL_PICTURE__HASH, oldHash, hash));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getHashFast() {
		return hashFast;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setHashFast(String newHashFast) {
		String oldHashFast = hashFast;
		hashFast = newHashFast;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GalleryPackage.REAL_PICTURE__HASH_FAST, oldHashFast, hashFast));
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
			case GalleryPackage.REAL_PICTURE__LINKED_BY:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getLinkedBy()).basicAdd(otherEnd, msgs);
			case GalleryPackage.REAL_PICTURE__METADATA:
				if (metadata != null)
					msgs = ((InternalEObject)metadata).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - GalleryPackage.REAL_PICTURE__METADATA, null, msgs);
				return basicSetMetadata((Metadata)otherEnd, msgs);
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
			case GalleryPackage.REAL_PICTURE__LINKED_BY:
				return ((InternalEList<?>)getLinkedBy()).basicRemove(otherEnd, msgs);
			case GalleryPackage.REAL_PICTURE__METADATA:
				return basicSetMetadata(null, msgs);
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
			case GalleryPackage.REAL_PICTURE__HASH:
				return getHash();
			case GalleryPackage.REAL_PICTURE__HASH_FAST:
				return getHashFast();
			case GalleryPackage.REAL_PICTURE__LINKED_BY:
				return getLinkedBy();
			case GalleryPackage.REAL_PICTURE__METADATA:
				return getMetadata();
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
			case GalleryPackage.REAL_PICTURE__HASH:
				setHash((String)newValue);
				return;
			case GalleryPackage.REAL_PICTURE__HASH_FAST:
				setHashFast((String)newValue);
				return;
			case GalleryPackage.REAL_PICTURE__LINKED_BY:
				getLinkedBy().clear();
				getLinkedBy().addAll((Collection<? extends LinkedPicture>)newValue);
				return;
			case GalleryPackage.REAL_PICTURE__METADATA:
				setMetadata((Metadata)newValue);
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
			case GalleryPackage.REAL_PICTURE__HASH:
				setHash(HASH_EDEFAULT);
				return;
			case GalleryPackage.REAL_PICTURE__HASH_FAST:
				setHashFast(HASH_FAST_EDEFAULT);
				return;
			case GalleryPackage.REAL_PICTURE__LINKED_BY:
				getLinkedBy().clear();
				return;
			case GalleryPackage.REAL_PICTURE__METADATA:
				setMetadata((Metadata)null);
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
			case GalleryPackage.REAL_PICTURE__HASH:
				return HASH_EDEFAULT == null ? hash != null : !HASH_EDEFAULT.equals(hash);
			case GalleryPackage.REAL_PICTURE__HASH_FAST:
				return HASH_FAST_EDEFAULT == null ? hashFast != null : !HASH_FAST_EDEFAULT.equals(hashFast);
			case GalleryPackage.REAL_PICTURE__LINKED_BY:
				return linkedBy != null && !linkedBy.isEmpty();
			case GalleryPackage.REAL_PICTURE__METADATA:
				return metadata != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == PictureWithHash.class) {
			switch (derivedFeatureID) {
				case GalleryPackage.REAL_PICTURE__HASH: return GalleryPackage.PICTURE_WITH_HASH__HASH;
				case GalleryPackage.REAL_PICTURE__HASH_FAST: return GalleryPackage.PICTURE_WITH_HASH__HASH_FAST;
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == PictureWithHash.class) {
			switch (baseFeatureID) {
				case GalleryPackage.PICTURE_WITH_HASH__HASH: return GalleryPackage.REAL_PICTURE__HASH;
				case GalleryPackage.PICTURE_WITH_HASH__HASH_FAST: return GalleryPackage.REAL_PICTURE__HASH_FAST;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (hash: ");
		result.append(hash);
		result.append(", hashFast: ");
		result.append(hashFast);
		result.append(')');
		return result.toString();
	}

} //RealPictureImpl
