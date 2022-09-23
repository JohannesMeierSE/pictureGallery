/**
 */
package gallery.impl;

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

import gallery.GalleryPackage;
import gallery.Metadata;
import gallery.RealPicture;

import java.util.Date;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Metadata</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gallery.impl.MetadataImpl#getPicture <em>Picture</em>}</li>
 *   <li>{@link gallery.impl.MetadataImpl#getSize <em>Size</em>}</li>
 *   <li>{@link gallery.impl.MetadataImpl#isLandscape <em>Landscape</em>}</li>
 *   <li>{@link gallery.impl.MetadataImpl#getCreated <em>Created</em>}</li>
 *   <li>{@link gallery.impl.MetadataImpl#getHeight <em>Height</em>}</li>
 *   <li>{@link gallery.impl.MetadataImpl#getWidth <em>Width</em>}</li>
 *   <li>{@link gallery.impl.MetadataImpl#getCamera <em>Camera</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MetadataImpl extends MinimalEObjectImpl.Container implements Metadata {
	/**
	 * The default value of the '{@link #getSize() <em>Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSize()
	 * @generated
	 * @ordered
	 */
	protected static final int SIZE_EDEFAULT = -1;
	/**
	 * The cached value of the '{@link #getSize() <em>Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSize()
	 * @generated
	 * @ordered
	 */
	protected int size = SIZE_EDEFAULT;
	/**
	 * The default value of the '{@link #isLandscape() <em>Landscape</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isLandscape()
	 * @generated
	 * @ordered
	 */
	protected static final boolean LANDSCAPE_EDEFAULT = true;
	/**
	 * The cached value of the '{@link #isLandscape() <em>Landscape</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isLandscape()
	 * @generated
	 * @ordered
	 */
	protected boolean landscape = LANDSCAPE_EDEFAULT;
	/**
	 * The default value of the '{@link #getCreated() <em>Created</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCreated()
	 * @generated
	 * @ordered
	 */
	protected static final Date CREATED_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getCreated() <em>Created</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCreated()
	 * @generated
	 * @ordered
	 */
	protected Date created = CREATED_EDEFAULT;
	/**
	 * The default value of the '{@link #getHeight() <em>Height</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHeight()
	 * @generated
	 * @ordered
	 */
	protected static final int HEIGHT_EDEFAULT = -1;
	/**
	 * The cached value of the '{@link #getHeight() <em>Height</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHeight()
	 * @generated
	 * @ordered
	 */
	protected int height = HEIGHT_EDEFAULT;
	/**
	 * The default value of the '{@link #getWidth() <em>Width</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWidth()
	 * @generated
	 * @ordered
	 */
	protected static final int WIDTH_EDEFAULT = -1;
	/**
	 * The cached value of the '{@link #getWidth() <em>Width</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWidth()
	 * @generated
	 * @ordered
	 */
	protected int width = WIDTH_EDEFAULT;
	/**
	 * The default value of the '{@link #getCamera() <em>Camera</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCamera()
	 * @generated
	 * @ordered
	 */
	protected static final String CAMERA_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getCamera() <em>Camera</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCamera()
	 * @generated
	 * @ordered
	 */
	protected String camera = CAMERA_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MetadataImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return GalleryPackage.Literals.METADATA;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RealPicture getPicture() {
		if (eContainerFeatureID() != GalleryPackage.METADATA__PICTURE) return null;
		return (RealPicture)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetPicture(RealPicture newPicture, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newPicture, GalleryPackage.METADATA__PICTURE, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPicture(RealPicture newPicture) {
		if (newPicture != eInternalContainer() || (eContainerFeatureID() != GalleryPackage.METADATA__PICTURE && newPicture != null)) {
			if (EcoreUtil.isAncestor(this, newPicture))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newPicture != null)
				msgs = ((InternalEObject)newPicture).eInverseAdd(this, GalleryPackage.REAL_PICTURE__METADATA, RealPicture.class, msgs);
			msgs = basicSetPicture(newPicture, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GalleryPackage.METADATA__PICTURE, newPicture, newPicture));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getSize() {
		return size;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSize(int newSize) {
		int oldSize = size;
		size = newSize;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GalleryPackage.METADATA__SIZE, oldSize, size));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isLandscape() {
		return landscape;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLandscape(boolean newLandscape) {
		boolean oldLandscape = landscape;
		landscape = newLandscape;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GalleryPackage.METADATA__LANDSCAPE, oldLandscape, landscape));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCreated(Date newCreated) {
		Date oldCreated = created;
		created = newCreated;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GalleryPackage.METADATA__CREATED, oldCreated, created));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setHeight(int newHeight) {
		int oldHeight = height;
		height = newHeight;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GalleryPackage.METADATA__HEIGHT, oldHeight, height));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWidth(int newWidth) {
		int oldWidth = width;
		width = newWidth;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GalleryPackage.METADATA__WIDTH, oldWidth, width));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getCamera() {
		return camera;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCamera(String newCamera) {
		String oldCamera = camera;
		camera = newCamera;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GalleryPackage.METADATA__CAMERA, oldCamera, camera));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case GalleryPackage.METADATA__PICTURE:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetPicture((RealPicture)otherEnd, msgs);
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
			case GalleryPackage.METADATA__PICTURE:
				return basicSetPicture(null, msgs);
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
			case GalleryPackage.METADATA__PICTURE:
				return eInternalContainer().eInverseRemove(this, GalleryPackage.REAL_PICTURE__METADATA, RealPicture.class, msgs);
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
			case GalleryPackage.METADATA__PICTURE:
				return getPicture();
			case GalleryPackage.METADATA__SIZE:
				return getSize();
			case GalleryPackage.METADATA__LANDSCAPE:
				return isLandscape();
			case GalleryPackage.METADATA__CREATED:
				return getCreated();
			case GalleryPackage.METADATA__HEIGHT:
				return getHeight();
			case GalleryPackage.METADATA__WIDTH:
				return getWidth();
			case GalleryPackage.METADATA__CAMERA:
				return getCamera();
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
			case GalleryPackage.METADATA__PICTURE:
				setPicture((RealPicture)newValue);
				return;
			case GalleryPackage.METADATA__SIZE:
				setSize((Integer)newValue);
				return;
			case GalleryPackage.METADATA__LANDSCAPE:
				setLandscape((Boolean)newValue);
				return;
			case GalleryPackage.METADATA__CREATED:
				setCreated((Date)newValue);
				return;
			case GalleryPackage.METADATA__HEIGHT:
				setHeight((Integer)newValue);
				return;
			case GalleryPackage.METADATA__WIDTH:
				setWidth((Integer)newValue);
				return;
			case GalleryPackage.METADATA__CAMERA:
				setCamera((String)newValue);
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
			case GalleryPackage.METADATA__PICTURE:
				setPicture((RealPicture)null);
				return;
			case GalleryPackage.METADATA__SIZE:
				setSize(SIZE_EDEFAULT);
				return;
			case GalleryPackage.METADATA__LANDSCAPE:
				setLandscape(LANDSCAPE_EDEFAULT);
				return;
			case GalleryPackage.METADATA__CREATED:
				setCreated(CREATED_EDEFAULT);
				return;
			case GalleryPackage.METADATA__HEIGHT:
				setHeight(HEIGHT_EDEFAULT);
				return;
			case GalleryPackage.METADATA__WIDTH:
				setWidth(WIDTH_EDEFAULT);
				return;
			case GalleryPackage.METADATA__CAMERA:
				setCamera(CAMERA_EDEFAULT);
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
			case GalleryPackage.METADATA__PICTURE:
				return getPicture() != null;
			case GalleryPackage.METADATA__SIZE:
				return size != SIZE_EDEFAULT;
			case GalleryPackage.METADATA__LANDSCAPE:
				return landscape != LANDSCAPE_EDEFAULT;
			case GalleryPackage.METADATA__CREATED:
				return CREATED_EDEFAULT == null ? created != null : !CREATED_EDEFAULT.equals(created);
			case GalleryPackage.METADATA__HEIGHT:
				return height != HEIGHT_EDEFAULT;
			case GalleryPackage.METADATA__WIDTH:
				return width != WIDTH_EDEFAULT;
			case GalleryPackage.METADATA__CAMERA:
				return CAMERA_EDEFAULT == null ? camera != null : !CAMERA_EDEFAULT.equals(camera);
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

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (size: ");
		result.append(size);
		result.append(", landscape: ");
		result.append(landscape);
		result.append(", created: ");
		result.append(created);
		result.append(", height: ");
		result.append(height);
		result.append(", width: ");
		result.append(width);
		result.append(", camera: ");
		result.append(camera);
		result.append(')');
		return result.toString();
	}

} //MetadataImpl
