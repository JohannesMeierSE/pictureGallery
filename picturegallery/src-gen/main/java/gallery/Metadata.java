/**
 */
package gallery;

import java.util.Date;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Metadata</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gallery.Metadata#getPicture <em>Picture</em>}</li>
 *   <li>{@link gallery.Metadata#getSize <em>Size</em>}</li>
 *   <li>{@link gallery.Metadata#isLandscape <em>Landscape</em>}</li>
 *   <li>{@link gallery.Metadata#getCreated <em>Created</em>}</li>
 *   <li>{@link gallery.Metadata#getHeight <em>Height</em>}</li>
 *   <li>{@link gallery.Metadata#getWidth <em>Width</em>}</li>
 *   <li>{@link gallery.Metadata#getCamera <em>Camera</em>}</li>
 * </ul>
 * </p>
 *
 * @see gallery.GalleryPackage#getMetadata()
 * @model
 * @generated
 */
public interface Metadata extends EObject {
	/**
	 * Returns the value of the '<em><b>Picture</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link gallery.RealPicture#getMetadata <em>Metadata</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Picture</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Picture</em>' container reference.
	 * @see #setPicture(RealPicture)
	 * @see gallery.GalleryPackage#getMetadata_Picture()
	 * @see gallery.RealPicture#getMetadata
	 * @model opposite="metadata" required="true" transient="false"
	 * @generated
	 */
	RealPicture getPicture();

	/**
	 * Sets the value of the '{@link gallery.Metadata#getPicture <em>Picture</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Picture</em>' container reference.
	 * @see #getPicture()
	 * @generated
	 */
	void setPicture(RealPicture value);

	/**
	 * Returns the value of the '<em><b>Size</b></em>' attribute.
	 * The default value is <code>"-1"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Size</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Size</em>' attribute.
	 * @see #setSize(int)
	 * @see gallery.GalleryPackage#getMetadata_Size()
	 * @model default="-1"
	 * @generated
	 */
	int getSize();

	/**
	 * Sets the value of the '{@link gallery.Metadata#getSize <em>Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Size</em>' attribute.
	 * @see #getSize()
	 * @generated
	 */
	void setSize(int value);

	/**
	 * Returns the value of the '<em><b>Landscape</b></em>' attribute.
	 * The default value is <code>"true"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Landscape</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Landscape</em>' attribute.
	 * @see #setLandscape(boolean)
	 * @see gallery.GalleryPackage#getMetadata_Landscape()
	 * @model default="true"
	 * @generated
	 */
	boolean isLandscape();

	/**
	 * Sets the value of the '{@link gallery.Metadata#isLandscape <em>Landscape</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Landscape</em>' attribute.
	 * @see #isLandscape()
	 * @generated
	 */
	void setLandscape(boolean value);

	/**
	 * Returns the value of the '<em><b>Created</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Created</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Created</em>' attribute.
	 * @see #setCreated(Date)
	 * @see gallery.GalleryPackage#getMetadata_Created()
	 * @model
	 * @generated
	 */
	Date getCreated();

	/**
	 * Sets the value of the '{@link gallery.Metadata#getCreated <em>Created</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Created</em>' attribute.
	 * @see #getCreated()
	 * @generated
	 */
	void setCreated(Date value);

	/**
	 * Returns the value of the '<em><b>Height</b></em>' attribute.
	 * The default value is <code>"-1"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Height</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Height</em>' attribute.
	 * @see #setHeight(int)
	 * @see gallery.GalleryPackage#getMetadata_Height()
	 * @model default="-1"
	 * @generated
	 */
	int getHeight();

	/**
	 * Sets the value of the '{@link gallery.Metadata#getHeight <em>Height</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Height</em>' attribute.
	 * @see #getHeight()
	 * @generated
	 */
	void setHeight(int value);

	/**
	 * Returns the value of the '<em><b>Width</b></em>' attribute.
	 * The default value is <code>"-1"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Width</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Width</em>' attribute.
	 * @see #setWidth(int)
	 * @see gallery.GalleryPackage#getMetadata_Width()
	 * @model default="-1"
	 * @generated
	 */
	int getWidth();

	/**
	 * Sets the value of the '{@link gallery.Metadata#getWidth <em>Width</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Width</em>' attribute.
	 * @see #getWidth()
	 * @generated
	 */
	void setWidth(int value);

	/**
	 * Returns the value of the '<em><b>Camera</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Camera</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Camera</em>' attribute.
	 * @see #setCamera(String)
	 * @see gallery.GalleryPackage#getMetadata_Camera()
	 * @model
	 * @generated
	 */
	String getCamera();

	/**
	 * Sets the value of the '{@link gallery.Metadata#getCamera <em>Camera</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Camera</em>' attribute.
	 * @see #getCamera()
	 * @generated
	 */
	void setCamera(String value);

} // Metadata
