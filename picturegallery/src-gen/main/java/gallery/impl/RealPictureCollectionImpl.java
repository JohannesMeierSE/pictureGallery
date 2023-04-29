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

import gallery.GalleryPackage;
import gallery.LinkedPictureCollection;
import gallery.Picture;
import gallery.PictureCollection;
import gallery.PictureLibrary;
import gallery.RealPictureCollection;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Real Picture Collection</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gallery.impl.RealPictureCollectionImpl#getLinkedBy <em>Linked By</em>}</li>
 *   <li>{@link gallery.impl.RealPictureCollectionImpl#getLibrary <em>Library</em>}</li>
 *   <li>{@link gallery.impl.RealPictureCollectionImpl#getPictures <em>Pictures</em>}</li>
 *   <li>{@link gallery.impl.RealPictureCollectionImpl#getSubCollections <em>Sub Collections</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RealPictureCollectionImpl extends PictureCollectionImpl implements RealPictureCollection {
	/**
	 * The cached value of the '{@link #getLinkedBy() <em>Linked By</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLinkedBy()
	 * @generated
	 * @ordered
	 */
	protected EList<LinkedPictureCollection> linkedBy;

	/**
	 * The cached value of the '{@link #getPictures() <em>Pictures</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPictures()
	 * @generated
	 * @ordered
	 */
	protected EList<Picture> pictures;

	/**
	 * The cached value of the '{@link #getSubCollections() <em>Sub Collections</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSubCollections()
	 * @generated
	 * @ordered
	 */
	protected EList<PictureCollection> subCollections;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RealPictureCollectionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return GalleryPackage.Literals.REAL_PICTURE_COLLECTION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<LinkedPictureCollection> getLinkedBy() {
		if (linkedBy == null) {
			linkedBy = new EObjectWithInverseResolvingEList<LinkedPictureCollection>(LinkedPictureCollection.class, this, GalleryPackage.REAL_PICTURE_COLLECTION__LINKED_BY, GalleryPackage.LINKED_PICTURE_COLLECTION__REAL_COLLECTION);
		}
		return linkedBy;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PictureLibrary getLibrary() {
		if (eContainerFeatureID() != GalleryPackage.REAL_PICTURE_COLLECTION__LIBRARY) return null;
		return (PictureLibrary)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetLibrary(PictureLibrary newLibrary, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newLibrary, GalleryPackage.REAL_PICTURE_COLLECTION__LIBRARY, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLibrary(PictureLibrary newLibrary) {
		if (newLibrary != eInternalContainer() || (eContainerFeatureID() != GalleryPackage.REAL_PICTURE_COLLECTION__LIBRARY && newLibrary != null)) {
			if (EcoreUtil.isAncestor(this, newLibrary))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newLibrary != null)
				msgs = ((InternalEObject)newLibrary).eInverseAdd(this, GalleryPackage.PICTURE_LIBRARY__BASE_COLLECTION, PictureLibrary.class, msgs);
			msgs = basicSetLibrary(newLibrary, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GalleryPackage.REAL_PICTURE_COLLECTION__LIBRARY, newLibrary, newLibrary));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Picture> getPictures() {
		if (pictures == null) {
			pictures = new EObjectContainmentWithInverseEList<Picture>(Picture.class, this, GalleryPackage.REAL_PICTURE_COLLECTION__PICTURES, GalleryPackage.PICTURE__COLLECTION);
		}
		return pictures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<PictureCollection> getSubCollections() {
		if (subCollections == null) {
			subCollections = new EObjectContainmentWithInverseEList<PictureCollection>(PictureCollection.class, this, GalleryPackage.REAL_PICTURE_COLLECTION__SUB_COLLECTIONS, GalleryPackage.PICTURE_COLLECTION__SUPER_COLLECTION);
		}
		return subCollections;
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
			case GalleryPackage.REAL_PICTURE_COLLECTION__LINKED_BY:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getLinkedBy()).basicAdd(otherEnd, msgs);
			case GalleryPackage.REAL_PICTURE_COLLECTION__LIBRARY:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetLibrary((PictureLibrary)otherEnd, msgs);
			case GalleryPackage.REAL_PICTURE_COLLECTION__PICTURES:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getPictures()).basicAdd(otherEnd, msgs);
			case GalleryPackage.REAL_PICTURE_COLLECTION__SUB_COLLECTIONS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getSubCollections()).basicAdd(otherEnd, msgs);
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
			case GalleryPackage.REAL_PICTURE_COLLECTION__LINKED_BY:
				return ((InternalEList<?>)getLinkedBy()).basicRemove(otherEnd, msgs);
			case GalleryPackage.REAL_PICTURE_COLLECTION__LIBRARY:
				return basicSetLibrary(null, msgs);
			case GalleryPackage.REAL_PICTURE_COLLECTION__PICTURES:
				return ((InternalEList<?>)getPictures()).basicRemove(otherEnd, msgs);
			case GalleryPackage.REAL_PICTURE_COLLECTION__SUB_COLLECTIONS:
				return ((InternalEList<?>)getSubCollections()).basicRemove(otherEnd, msgs);
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
			case GalleryPackage.REAL_PICTURE_COLLECTION__LIBRARY:
				return eInternalContainer().eInverseRemove(this, GalleryPackage.PICTURE_LIBRARY__BASE_COLLECTION, PictureLibrary.class, msgs);
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
			case GalleryPackage.REAL_PICTURE_COLLECTION__LINKED_BY:
				return getLinkedBy();
			case GalleryPackage.REAL_PICTURE_COLLECTION__LIBRARY:
				return getLibrary();
			case GalleryPackage.REAL_PICTURE_COLLECTION__PICTURES:
				return getPictures();
			case GalleryPackage.REAL_PICTURE_COLLECTION__SUB_COLLECTIONS:
				return getSubCollections();
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
			case GalleryPackage.REAL_PICTURE_COLLECTION__LINKED_BY:
				getLinkedBy().clear();
				getLinkedBy().addAll((Collection<? extends LinkedPictureCollection>)newValue);
				return;
			case GalleryPackage.REAL_PICTURE_COLLECTION__LIBRARY:
				setLibrary((PictureLibrary)newValue);
				return;
			case GalleryPackage.REAL_PICTURE_COLLECTION__PICTURES:
				getPictures().clear();
				getPictures().addAll((Collection<? extends Picture>)newValue);
				return;
			case GalleryPackage.REAL_PICTURE_COLLECTION__SUB_COLLECTIONS:
				getSubCollections().clear();
				getSubCollections().addAll((Collection<? extends PictureCollection>)newValue);
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
			case GalleryPackage.REAL_PICTURE_COLLECTION__LINKED_BY:
				getLinkedBy().clear();
				return;
			case GalleryPackage.REAL_PICTURE_COLLECTION__LIBRARY:
				setLibrary((PictureLibrary)null);
				return;
			case GalleryPackage.REAL_PICTURE_COLLECTION__PICTURES:
				getPictures().clear();
				return;
			case GalleryPackage.REAL_PICTURE_COLLECTION__SUB_COLLECTIONS:
				getSubCollections().clear();
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
			case GalleryPackage.REAL_PICTURE_COLLECTION__LINKED_BY:
				return linkedBy != null && !linkedBy.isEmpty();
			case GalleryPackage.REAL_PICTURE_COLLECTION__LIBRARY:
				return getLibrary() != null;
			case GalleryPackage.REAL_PICTURE_COLLECTION__PICTURES:
				return pictures != null && !pictures.isEmpty();
			case GalleryPackage.REAL_PICTURE_COLLECTION__SUB_COLLECTIONS:
				return subCollections != null && !subCollections.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //RealPictureCollectionImpl
