/**
 */
package gallery.util;

import gallery.*;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see gallery.GalleryPackage
 * @generated
 */
public class GallerySwitch<T> extends Switch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static GalleryPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GallerySwitch() {
		if (modelPackage == null) {
			modelPackage = GalleryPackage.eINSTANCE;
		}
	}

	/**
	 * Checks whether this is a switch for the given package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @parameter ePackage the package in question.
	 * @return whether this is a switch for the given package.
	 * @generated
	 */
	@Override
	protected boolean isSwitchFor(EPackage ePackage) {
		return ePackage == modelPackage;
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	@Override
	protected T doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case GalleryPackage.PICTURE_COLLECTION: {
				PictureCollection pictureCollection = (PictureCollection)theEObject;
				T result = casePictureCollection(pictureCollection);
				if (result == null) result = casePathElement(pictureCollection);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case GalleryPackage.PICTURE: {
				Picture picture = (Picture)theEObject;
				T result = casePicture(picture);
				if (result == null) result = casePathElement(picture);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case GalleryPackage.REAL_PICTURE: {
				RealPicture realPicture = (RealPicture)theEObject;
				T result = caseRealPicture(realPicture);
				if (result == null) result = casePicture(realPicture);
				if (result == null) result = casePathElement(realPicture);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case GalleryPackage.LINKED_PICTURE: {
				LinkedPicture linkedPicture = (LinkedPicture)theEObject;
				T result = caseLinkedPicture(linkedPicture);
				if (result == null) result = casePicture(linkedPicture);
				if (result == null) result = casePathElement(linkedPicture);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case GalleryPackage.PATH_ELEMENT: {
				PathElement pathElement = (PathElement)theEObject;
				T result = casePathElement(pathElement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case GalleryPackage.PICTURE_LIBRARY: {
				PictureLibrary pictureLibrary = (PictureLibrary)theEObject;
				T result = casePictureLibrary(pictureLibrary);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case GalleryPackage.METADATA: {
				Metadata metadata = (Metadata)theEObject;
				T result = caseMetadata(metadata);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case GalleryPackage.REAL_PICTURE_COLLECTION: {
				RealPictureCollection realPictureCollection = (RealPictureCollection)theEObject;
				T result = caseRealPictureCollection(realPictureCollection);
				if (result == null) result = casePictureCollection(realPictureCollection);
				if (result == null) result = casePathElement(realPictureCollection);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case GalleryPackage.LINKED_PICTURE_COLLECTION: {
				LinkedPictureCollection linkedPictureCollection = (LinkedPictureCollection)theEObject;
				T result = caseLinkedPictureCollection(linkedPictureCollection);
				if (result == null) result = casePictureCollection(linkedPictureCollection);
				if (result == null) result = casePathElement(linkedPictureCollection);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Picture Collection</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Picture Collection</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePictureCollection(PictureCollection object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Picture</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Picture</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePicture(Picture object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Real Picture</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Real Picture</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseRealPicture(RealPicture object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Linked Picture</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Linked Picture</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseLinkedPicture(LinkedPicture object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Path Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Path Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePathElement(PathElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Picture Library</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Picture Library</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePictureLibrary(PictureLibrary object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Metadata</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Metadata</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMetadata(Metadata object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Real Picture Collection</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Real Picture Collection</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseRealPictureCollection(RealPictureCollection object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Linked Picture Collection</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Linked Picture Collection</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseLinkedPictureCollection(LinkedPictureCollection object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	@Override
	public T defaultCase(EObject object) {
		return null;
	}

} //GallerySwitch
