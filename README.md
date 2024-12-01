# pictureGallery
An individual picture viewer written in Java.

## Main Ideas for this Tool
* show and manage large collections of images with commands by pressing keys on the keyboard (most useful for desktop)
* in particular, move rejected images into another folder instead of deleting them
* pre-load images for showing them faster (important for huge images on slow hard disks)
* no preconditions for the current structure of folders and images to work on

## Supported Features
* analyze file system: searches for images, no persistence required
* show picture(s) in configurable orders
* move current picture into another folder
* link current picture for another folder
* commands by keyboard only
* full screen mode
* real and linked pictures/images (use links to real images to mark them for easy access)
* real and linked collections/folders (use links to real collections to mark them for easy access)
* temporary collection which are recursively nestable (for comparing images which are not "neighbored")
* search for duplicated real pictures
	* of the current collection in the (recursive) sub-collections, or
	* within the current collection
	* to replace them by linked pictures, or
	* to delete them
* image caching and pre-loading with fixed size memory consumption
* show some meta data of images
* fix names of multiple images at once

## How to run the pictureGallery
1. Get the sourcecode: `git clone https://github.com/JohannesMeierSE/pictureGallery.git`
2. `cd pictureGallery/picturegallery`
3. Use Maven to
  * directly start the picture viewer with `mvn javafx:run` (recommended option), or to
  * create an executable JAR with the steps (TODO: check this)
    1. `mvn jfx:jar`
    2. `java -jar target/jfx/app/picturegallery-jfx.jar`, or to
  * create a native bundle with `mvn jfx:native`

## How to debug the pictureGallery in Eclipse

* Follow this [JavaFX tutorial](https://openjfx.io/openjfx-docs/#IDE-Eclipse)
* Create a user library called "JavaFX" which points to your local installation of the JavaFX SDK (-> Preferences -> Java -> Build Path -> User Libraries -> New)
* This user library should already be added to the build path
* Run/debug the project as usual

## Used Technology
* Java 11
* JavaFX as GUI framework
* some more JavaFX widgets created by the ControlsFX project: http://fxexperience.com/controlsfx/
* EMF for data representation
  * EMF files: `picturegallery/src/main/resources/model`
  * image of the ECore model: `picturegallery/src/main/resources/model/classdiagram.jpg`
  * generated sourcecode (started by hand, not by Maven): `picturegallery/src-gen/main/java`
* Apache Tika for reading meta data of image files
* jpHash for image comparison: https://github.com/pragone/jphash
* Maven as build tool

## Technical Design Decisions
* JavaFX was chosen in order to become more familiar with it, since it is the recommended technology to create Java GUIs, to try to create a JavaFX GUI without FXML, and to investigate alternative widgets (ControlsFX).
* EMF was chosen as an experiment to test, if it is possible to use EMF as stand-alone application managed by Maven without Eclipse GUI.
* Maven was chosen to be independent from the used IDE.
