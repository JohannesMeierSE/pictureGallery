package picturegallery.action;

import javafx.scene.input.KeyCode;
import picturegallery.Logic;
import picturegallery.state.PictureSwitchingState;
import picturegallery.state.State;

public class JumpParticularPictureAction extends Action {

	@Override
	public void run(State currentState) {
		if (!(currentState instanceof PictureSwitchingState)) {
			throw new IllegalStateException();
		}
		PictureSwitchingState state = (PictureSwitchingState) currentState;

		// ask for the index of the wanted picture, index starts at 1 as shown in the GUI!
		String newIndexString = Logic.askForString("Jump to Picture",
				"Specify the picture to jump to by its index in the current collection!",
				"Index of Picture: ", false, "");
		if (newIndexString == null || newIndexString.isEmpty()) {
			return;
		}
		// convert to integer
		int newIndex = 0;
		try {
			newIndex = Integer.parseInt(newIndexString);
		} catch (Throwable e) {
			return;
		}
		if (newIndex <= 0 || newIndex > state.getSize()) {
			return;
		}

		// change the current picture
		state.jumpedBefore();
		state.changeIndex(newIndex - 1, true);
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.J;
	}
	@Override
	public boolean requiresCtrl() {
		return true;
	}

	@Override
	public String getDescription() {
		return "jump to a particular picture by specifying its index";
	}

	@Override
	public String getKeyDescription() {
		return "J";
	}
}
