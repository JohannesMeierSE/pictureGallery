# pictureGallery
An individual picture viewer written in Java.

## Supported base Features
* analyze file system: searches for images, no persistence required
* show picture(s)
* move current picture into another folder
* link current picture for another folder
* commands by keyboard only
* full screen mode
* real and linked images
* real and linked collections/folders
* temporary collection
* search for duplicated real pictures of the current collection in the (recursive) sub-collections and replace them by linked pictures
* image caching and pre-loading

## Used Technology
* Java 8
* JavaFX 8 as GUI framework
* EMF for data representation
  * EMF files: `picturegallery/src/main/resources/model`
  * image of the ECore model: `picturegallery/src/main/resources/model/classdiagram.jpg`
  * generated sourcecode: `picturegallery/src-gen/main/java`
* jpHash for image comparison: https://github.com/pragone/jphash
* Maven as build tool

## How to start the pictureGallery
1. Get the sourcecode: `git clone https://github.com/JohannesMeierSE/pictureGallery.git`
2. `cd pictureGallery/picturegallery`
3. Use Maven to
  * directly start the picture viewer with `mvn jfx:run`, or to
  * create an executable JAR with the steps
    1. `mvn jfx:jar`
    2. `java -jar target/jfx/app/picturegallery-jfx.jar`, or to
  * create a native bundle with `mvn jfx:native`
