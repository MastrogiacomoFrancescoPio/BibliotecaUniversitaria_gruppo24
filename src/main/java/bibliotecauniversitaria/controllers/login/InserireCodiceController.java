
package bibliotecauniversitaria.controllers.login;

import bibliotecauniversitaria.utils.StageHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * @brief Controller per l'interfaccia di inserimento del codice di verifica.
 * Questa classe gestisce la schermata intermedia di recupero password
 * o cambio credenziali, dove l'utente deve inserire un codice di verifica arrivatogli via mail.
 * Il controller è responsabile della validazione del codice inserito e della navigazione successiva.
 * * @see inserirecodice.fxml Interfaccia utente gestita da questo controller.
 */
public class InserireCodiceController {

    public TextField codiceFld;

    public String codice;

    /**
     * @brief Gestisce la conferma del codice di verifica.
     * Questo metodo viene chiamato al click del pulsante "Conferma" nella schermata di inserimento del codice.
     * Le sue funzionalità principali includono:
     * - Validazione del codice: verifica che il valore inserito nel campo `codiceFld` corrisponda al codice (`codice`)
     * atteso (precedentemente generato e assegnato).
     * - Gestione di errore: se il codice non corrisponde, viene mostrato un Alert di tipo ERROR all'utente.
     * - Navigazione: se la validazione ha successo, il metodo utilizza la classe @ref StageHelper per effettuare
     * il cambio di scena verso l'interfaccia di {@link RegistrazioneController modifica credenziali} (destinata al cambio password).
     * * @see registrazione.fxml Interfaccia di destinazione (cambio password).
     */
    @FXML
    protected void onConferma() {
        if (!codiceFld.getText().equals(codice)) {
            new Alert(Alert.AlertType.ERROR, "Codice errato!").showAndWait();
            return;
        }
        StageHelper.switchToNew((Stage) codiceFld.getScene().getWindow(), "registrazione", "Cambio password");
    }


}
