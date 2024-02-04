package hr.fer.zavrsni.converter.visualization;

import java.io.IOException;

import hr.fer.zavrsni.converter.converting.Converter;
import hr.fer.zavrsni.converter.converting.PitchConverter;
import hr.fer.zavrsni.converter.keyboard.Keyboard;
import hr.fer.zavrsni.converter.keyboard.KeyboardFactory;
import hr.fer.zavrsni.converter.model.note.NoteType;
import hr.fer.zavrsni.converter.model.song.TimeSignature;
import hr.fer.zavrsni.converter.util.Constants;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

/**
 * Kontroler za scenu definiranja parametara pjesme.
 * 
 * @author Ana Bagić
 *
 */
public class ParametersController extends AbstractController {

	@FXML
	private ComboBox<Keyboard> keyboardPick;
	@FXML
	private TextField tempoPick;
	@FXML
	private ComboBox<TimeSignature> timeSignaturePick;
	@FXML
	private Label errorMsg;
	
	public void init() {
		Callback<ListView<Keyboard>, ListCell<Keyboard>> factoryKeyboard = lv -> new ListCell<Keyboard>() {
		    @Override
		    protected void updateItem(Keyboard item, boolean empty) {
		        super.updateItem(item, empty);
		        setText(empty ? "" : item.getRange());
		    }
		};
		
		keyboardPick.setCellFactory(factoryKeyboard);
		keyboardPick.setButtonCell(factoryKeyboard.call(null));
		
		keyboardPick.getItems().addAll(KeyboardFactory.getKeyboards());
		keyboardPick.getSelectionModel().selectFirst();
		
		Callback<ListView<TimeSignature>, ListCell<TimeSignature>> factorySignature = lv -> new ListCell<TimeSignature>() {
		    @Override
		    protected void updateItem(TimeSignature ts, boolean empty) {
		        super.updateItem(ts, empty);
		        setText(empty ? "" : ts.toString());
		    }
		};
		
		timeSignaturePick.setCellFactory(factorySignature);
		timeSignaturePick.setButtonCell(factorySignature.call(null));
		timeSignaturePick.getSelectionModel().selectFirst();
		
		timeSignaturePick.getItems().addAll(new TimeSignature(2, NoteType.HALF),
				new TimeSignature(2, NoteType.QUARTER),
				new TimeSignature(3, NoteType.QUARTER),
				new TimeSignature(4, NoteType.QUARTER),
				new TimeSignature(6, NoteType.EIGHTH));
	}
	
	@FXML
	private void generateClicked(MouseEvent event) throws IOException {
		errorMsg.setText("");
		
		String tempoString = tempoPick.getText();
		int tempo;
		try {
			tempo = Integer.parseInt(tempoString);
			if(tempo < 5 || tempo > 500) {
				errorMsg.setText("Tempo treba biti u intervalu 5-500");
				return;
			}
		} catch(NumberFormatException e) {
			errorMsg.setText("Zadani tempo nije cijeli broj.");
			return;
		}
		
		PitchConverter.getInstance().setKeyboard(keyboardPick.getValue());
		Converter.getInstance().getSong().setTimeSignature(timeSignaturePick.getValue());
		Converter.getInstance().getSong().setBeatsPerMinute(tempo);
		
		FXMLLoader loader = new FXMLLoader(
				getClass().getResource(Constants.PATH_TO_VISUALIZATION + "FindNotesScene.fxml"));
		loader.load();
		FindNotesController controller = loader.getController();
		controller.setPreviousSceneRoot(rootPane);
		controller.init();
	}
}
