package picturegallery;

import gallery.GalleryFactory;
import gallery.Picture;
import gallery.PictureCollection;
import gallery.PictureLibrary;
import gallery.RealPicture;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Comparator;

import org.apache.commons.io.FileUtils;
import org.eclipse.emf.common.util.ECollections;

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
        sortPicturesInCollection(currentCollection);
        sortSubCollections(currentCollection, false);
        if (recursive) {
        	for (PictureCollection newSubCollection : currentCollection.getSubCollections()) {
        		loadDirectory(newSubCollection, recursive);
        	}
        }
	}

	public static PictureCollection createEmptyLibrary(final String baseDir) {
		String parentDir = baseDir.substring(0, baseDir.lastIndexOf(File.separator));
        String dirName = baseDir.substring(baseDir.lastIndexOf(File.separator) + 1);
        System.out.println(baseDir + " == " + parentDir + " + " + dirName);

        PictureLibrary lib = GalleryFactory.eINSTANCE.createPictureLibrary();
        lib.setBasePath(parentDir);
        lib.setName("TestLibrary");

        PictureCollection base = GalleryFactory.eINSTANCE.createPictureCollection();
        base.setLibrary(lib);
        lib.setBaseCollection(base);
        base.setName(dirName);
		return base;
	}

	public static Picture findFirstPicture(PictureCollection col) {
		if (!col.getPictures().isEmpty()) {
			return col.getPictures().get(0);
		}
		// recursive depth-first-search
		for (PictureCollection sub : col.getSubCollections()) {
			Picture current = findFirstPicture(sub);
			if (current != null) {
				return current;
			}
		}
		return null;
	}

	public static PictureCollection findFirstNonEmptyCollection(PictureCollection col) {
		if (!col.getPictures().isEmpty()) {
			return col;
		}
		for (PictureCollection sub : col.getSubCollections()) {
			PictureCollection res = findFirstNonEmptyCollection(sub);
			if (res != null) {
				return res;
			}
		}
		return null;
	}

	/**
	 * Changes the order of the pictures in the collection (ascending names) => works in-place!
	 * @param col
	 */
	public static void sortPicturesInCollection(PictureCollection col) {
		// http://download.eclipse.org/modeling/emf/emf/javadoc/2.11/org/eclipse/emf/common/util/ECollections.html#sort(org.eclipse.emf.common.util.EList)
		ECollections.sort(col.getPictures(), new Comparator<Picture>() {
			@Override
			public int compare(Picture o1, Picture o2) {
				if (o1 == o2) {
					return 0;
				}
				if (o1.getName() == o2.getName()) {
					throw new IllegalStateException();
				}
				return o1.getName().compareTo(o2.getName());
			}
		});
	}

	public static void sortSubCollections(PictureCollection base, boolean recursive) {
		ECollections.sort(base.getSubCollections(), new Comparator<PictureCollection>() {
			@Override
			public int compare(PictureCollection o1, PictureCollection o2) {
				if (o1 == o2) {
					return 0;
				}
				if (o1.getName() == o2.getName()) {
					throw new IllegalStateException();
				}
				return o1.getName().compareTo(o2.getName());
			}
		});
	}

	public static String formatBytes(int bytes) {
		String text = bytes + " Bytes";

		String unit = "";
		int before = bytes;
		int after = -1;
		if (bytes >= 1024) {
			int kb = bytes / 1024;
			if (kb >= 1014) {
				int mb = kb / 1024;
				if (mb >= 1024) {
					int gb = mb / 1024;
					unit = "GB";
					before = gb;
					after = Math.round((mb % 1014) / 1024f * 10);
				} else {
					unit = "MB";
					before = mb;
					after = Math.round((kb % 1014) / 1024f * 10);
				}
			} else {
				unit = "KB";
				before = kb;
				after = Math.round((bytes % 1014) / 1024f * 10);
			}
		}
		if (after >= 0) {
			text = text + " ~ " + before + "." + after + " " + unit;
		}
		return text;
	}
}
