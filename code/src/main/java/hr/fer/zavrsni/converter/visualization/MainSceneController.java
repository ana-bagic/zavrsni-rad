package hr.fer.zavrsni.converter.visualization;

import java.io.IOException;

import hr.fer.zavrsni.converter.util.Constants;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;

/**
 * Kontroler početne scene aplikacije.
 * 
 * @author Ana Bagić
 *
 */
public class MainSceneController extends AbstractController {

	@FXML
	private void startButtonClicked(MouseEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(
				getClass().getResource(Constants.PATH_TO_VISUALIZATION + "PickSongScene.fxml"));
		loader.load();
		PickSongSceneController controller = loader.getController();
		controller.setPreviousSceneRoot(rootPane);
	}
}
