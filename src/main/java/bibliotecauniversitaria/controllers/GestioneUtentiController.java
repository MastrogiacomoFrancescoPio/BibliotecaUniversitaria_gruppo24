package bibliotecauniversitaria.controllers;

import bibliotecauniversitaria.exceptions.UtenteGiaEsistenteException;
import bibliotecauniversitaria.exceptions.UtenteHaPrestitiException;
import bibliotecauniversitaria.models.Archivio;
import bibliotecauniversitaria.models.Biblioteca;
import bibliotecauniversitaria.models.Prestito;
import bibliotecauniversitaria.models.Utente;
import bibliotecauniversitaria.utils.Configurazione;
import bibliotecauniversitaria.utils.StageHelper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

/**
 * @brief Controller relativo alla gestione degli Utenti
 * 
 * Questa classe gestisce l'interfaccia grafica Gestutenti relativa all'amministrazione degli utenti registrati nell'archivio della biblioteca.
 * Le funzionalità principali includono:
 * - Visualizzazione tabellare e ordinamento (tramite nome, cognome o matricola).
 * - Aggiunta di un utente all'archivio.
 * - Rimozione di un utente dall'archivio
 * - Modifica di determinati campi di un utente.
 * - Ricerca di utenti in base a criteri selezionati.
 * - Gestione della sospensione e della revoca di sospensione degli utenti.
 * - Ritorno all'interfaccia "menu".
 * 
 * Nella gestione di queste funzionalità il controller utilizza i metodi dei model @ref Biblioteca e @ref Archivio
 * 
 * @see Gestutenti.fxml Interfaccia utente gestita da questo controller.
 * @see menu.fxml Interfaccia di Menu.
 */
public class GestioneUtentiController {
    @FXML
    private TextField nomeTxt;
    @FXML
    private TextField cognomeTxt;
    @FXML
    private TextField matricolaTxt;
    @FXML
    private TextField emailTxt;
    
    @FXML
    private Button sospendiBtn;


    @FXML
    private TableView<Utente> tabellaUtenti;
    @FXML
    private TableColumn<Utente, String> colonnaNome;
    @FXML
    private TableColumn<Utente, String> colonnaCognome;
    @FXML
    private TableColumn<Utente, String> colonnaMatricola;
    @FXML
    private TableColumn<Utente, String> colonnaEmail;
    @FXML
    private TableColumn<Utente, ArrayList<Prestito>> colonnaPrestiti;
    @FXML
    private TableColumn<Utente, Integer> colonnaSegnalazioni;
    @FXML
    private TableColumn<Utente, LocalDate> colonnaSospensioni;
    @FXML
    private TableColumn<Utente, LocalDate> colonnaFineSospensioni;

