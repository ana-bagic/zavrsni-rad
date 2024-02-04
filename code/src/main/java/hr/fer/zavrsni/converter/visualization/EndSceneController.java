package hr.fer.zavrsni.converter.visualization;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.opencv.core.Point;

import hr.fer.zavrsni.converter.SheetMusicConverter;
import hr.fer.zavrsni.converter.converting.Converter;
import hr.fer.zavrsni.converter.util.ScaleUtil;
import hr.fer.zavrsni.converter.util.TempoUtil;
import hr.fer.zavrsni.converter.util.XMLUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

/**
 * Kontroler scene u kojoj se pokreće algoritam generiranja notnog zapisa.
 * 
 * @author Ana Bagić
 *
 */
public class EndSceneController extends AbstractController {

	/** Gornja lijeva točka pravokutnika po kojemu se obrezuje videozapis. */
	private Point p0;
	/** Donja desna točka pravokutnika po kojemu se obrezuje videozapis. */
	private Point p1;
	@FXML
	private Text readyLabel;
	@FXML
	private Text enjoyLabel;
	@FXML
	private Button generateButton;
	
	public void init(Point p0, Point p1) {
		this.p0 = p0;
		this.p1 = p1;
		
		readyLabel.setVisible(true);
		enjoyLabel.setVisible(false);
	}
	
	@FXML
	private void generate(MouseEvent event) throws IOException {		
		Converter con = Converter.getInstance();
		con.extractNotes(p0, p1);
		
		ScaleUtil.findScale(con.getSong());
		ScaleUtil.fitToScale(con.getSong());
		
		TempoUtil.findTempo(con.getSong());
		TempoUtil.createTimeline(con.getSong(), con.getFirstFrame());
		
		String document = XMLUtil.generate(con.getSong());
		
		FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("MusicXML files (*.musicxml)", "*.musicxml", ".xml");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(SheetMusicConverter.getStage());

        if (file != null) {
    		try {
				Files.write(file.toPath(), document.getBytes(StandardCharsets.UTF_8));
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        
        readyLabel.setVisible(false);
        enjoyLabel.setVisible(true);
        generateButton.setVisible(false);
	}
}
