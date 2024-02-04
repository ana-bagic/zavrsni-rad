package hr.fer.zavrsni.converter.visualization;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import hr.fer.zavrsni.converter.converting.Converter;
import hr.fer.zavrsni.converter.converting.PitchConverter;
import hr.fer.zavrsni.converter.util.Constants;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;

/**
 * Kontroler scene u kojoj se bira područje s notama.
 * 
 * @author Ana Bagić
 *
 */
public class FindNotesController extends AbstractController {
	
	@FXML
	private AnchorPane frameAnchor;
	@FXML
	private ImageView frameImage;
	@FXML
	private Line lineLeft;
	@FXML
	private Line lineRight;
	@FXML
	private Line lineUp;
	@FXML
	private Line lineDown;
	@FXML
	private Slider sliderX0;
	@FXML
	private Slider sliderX1;
	@FXML
	private Slider sliderY0;
	@FXML
	private Slider sliderY1;
	
	public void init() {
		VideoCapture cap = new VideoCapture(Converter.getInstance().getPathToVideo());
		
		Mat frame = new Mat();
		for(int i = 0; i < 150; i++) {
			cap.read(frame);
		}
		
		int width = frame.width()/2;
		int height = frame.height()/2;
		
		frameAnchor.setPrefWidth(width);
		frameAnchor.setPrefHeight(height);
		
		frameImage.fitWidthProperty().bind(frameAnchor.widthProperty()); 
		MatOfByte byteMat = new MatOfByte();
		Imgcodecs.imencode(".bmp", frame, byteMat);
		Image image = new Image(new ByteArrayInputStream(byteMat.toArray()));
		frameImage.setImage(image);
		
		lineLeft.setEndY(height);
		
		lineRight.setStartX(width);
		lineRight.setEndX(width);
		lineRight.setEndY(height);
		
		lineUp.setEndX(width);
		
		lineDown.setStartY(height);
		lineDown.setEndX(width);
		lineDown.setEndY(height);
		
		sliderX0.setMax(frame.width());
		sliderX1.setMax(frame.width());
		sliderX1.setValue(frame.width());
		sliderY0.setMax(frame.height());
		sliderY1.setMax(frame.height());
		sliderY1.setValue(frame.height());
		
		sliderX0.valueProperty().addListener((o, oldValue, newValue) -> {
			lineLeft.setStartX(newValue.intValue()/2);
			lineLeft.setEndX(newValue.intValue()/2);
		});
		
		sliderX1.valueProperty().addListener((o, oldValue, newValue) -> {
			lineRight.setStartX(newValue.intValue()/2);
			lineRight.setEndX(newValue.intValue()/2);
		});
		
		sliderY0.valueProperty().addListener((o, oldValue, newValue) -> {
			lineUp.setStartY(newValue.intValue()/2);
			lineUp.setEndY(newValue.intValue()/2);
		});
		
		sliderY1.valueProperty().addListener((o, oldValue, newValue) -> {
			lineDown.setStartY(newValue.intValue()/2);
			lineDown.setEndY(newValue.intValue()/2);
		});
	}
	
	@FXML
	private void generateClicked(MouseEvent event) throws IOException {
		int x0 = (int) sliderX0.getValue();
		int x1 = (int) sliderX1.getValue();
		int y0 = (int) sliderY0.getValue();
		int y1 = (int) sliderY1.getValue();
		
		PitchConverter.getInstance().setKeyboardWidth(x1 - x0);
		
		FXMLLoader loader = new FXMLLoader(
					getClass().getResource(Constants.PATH_TO_VISUALIZATION + "EndScene.fxml"));
		loader.load();
		EndSceneController controller = loader.getController();
		controller.setPreviousSceneRoot(rootPane);
		controller.init(new Point(Math.min(x0, x1), Math.min(y0, y1)), new Point(Math.max(x0, x1), Math.max(y0, y1)));
	}
}
