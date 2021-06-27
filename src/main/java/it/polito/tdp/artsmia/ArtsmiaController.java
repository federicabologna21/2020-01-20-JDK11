package it.polito.tdp.artsmia;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.artsmia.model.Adiacenza;
import it.polito.tdp.artsmia.model.Artist;
import it.polito.tdp.artsmia.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ArtsmiaController {
	
	private Model model ;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private Button btnArtistiConnessi;

    @FXML
    private Button btnCalcolaPercorso;

    @FXML
    private ComboBox<String> boxRuolo;

    @FXML
    private TextField txtArtista;

    @FXML
    private TextArea txtResult;

    @FXML
    void doArtistiConnessi(ActionEvent event) {
    	txtResult.clear();
    	// txtResult.appendText("Calcola artisti connessi\n");
    	String ruolo = this.boxRuolo.getValue();
    	if (ruolo == null) {
    		txtResult.appendText("Selezionare un ruolo per creare il grafo e calcolare gli artisti connessi!\n");
    		return;
    	}
    	this.model.creaGrafo(ruolo);
    	
    	txtResult.appendText("Gli artisti connessi sono:\n");
    	for(Adiacenza a: this.model.getArtistiConnessi(ruolo)) {
    		txtResult.appendText(a+"\n");
    	}
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	// txtResult.appendText("Calcola percorso");
    	
    	String idS = this.txtArtista.getText();
    	int id;
    	try {
    		id = Integer.parseInt(idS);
    	}catch (NumberFormatException ne) {
    		txtResult.appendText("Inserire un id per calcolare il percorso!\n");
    		return ;
    	}
    	
    	txtResult.appendText("Percorso calcolato:\n");
    	for (Artist a : this.model.trovaPercorso(id)) {
    		txtResult.appendText(a+"\n");
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	// txtResult.appendText("Crea grafo");
    	String ruolo = this.boxRuolo.getValue();
    	if (ruolo == null) {
    		txtResult.appendText("Selezionare un ruolo per creare il grafo!\n");
    		return;
    	}
    	this.model.creaGrafo(ruolo);
    	txtResult.appendText("GRAFO CREATO!\n");
    	txtResult.appendText("# VERTICI: "+this.model.getNumVertici()+"\n");
    	txtResult.appendText("# ARCHI: "+this.model.getNumArchi()+"\n");
    	
    	
    	
    }

    public void setModel(Model model) {
    	this.model = model;
    	
    	this.boxRuolo.getItems().addAll(this.model.getRole());
    }

    
    @FXML
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnArtistiConnessi != null : "fx:id=\"btnArtistiConnessi\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnCalcolaPercorso != null : "fx:id=\"btnCalcolaPercorso\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert boxRuolo != null : "fx:id=\"boxRuolo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtArtista != null : "fx:id=\"txtArtista\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Artsmia.fxml'.";

    }
}
