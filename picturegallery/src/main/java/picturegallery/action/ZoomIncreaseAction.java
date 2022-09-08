package picturegallery.action;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import picturegallery.state.SinglePictureState;
import picturegallery.state.State;

public class ZoomIncreaseAction extends Action {

	@Override
	public void run(State currentState) {
		if (currentState instanceof SinglePictureState == false) {
			throw new IllegalStateException();
		}
		SinglePictureState state = (SinglePictureState) currentState;

		state.zoom.set(state.zoom.get() * 1.15);
	}

	@Override
	public KeyCode getKey() {
		return null;
	}

	@Override
	public boolean acceptKeyEvent(KeyEvent event) {
		/* on a MacBook with german keyboard layout, "+" and "-" were not detected, since the keys produced CLOSE_BRACKET or SLASH as key codes ...
		 * - the real reason for this strange behaviour is unknown
		 * - by the fix with checking the text representation of the events works ...
		 * https://docs.oracle.com/javase/8/javafx/api/javafx/scene/input/KeyEvent.html#isControlDown--
		 * https://docs.oracle.com/javafx/2/api/javafx/scene/input/KeyCombination.html#SHORTCUT_ANY
		 * https://stackoverflow.com/questions/32810168/how-to-detect-plus-key-on-different-language-keyboards-in-javafx
		 */
//		KeyCodeCombination combinationPlus = new KeyCodeCombination(KeyCode.PLUS);
//		KeyCodeCombination combinationAdd = new KeyCodeCombination(KeyCode.ADD);
//		return combinationPlus.match(event) || combinationAdd.match(event);

		return event.getCode().equals(KeyCode.PLUS) || event.getCode().equals(KeyCode.ADD) || "+".equals(event.getText());
	}

	@Override
	public boolean allowKeyPressed() {
		return true;
	}

	@Override
	public String getKeyDescription() {
		return "+";
	}

	@Override
	public String getDescription() {
		return "zoom in";
	}
}
