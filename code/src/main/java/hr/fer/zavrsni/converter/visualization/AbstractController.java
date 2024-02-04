package hr.fer.zavrsni.converter.visualization;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * Abpstraktni kontroler scena.
 * 
 * @author Ana Bagić
 *
 */
public abstract class AbstractController {

    @FXML
    protected StackPane rootPane;
    
	/**
	 * Postavlja ovu scenu kao trenutnu.
	 * 
	 * @param previousSceneRootPane
	 */
	public void setPreviousSceneRoot(Pane previousSceneRootPane) {
		previousSceneRootPane.getChildren().add(rootPane);
	}
}
