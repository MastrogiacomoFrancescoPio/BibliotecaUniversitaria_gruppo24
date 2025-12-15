package bibliotecauniversitaria.controllers.login;

import bibliotecauniversitaria.models.UserArchive;
import bibliotecauniversitaria.utils.Email;
import bibliotecauniversitaria.utils.PasswordHelper;
import bibliotecauniversitaria.utils.StageHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @brief Controller per la schermata di Accesso.
 * <p>
 * Questa classe gestisce la logica di accesso dell'utente, inclusa la validazione
 * delle credenziali e la gestione di funzionalità  come la
 * visualizzazione della password e il recupero password.
 * <p>
 * Le funzionalità principali includono:
 * - Caricamento dei dati utente da archivio (@ref UserArchive).
 * - Login tramite confronto della password.
 * - Gestione del recupero password tramite invio di codice via email.
 * - Controllo per la visualizzazione/nascondimento della password.
 * @see schermatalogin.fxml Interfaccia utente gestita da questo controller.
 */
public class LoginController {

    public PasswordField passwordField;
    public TextField passwordTextField;
    public CheckBox mostraPassword;

    @FXML
    private UserArchive user;

    /**
     * @brief Inizializza il controller dopo il caricamento del file FXML.
     * <p>
     * Le sue funzionalità principali includono:
     * - Caricamento dell'Archivio Utente: tenta di caricare i dati utente presistenti da @ref UserArchive.
     * - Prima esecuzione: in caso di errore di caricamento (prima esecuzione o assenza di dati),
     * reindirizza l'utente alla {@link RegistrazioneController schermata di registrazione}.
     * - Binding:effettua il binding bidirezionale tra i campi `passwordField` (nascosto) e
     * `passwordTextField` (visibile), in modo che l'input sia sincronizzato.
     */

    @FXML
    protected void initialize() {
        try {
            user = UserArchive.loadFrom(UserArchive.NAME);
        } catch (IOException e) {
            StageHelper.switchToNew((Stage) mostraPassword.getScene().getWindow(), "registrazione", "Schermata di registrazione");
        }
        passwordField.textProperty().bindBidirectional(passwordTextField.textProperty());
    }

    /**
     * @brief Gestisce l'interazione per visualizzare/nascondere la password.
     * <p>
     * Questo metodo viene chiamato al click sul CheckBox (@ref mostraPassword):
     * - Visualizzazione: se il CheckBox è selezionato, rende visibile {@link #passwordTextField passwordTextField}
     * e nasconde {@link #passwordField passwordField}.
     * - Nascondimento: se il CheckBox non è selezionato, nasconde {@link #passwordTextField passwordTextField}
     * e rende visibile {@link #passwordField passwordField}.
     */

    @FXML
    protected void onPasswordShowButton() {
        passwordTextField.setVisible(mostraPassword.isSelected());
        passwordField.setVisible(!mostraPassword.isSelected());

    }

    /**
     * @brief Avvia il processo di recupero password.
     * <p>
     * Questo metodo viene chiamato al click sul pulsante o link "Password Dimenticata":
     * - Configurazione Email: controlla se il servizio SMTP (tramite @ref Email) è configurato: se non lo è, mostra un Alert di Warning e interrompe.
     * - Invio Codice: chiama il metodo `mandaReset()` di @ref Email per inviare un codice di verifica all'email dell'utente registrato.
     * - Cambio d'interfaccia: se l'invio ha successo, effettua un cambio di scena tramite @ref StageHelper dalla scena corrente a
     * {@link InserireCodiceController Inserisci codice} e passa il codice generato al controller di destinazione.
     *
     */

    @FXML
    protected void onPasswordDimenticata() {
        if (!Email.isConfigurato()) {
            new Alert(Alert.AlertType.WARNING, "Servizio SMTP non configurato.").showAndWait();
            return;
        }
        String codice = Email.mandaReset(user.email);
        if (!codice.equals("")) {
            FXMLLoader loader = StageHelper.switchToNew((Stage) mostraPassword.getScene().getWindow(), "inseriscicodice", "Inserisci codice");
            ((InserireCodiceController) loader.getController()).codice = codice;
        }
    }

    /**
     * @brief Esegue il tentativo di accesso (login).
     * <p>
     * Questo metodo viene chiamato al click del pulsante "Login":
     * - Validazione dei campi: verifica che il campo password non sia vuoto, mostrando un Alert in caso affermativo.
     * - Verifica credenziali: utilizza {@link PasswordHelper#checkpw(String, String) checkpw()} per confrontare la password inserita con l'hash salvato nell'archivio.
     * - Successo: in caso di corrispondenza, effettua il cambio di scena verso l'{@link MenuController interfaccia Menu}.
     * - Fallimento: se le password non corrispondono, mostra un Alert di tipo ERROR ("Password errata!").
     */
    @FXML
    protected void onLogin() {
        if (passwordField.getText().equals("")) {
            new Alert(Alert.AlertType.ERROR, "La password non può essere vuota").showAndWait();
            return;
        }
        if (PasswordHelper.checkpw(user.hashedPassword, passwordField.textProperty().get())) {
            StageHelper.switchToNew((Stage) passwordField.getScene().getWindow(), "menu", "Menu");
        } else {
            new Alert(Alert.AlertType.ERROR, "Password errata!").showAndWait();
        }
    }

}
