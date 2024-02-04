package hr.fer.zavrsni.converter.visualization;

import java.io.File;
import java.io.IOException;

import hr.fer.zavrsni.converter.SheetMusicConverter;
import hr.fer.zavrsni.converter.converting.Converter;
import hr.fer.zavrsni.converter.model.song.SongMetadata;
import hr.fer.zavrsni.converter.util.Constants;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

/**
 * Kontroler za scenu odabira pjesme.
 * 
 * @author Ana Bagić
 *
 */
public class PickSongSceneController extends AbstractController {

	/** Putanja na disku do videozapisa. */
	private String pathToVideo;
	@FXML
	private TextField songNameField;
	@FXML
	private TextField artistNameField;
	@FXML
	private Label errorMsg;
	@FXML
	private Button generateButton;

	@FXML
	private void chooseFileClicked(MouseEvent event) throws IOException {
		FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showOpenDialog(SheetMusicConverter.getStage());

		if(file == null || !file.isFile()) {
			errorMsg.setText("Putanja " + file.getName() + " nije ispravan dokument.");
			generateButton.setDisable(true);
			return;
		}

		/*
		Media media = new Media(file.getPath());
		if(media.getDuration() == Duration.UNKNOWN || media.getHeight() == 0) {
			errorMsg.setText("Path " + file.getName() + " cannot be read as a video file.");
			generateButton.setDisable(true);
			return;
		}
		*/

		pathToVideo = file.getPath();
		generateButton.setDisable(false);
	}

	@FXML
	private void generateClicked(MouseEvent event) throws IOException {
		String songName = songNameField.getText();
		String artistName = artistNameField.getText();
		SongMetadata metadata = new SongMetadata(songName.length() == 0 ? Constants.DEFAULT_SONG_NAME : songName,
				artistName.length() == 0 ? Constants.DEFAULT_ARTIST_NAME : artistName);

		Converter c = Converter.getInstance();
		c.setPathToVideo(pathToVideo);
		c.setSongMetadata(metadata);

		FXMLLoader loader = new FXMLLoader(
				getClass().getResource(Constants.PATH_TO_VISUALIZATION + "ParametersScene.fxml"));
		loader.load();
		ParametersController controller = loader.getController();
		controller.setPreviousSceneRoot(rootPane);
		controller.init();
	}
}
