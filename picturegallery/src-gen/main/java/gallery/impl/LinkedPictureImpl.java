/**
 */
package gallery.impl;

import gallery.GalleryPackage;
import gallery.LinkedPicture;
import gallery.Metadata;
import gallery.RealPicture;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Linked Picture</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gallery.impl.LinkedPictureImpl#getRealPicture <em>Real Picture</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LinkedPictureImpl extends PictureImpl implements LinkedPicture {
	/**
	 * The cached value of the '{@link #getRealPicture() <em>Real Picture</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRealPicture()
	 * @generated
	 * @ordered
	 */
	protected RealPicture realPicture;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LinkedPictureImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return GalleryPackage.Literals.LINKED_PICTURE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RealPicture getRealPicture() {
		if (realPicture != null && realPicture.eIsProxy()) {
			InternalEObject oldRealPicture = (InternalEObject)realPicture;
			realPicture = (RealPicture)eResolveProxy(oldRealPicture);
			if (realPicture != oldRealPicture) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, GalleryPackage.LINKED_PICTURE__REAL_PICTURE, oldRealPicture, realPicture));
			}
		}
		return realPicture;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RealPicture basicGetRealPicture() {
		return realPicture;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetRealPicture(RealPicture newRealPicture, NotificationChain msgs) {
		RealPicture oldRealPicture = realPicture;
		realPicture = newRealPicture;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, GalleryPackage.LINKED_PICTURE__REAL_PICTURE, oldRealPicture, newRealPicture);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRealPicture(RealPicture newRealPicture) {
		if (newRealPicture != realPicture) {
			NotificationChain msgs = null;
			if (realPicture != null)
				msgs = ((InternalEObject)realPicture).eInverseRemove(this, GalleryPackage.REAL_PICTURE__LINKED_BY, RealPicture.class, msgs);
			if (newRealPicture != null)
				msgs = ((InternalEObject)newRealPicture).eInverseAdd(this, GalleryPackage.REAL_PICTURE__LINKED_BY, RealPicture.class, msgs);
			msgs = basicSetRealPicture(newRealPicture, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GalleryPackage.LINKED_PICTURE__REAL_PICTURE, newRealPicture, newRealPicture));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case GalleryPackage.LINKED_PICTURE__REAL_PICTURE:
				if (realPicture != null)
					msgs = ((InternalEObject)realPicture).eInverseRemove(this, GalleryPackage.REAL_PICTURE__LINKED_BY, RealPicture.class, msgs);
				return basicSetRealPicture((RealPicture)otherEnd, msgs);
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
			case GalleryPackage.LINKED_PICTURE__REAL_PICTURE:
				return basicSetRealPicture(null, msgs);
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
			case GalleryPackage.LINKED_PICTURE__REAL_PICTURE:
				if (resolve) return getRealPicture();
				return basicGetRealPicture();
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
			case GalleryPackage.LINKED_PICTURE__REAL_PICTURE:
				setRealPicture((RealPicture)newValue);
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
			case GalleryPackage.LINKED_PICTURE__REAL_PICTURE:
				setRealPicture((RealPicture)null);
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
			case GalleryPackage.LINKED_PICTURE__REAL_PICTURE:
				return realPicture != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * user-defined code!
	 */
	@Override
	public Metadata getMetadata() {
		return getRealPicture().getMetadata();
	}

} //LinkedPictureImpl
