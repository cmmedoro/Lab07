/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.poweroutages;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import it.polito.tdp.poweroutages.model.Model;
import it.polito.tdp.poweroutages.model.Nerc;
import it.polito.tdp.poweroutages.model.PowerOutages;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="cmbNerc"
    private ComboBox<Nerc> cmbNerc; // Value injected by FXMLLoader

    @FXML // fx:id="txtYears"
    private TextField txtYears; // Value injected by FXMLLoader

    @FXML // fx:id="txtHours"
    private TextField txtHours; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    private Model model;
    private List<PowerOutages> events;
    
    @FXML
    void doRun(ActionEvent event) {
    	txtResult.clear();
    	// prendo i valori in input
    	String anni = this.txtYears.getText();
    	String ore = this.txtHours.getText();
    	Nerc nerc = this.cmbNerc.getValue();
    	// controlli sull'input
    	if( nerc == null) {
    		this.txtResult.setText("Seleziona un NERC!");
    		return;
    	}	
    	if( anni == "") {
    		this.txtResult.setText("Inserisci gli anni della polizza!");
    		return;
    	}
    	if( ore == "") {
    		this.txtResult.setText("Inserisci le ore di disservizio della polizza!");
    		return;
    	}
    	//converti anni e ore in numeri
    	int x = 0,y = 0;
    	if( ore != "" && anni != "") {
    		x = Integer.parseInt(anni);
        	y = Integer.parseInt(ore);
    	}
    	//this.txtResult.setText("Nerc selezionato: "+nerc+". Anni: "+ x + ". Ore: "+ y +".");
    	events = this.model.getEventList(nerc, x, y);
    	if( events.size() == 0) {
    		this.txtResult.setText("Non ci sono eventi che soddisfino le caratteristiche");
    		return;
    	}
    	//conto le persone affette
    	int sumAffected = 0;
    	for(PowerOutages po : events) {
    		sumAffected += po.getCustomersAffected();
    	}
    	//conto le ore
    	int hours = 0;
    	for(PowerOutages po : events) {
    		hours += po.getDuration();
    	}
    	this.txtResult.appendText("Tot people affected: "+sumAffected+"\n");
    	this.txtResult.appendText("Tot hours of outage: "+hours+"\n");
    	//stampa l'elenco di eventi
    	for(PowerOutages po : events) {
    		this.txtResult.appendText(""+po.toString()+"\n");
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert cmbNerc != null : "fx:id=\"cmbNerc\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtYears != null : "fx:id=\"txtYears\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtHours != null : "fx:id=\"txtHours\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        
        // Utilizzare questo font per incolonnare correttamente i dati;
        txtResult.setStyle("-fx-font-family: monospace");
    }
    
    public void setModel(Model model) {
    	this.model = model;
    	// setto la combobox con i valori del NERC ricavati dal database
    	this.cmbNerc.getItems().clear();
    	for(Nerc n : this.model.getNercList()) {
    		this.cmbNerc.getItems().add(n);
    	}
    }
}
