package picturegallery.ui;

/*-
 * BEGIN-LICENSE
 * picturegallery
 * %%
 * Copyright (C) 2016 - 2023 Johannes Meier
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * END-LICENSE
 */

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import gallery.LinkedPictureCollection;
import gallery.PictureCollection;
import gallery.RealPictureCollection;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.util.Callback;
import picturegallery.MainApp;
import picturegallery.persistency.Settings;
import picturegallery.state.SinglePictureSwitchingState;
import picturegallery.state.State;

public class JavafxHelper {

	public static void showNotification(String title, String header, String content, boolean waitUntilClosed) {
		Alert dialog = new Alert(AlertType.INFORMATION, content);
		// Dialog dialog = new Dialog<>();
		dialog.setTitle(title);
		dialog.setHeaderText(header);
		dialog.setContentText(content);
		if (waitUntilClosed) {
			dialog.showAndWait();
		} else {
			dialog.show();
		}
	}

	public static String askForString(String title, String header, String content,
			boolean nullAndEmptyAreForbidden, String defaultValue) {
		while (true) {
			final TextInputDialog dialog;
			if (defaultValue == null || defaultValue.isEmpty()) {
				dialog = new TextInputDialog();
			} else {
				dialog = new TextInputDialog(defaultValue);
			}
			dialog.setTitle(title);
			dialog.setHeaderText(header);
			dialog.setContentText(content);
			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()) {
				String res = result.get();
				if (nullAndEmptyAreForbidden && (res == null || res.isEmpty())) {
					// next iteration
				} else {
					return result.get();
				}
			} else {
				if (nullAndEmptyAreForbidden) {
					// next iteration
				} else {
					return null;
				}
			}
		}
	}

	public static boolean askForConfirmation(String title, String header, String content) {
		return askForYesNo(title, header, content, null);
	}
	public static boolean askForYesNo(String title, String header, String content, RememberDecisionInformation<Boolean> rememberInfos) {
		// http://code.makery.ch/blog/javafx-dialogs-official/
//		Alert alert = new Alert(AlertType.CONFIRMATION);
		Dialog<Boolean> dialog = new Dialog<>();
		dialog.setTitle(title);
		dialog.setHeaderText(header);
//		alert.setContentText(content);

		// the label with the decision/question
		VBox box = new VBox(20.0, new Label(content));

		// the checkbox for controlling, whether the current selection should be remembered
		if (rememberInfos != null) {
			Node check = rememberInfos.getElementForCurrentDialog();
			if (check != null) {
				box.getChildren().add(check);
			}
		}

		dialog.getDialogPane().setContent(box);

		// select button
		ButtonType yesType = new ButtonType("Yes", ButtonData.YES);
		dialog.getDialogPane().getButtonTypes().add(yesType);
		Button yesButton = (Button) dialog.getDialogPane().lookupButton(yesType);
		yesButton.setDisable(false);

		// cancel button
		ButtonType noType = ButtonType.NO;
		dialog.getDialogPane().getButtonTypes().add(noType);
		Button noButton = (Button) dialog.getDialogPane().lookupButton(noType);
		noButton.setDisable(false);

		dialog.setResultConverter(new Callback<ButtonType, Boolean>() {
			@Override
			public Boolean call(ButtonType arg0) {
				return arg0 == yesType;
			}
		});
		Optional<Boolean> result = dialog.showAndWait();
		if (result.isPresent()) {
			if (rememberInfos != null) {
				rememberInfos.setCurrentDecision(result.get());
			}
			return result.get() == true;
		} else {
			if (rememberInfos != null) {
				rememberInfos.setCurrentDecision(null);
			}
			return false;
		}
//		Optional<ButtonType> result = alert.showAndWait();
//		if (result.get() == ButtonType.OK){
//		    // ... user chose OK
//			return true;
//		} else {
//		    // ... user chose CANCEL or closed the dialog
//			return false;
//		}
	}

	public static String askForDirectory(String title, boolean allowNull) {
		// https://docs.oracle.com/javafx/2/ui_controls/file-chooser.htm
		String baseDir = null;
		while (baseDir == null) {
			DirectoryChooser dialog = new DirectoryChooser();
			dialog.setTitle(title);
			dialog.setInitialDirectory(new File(Settings.getBasePath()));
			File choosenLibrary = dialog.showDialog(MainApp.get().getStage());
	    	if (choosenLibrary != null) {
	    		baseDir = choosenLibrary.getAbsolutePath();
	    	} else if (allowNull) {
	    		break;
	    	} else {
	    		// ask the user again
	    	}
		}
		return baseDir;
	}

	public static int askForChoice(List<String> options, boolean allowNull,
			String title, String header, String content) {
		return JavafxHelper.askForChoice(options, allowNull, title, header, content, -1);
	}

	public static int askForChoice(List<String> options, boolean allowNull,
			String title, String header, String content, int defaultIndex) {
		if (options == null || options.size() < 2) {
			throw new IllegalArgumentException();
		}
		int result = -1;
		while (result < 0) {
			ChoiceDialog<String> dialog = new ChoiceDialog<>(options.get(0), options);
			dialog.setTitle(title);
			dialog.setHeaderText(header);
			dialog.setContentText(content);
			if (0 <= defaultIndex && defaultIndex < options.size()) {
				dialog.setSelectedItem(options.get(defaultIndex));
			}
	
			Optional<String> answer = dialog.showAndWait();
			if (answer.isPresent()){
				result = options.indexOf(answer.get());
			} else if (allowNull) {
				break;
			} else {
				// next iteration
			}
		}
		return result;
	}

	public static PictureCollection selectCollection(
			State currentState,
			boolean allowNull, boolean allowEmptyCollectionForSelection, boolean allowLinkedCollections) {
		return JavafxHelper.selectCollection(currentState,
				allowNull, allowEmptyCollectionForSelection, allowLinkedCollections,
				Collections.emptyList());
	}

	public static PictureCollection selectCollection(
			State currentState,
			boolean allowNull, boolean allowEmptyCollectionForSelection, boolean allowLinkedCollections,
			List<? extends PictureCollection> ignoredCollections) {
		final PictureCollection currentCollection;
		final PictureCollection movetoCollection;
		final PictureCollection linktoCollection;
		if (currentState != null && currentState instanceof SinglePictureSwitchingState) {
			currentCollection = ((SinglePictureSwitchingState) currentState).getCurrentCollection();
			movetoCollection = ((SinglePictureSwitchingState) currentState).getMovetoCollection();
			linktoCollection = ((SinglePictureSwitchingState) currentState).getLinktoCollection();
		} else {
			currentCollection = null;
			movetoCollection = null;
			linktoCollection = null;
		}
		PictureCollection result = null;
		boolean found = false;

		while (found == false) {
			// create the dialog
			// http://code.makery.ch/blog/javafx-dialogs-official/
			Dialog<PictureCollection> dialog = new Dialog<>();
			dialog.setTitle("Select picture collection");
			dialog.setHeaderText("Select one existing picture collection out of the following ones!");

			// select button
			ButtonType selectType = new ButtonType("Select", ButtonData.OK_DONE);
			dialog.getDialogPane().getButtonTypes().add(selectType);
			Button selectButton = (Button) dialog.getDialogPane().lookupButton(selectType);
			selectButton.setDisable(true);

			// cancel button: handle the "null collection" (1)
			Button cButton = null;
			ButtonType cancelType = ButtonType.CANCEL;
			if (allowNull) {
				dialog.getDialogPane().getButtonTypes().add(cancelType);
				cButton = (Button) dialog.getDialogPane().lookupButton(cancelType);
			}
			Button cancelButton = cButton;

			// create the tree view
			TreeItem<PictureCollection> rootItem = new TreeItem<PictureCollection>(MainApp.get().getBaseCollection());
			rootItem.setExpanded(true);
			JavafxHelper.handleTreeItem(rootItem, allowLinkedCollections);
			TreeView<PictureCollection> tree = new TreeView<>(rootItem);
			tree.setCellFactory(new Callback<TreeView<PictureCollection>, TreeCell<PictureCollection>>() {
				@Override
				public TreeCell<PictureCollection> call(TreeView<PictureCollection> param) {
					return new TreeCell<PictureCollection>() {
						@Override
						protected void updateItem(PictureCollection item, boolean empty) {
							super.updateItem(item, empty);
							if (empty) {
								setText(null);
								setGraphic(null);
							} else {
								setText(null);
								final Label label = new Label();
								setGraphic(label);
								String textToShow = item.getName() + " (" + item.getPictures().size() + ")";
								if (item == currentCollection) {
									textToShow = textToShow + " [currently shown]";
								}
								if (item == movetoCollection) {
									textToShow = textToShow + " [currently moving into]";
								}
								if (item == linktoCollection) {
									textToShow = textToShow + " [currently linking into]";
								}
								// show the source of this link
								if (item instanceof LinkedPictureCollection) {
									textToShow = textToShow + " [=> " + ((LinkedPictureCollection) item).getRealCollection().getRelativePath() + "]";
								}
								boolean disabled = item.getPictures().isEmpty() && !allowEmptyCollectionForSelection;
								boolean ignore = disabled || ignoredCollections.contains(item);
								// https://stackoverflow.com/questions/32370394/javafx-combobox-change-value-causes-indexoutofboundsexception
								setDisable(ignore);
								label.setDisable(ignore);
								if (disabled) {
									textToShow = textToShow + " [empty]";
								}
								label.setText(textToShow);
							}
						}
					};
				}
			});
			dialog.getDialogPane().setOnKeyReleased(new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent event) {
					// closes the dialog with "ENTER"
					if (event.getCode() == KeyCode.ENTER && !selectButton.isDisabled()) {
						selectButton.fire();
					}
					// cancel the dialog with "Esc"
					if (event.getCode() == KeyCode.ESCAPE && cancelButton != null) {
						cancelButton.fire();
					}
					// open/close sub-collections in the tree, compare with OpenCloseCollectionsOfSameLevelAction
					if (event.getCode() == KeyCode.Q) {
						TreeItem<PictureCollection> selectedItem = tree.getSelectionModel().getSelectedItem();
						if (selectedItem != null) {
							boolean expanded = selectedItem.isExpanded() == false;
							TreeItem<PictureCollection> parent = selectedItem.getParent();
							if (parent == null) {
								selectedItem.setExpanded(expanded);
							} else {
								PictureCollection collection = selectedItem.getValue();
								for (TreeItem<PictureCollection> child : parent.getChildren()) {
									child.setExpanded(expanded);
								}
								// select/focus the current item (again), since the focus is sometimes wrong afterwards (why?)
								selectCollection(tree, collection);
							}
						}
					}
				}
			});

			// handle the "null collection" (2)
			tree.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<PictureCollection>>() {
				@Override
				public void changed(
						ObservableValue<? extends TreeItem<PictureCollection>> observable,
						TreeItem<PictureCollection> oldValue,
						TreeItem<PictureCollection> newValue) {
					selectButton.setDisable(newValue == null || newValue.getValue() == null ||
							// benötigt, da man auch nicht-wählbare Einträge auswählen kann, diese Abfrage funktioniert aber auch nicht!!
							(newValue.getGraphic() != null && newValue.getGraphic().isDisabled()));
				}
			});

			// finish the dialog
			tree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			tree.getSelectionModel().clearSelection();
			dialog.getDialogPane().setContent(tree);
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					// request focus on the tree view by default
					tree.requestFocus();
				}
			});
			dialog.setResultConverter(new Callback<ButtonType, PictureCollection>() {
				@Override
				public PictureCollection call(ButtonType param) {
					if (param == selectType) {
						return tree.getSelectionModel().getSelectedItem().getValue();
					}
					return null;
				}
			});

			// jump to the currently selected item!
			if (currentCollection != null) {
				selectCollection(tree, currentCollection);
			}

			// run the dialog
			Optional<PictureCollection> dialogResult = dialog.showAndWait();
			if (dialogResult.isPresent()) {
				result = dialogResult.get();
				if (result.getPictures().isEmpty() && !allowEmptyCollectionForSelection) {
					result = null;
				}
			}

			// handle the result
			if (result != null || allowNull) {
				found = true;
			}
		}
		return result;
	}

	private static void selectCollection(TreeView<PictureCollection> tree, PictureCollection currentCollection) {
		TreeItem<PictureCollection> currentSelectedItem = JavafxHelper.searchForEntry(currentCollection, tree.getRoot());
		if (currentSelectedItem != null) {
			// https://stackoverflow.com/questions/17413206/listview-not-showing-selected-item-when-selected-programatically
			tree.getSelectionModel().select(currentSelectedItem);
			int row = tree.getRow(currentSelectedItem);
			tree.getFocusModel().focus(row);
			tree.scrollTo(row);
		}
	}

	public static void handleTreeItem(TreeItem<PictureCollection> item, boolean showLinkedCollections) {
		for (PictureCollection subCol : item.getValue().getSubCollections()) {
			if (subCol instanceof LinkedPictureCollection && !showLinkedCollections) {
				continue;
			}
			TreeItem<PictureCollection> newItem = new TreeItem<PictureCollection>(subCol);
			newItem.setExpanded(subCol instanceof RealPictureCollection); // expand only not-linked collections == expand only real collections
			item.getChildren().add(newItem);
			handleTreeItem(newItem, showLinkedCollections);
		}
	}

	private static <T> TreeItem<T> searchForEntry(T element, TreeItem<T> root) {
		if (root.getValue().equals(element)) {
			return root;
		}
		for (TreeItem<T> sub : root.getChildren()) {
			TreeItem<T> result = searchForEntry(element, sub);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

	public static void runOnUiThread(Runnable run) {
		if (Platform.isFxApplicationThread()) {
			run.run();
		} else {
			Platform.runLater(run);
		}
	}

	public static void runNotOnUiThread(Runnable run) {
		if (Platform.isFxApplicationThread()) {
			Task<?> task;
			if (run instanceof Task<?>) {
				// since Task implements Runnable, reuse the Task directly instead of creating an additional Task and of nesting Tasks ...
				task = (Task<?>) run;
			} else {
				task = new Task<Void>() {
					@Override
					protected Void call() throws Exception {
						run.run();
						return null;
					}
				};
			}
	        new Thread(task).start();
		} else {
			run.run();
		}
	}

}
