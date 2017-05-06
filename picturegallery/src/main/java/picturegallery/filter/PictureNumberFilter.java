package picturegallery.filter;

import gallery.PictureCollection;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;

public class PictureNumberFilter extends CollectionFilter {
	public static class IntegerFilter implements
			UnaryOperator<TextFormatter.Change> {
		// https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
		private final static Pattern DIGIT_PATTERN = Pattern.compile("-?\\d*");

		@Override
		public Change apply(TextFormatter.Change change) {
			return DIGIT_PATTERN.matcher(change.getText()).matches() ? change
					: null;
		}
	}

	public final SimpleObjectProperty<Integer> min;
	public final SimpleObjectProperty<Integer> max;

	public PictureNumberFilter(CompositeCollectionFilter parentFilter) {
		super(parentFilter);

		min = new SimpleObjectProperty<Integer>(0);
		min.addListener(listenerForNotification);
		max = new SimpleObjectProperty<Integer>(-1);
		max.addListener(listenerForNotification);

		// init the UI
		Label minLabel = new Label("min:");
		Label maxLabel = new Label("max:");

		// https://stackoverflow.com/questions/40472668/numeric-textfield-for-integers-in-javafx-8-with-textformatter-and-or-unaryoperat
		// modified version of standard converter that evaluates an empty string as zero instead of null
		StringConverter<Integer> converter = new IntegerStringConverter() {
			@Override
			public Integer fromString(String s) {
				if (s == null || s.isEmpty()) {
					return 0;
				} else {
					return super.fromString(s);
				}
			}
		};
		// https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
		TextField minField = new TextField();
		TextFormatter<Integer> minFormatter = new TextFormatter<>(converter, 0, new IntegerFilter());
		minFormatter.valueProperty().bindBidirectional(min);
		minField.setTextFormatter(minFormatter);

		TextField maxField = new TextField();
		TextFormatter<Integer> maxFormatter = new TextFormatter<>(converter, 0, new IntegerFilter());
		maxFormatter.valueProperty().bindBidirectional(max);
		maxField.setTextFormatter(maxFormatter);

		hbox.getChildren().addAll(minLabel, minField, maxLabel, maxField);
	}

	@Override
	protected boolean isUsableLogic(PictureCollection collection) {
		int size = collection.getPictures().size();

		if (size < min.get()) {
			return false;
		}

		int maximum = max.get();
		if (maximum < 0) {
			// allow an unlimited number of pictures
			return true;
		} else {
			return size <= maximum;
		}
	}

	@Override
	public Node getUI() {
		return hbox;
	}

	@Override
	public void close() {
		// TODO: unbind
		super.close();
	}
}
