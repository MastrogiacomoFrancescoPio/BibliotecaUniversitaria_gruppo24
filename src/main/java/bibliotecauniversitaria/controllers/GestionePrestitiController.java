package bibliotecauniversitaria.controllers;

import bibliotecauniversitaria.models.Biblioteca;
import bibliotecauniversitaria.models.Libro;
import bibliotecauniversitaria.models.Prestito;
import bibliotecauniversitaria.models.Utente;
import bibliotecauniversitaria.utils.StageHelper;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class GestionePrestitiController {
    @FXML
    private TextField matricolaTxt;
    @FXML
    private TextField ISBNText;
    @FXML
    private DatePicker dataInizioPicker;
    @FXML
    private DatePicker dataRestituzionePicker;
    
    @FXML private TableView<Prestito> tabellaPrestiti;
    @FXML private TableColumn<Prestito, String> colonnaMatricola;
    @FXML private TableColumn<Prestito, String> colonnaISBN;
    @FXML private TableColumn<Prestito, LocalDate> colonnaDataInizio;
    @FXML private TableColumn<Prestito, LocalDate> colonnaDataRestituzione;
    
    
    @FXML
    public void initialize() {
        colonnaISBN.setCellValueFactory(tc -> tc.getValue().getLibro().ISBNProperty());
        colonnaMatricola.setCellValueFactory(tc -> tc.getValue().getUtente().matricolaProperty());
        colonnaDataInizio.setCellValueFactory(cellData -> cellData.getValue().dataInizioProperty());
        colonnaDataRestituzione.setCellValueFactory(cellData -> cellData.getValue().dataRestituzionePrevistaProperty());
    
        colonnaMatricola.setCellFactory(TextFieldTableCell.forTableColumn());
        colonnaISBN.setCellFactory(TextFieldTableCell.forTableColumn());
       
        colonnaDataInizio.setCellFactory(tc -> new TableCell<Prestito, LocalDate>() {
            
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                }
            }
        });
        colonnaDataRestituzione.setCellFactory(tc -> new TableCell<Prestito, LocalDate>() {
           
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                }
            }
        });

        tabellaPrestiti.setItems(Biblioteca.getListaPrestiti());

    }
    
    @FXML
    public void onMenu(){
        StageHelper.switchToNew((Stage)matricolaTxt.getScene().getWindow(), "menu", "Menu");
    }

    
    @FXML
    public void onAggiungi(){
        
        if (dataInizioPicker.getValue() == null || dataRestituzionePicker.getValue() == null) {
            new Alert(Alert.AlertType.ERROR, "Devi selezionare entrambe le date.").showAndWait();
            return;
        }
        
        if(dataInizioPicker.getValue().isAfter(dataRestituzionePicker.getValue())){
            Dialog alert = new Alert(Alert.AlertType.ERROR, "La data di resitituzione non può essere prima di quella di inizio.");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.setResizable(true);
            alert.showAndWait();
            return;
        }
        Libro libro = null;
        try {
            libro = Biblioteca.cercaLibro(new Libro("","",ISBNText.getText(),0,0)).get(0);
        } catch (IndexOutOfBoundsException e) {
            new Alert(Alert.AlertType.ERROR,"Nessun libro corrisponde all'ISBN inserito!").showAndWait();
            return;
        }
        Utente utente = null;
        try {
            utente = Biblioteca.cercaUtente(new Utente(matricolaTxt.getText(),"","","")).get(0);
        } catch (IndexOutOfBoundsException e) {
            new Alert(Alert.AlertType.ERROR,"Nessun utente corrisponde alla matricola inserita!").showAndWait();
            return;
        }
        if(!libro.haCopieDisponibili()){
            new Alert(Alert.AlertType.ERROR, "Il libro non ha copie disponibili").showAndWait();
            return;
        }
        if(utente.verificaLimitePrestitiRaggiunto()){
            new Alert(Alert.AlertType.ERROR, "L'utente ha già raggiunto il limite di prestiti attivi").showAndWait();
            return;
        }
        if(utente.isSospeso()){
            new Alert(Alert.AlertType.ERROR, "L'utente è sospeso").showAndWait();
            return;
        }
        Biblioteca.aggiungiPrestito(new Prestito(libro.getUUID(),utente.getUUID(),dataInizioPicker.getValue(),dataRestituzionePicker.getValue()),true);
        tabellaPrestiti.setItems(Biblioteca.getListaPrestiti());
        
        matricolaTxt.clear();
        ISBNText.clear();
        dataInizioPicker.setValue(null);
        dataRestituzionePicker.setValue(null);
    }
    
    
    @FXML
    public void onRimuovi(){
        Prestito selezionato=tabellaPrestiti.getSelectionModel().getSelectedItem();
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION, "Sicuro di voler rimuovere il prestito selezionato?", ButtonType.YES, ButtonType.NO);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.YES){
           Biblioteca.rimuoviPrestito(selezionato);
           tabellaPrestiti.setItems(Biblioteca.getListaPrestiti());
        }
    }
    
    @FXML
    public void onRicerca(){
        Libro libro=null;
        if(!ISBNText.getText().equals("")){
            try {
                libro = Biblioteca.cercaLibro(new Libro("","",ISBNText.getText(),0,0)).get(0);
            } catch (IndexOutOfBoundsException e) {
                new Alert(Alert.AlertType.ERROR,"Nessun libro corrisponde all'ISBN inserito!").showAndWait();
                return;
            }
        }
        Utente utente = null;
        if(!matricolaTxt.getText().equals("")){
            try {
                utente = Biblioteca.cercaUtente(new Utente(matricolaTxt.getText(),"","","")).get(0);
            } catch (IndexOutOfBoundsException e) {
                new Alert(Alert.AlertType.ERROR,"Nessun utente corrisponde alla matricola inserita!").showAndWait();
                return;
            }
        }
        ObservableList<Prestito> prestitiTrovati = Biblioteca.cercaPrestito(new Prestito(libro==null?null:libro.getUUID(),utente==null?null:utente.getUUID(),dataInizioPicker.getValue(), dataRestituzionePicker.getValue()));

        if (prestitiTrovati.isEmpty()) {
            tabellaPrestiti.setItems(Biblioteca.getListaPrestiti());
            new Alert(Alert.AlertType.INFORMATION, "Nessun prestito trovato corrispondente ai criteri inseriti.").showAndWait();
        } else {
            tabellaPrestiti.setItems(prestitiTrovati);
        }
    }
    
    @FXML
    public void onRestituisci(){
        Prestito selezionato=tabellaPrestiti.getSelectionModel().getSelectedItem();
        if(selezionato==null){
            Alert a = new Alert(Alert.AlertType.ERROR, "Devi selezionare un prestito per poterlo restituire!");
            a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            a.showAndWait();
            return;
        }
        if(selezionato.verificaRitardo(Biblioteca.configurazione.getNumero("RITARDO_SOSPENSIONE_AUTOMATICA"))){
            selezionato.getUtente().sospendi(Biblioteca.configurazione.getNumero("GIORNI_SOSPENSIONE"),true);
        } else if (selezionato.verificaRitardo(Biblioteca.configurazione.getNumero("RITARDO_SEGNALAZIONE"))) {
            new Alert(Alert.AlertType.INFORMATION, "L'utente è stato segnalato per il suo ritardo").showAndWait();
            selezionato.getUtente().incrementaSegnalazioni();
            selezionato.getUtente().controllaSegnalazioni(Biblioteca.configurazione.getNumero("MASSIME_SEGNALAZIONI"), Biblioteca.configurazione.getNumero("GIORNI_SOSPENSIONE"));
        }
        Biblioteca.rimuoviPrestito(selezionato);
        tabellaPrestiti.setItems(Biblioteca.getListaPrestiti());
    }

    @FXML
    public void onOrdinaISBN() {
        tabellaPrestiti.setItems(Biblioteca.ordinaPrestitiISBN(tabellaPrestiti.getItems()));
    }

    @FXML
    public void onOrdinaMatricola() {
        tabellaPrestiti.setItems(Biblioteca.ordinaPrestitiMatricola(tabellaPrestiti.getItems()));
    }

    @FXML
    public void onOrdinaDataInizio() {
        tabellaPrestiti.setItems(Biblioteca.ordinaPrestitiDataInizio(tabellaPrestiti.getItems()));
    }

    @FXML
    public void onOrdinaDataRestituzionePrevista() {
        tabellaPrestiti.setItems(Biblioteca.ordinaPrestitiDataRestituzionePrevista(tabellaPrestiti.getItems()));
    }

}
