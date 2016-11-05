/**
 */
package gallery.impl;

import gallery.GalleryPackage;
import gallery.LinkedPicture;
import gallery.RealPicture;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Real Picture</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gallery.impl.RealPictureImpl#getLinkedBy <em>Linked By</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RealPictureImpl extends PictureImpl implements RealPicture {
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
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case GalleryPackage.REAL_PICTURE__LINKED_BY:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getLinkedBy()).basicAdd(otherEnd, msgs);
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
			case GalleryPackage.REAL_PICTURE__LINKED_BY:
				return getLinkedBy();
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
			case GalleryPackage.REAL_PICTURE__LINKED_BY:
				getLinkedBy().clear();
				getLinkedBy().addAll((Collection<? extends LinkedPicture>)newValue);
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
			case GalleryPackage.REAL_PICTURE__LINKED_BY:
				getLinkedBy().clear();
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
			case GalleryPackage.REAL_PICTURE__LINKED_BY:
				return linkedBy != null && !linkedBy.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //RealPictureImpl
