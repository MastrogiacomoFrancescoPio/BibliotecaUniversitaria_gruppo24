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

/**
 * @brief Controller relativo alla gestione dei Libri.
 * 
 * Questa classe gestisce l'interfaccia grafica Gestlibri relativa all'amministrazione dei libri registrati nell'archivio della biblioteca.
 * Le funzionalità principali includono:
 * - Visualizzazione tabellare e ordinamento (tramite titolo, autore o ISBN).
 * - Aggiunta di un libro all'archivio.
 * - Rimozione di un libro dall'archivio
 * - Modifica di determinati campi di un libro.
 * - Ricerca di libri in base a criteri selezionati.
 * - Ritorno all'interfaccia "menu".
 * 
 * Nella gestione di queste funzionalità il controller utilizza i metodi dei model @ref Biblioteca e @ref Archivio
 * 
 * @see Gestlibri.fxml Interfaccia utente gestita da questo controller.
 * @see menu.fxml Interfaccia di Menu.
 */

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
     
    /**
    * @brief Configura la tabella libri e inizializza lo stato del controller.
    * * Questo metodo viene chiamato automaticamente da JavaFX dopo il caricamento del file FXML.
    * Le sue funzionalità principali includono:
    * - Configurazione della tabella: associa le celle della @ref tabellaLibri alle Properties degli attributi 
    * della classe @ref Libro tramite PropertyValueFactory.
    * - Editabilità: configura le celle affinchè i campi siano modificabili, includendo il salvataggio automatico 
    * dei dati modificati sull'archivio (@ref Archivio).
    * - Caricamento della tabella: imposta la @ref tabellaLibri utilizzando la lista osservabile 
    * (ObservableList) di libri fornita dalla classe @ref Biblioteca.
    
    * 
    */
    
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
           try {
               e.getRowValue().setISBN(e.getNewValue());
           } catch (LibroGiaEsistenteException ex) {
               new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK).showAndWait();
           }
           tabellaLibri.refresh();
           Archivio.scrivi(tabellaLibri.getItems(), Archivio.fileLibri);
        });
        
        colonnaCopie.setOnEditCommit(e -> {
            try {
                e.getRowValue().setNumeroCopieDisponibili(e.getNewValue());
            } catch (IllegalArgumentException ex){
                Alert alert = new Alert(Alert.AlertType.WARNING, ex.getMessage());
                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                alert.showAndWait();
                tabellaLibri.refresh();
            }
            Archivio.scrivi(tabellaLibri.getItems(), Archivio.fileLibri);
        });
        tabellaLibri.setItems(Biblioteca.getListaLibri());
    }
    
    /**
    * @brief Ritorno all'interfaccia "menu".
    * Questo metodo viene chiamato al click del tasto "Torna al Menu" di Gestlibri.
    * Utilizza la classe @ref StageHelper per effettuare un cambio dalla scena corrente 
    * a quella di {@link MenuController interfaccia Menu}.
    * 
    * @see menu.fxml Interfaccia di Menu.
    */
    @FXML
    public void onMenu(){
        StageHelper.switchToNew((Stage)titoloTxt.getScene().getWindow(), "menu", "Menu");
    }
    
    /**
    * @brief Aggiunta di un libro in archivio.
    * * Questo metodo viene chiamato al click del tasto "Aggiungi" di Gestlibri.
    * Esso effettua una serie di controlli prima di procedere all'aggiunta del libro, 
    * accertandosi che nessun campo sia vuoto e che l'anno e il numero di copie inseriti siano effettivamente int. 
    * Procede poi all'aggiunta del libro in un blocco try, nel caso in cui si voglia inserire un utente il cui ISBN 
    * è già presente in archivio sarà lanciata un'eccezione di tipo @ref LibroGiaEsistenteException con relativo alert. 
    * Nel caso in cui l'anno inserito sia oltre l'anno corrente o sia inserito un numero negativo di copie sarà lanciata 
    * una IllegalArgumentException con relativo Alert. Per entrambi i tipi di eccezioni non si proseguirà con l'aggiunta del libro. 
    * Il metodo infine si occupa di aggiornare tabellaLibri con l'aggiornata listaLibri di
    * @ref Biblioteca, per poi in conclusione resettare i valori delle caselle di testo.
    * 
    * @throws LibroGiaEsistenteException Se l'ISBN inserito è già presente nell'archivio.
    * @throws IllegalArgumentException Se l'anno inserito è oltre l'anno corrente o sia inserito un numero negativo di copie.
    *
    */
    @FXML
    public void onAggiungi(){
        int anno, copie;
        if(titoloTxt.getText().equals("")){
            new Alert(Alert.AlertType.WARNING, "Il campo titolo non può essere vuoto.").showAndWait();
            return;
        }
        if(autoreTxt.getText().equals("")) {
            new Alert(Alert.AlertType.WARNING, "Il campo autore non può essere vuoto.").showAndWait();
            return;
        }
        if(annoTxt.getText().equals("")) {
            new Alert(Alert.AlertType.WARNING, "Il campo anno di pubblicazione non può essere vuoto.").showAndWait();
            return;
        }
        try{
            anno=Integer.parseInt(annoTxt.getText());
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.WARNING, "Il campo anno di pubblicazione deve essere un numero intero").showAndWait();
            return;
        }
        if(ISBNTxt.getText().equals("")) {
            new Alert(Alert.AlertType.WARNING, "Il campo ISBN non può essere vuoto.").showAndWait();
            return;
        }
        if(copieTxt.getText().equals("")){
            new Alert(Alert.AlertType.WARNING, "Il campo copie non può essere vuoto.").showAndWait();
            return;
        }
        try{
           copie=Integer.parseInt(copieTxt.getText());
        } catch (NumberFormatException e){
            new Alert(Alert.AlertType.WARNING, "Il campo copie deve essere un numero intero").showAndWait();
            return;
        }
        try{
            Biblioteca.aggiungiLibro(new Libro(titoloTxt.getText(), autoreTxt.getText(),ISBNTxt.getText(),anno,copie));
        } catch(LibroGiaEsistenteException ex){
            new Alert(Alert.AlertType.WARNING, "È già presente un libro con lo stesso ISBN").showAndWait();
            return;
        } catch(IllegalArgumentException e){
            new Alert(Alert.AlertType.WARNING, e.getMessage()).showAndWait();
        }
        tabellaLibri.setItems(Biblioteca.getListaLibri());
        
        titoloTxt.clear();
        autoreTxt.clear();
        annoTxt.clear();
        copieTxt.clear();
        ISBNTxt.clear();
    }
    
    /**
    * @brief Rimozione di un libro dall'archivio.
    * * Questo metodo viene chiamato al click del tasto "Rimuovi" di Gestlibri.
    *  Presenta diverse funzionalità:
    * - Controlla se sia stato selezionato tramite click un libro dalla tabella, in caso contrario
    *  mostra un Alert di tipo Warning.
    * - Nel caso sia stato selezionato un libro, prima di procedere alla rimozione, viene visualizzato
    * un Alert di tipo Confirm in cui si richiede la conferma per rimuovere il libro. 
    * - In caso di esito affermativo dell'Alert, verrà chiamato il metodo rimuoviLibro() di
    * @ref Biblioteca all'interno di un blocco try. Nel caso in cui il libro abbia presenti copie in prestito
    * sarà lanciata un'eccezione di tipo @ref LibroInPrestitoException e non si procederà alla rimozione
    * - Infine sarà impostata tabellaLibri al valore aggiornato di listaLibri.
    * 
    * @throws LibroInPrestitoException se il libro presenta copie in prestito.
    *
    */
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
                new Alert(Alert.AlertType.WARNING, e.getMessage()).showAndWait();
            }
            tabellaLibri.setItems(Biblioteca.getListaLibri());
        }
    }

    /**
    * @brief Ricerca di un libro nella tabella.
    * * Questo metodo viene chiamato al click del tasto "Ricerca" di Gestlibri.
    * Permette di visualizzare nella tabella esclusivamente i libri trovati
    * secondo i criteri di ricerca inseriti nelle apposite caselle di testo. Per trovare
    * la lista dei libri corrispondenti ai criteri di ricerca viene richiamato il metodo 
    * cercaLibro() di @ref Biblioteca. Se quest'ultimo restituisce una lista vuota verrà
    * visualizzato un Alert e la tabella continuerà a mostrare tutti i libri dell'archivio.
    * Se la lista restituita non è vuota, la @ref tabellaLibri viene impostata su tale lista, 
    * visualizzando di conseguenza i soli risultati filtrati..
    *
    */
    @FXML
    public void onRicerca() {
        ObservableList<Libro> libriTrovati = Biblioteca.cercaLibro(new Libro(titoloTxt.getText(),autoreTxt.getText(),ISBNTxt.getText(),annoTxt.getText().equals("")?0:Integer.parseInt(annoTxt.getText()),copieTxt.getText().equals("")?0:Integer.parseInt(copieTxt.getText())));

        if (libriTrovati.isEmpty()) {
            tabellaLibri.setItems(Biblioteca.getListaLibri());
            new Alert(Alert.AlertType.INFORMATION, "Nessun libro trovato corrispondente ai criteri inseriti.").showAndWait();
        } else {
            tabellaLibri.setItems(libriTrovati);
        }
    }
    
    /**
    * @brief Ordinamento degli utenti per titolo.
    * Questo metodo viene chiamato al click dell'opzione "Titolo" del menù a tendina di Gestlibri.
    * Permette la visualizzazione dei libri nella tabella in ordine alfabetico per titolo. 
    * Richiama il metodo {@link Biblioteca#ordinaLibriTitolo(ObservableList) ordinaLibriTitolo()}  di @ref Biblioteca, 
    * il quale crea un clone ordinato di listalibri.
    * 
    */

    @FXML
    public void onOrdinaTitolo(){
        tabellaLibri.setItems(Biblioteca.ordinaLibriTitolo(tabellaLibri.getItems()));
    }
    
    /**
    * @brief Ordinamento degli utenti per autore.
    * Questo metodo viene chiamato al click dell'opzione "Autore" del menù a tendina di Gestlibri.
    * Permette la visualizzazione dei libri nella tabella in ordine alfabetico per autore. 
    * Richiama il metodo {@link Biblioteca#ordinaLibriAutore(ObservableList) ordinaLibriAutore()}  di @ref Biblioteca, 
    * il quale crea un clone ordinato di listalibri.
    * 
    */
    @FXML
    public void onOrdinaAutore(){
        tabellaLibri.setItems(Biblioteca.ordinaLibriAutore(tabellaLibri.getItems()));
    }
    
    /**
    * @brief Ordinamento degli utenti per ISBN.
    * Questo metodo viene chiamato al click dell'opzione "ISBN" del menù a tendina di Gestlibri.
    * Permette la visualizzazione dei libri nella tabella in ordine di ISBN. 
    * Richiama il metodo {@link Biblioteca#ordinaLibriISBN(ObservableList) ordinaLibriISBN()} di @ref Biblioteca, 
    * il quale crea un clone ordinato di listalibri.
    * 
    */
    @FXML
    public void onOrdinaISBN(){
        tabellaLibri.setItems(Biblioteca.ordinaLibriISBN(tabellaLibri.getItems()));
    }

    
}


