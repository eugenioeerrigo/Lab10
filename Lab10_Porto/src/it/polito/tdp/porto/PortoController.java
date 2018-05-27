package it.polito.tdp.porto;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class PortoController {

	private Model model;
	
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Author> boxPrimo;

    @FXML
    private ComboBox<Author> boxSecondo;

    @FXML
    private TextArea txtResult;

    @FXML
    void handleCoautori(ActionEvent event) {
    	txtResult.clear();
    	boxSecondo.getItems().clear();
    	
    	for(Author x : model.getNonCoautori(boxPrimo.getValue())) {
			boxSecondo.getItems().add(x);
		}
    	
    	List<Author> list = model.getCoautori(boxPrimo.getValue());
    	for(Author a : list)
    		txtResult.appendText(a+"\n");
    }

    @FXML
    void handleSequenza(ActionEvent event) {
    	txtResult.appendText("\n----------------\n");
    	String seq = model.trovaArticoliConn(boxPrimo.getValue(), boxSecondo.getValue());
    	txtResult.appendText(seq);
    }

    @FXML
    void initialize() {
        assert boxPrimo != null : "fx:id=\"boxPrimo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert boxSecondo != null : "fx:id=\"boxSecondo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Porto.fxml'.";

    }

	public void setModel(Model m) {
		this.model = m;
		
		for(Author a : m.getAutori())
			boxPrimo.getItems().add(a);
		
	}
}
