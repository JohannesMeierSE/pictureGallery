package picturegallery.action;

import gallery.Picture;

import java.io.IOException;

import javafx.scene.input.KeyCode;

import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import picturegallery.Logic;
import picturegallery.state.PictureSwitchingState;
import picturegallery.state.State;

public class PrintMetadataAction extends Action {

	@Override
	public void run(State currentState) {
		if (!(currentState instanceof PictureSwitchingState)) {
			throw new IllegalStateException();
		}
		PictureSwitchingState state = (PictureSwitchingState) currentState;

		Picture currentPicture = state.getCurrentPicture();
		if (currentPicture == null) {
			return;
		}

		// print the raw metadata into the console
		try {
			Logic.extractMetadata(Logic.getRealPicture(currentPicture), false, true);
		} catch (IOException | SAXException | TikaException e) {
			e.printStackTrace();
		}
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.M;
	}

	@Override
	public String getDescription() {
		return "Prints all the raw metadata of the current picture into the console.";
	}
}