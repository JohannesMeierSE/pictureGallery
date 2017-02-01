package picturegallery.state;

import javafx.scene.control.Label;
import javafx.scene.layout.Region;

public class WaitingState extends State {
	private final Label label;

	public WaitingState() {
		super();
		label = new Label("please wait!");
	}

	@Override
	public Region getRootNode() {
		return label;
	}
}
