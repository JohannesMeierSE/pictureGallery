package picturegallery.action;

import gallery.LinkedPictureCollection;
import gallery.PictureCollection;
import gallery.RealPictureCollection;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.input.KeyCode;
import picturegallery.Logic;
import picturegallery.state.CollectionState;
import picturegallery.state.State;

public class JumpRelatedCollectionAction extends Action {

	@Override
	public void run(State currentState) {
		if (!(currentState instanceof CollectionState)) {
			throw new IllegalStateException();
		}
		CollectionState state = (CollectionState) currentState;

		PictureCollection currentCollection = state.getSelection();
		if (currentCollection == null) {
			return;
		}
		if (currentCollection instanceof LinkedPictureCollection) {
			// jump to the real collection
			jumpLogic(state, ((LinkedPictureCollection) currentCollection).getRealCollection());
		} else {
			RealPictureCollection realCollection = (RealPictureCollection) currentCollection;
			if (realCollection.getLinkedBy().isEmpty()) {
				// no links => do nothing
				return;
			}

			LinkedPictureCollection jumpTarget = realCollection.getLinkedBy().get(0);
			if (realCollection.getLinkedBy().size() > 1) {
				// select the jump target (if there are more than one link)
				List<String> options = new ArrayList<>();
				for (int i = 0; i < realCollection.getLinkedBy().size(); i++) {
					options.add(i, realCollection.getLinkedBy().get(i).getRelativePathWithoutBase());
				}

				int selectedOption = Logic.askForChoice(options, true, "Select the jump target", "There are several links to this collection.", "Select the target to jump to:");
				if (selectedOption < 0) {
					return;
				}
				jumpTarget = realCollection.getLinkedBy().get(selectedOption);
			}

			// jump to the (selected) target
			jumpLogic(state, jumpTarget);
		}
	}

	private void jumpLogic(CollectionState state, PictureCollection toCollection) {
		state.jumpToCollection(toCollection);
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.J;
	}

	@Override
	public String getDescription() {
		return "Jump to a related (linked by or real of) collection";
	}
}
