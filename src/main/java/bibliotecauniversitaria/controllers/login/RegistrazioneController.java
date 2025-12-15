/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bibliotecauniversitaria.controllers.login;

import bibliotecauniversitaria.models.UserArchive;
import bibliotecauniversitaria.utils.Email;
import bibliotecauniversitaria.utils.PasswordHelper;
import bibliotecauniversitaria.utils.StageHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @brief Controller per la schermata di Registrazione o Reimpostazione Credenziali.
 * <p>
 * Questa classe gestisce la creazione iniziale dell'utente amministratore o la reimpostazione
 * delle sue credenziali (email e password).
 * @see registrazione.fxml Interfaccia utente gestita da questo controller.
 */
public class RegistrazioneController {
    public TextField email;
    public Text registrazioneFld;
    public PasswordField passwordField;
    public TextField passwordTextField;
    public PasswordField passwordConfermaField;
    public TextField passwordConfermaTextField;
    public CheckBox mostraPassword;

    public UserArchive user = null;

    /**
     * @brief Inizializza il controller e verifica lo stato dell'utente.
     * <p>
     * - Verifica se l'utente esiste: tenta di caricare i dati utente .
     * -se un utente esiste, imposta il testo del campo (`registrazioneFld`) su "REIMPOSTA UTENTE"
     * e precompila l'email; altrimenti, rimane in modalità "REGISTRAZIONE".
     * - Binding: effettua il binding bidirezionale per i campi password.
     */

    @FXML
    protected void initialize() {
        try {
            user = UserArchive.loadFrom(UserArchive.NAME);
            registrazioneFld.setText("REIMPOSTA UTENTE");
        } catch (IOException e) {

        }
        passwordField.textProperty().bindBidirectional(passwordTextField.textProperty());
        passwordConfermaField.textProperty().bindBidirectional(passwordConfermaTextField.textProperty());
        if (user != null) email.setText(user.email);
    }

    /**
     * @brief Gestisce la registrazione o la reimpostazione delle credenziali.
     * <p>
     * Questo metodo viene chiamato al click del pulsante "Registra" (o "Reimposta"):
     * - Validazione dei campi: controlla la validità dell'email e verifica che tutti i campi password siano pieni.
     * - Conferma Password: verifica che le due password inserite corrispondano.
     * - Creazione/salvataggio: crea un nuovo oggetto @ref UserArchive, cripta la password e salva l'archivio.
     * - Cambio di interfaccia: in caso di successo, reindirizza l'utente alla {@link LoginController schermata di accesso}.
     */

    @FXML
    protected void onRegister() {
        if (email.getText().equals("") || !email.getText().contains("@") || !Email.isValida(email.getText())) {
            new Alert(Alert.AlertType.ERROR, "La email non è valida.").showAndWait();
            return;
        }
        if (passwordField.getText().equals("")) {
            new Alert(Alert.AlertType.ERROR, "La password non può essere vuota.").showAndWait();
            return;
        }
        if (passwordConfermaField.getText().equals("")) {
            new Alert(Alert.AlertType.ERROR, "La conferma password non può essere vuota.").showAndWait();
            return;
        }
        if (!passwordField.getText().equals(passwordConfermaField.getText())) {
            new Alert(Alert.AlertType.ERROR, "Le password non combaciano.").showAndWait();
            return;
        }
        UserArchive user = new UserArchive(PasswordHelper.encrypt(passwordField.getText()), email.getText());
        try {
            user.saveTo(UserArchive.NAME);
            StageHelper.switchToNew((Stage) email.getScene().getWindow(), "schermatalogin", "Schermata di accesso");
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Impossibile salvare l'utente! (" + e.getMessage() + ")").showAndWait();
        }
    }

    /**
     * @brief Gestisce l'interazione per visualizzare/nascondere le password.
     * <p>
     * Questo metodo viene chiamato in risposta al click sul CheckBox "Mostra Password":
     * - alterna la visibilità tra i campi `PasswordField` e `TextField` per entrambe le password.
     */

    @FXML
    protected void onPasswordShowButton() {
        passwordTextField.setVisible(mostraPassword.isSelected());
        passwordField.setVisible(!mostraPassword.isSelected());
        passwordConfermaTextField.setVisible(mostraPassword.isSelected());
        passwordConfermaField.setVisible(!mostraPassword.isSelected());

    }
}
