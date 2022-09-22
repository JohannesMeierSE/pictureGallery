package picturegallery.persistency;

import java.io.File;

public class Settings {
	private final static String basePath = "/home/johannes/Documents/photo/2000";
//	private final static String basePath = "/home/johannes/Documents/photo/pictureGallery/picturegallery/src/main/resources/images";

	public static String getBasePath() {
		if (new File(basePath).exists()) {
			return basePath;
		} else {
			return "./";
		}
	}
}
