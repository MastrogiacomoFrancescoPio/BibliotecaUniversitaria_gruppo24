package bibliotecauniversitaria.controllers;

import bibliotecauniversitaria.models.Biblioteca;
import bibliotecauniversitaria.models.Libro;
import bibliotecauniversitaria.models.Prestito;
import bibliotecauniversitaria.models.Utente;
import bibliotecauniversitaria.utils.StageHelper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * @brief Controller relativo alla gestione dei Prestiti.
 * * Questa classe gestisce l'interfaccia grafica GestPrestiti relativa alla registrazione e amministrazione
 * dei prestiti attivi tra utenti e libri nell'archivio della biblioteca.
 * Le funzionalità principali includono:
 * - Visualizzazione tabellare e ordinamento dei prestiti attivi.
 * - Registrazione di un nuovo prestito.
 * - Eliminazione di un prestito attivo.
 * - Registrazione della restituzione di un libro con gestione di ritardi e sospensioni.
 * - Ricerca di prestiti in base a Utente (Matricola) e/o Libro (ISBN).
 * - Ritorno all'interfaccia "menu".
 * * Nella gestione di queste funzionalità il controller utilizza i metodi del modello @ref Biblioteca.
 * * @see GestPrestiti.fxml Interfaccia utente gestita da questo controller.
 * @see menu.fxml Interfaccia di Menu.
 */

public class GestionePrestitiController {
    @FXML
    private TextField matricolaTxt;
    @FXML
    private TextField ISBNText;
    @FXML
    private DatePicker dataInizioPicker;
    @FXML
    private DatePicker dataRestituzionePicker;

    @FXML
    private TableView<Prestito> tabellaPrestiti;
    @FXML
    private TableColumn<Prestito, String> colonnaMatricola;
    @FXML
    private TableColumn<Prestito, String> colonnaISBN;
    @FXML
    private TableColumn<Prestito, LocalDate> colonnaDataInizio;
    @FXML
    private TableColumn<Prestito, LocalDate> colonnaDataRestituzione;


    /**
     * @brief Configura la tabella prestiti e inizializza lo stato del controller.
     * * Questo metodo viene chiamato automaticamente da JavaFX dopo il caricamento del file FXML.
     * Le sue funzionalità principali includono:
     * - Configurazione della tabella: associa le celle della @ref tabellaPrestiti alle Properties dei modelli
     * @ref Prestito, @ref Libro e @ref Utente tramite PropertyValueFactory e lambda expression.
     * - Formattazione Date: configura le colonne di data (@ref colonnaDataInizio e @ref colonnaDataRestituzione)
     * per visualizzare il formato "gg/MM/aaaa".
     * - Caricamento dei dati: imposta la @ref tabellaPrestiti utilizzando la lista osservabile
     * (ObservableList) dei prestiti attivi fornita dalla classe @ref Biblioteca.
     */
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

