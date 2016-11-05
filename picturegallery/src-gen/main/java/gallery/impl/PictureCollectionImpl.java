/**
 */
package gallery.impl;

import gallery.GalleryPackage;
import gallery.Picture;
import gallery.PictureCollection;
import gallery.PictureLibrary;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Picture Collection</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gallery.impl.PictureCollectionImpl#getSubCollections <em>Sub Collections</em>}</li>
 *   <li>{@link gallery.impl.PictureCollectionImpl#getSuperCollection <em>Super Collection</em>}</li>
 *   <li>{@link gallery.impl.PictureCollectionImpl#getPictures <em>Pictures</em>}</li>
 *   <li>{@link gallery.impl.PictureCollectionImpl#getLibrary <em>Library</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PictureCollectionImpl extends PathElementImpl implements PictureCollection {
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
	 * The cached value of the '{@link #getPictures() <em>Pictures</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPictures()
	 * @generated
	 * @ordered
	 */
	protected EList<Picture> pictures;

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
	 * @generated
	 */
	public EList<PictureCollection> getSubCollections() {
		if (subCollections == null) {
			subCollections = new EObjectContainmentWithInverseEList<PictureCollection>(PictureCollection.class, this, GalleryPackage.PICTURE_COLLECTION__SUB_COLLECTIONS, GalleryPackage.PICTURE_COLLECTION__SUPER_COLLECTION);
		}
		return subCollections;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PictureCollection getSuperCollection() {
		if (eContainerFeatureID() != GalleryPackage.PICTURE_COLLECTION__SUPER_COLLECTION) return null;
		return (PictureCollection)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSuperCollection(PictureCollection newSuperCollection, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newSuperCollection, GalleryPackage.PICTURE_COLLECTION__SUPER_COLLECTION, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSuperCollection(PictureCollection newSuperCollection) {
		if (newSuperCollection != eInternalContainer() || (eContainerFeatureID() != GalleryPackage.PICTURE_COLLECTION__SUPER_COLLECTION && newSuperCollection != null)) {
			if (EcoreUtil.isAncestor(this, newSuperCollection))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newSuperCollection != null)
				msgs = ((InternalEObject)newSuperCollection).eInverseAdd(this, GalleryPackage.PICTURE_COLLECTION__SUB_COLLECTIONS, PictureCollection.class, msgs);
			msgs = basicSetSuperCollection(newSuperCollection, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GalleryPackage.PICTURE_COLLECTION__SUPER_COLLECTION, newSuperCollection, newSuperCollection));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Picture> getPictures() {
		if (pictures == null) {
			pictures = new EObjectContainmentWithInverseEList<Picture>(Picture.class, this, GalleryPackage.PICTURE_COLLECTION__PICTURES, GalleryPackage.PICTURE__COLLECTION);
		}
		return pictures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PictureLibrary getLibrary() {
		if (eContainerFeatureID() != GalleryPackage.PICTURE_COLLECTION__LIBRARY) return null;
		return (PictureLibrary)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetLibrary(PictureLibrary newLibrary, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newLibrary, GalleryPackage.PICTURE_COLLECTION__LIBRARY, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLibrary(PictureLibrary newLibrary) {
		if (newLibrary != eInternalContainer() || (eContainerFeatureID() != GalleryPackage.PICTURE_COLLECTION__LIBRARY && newLibrary != null)) {
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
			eNotify(new ENotificationImpl(this, Notification.SET, GalleryPackage.PICTURE_COLLECTION__LIBRARY, newLibrary, newLibrary));
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
			case GalleryPackage.PICTURE_COLLECTION__SUB_COLLECTIONS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getSubCollections()).basicAdd(otherEnd, msgs);
			case GalleryPackage.PICTURE_COLLECTION__SUPER_COLLECTION:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetSuperCollection((PictureCollection)otherEnd, msgs);
			case GalleryPackage.PICTURE_COLLECTION__PICTURES:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getPictures()).basicAdd(otherEnd, msgs);
			case GalleryPackage.PICTURE_COLLECTION__LIBRARY:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetLibrary((PictureLibrary)otherEnd, msgs);
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
			case GalleryPackage.PICTURE_COLLECTION__SUB_COLLECTIONS:
				return ((InternalEList<?>)getSubCollections()).basicRemove(otherEnd, msgs);
			case GalleryPackage.PICTURE_COLLECTION__SUPER_COLLECTION:
				return basicSetSuperCollection(null, msgs);
			case GalleryPackage.PICTURE_COLLECTION__PICTURES:
				return ((InternalEList<?>)getPictures()).basicRemove(otherEnd, msgs);
			case GalleryPackage.PICTURE_COLLECTION__LIBRARY:
				return basicSetLibrary(null, msgs);
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
				return eInternalContainer().eInverseRemove(this, GalleryPackage.PICTURE_COLLECTION__SUB_COLLECTIONS, PictureCollection.class, msgs);
			case GalleryPackage.PICTURE_COLLECTION__LIBRARY:
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
			case GalleryPackage.PICTURE_COLLECTION__SUB_COLLECTIONS:
				return getSubCollections();
			case GalleryPackage.PICTURE_COLLECTION__SUPER_COLLECTION:
				return getSuperCollection();
			case GalleryPackage.PICTURE_COLLECTION__PICTURES:
				return getPictures();
			case GalleryPackage.PICTURE_COLLECTION__LIBRARY:
				return getLibrary();
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
			case GalleryPackage.PICTURE_COLLECTION__SUB_COLLECTIONS:
				getSubCollections().clear();
				getSubCollections().addAll((Collection<? extends PictureCollection>)newValue);
				return;
			case GalleryPackage.PICTURE_COLLECTION__SUPER_COLLECTION:
				setSuperCollection((PictureCollection)newValue);
				return;
			case GalleryPackage.PICTURE_COLLECTION__PICTURES:
				getPictures().clear();
				getPictures().addAll((Collection<? extends Picture>)newValue);
				return;
			case GalleryPackage.PICTURE_COLLECTION__LIBRARY:
				setLibrary((PictureLibrary)newValue);
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
			case GalleryPackage.PICTURE_COLLECTION__SUB_COLLECTIONS:
				getSubCollections().clear();
				return;
			case GalleryPackage.PICTURE_COLLECTION__SUPER_COLLECTION:
				setSuperCollection((PictureCollection)null);
				return;
			case GalleryPackage.PICTURE_COLLECTION__PICTURES:
				getPictures().clear();
				return;
			case GalleryPackage.PICTURE_COLLECTION__LIBRARY:
				setLibrary((PictureLibrary)null);
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
			case GalleryPackage.PICTURE_COLLECTION__SUB_COLLECTIONS:
				return subCollections != null && !subCollections.isEmpty();
			case GalleryPackage.PICTURE_COLLECTION__SUPER_COLLECTION:
				return getSuperCollection() != null;
			case GalleryPackage.PICTURE_COLLECTION__PICTURES:
				return pictures != null && !pictures.isEmpty();
			case GalleryPackage.PICTURE_COLLECTION__LIBRARY:
				return getLibrary() != null;
		}
		return super.eIsSet(featureID);
	}

} //PictureCollectionImpl
