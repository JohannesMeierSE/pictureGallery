package picturegallery;

import gallery.GalleryFactory;
import gallery.PictureCollection;
import gallery.RealPicture;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.apache.commons.io.FileUtils;

public class Logic {
	public static void loadDirectory(PictureCollection currentCollection, boolean recursive) {
		String baseDir = currentCollection.getFullPath();
        try {
	        // https://stackoverflow.com/questions/1844688/read-all-files-in-a-folder/23814217#23814217
			Files.walkFileTree(Paths.get(baseDir), new SimpleFileVisitor<Path>() {
			    @Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
			    	// ignore sub-folders, but accept the initial base path!
			    	String name = dir.toString();
					if (name.equals(baseDir)) {
			    		return FileVisitResult.CONTINUE;
			    	}
			    	if (recursive) {
			    		PictureCollection sub = GalleryFactory.eINSTANCE.createPictureCollection();
			    		sub.setSuperCollection(currentCollection);
			    		currentCollection.getSubCollections().add(sub);
			    		sub.setName(name.substring(name.lastIndexOf(File.separator) + 1));
			    	}
					return FileVisitResult.SKIP_SUBTREE;
				}

				@Override
			    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					String name = file.toString();
					String nameLower = name.toLowerCase();
			        System.out.println("file: " + name);
			        if (nameLower.endsWith(".png") || nameLower.endsWith(".jpg")) {
			        	if (FileUtils.isSymlink(new File(name))) {
			        		// TODO
			        	} else {
			        		RealPicture pic = GalleryFactory.eINSTANCE.createRealPicture();
			        		pic.setCollection(currentCollection);
			        		currentCollection.getPictures().add(pic);
			        		pic.setFileExtension(name.substring(name.lastIndexOf(".") + 1));
			        		pic.setName(name.substring(name.lastIndexOf(File.separator) + 1, name.lastIndexOf(".")));
			        	}
			        }
			        return FileVisitResult.CONTINUE;
			    }
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
        if (recursive) {
        	for (PictureCollection newSubCollection : currentCollection.getSubCollections()) {
        		loadDirectory(newSubCollection, recursive);
        	}
        }
	}
}
