package picturegallery.action;

import gallery.LinkedPicture;
import gallery.Picture;
import gallery.RealPicture;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.input.KeyCode;
import picturegallery.Logic;
import picturegallery.state.SingleCollectionState;
import picturegallery.state.State;

public class JumpRelatedPictureAction extends Action {

	@Override
	public void run(State currentState) {
		if (!(currentState instanceof SingleCollectionState)) {
			throw new IllegalStateException();
		}
		SingleCollectionState state = (SingleCollectionState) currentState;

		Picture currentPicture = state.getCurrentPicture();
		if (currentPicture == null) {
			return;
		}
		if (currentPicture instanceof LinkedPicture) {
			// jump to the real picture
			jumpLogic(state, currentPicture, ((LinkedPicture) currentPicture).getRealPicture());
		} else {
			RealPicture realPicture = (RealPicture) currentPicture;
			if (realPicture.getLinkedBy().isEmpty()) {
				return;
			}
			LinkedPicture jumpTarget = realPicture.getLinkedBy().get(0);
			if (realPicture.getLinkedBy().size() > 1) {
				List<String> options = new ArrayList<>();
				for (int i = 0; i < realPicture.getLinkedBy().size(); i++) {
					options.add(i, realPicture.getLinkedBy().get(i).getRelativePathWithoutBase());
				}

				int selectedOption = Logic.askForChoice(options, true, "Select the jump target", "There are several links to this picture.", "Select the target to jump to:");
				if (selectedOption < 0) {
					return;
				}
				jumpTarget = realPicture.getLinkedBy().get(selectedOption);
			}
			jumpLogic(state, realPicture, jumpTarget);
		}
	}

	private void jumpLogic(SingleCollectionState state, Picture fromPicture, Picture toPicture) {
		if (toPicture.getCollection() == fromPicture.getCollection()) {
			// linked and real picture are in the same collection
			state.gotoPicture(toPicture, true);
		} else {
			// linked and real picture are in the different collections
			state.setCurrentCollection(toPicture.getCollection(), toPicture);
		}
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.J;
	}

	@Override
	public String getDescription() {
		return "Jump to a related (linked by or real of) picture";
	}
}