package it.polito.tdp.nyc;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.nyc.model.Arco;
import it.polito.tdp.nyc.model.Model;
import it.polito.tdp.nyc.model.NTAcode;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class FXMLController {

	Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnAdiacenti"
    private Button btnAdiacenti; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaLista"
    private Button btnCreaLista; // Value injected by FXMLLoader

    @FXML // fx:id="clPeso"
    private TableColumn<Arco, Double> clPeso; // Value injected by FXMLLoader

    @FXML // fx:id="clV1"
    private TableColumn<Arco, String> clV1; // Value injected by FXMLLoader

    @FXML // fx:id="clV2"
    private TableColumn<Arco, String> clV2; // Value injected by FXMLLoader

    @FXML // fx:id="cmbBorough"
    private ComboBox<String> cmbBorough; // Value injected by FXMLLoader

    @FXML // fx:id="tblArchi"
    private TableView<Arco> tblArchi; // Value injected by FXMLLoader

    @FXML // fx:id="txtDurata"
    private TextField txtDurata; // Value injected by FXMLLoader

    @FXML // fx:id="txtProb"
    private TextField txtProb; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doAnalisiArchi(ActionEvent event) {
    	this.txtResult.clear();
    	
    	if(!this.model.isGrafoCreato()) {
    		this.txtResult.appendText("Creare prima il grafo!");
    		return;
    	}
    	
    	List<Arco> archiMigliori = this.model.getArchiPesoMaggiore();
    	this.tblArchi.setItems(FXCollections.observableArrayList(archiMigliori));
    	
    	this.txtResult.appendText("PESO MEDIO: ");
    	this.txtResult.appendText(String.format("%.3f", this.model.pesoMedioArchi()));
    	this.txtResult.appendText("\nARCHI CON PESO MAGGIORE DEL PESO MEDIO: " + archiMigliori.size());

    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	this.txtResult.clear();
    	String borgo = this.cmbBorough.getValue();
    	
    	if(borgo==null) {
    		this.txtResult.appendText("Selezionare un provider!");
    		return;
    	}
    	
    	this.model.creaGrafo(borgo);
    	
    	this.txtResult.appendText("Grafo creato!\n");
    	this.txtResult.appendText("# Vertici : " + this.model.getNumVertici() +"\n");
    	this.txtResult.appendText("# Archi : " + this.model.getNumArchi() +"\n");
    	
    	   	
    	
    	
    }

    @FXML
    void doSimula(ActionEvent event) {
    	this.txtResult.clear();
    	
    	if(!this.model.isGrafoCreato()) {
    		this.txtResult.appendText("Creare prima il grafo!");
    		return;
    	}
    	
    	try {
    		double probabilita = Double.parseDouble(this.txtProb.getText());
    		
    		if(probabilita<0.2 || probabilita>0.9) {
    			this.txtResult.appendText("Il valore di p deve essere compreso tra 0.2 e 0.9\n");
    			return;
    		}
    		int durata = Integer.parseInt(this.txtDurata.getText());
    		
    		this.model.simula(probabilita, durata);
    		
    		this.txtResult.appendText("NTA con relativo numero di file condivisi o ricondivisi:\n");
    		
    		for(NTAcode n : this.model.getMappaNTA().values()) {
    			this.txtResult.appendText(n.toString()+ "\n");
    		}
    		
    		
    	}catch(NumberFormatException ex) {
    		txtResult.appendText("Errore: p deve essere compreso tra 0.2 e 0.9 e d deve essere un intero positivo\n");
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnAdiacenti != null : "fx:id=\"btnAdiacenti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaLista != null : "fx:id=\"btnCreaLista\" was not injected: check your FXML file 'Scene.fxml'.";
        assert clPeso != null : "fx:id=\"clPeso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert clV1 != null : "fx:id=\"clV1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert clV2 != null : "fx:id=\"clV2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbBorough != null : "fx:id=\"cmbBorough\" was not injected: check your FXML file 'Scene.fxml'.";
        assert tblArchi != null : "fx:id=\"tblArchi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtDurata != null : "fx:id=\"txtDurata\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtProb != null : "fx:id=\"txtProb\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

        this.clV1.setCellValueFactory(new PropertyValueFactory<Arco, String>("v1"));
        this.clV2.setCellValueFactory(new PropertyValueFactory<Arco, String>("v2"));
        this.clPeso.setCellValueFactory(new PropertyValueFactory<Arco, Double>("peso"));
    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.cmbBorough.getItems().clear();
    	this.cmbBorough.getItems().addAll(this.model.getBorghi());
    }

}