        tabellaPrestiti.setRowFactory(tv -> {
            return new TableRow<Prestito>() {
                @Override
                protected void updateItem(Prestito item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setStyle("");
                    } else if (item.verificaRitardo(0)) {
                        setStyle("-fx-background-color: #e53632;");
                    } else {
                        setStyle("");
                    }
                }
            };
        });
        tabellaPrestiti.setItems(Biblioteca.getListaPrestiti());

    }

    /**
     * @brief Ritorno all'interfaccia "menu".
     * * Questo metodo viene chiamato al click del tasto "Torna al Menu" di GestPrestiti.
     * Utilizza la classe @ref StageHelper per effettuare un cambio dalla scena corrente
     * a quella di {@link MenuController interfaccia Menu}.
     * * @see menu.fxml Interfaccia di Menu.
     */

    @FXML
    public void onMenu() {
        StageHelper.switchToNew((Stage) matricolaTxt.getScene().getWindow(), "menu", "Menu");
    }

    /**
     * @brief Registra l'aggiunta di un nuovo prestito.
     * * Questo metodo viene chiamato al click del tasto "Aggiungi" di GestPrestiti.
     * Effettua una serie di controlli incrociati sui dati inseriti e sui vincoli del prestito:
     * - Validazione Campi: controlla che le date siano selezionate e che la data di inizio non sia successiva a quella di restituzione.
     * - Ricerca Oggetti: cerca il @ref Libro tramite ISBN e l' @ref Utente tramite Matricola. Nel caso non li trovi lancia un'eccezione
     * IndexOutOfBoundsException e mostra un Alert.
     * - Controlla i vincoli di prestito:
     * - Se il @ref Libro non ha copie disponibili.
     * - Se l' @ref Utente ha già raggiunto il limite massimo di prestiti attivi.
     * - Se l' @ref Utente è attualmente sospeso.
     * - Registrazione: se tutti i controlli hanno esito positivo, registra il prestito chiamando il metodo `aggiungiPrestito()` di @ref Biblioteca.
     * - Aggiornamento: infine, aggiorna la @ref tabellaPrestiti e pulisce i campi di input.
     * * @throws IndexOutOfBoundsException Se l'ISBN o la Matricola inseriti non corrispondono a nessun record valido in archivio (gestita con Alert).
     */

    @FXML
    public void onAggiungi() {

        if (dataInizioPicker.getValue() == null || dataRestituzionePicker.getValue() == null) {
            new Alert(Alert.AlertType.ERROR, "Devi selezionare entrambe le date.").showAndWait();
            return;
        }

        if (dataInizioPicker.getValue().isAfter(dataRestituzionePicker.getValue())) {
            Dialog alert = new Alert(Alert.AlertType.ERROR, "La data di resitituzione non può essere prima di quella di inizio.");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.setResizable(true);
            alert.showAndWait();
            return;
        }
        Libro libro = null;
        try {
            libro = Biblioteca.cercaLibro(new Libro("", "", ISBNText.getText(), 0, 0)).get(0);
        } catch (IndexOutOfBoundsException e) {
            new Alert(Alert.AlertType.ERROR, "Nessun libro corrisponde all'ISBN inserito!").showAndWait();
            return;
        }
        Utente utente = null;
        try {
            utente = Biblioteca.cercaUtente(new Utente(matricolaTxt.getText(), "", "", "")).get(0);
        } catch (IndexOutOfBoundsException e) {
            new Alert(Alert.AlertType.ERROR, "Nessun utente corrisponde alla matricola inserita!").showAndWait();
            return;
        }
        if (!libro.haCopieDisponibili()) {
            new Alert(Alert.AlertType.ERROR, "Il libro non ha copie disponibili").showAndWait();
            return;
        }
        if (utente.verificaLimitePrestitiRaggiunto()) {
            new Alert(Alert.AlertType.ERROR, "L'utente ha già raggiunto il limite di prestiti attivi").showAndWait();
            return;
        }
        if (utente.isSospeso()) {
            new Alert(Alert.AlertType.ERROR, "L'utente è sospeso").showAndWait();
            return;
        }
        Biblioteca.aggiungiPrestito(new Prestito(libro.getUUID(), utente.getUUID(), dataInizioPicker.getValue(), dataRestituzionePicker.getValue()), true);
        tabellaPrestiti.setItems(Biblioteca.getListaPrestiti());

        matricolaTxt.clear();
        ISBNText.clear();
        dataInizioPicker.setValue(null);
        dataRestituzionePicker.setValue(null);
    }

    /**
     * @brief Elimina un prestito attivo dall'archivio (operazione di rimozione generica).
     * Questo metodo viene chiamato al click del tasto "Rimuovi" di GestPrestiti.
     * - Richiede la conferma all'utente tramite Alert di tipo CONFIRMATION.
     * - In caso di esito affermativo, chiama il metodo `rimuoviPrestito()` di @ref Biblioteca,
     * che si occupa di rimuovere il prestito selezionato e di aggiornare le copie disponibili del libro.
     * - Infine, aggiorna la @ref tabellaPrestiti.
     * * @note Questo metodo è destinato a rimuovere un prestito senza i controlli di ritardo che sono invece inclusi in {@link #onRestituisci() onRestituisci()}.
     */

    @FXML
    public void onRimuovi() {
        Prestito selezionato = tabellaPrestiti.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Sicuro di voler rimuovere il prestito selezionato?", ButtonType.YES, ButtonType.NO);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            Biblioteca.rimuoviPrestito(selezionato);
            tabellaPrestiti.setItems(Biblioteca.getListaPrestiti());
        }
    }

    /**
     * @brief Esegue una ricerca di prestiti in base ai criteri di filtro inseriti.
     * Questo metodo viene chiamato al click del tasto "Ricerca" di GestPrestiti.
     * - Ricerca Criteri: cerca gli UUID di @ref Libro (tramite ISBN) e @ref Utente (tramite Matricola)
     * solo se i relativi campi di testo non sono vuoti.
     * - Esecuzione Ricerca: richiama il metodo `cercaPrestito()` di @ref Biblioteca, passando gli UUID e le Date selezionate come criteri di filtro.
     * - Aggiornamento Tabella:
     * - Se la lista restituita è vuota, viene mostrato un Alert e la tabella viene ripristinata a tutti i prestiti attivi.
     * - Se la lista restituita non è vuota, la @ref tabellaPrestiti viene impostata su tale lista, visualizzando di conseguenza i soli risultati filtrati.
     * * @throws IndexOutOfBoundsException Se l'ISBN o la Matricola inseriti come criteri non esistono nell'archivio (gestita con Alert).
     */
    @FXML
    public void onRicerca() {
        Libro libro = null;
        if (!ISBNText.getText().equals("")) {
            try {
                libro = Biblioteca.cercaLibro(new Libro("", "", ISBNText.getText(), 0, 0)).get(0);
            } catch (IndexOutOfBoundsException e) {
                new Alert(Alert.AlertType.ERROR, "Nessun libro corrisponde all'ISBN inserito!").showAndWait();
                return;
            }
        }
        Utente utente = null;
        if (!matricolaTxt.getText().equals("")) {
            try {
                utente = Biblioteca.cercaUtente(new Utente(matricolaTxt.getText(), "", "", "")).get(0);
            } catch (IndexOutOfBoundsException e) {
                new Alert(Alert.AlertType.ERROR, "Nessun utente corrisponde alla matricola inserita!").showAndWait();
                return;
            }
        }
        ObservableList<Prestito> prestitiTrovati = Biblioteca.cercaPrestito(new Prestito(libro == null ? null : libro.getUUID(), utente == null ? null : utente.getUUID(), dataInizioPicker.getValue(), dataRestituzionePicker.getValue()));

        if (prestitiTrovati.isEmpty()) {
            tabellaPrestiti.setItems(Biblioteca.getListaPrestiti());
            new Alert(Alert.AlertType.INFORMATION, "Nessun prestito trovato corrispondente ai criteri inseriti.").showAndWait();
        } else {
            tabellaPrestiti.setItems(prestitiTrovati);
        }
    }

    /**
     * @brief Registra la restituzione di un libro con gestione dei ritardi.
     * Questo metodo viene chiamato per registrare la restituzione di un prestito selezionato in tabella.
     * - Controllo Selezione: richiede la selezione di un prestito tramite Alert in caso di campo vuoto.
     * - Gestione Ritardo: esegue il controllo del ritardo di restituzione sul prestito selezionato:
     * - Ritardo Grave: se il ritardo supera il valore configurato (`RITARDO_SOSPENSIONE_AUTOMATICA`), l' @ref Utente viene automaticamente sospeso
     * per il periodo configurato (`GIORNI_SOSPENSIONE`).
     * - Ritardo Lieve: se il ritardo supera il valore configurato (`RITARDO_SEGNALAZIONE`),
     * l' @ref Utente viene segnalato e il contatore segnalazioni viene aggiornato.
     * - Rimozione/Aggiornamento: indipendentemente dal ritardo (dopo aver applicato eventuali sanzioni), il prestito viene rimosso
     * tramite {@link Biblioteca#rimuoviPrestito(Prestito) rimuoviPrestito()} e la @ref tabellaPrestiti viene aggiornata.
     */

    @FXML
    public void onRestituisci() {
        Prestito selezionato = tabellaPrestiti.getSelectionModel().getSelectedItem();
        if (selezionato == null) {
            Alert a = new Alert(Alert.AlertType.ERROR, "Devi selezionare un prestito per poterlo restituire!");
            a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            a.showAndWait();
            return;
        }
        if (selezionato.verificaRitardo(Biblioteca.configurazione.getNumero("RITARDO_SOSPENSIONE_AUTOMATICA"))) {
            selezionato.getUtente().sospendi(Biblioteca.configurazione.getNumero("GIORNI_SOSPENSIONE"), true);
        } else if (selezionato.verificaRitardo(Biblioteca.configurazione.getNumero("RITARDO_SEGNALAZIONE"))) {
            new Alert(Alert.AlertType.INFORMATION, "L'utente è stato segnalato per il suo ritardo").showAndWait();
            selezionato.getUtente().incrementaSegnalazioni();
            selezionato.getUtente().controllaSegnalazioni(Biblioteca.configurazione.getNumero("MASSIME_SEGNALAZIONI"), Biblioteca.configurazione.getNumero("GIORNI_SOSPENSIONE"));
        }
        Biblioteca.rimuoviPrestito(selezionato);
        tabellaPrestiti.setItems(Biblioteca.getListaPrestiti());
    }

    /**
     * @brief Ordinamento dei prestiti per ISBN del libro.
     * Questo metodo viene chiamato al click dell'opzione "ISBN" nel menù a tendina di Gestprestiti.
     * Richiama il metodo {@link Biblioteca#ordinaPrestitiISBN(ObservableList) ordinaPrestitiISBN()} di @ref Biblioteca
     * per visualizzare i prestiti in ordine crescente di ISBN.
     */
    @FXML
    public void onOrdinaISBN() {
        tabellaPrestiti.setItems(Biblioteca.ordinaPrestitiISBN(tabellaPrestiti.getItems()));
    }

    /**
     * @brief Ordinamento dei prestiti per Matricola dell'utente.
     * Questo metodo viene chiamato al click dell'opzione "Matricola" nel menù a tendina.
     * Richiama il metodo {@link Biblioteca#ordinaPrestitiMatricola(ObservableList) ordinaPrestitiMatricola()} di @ref Biblioteca
     * per visualizzare i prestiti in ordine crescente di Matricola.
     */

    @FXML
    public void onOrdinaMatricola() {
        tabellaPrestiti.setItems(Biblioteca.ordinaPrestitiMatricola(tabellaPrestiti.getItems()));
    }

    /**
     * @brief Ordinamento dei prestiti per Data di Inizio.
     * Questo metodo viene chiamato al click dell'opzione "Data Inizio" nel menù a tendina.
     * Richiama il metodo {@link Biblioteca#ordinaPrestitiDataInizio(ObservableList) ordinaPrestitiDataInizio()} di @ref Biblioteca
     * per visualizzare i prestiti in ordine cronologico di data di inizio.
     */
    @FXML
    public void onOrdinaDataInizio() {
        tabellaPrestiti.setItems(Biblioteca.ordinaPrestitiDataInizio(tabellaPrestiti.getItems()));
    }

    /**
     * @brief Ordinamento dei prestiti per Data di Restituzione Prevista.
     * Questo metodo viene chiamato al click dell'opzione "Data Restituzione Prevista" nel menù a tendina.
     * Richiama il metodo {@link Biblioteca#ordinaPrestitiDataRestituzionePrevista(ObservableList) ordinaPrestitiDataRestituzionePrevista()} di @ref Biblioteca
     * per visualizzare i prestiti in ordine cronologico di scadenza.
     */
    @FXML
    public void onOrdinaDataRestituzionePrevista() {
        tabellaPrestiti.setItems(Biblioteca.ordinaPrestitiDataRestituzionePrevista(tabellaPrestiti.getItems()));
    }

}
