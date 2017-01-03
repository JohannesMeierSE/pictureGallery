package picturegallery.action;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import picturegallery.MainApp;
import picturegallery.state.PictureSwitchingState;
import picturegallery.state.State;

public class HideInformationAction extends Action {

	@Override
	public void run(State currentState) {
		Label label = MainApp.get().getLabelKeys();
		hideShow(label);

		if (currentState instanceof PictureSwitchingState) {
			VBox box = ((PictureSwitchingState) currentState).getLabels();
			hideShow(box);
		}
	}

	private void hideShow(Node node) {
		node.setVisible( ! node.isVisible());
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.H;
	}

	@Override
	public String getDescription() {
		return "hide/show these information";
	}
}