    /**
    * @brief Configura la tabella utenti e inizializza lo stato del controller.
    * * Questo metodo viene chiamato automaticamente da JavaFX dopo il caricamento del file FXML.
    * Le sue funzionalità principali includono:
    * - Configurazione della tabella: associa le celle della @ref tabellaUtenti alle Properties degli attributi 
    * della classe @ref Utente tramite PropertyValueFactory.
    * - Editabilità: configura le celle affinchè i campi siano modificabili, includendo il salvataggio automatico 
    * dei dati modificati sull'archivio (@ref Archivio).
    * - Caricamento della tabella: imposta la @ref tabellaUtenti utilizzando la lista osservabile 
    * (ObservableList) di utenti fornita dalla classe @ref Biblioteca.
    * - Aggiornamento Sospensioni: richiama il metodo di @ref Biblioteca per aggiornare lo stato delle sospensioni degli utenti scadute.
    * 
    */
    @FXML
    public void initialize() {
        colonnaNome.setCellValueFactory(new PropertyValueFactory("nome"));
        colonnaCognome.setCellValueFactory(new PropertyValueFactory("cognome"));
        colonnaMatricola.setCellValueFactory(new PropertyValueFactory("matricola"));
        colonnaEmail.setCellValueFactory(new PropertyValueFactory("email"));
        colonnaPrestiti.setCellValueFactory(new PropertyValueFactory("prestiti"));
        colonnaSegnalazioni.setCellValueFactory(new PropertyValueFactory("numeroSegnalazioni"));
        colonnaSospensioni.setCellValueFactory(new PropertyValueFactory("dataSospensione"));
        colonnaFineSospensioni.setCellValueFactory(new PropertyValueFactory("dataFineSospensione"));

        colonnaNome.setCellFactory(TextFieldTableCell.forTableColumn());
        colonnaCognome.setCellFactory(TextFieldTableCell.forTableColumn());
        colonnaMatricola.setCellFactory(TextFieldTableCell.forTableColumn());
        colonnaEmail.setCellFactory(TextFieldTableCell.forTableColumn());
        colonnaSegnalazioni.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        colonnaSospensioni.setCellValueFactory(cellData -> cellData.getValue().dataSospensioneProperty());

        colonnaFineSospensioni.setCellValueFactory(cellData -> cellData.getValue().dataFineSospensioneProperty());


        colonnaSospensioni.setCellFactory(column -> {
            return new TableCell<Utente, LocalDate>() {
                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null) {
                        setText(null);
                    } else {
                        setText(item.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                        Archivio.scrivi(Biblioteca.getListaUtenti(), Archivio.fileUtenti);
                    }
                }
            };
        });
        colonnaFineSospensioni.setCellFactory(column -> {
            return new TableCell<Utente, LocalDate>() {
                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                        Archivio.scrivi(Biblioteca.getListaUtenti(), Archivio.fileUtenti);
                    }
                }
            };
        });
        colonnaPrestiti.setCellFactory(tc -> new TableCell<Utente, ArrayList<Prestito>>() {
            @Override
            protected void updateItem(ArrayList<Prestito> item, boolean empty) {
                super.updateItem(item, empty);
                if(empty || item == null) {
                    setText(null);
                } else {
                    setText(item.size() + "[" + item.stream().map(i -> i.getLibro().getTitolo()).collect(Collectors.joining(", ")) + "]");
                }
            }
        });

        colonnaSospensioni.setEditable(false);
        tabellaUtenti.setEditable(true);
        tabellaUtenti.getSelectionModel().selectedItemProperty().addListener((obs,old,newValue) -> {
            if(newValue.isSospeso()) {
                sospendiBtn.setText("SBLOCCA");
            } else {
                sospendiBtn.setText("SOSPENDI");
            }
        });

        colonnaNome.setOnEditCommit(e -> {
            e.getRowValue().setNome(e.getNewValue());
            Archivio.scrivi(Biblioteca.getListaUtenti(), Archivio.fileUtenti);
            Archivio.scrivi(Biblioteca.getListaPrestiti(), Archivio.filePrestiti);
        });

        colonnaCognome.setOnEditCommit(e -> {
            e.getRowValue().setCognome(e.getNewValue());
            Archivio.scrivi(Biblioteca.getListaUtenti(), Archivio.fileUtenti);
        });

        colonnaMatricola.setOnEditCommit(e -> {
            e.getRowValue().setMatricola(e.getNewValue());
            Archivio.scrivi(Biblioteca.getListaUtenti(), Archivio.fileUtenti);
            Archivio.scrivi(Biblioteca.getListaPrestiti(), Archivio.filePrestiti);
            System.out.println(Biblioteca.getListaPrestiti().stream().map(aa -> aa.toString()).collect(Collectors.toList()));
        });

        colonnaEmail.setOnEditCommit(e -> {
            try {
                e.getRowValue().setEmail(e.getNewValue());
            } catch (IllegalArgumentException ex) {
                new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
                tabellaUtenti.refresh();
            }
            Archivio.scrivi(Biblioteca.getListaUtenti(), Archivio.fileUtenti);
        });

        tabellaUtenti.setItems(Biblioteca.getListaUtenti());
        tabellaUtenti.setRowFactory(tv -> {
            return new TableRow<Utente>() {
                @Override
                protected void updateItem(Utente item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setStyle("");
                    } else if (item.isSospeso()) {
                        setStyle("-fx-background-color: #e53632;");
                    } else {
                        setStyle("");
                    }
                }
            };
        });
        Biblioteca.togliSospensioni();
        tabellaUtenti.setItems(Biblioteca.getListaUtenti());
    }
    
    /**
    * @brief Ritorno all'interfaccia "menu".
    * Questo metodo viene chiamato al click del tasto "Torna al Menu" di Gestutenti.
    * Utilizza la classe @ref StageHelper per effettuare un cambio dalla scena corrente 
    * a quella di "Menu".
    * 
    * @see menu.fxml Interfaccia di Menu.
    */

    @FXML
    public void onMenu() {
        StageHelper.switchToNew((Stage) nomeTxt.getScene().getWindow(), "menu", "Menu");
    }
    
    /**
    * @brief Aggiunta di un utente in archivio.
    * * Questo metodo viene chiamato al click del tasto "Aggiungi" di GestUtenti.
    * Esso effettua una serie di controlli prima di procedere all'aggiunta dell'utente, 
    * accertandosi che nessun campo sia vuoto e tutti siano stati riempiti tramite le apposite
    * caselle di testo in GestUtenti. Procede poi all'aggiunta dell'utente in un blocco try, nel caso
    * in cui si voglia inserire un utente la cui matricola o email è già presente in archivio sarà 
    * lanciata un'eccezione di tipo @ref UtenteGiaEsistenteException con relativo alert. Nel caso in 
    * cui l'utente da inserire presenti una mail non valida sarà lanciata una IllegalArgumentException 
    * con relativo Alert. Per entrambi i tipi di eccezioni non si proseguirà con l'aggiunta dell'utente. 
    * Il metodo infine si occupa di aggiornare tabellaUtenti con l'aggiornata listaUtenti di
    * @ref Biblioteca, per poi in conclusione resettare i valori delle caselle di testo.
    * 
    * @throws UtenteGiaEsistenteException Se la matricola o l'email inserita è già presente nell'archivio.
    * @throws IllegalArgumentException Se l'indirizzo email non rispetta il formato atteso.
    *
    */
    @FXML
    public void onAggiungi() {
        if (nomeTxt.getText().equals("")) {
            new Alert(Alert.AlertType.ERROR, "Il campo nome non può essere vuoto.").showAndWait();
            return;
        }
        if (cognomeTxt.getText().equals("")) {
            new Alert(Alert.AlertType.ERROR, "Il campo cognome non può essere vuoto.").showAndWait();
            return;
        }
        if (matricolaTxt.getText().equals("")) {
            new Alert(Alert.AlertType.ERROR, "Il campo matricola non può essere vuoto.").showAndWait();
            return;
        }

        if (emailTxt.getText().equals("")) {
            new Alert(Alert.AlertType.ERROR, "Il campo email non può essere vuoto.").showAndWait();
            return;
        }
        try{
            Utente u = new Utente(matricolaTxt.getText(), nomeTxt.getText(), cognomeTxt.getText(), emailTxt.getText());
            Biblioteca.aggiungiUtente(u);
        }catch(UtenteGiaEsistenteException | IllegalArgumentException e){
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
            return;
        }
        tabellaUtenti.setItems(Biblioteca.getListaUtenti());
        matricolaTxt.clear();
        nomeTxt.clear();
        cognomeTxt.clear();
        emailTxt.clear();

    }
    /**
    * @brief Rimozione di un utente dall'archivio.
    * * Questo metodo viene chiamato al click del tasto "Rimuovi" di GestUtenti.
    *  Presenta diverse funzionalità:
    * - Controlla se sia stato selezionato tramite click un utente dalla tabella, in caso contrario
    *  mostra un Alert di tipo Warning.
    * - Nel caso sia stato selezionato un utente, prima di procedere alla rimozione, viene visualizzato
    * un Alert di tipo Confirm in cui si richiede la conferma per rimuovere l'utente. 
    * - In caso di esito affermativo dell'Alert, verrà chiamato il metodo rimuoviUtente() di
    * @ref Biblioteca all'interno di un blocco try. Nel caso in cui l'utente presenti prestiti attivi
    * sarà lanciata un'eccezione di tipo @ref UtenteHaPrestitiException e non si procederà alla rimozione
    * - Infine sarà impostata tabellaUtenti al valore aggiornato di listaUtenti.
    * 
    * @throws UtenteHaPrestitiException Se l'utente presenta prestiti attivi.
    *
    */

    @FXML
    public void onRimuovi() {
        Utente selezionato = tabellaUtenti.getSelectionModel().getSelectedItem();
         if (selezionato == null) {
            new Alert(Alert.AlertType.WARNING, "Seleziona un utente da rimuovere.")
                .showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Sicuro di voler rimuovere " + selezionato.getNome() + " " + selezionato.getCognome() + "?", ButtonType.YES, ButtonType.NO);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            try{
                Biblioteca.rimuoviUtente(selezionato);
            }catch(UtenteHaPrestitiException e){
                new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
            }
                tabellaUtenti.setItems(Biblioteca.getListaUtenti());
        }
    }

    /**
    * @brief Ricerca di un utente nella tabella.
    * * Questo metodo viene chiamato al click del tasto "Ricerca" di GestUtenti.
    * Permette di visualizzare nella tabella esclusivamente gli utenti trovati
    * secondo i criteri di ricerca inseriti nelle apposite caselle di testo. Per trovare
    * la lista degli utenti corrispondenti ai criteri di ricerca viene richiamato il metodo 
    * cercaUtente() di @ref Biblioteca. Se quest'ultimo restituisce una lista vuota verrà
    * visualizzato un Alert e la tabella continuerà a mostrare tutti gli utenti dell'archivio.
    * Se la lista restituita non è vuota, la @ref tabellaUtenti viene impostata su tale lista, 
    * visualizzando di conseguenza i soli risultati filtrati.
    *
    */
    @FXML
    public void onRicerca() {
        ObservableList<Utente> utentiTrovati = Biblioteca.cercaUtente(new Utente(matricolaTxt.getText(), nomeTxt.getText(), cognomeTxt.getText(), emailTxt.getText()));

        if (utentiTrovati.isEmpty()) {
            tabellaUtenti.setItems(Biblioteca.getListaUtenti());
            new Alert(Alert.AlertType.INFORMATION, "Nessun utente trovato corrispondente ai criteri inseriti.").showAndWait();
        } else {
            tabellaUtenti.setItems(utentiTrovati);
        }
    }
    
     /**
    * @brief Sospensione/Revoca della sospensione di un utente.
    * * Questo metodo viene chiamato al click del tasto "Sospendi" di GestUtenti.
    *  Presenta diverse funzionalità:
    * - Controlla se è stato selezionato tramite click un utente dalla tabella, in caso contrario
    *  mostra un Alert di tipo Warning.
    * - Nel caso sia stato selezionato un utente, prima di procedere alla sospensione/ revoca sospensione, viene visualizzato
    * un Alert di tipo Confirm in cui si richiede la conferma per procedere. 
    * - In caso di esito affermativo dell'Alert, viene effettuato un controllo sullo stato di sospensione 
    * dell'utnete selezionato, nel caso non sia già sospeso verrà chiamato il metodo sospendiUtente() di
    * @ref Biblioteca per un periodo GIORNI_SOSPENSIONE letto dal file configurazione e varrà settato il testo del pulsante a "SBLOCCA".
    * Nel caso in cui l'utente presenti sia già sospeso verrà richiamato revocaSospensione() di @ref Biblioteca
    * e varrà settato il testo del pulsante a "SOSPENDI".
    * In entrambi i casi la tabella sarà aggiornta.
    *
    */
    @FXML
    public void onSospendi() {
        Utente selezionato = tabellaUtenti.getSelectionModel().getSelectedItem();

        if (selezionato == null) {
            new Alert(Alert.AlertType.WARNING, "Selezionare prima un utente dalla tabella da sospendere.").showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Sicuro di voler " + (selezionato.isSospeso() ? "revocare la sospensione a " : "sospendere ") + selezionato.getNome() + " " + selezionato.getCognome() + "?", ButtonType.YES, ButtonType.NO);
        alert.setResizable(true);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        Optional<ButtonType> result = alert.showAndWait();
        if (!result.isPresent() || result.get() != ButtonType.YES) return;
        if (selezionato.isSospeso()) {
            selezionato.revocaSospensione(true);
            sospendiBtn.setText("SOSPENDI");
            tabellaUtenti.setItems(Biblioteca.getListaUtenti());
            tabellaUtenti.refresh();
            Archivio.scrivi(Biblioteca.getListaUtenti(), Archivio.fileUtenti);
        } else {
            selezionato.sospendi(Biblioteca.configurazione.getNumero("GIORNI_SOSPENSIONE"),true);
            sospendiBtn.setText("SBLOCCA");
            tabellaUtenti.setItems(Biblioteca.getListaUtenti());
            tabellaUtenti.refresh();
            Archivio.scrivi(Biblioteca.getListaUtenti(), Archivio.fileUtenti);
        }
    }
    
    /**
    * @brief Ordinamento degli utenti per cognome.
    * Questo metodo viene chiamato al click dell'opzione "Cognome" del menù a tendina di GestUtenti.
    * Permette la visualizzazione degli utenti nella tabella in ordine alfabetico per cognome e 
    * a parità di cognome per nome. Richiama il metodo {@link Biblioteca#ordinaUtentiCognome(ObservableList) ordinaUtentiCognome()}) 
    * di @ref Biblioteca, il quale crea un clone ordinato di listaUtenti.
    * 
    */
    
    @FXML
    public void onOrdinaCognome() {
        tabellaUtenti.setItems(Biblioteca.ordinaUtentiCognome(tabellaUtenti.getItems()));
    }

    /**
    * @brief Ordinamento degli utenti per nome.
    * Questo metodo viene chiamato al click dell'opzione "Nome" del menù a tendina di GestUtenti.
    * Permette la visualizzazione degli utenti nella tabella in ordine alfabetico per nome e 
    * a parità di nome per cognome. Richiama il metodo {@link Biblioteca#ordinaUtentiNome(ObservableList) ordinaUtentiNome()}) di @ref Biblioteca, 
    * il quale crea un clone ordinato di listaUtenti.
    * 
    */
    @FXML
    public void onOrdinaNome() {
        tabellaUtenti.setItems(Biblioteca.ordinaUtentiNome(tabellaUtenti.getItems()));
    }

    
    /**
    * @brief Ordinamento degli utenti per matricola.
    * Questo metodo viene chiamato al click dell'opzione "Matricola" del menù a tendina di GestUtenti.
    * Permette la visualizzazione degli utenti nella tabella in ordine di matricola. 
    * Richiama il metodo {@link Biblioteca#ordinaUtentiMatricola(ObservableList) ordinaUtentiMatricola()} di @ref Biblioteca, il quale crea un clone ordinato di listaUtenti.
    * 
    */
    @FXML
    public void onOrdinaMatricola() {
        tabellaUtenti.setItems(Biblioteca.ordinaUtentiMatricola(tabellaUtenti.getItems()));
    }
}
