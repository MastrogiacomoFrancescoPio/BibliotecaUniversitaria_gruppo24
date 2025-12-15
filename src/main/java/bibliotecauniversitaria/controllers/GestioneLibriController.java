package bibliotecauniversitaria.controllers;

import bibliotecauniversitaria.exceptions.LibroGiaEsistenteException;
import bibliotecauniversitaria.exceptions.LibroInPrestitoException;
import bibliotecauniversitaria.models.Archivio;
import bibliotecauniversitaria.models.Biblioteca;
import bibliotecauniversitaria.models.Libro;
import bibliotecauniversitaria.utils.StageHelper;
import java.util.Optional;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

    public class GestioneLibriController {
    @FXML private TextField titoloTxt;
    @FXML private TextField autoreTxt;
    @FXML private TextField annoTxt;
    @FXML private TextField copieTxt;
    @FXML private TextField ISBNTxt;
    @FXML private TableView<Libro> tabellaLibri;
    @FXML private TableColumn<Libro, String> colonnaTitolo;
    @FXML private TableColumn<Libro, String> colonnaAutore;
    @FXML private TableColumn<Libro, Integer> colonnaAnno;
    @FXML private TableColumn<Libro, String> colonnaISBN;
    @FXML private TableColumn<Libro, Integer> colonnaCopie;
     
    
    
    @FXML
    public void initialize(){
        colonnaTitolo.setCellValueFactory(new PropertyValueFactory("titolo"));
        colonnaAutore.setCellValueFactory(new PropertyValueFactory("autore"));
        colonnaAnno.setCellValueFactory(new PropertyValueFactory("annoPubblicazione"));
        colonnaISBN.setCellValueFactory(new PropertyValueFactory("ISBN"));
        colonnaCopie.setCellValueFactory(new PropertyValueFactory("numeroCopieDisponibili"));
        
        colonnaTitolo.setCellFactory(TextFieldTableCell.forTableColumn());
        colonnaAutore.setCellFactory(TextFieldTableCell.forTableColumn());
        colonnaAnno.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colonnaISBN.setCellFactory(TextFieldTableCell.forTableColumn());
        colonnaCopie.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        tabellaLibri.setEditable(true);
        
        colonnaTitolo.setOnEditCommit(e -> {
            e.getRowValue().setTitolo(e.getNewValue());
            Archivio.scrivi(tabellaLibri.getItems(), Archivio.fileLibri);
        });
        
        colonnaAutore.setOnEditCommit(e -> {
            e.getRowValue().setAutore(e.getNewValue());
            Archivio.scrivi(tabellaLibri.getItems(), Archivio.fileLibri);
        });
        
        colonnaAnno.setOnEditCommit(e -> {
            e.getRowValue().setAnnoPubblicazione(e.getNewValue());
            Archivio.scrivi(tabellaLibri.getItems(), Archivio.fileLibri);
        });
        
        colonnaISBN.setOnEditCommit(e -> {
            e.getRowValue().setISBN(e.getNewValue());
            Archivio.scrivi(tabellaLibri.getItems(), Archivio.fileLibri);
        });
        
        colonnaCopie.setOnEditCommit(e -> {
            try {
                e.getRowValue().setNumeroCopieDisponibili(e.getNewValue());
            } catch (IllegalArgumentException ex){
                new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
                tabellaLibri.refresh();
            }
            Archivio.scrivi(tabellaLibri.getItems(), Archivio.fileLibri);
        });

        
        tabellaLibri.setItems(Biblioteca.getListaLibri());
    }
    
    @FXML
    public void onMenu(){
        StageHelper.switchToNew((Stage)titoloTxt.getScene().getWindow(), "menu", "Menu");
    }
    
    @FXML
    public void onAggiungi(){
        int anno, copie;
        if(titoloTxt.getText().equals("")){
            new Alert(Alert.AlertType.ERROR, "Il campo titolo non può essere vuoto.").showAndWait();
            return;
        }
        if(autoreTxt.getText().equals("")) {
            new Alert(Alert.AlertType.ERROR, "Il campo autore non può essere vuoto.").showAndWait();
            return;
        }
        if(annoTxt.getText().equals("")) {
            new Alert(Alert.AlertType.ERROR, "Il campo anno di pubblicazione non può essere vuoto.").showAndWait();
            return;
        }
        try{
            anno=Integer.parseInt(annoTxt.getText());
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Il campo anno di pubblicazione deve essere un numero intero").showAndWait();
            return;
        }
        if(ISBNTxt.getText().equals("")) {
            new Alert(Alert.AlertType.ERROR, "Il campo ISBN non può essere vuoto.").showAndWait();
            return;
        }
        if(copieTxt.getText().equals("")){
            new Alert(Alert.AlertType.ERROR, "Il campo copie non può essere vuoto.").showAndWait();
            return;
        }
        try{
           copie=Integer.parseInt(copieTxt.getText());
        } catch (NumberFormatException e){
            new Alert(Alert.AlertType.ERROR, "Il campo copie deve essere un numero intero").showAndWait();
            return;
        }
        try{
            Biblioteca.aggiungiLibro(new Libro(titoloTxt.getText(), autoreTxt.getText(),ISBNTxt.getText(),anno,copie));
        } catch(LibroGiaEsistenteException ex){
            new Alert(Alert.AlertType.ERROR, "È già presente un libro con lo stesso ISBN").showAndWait();
            return;
        } catch(IllegalArgumentException e){
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        }
        tabellaLibri.setItems(Biblioteca.getListaLibri());
        
        titoloTxt.clear();
        autoreTxt.clear();
        annoTxt.clear();
        copieTxt.clear();
        ISBNTxt.clear();
    }
    
    @FXML
    public void onRimuovi(){
        Libro selezionato = tabellaLibri.getSelectionModel().getSelectedItem();
        if (selezionato == null) {
            new Alert(Alert.AlertType.WARNING, "Seleziona un libro da rimuovere.").showAndWait();
            return;
        }
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION, "Sicuro di voler rimuovere "+selezionato.getTitolo()+"?", ButtonType.YES, ButtonType.NO);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        Optional<ButtonType> result =alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.YES){
            try{
                Biblioteca.rimuoviLibro(selezionato);
            } catch (LibroInPrestitoException e){
                new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
            }
            tabellaLibri.setItems(Biblioteca.getListaLibri());
        }
    }

    @FXML
    public void onRicerca() {
        ObservableList<Libro> libriTrovati = Biblioteca.cercaLibro(new Libro(titoloTxt.getText(),autoreTxt.getText(),ISBNTxt.getText(),annoTxt.getText().equals("")?-1:Integer.parseInt(annoTxt.getText()),copieTxt.getText().equals("")?-1:Integer.parseInt(copieTxt.getText())));

        if (libriTrovati.isEmpty()) {
            tabellaLibri.setItems(Biblioteca.getListaLibri());
            new Alert(Alert.AlertType.INFORMATION, "Nessun libro trovato corrispondente ai criteri inseriti.").showAndWait();
        } else {
            tabellaLibri.setItems(libriTrovati);
        }
    }

    @FXML
    public void onOrdinaTitolo(){
        tabellaLibri.setItems(Biblioteca.ordinaLibriTitolo(tabellaLibri.getItems()));
    }
    
    @FXML
    public void onOrdinaAutore(){
        tabellaLibri.setItems(Biblioteca.ordinaLibriAutore(tabellaLibri.getItems()));
    }
    
    @FXML
    public void onOrdinaISBN(){
        tabellaLibri.setItems(Biblioteca.ordinaLibriISBN(tabellaLibri.getItems()));
    }

    
}


