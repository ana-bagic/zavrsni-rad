package hr.fer.zavrsni.converter;

import org.opencv.core.Core;

import hr.fer.zavrsni.converter.util.Constants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Ulazni razred za aplikaciju generiranja notnog zapisa iz videozapisa.
 * 
 * @author Ana Bagić
 *
 */
public class SheetMusicConverter extends Application {
	
	/** Početna scena aplikacije. */
	private static Stage primaryStage;
	
	/**
	 * Vraća početnu scenu aplikacije.
	 * 
	 * @return početna scena aplikacije
	 */
	public static Stage getStage() {
		return primaryStage;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Constants.PATH_TO_VISUALIZATION + "MainScene.fxml"));
    	Parent root = fxmlLoader.load();
    	Scene scene = createScaledScene(root, primaryStage);
        
		primaryStage.setTitle("Generator notnog zapisa");
		primaryStage.setScene(scene);
	    primaryStage.show();
	}
	
	/**
	 * Stvara novu scenu i postavlja ju kao prvu scenu aplikacije.
	 * 
	 * @param root početni čvor stabla scena
	 * @param stage početna scena
	 * @return novo stvorenu scenu
	 */
	public Scene createScaledScene(Node root, Stage stage) {
        double origW = Constants.WIDTH;
        double origH = Constants.HEIGHT;

        StackPane rootPane = new StackPane();
        rootPane.getChildren().add(root);

        Scene scene = new Scene(rootPane, origW, origH);
        
        root.scaleXProperty().bind(scene.widthProperty().divide(origW));
        root.scaleYProperty().bind(scene.heightProperty().divide(origH));
        
		stage.setResizable(false);        
        return scene;
	}
	
    /**
     * Metoda pokreće aplikaciju.
     */
    public static void main(String[] args) {
    	System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    	
		launch(args);
	}

}
