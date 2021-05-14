package it.polito.tdp.borders;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtAnno"
    private TextField txtAnno; // Value injected by FXMLLoader
    
    @FXML
    private ComboBox<Country> cmbBox;

    @FXML
    private Button rcbCountries;

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaConfini(ActionEvent event) {
    	
    	cmbBox.getItems().clear();
    	txtResult.clear();
    	String anno=txtAnno.getText();
    	boolean ok = false;
    	int annoNum;
    	
    	try {
    		annoNum = Integer.parseInt(anno);
    		if(annoNum >= 1816 && annoNum <= 2016)
    			ok = true;
    	}
    	catch(NumberFormatException nfe) {
    		txtResult.setText("Inserire un numero");
    		return;
    	}
    	if(ok) {
    		model.creaGrafo(annoNum);
    		txtResult.setText(""+model.getNumberOfConnectedComponents()+"\n");
    	    txtResult.appendText(""+model.getGradoVertici());
    	    this.cmbBox.getItems().addAll(model.getCountries());
    	}
    	else {
    		txtResult.setText("Inserire un anno compreso tra 1816 e 2016");
    		return;
    	}
    }
    
    @FXML
    void doRcbCountries(ActionEvent event) {
    	
    	txtResult.clear();
    	Country c = cmbBox.getValue();
    	txtResult.setText("Stati raggiungibili a partire da " + c.toString() + ": \n\n");
    	txtResult.appendText(model.getRaggiungibili(c));
    
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbBox != null : "fx:id=\"cmbBox\" was not injected: check your FXML file 'Scene.fxml'.";
        assert rcbCountries != null : "fx:id=\"rcbCountries\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
