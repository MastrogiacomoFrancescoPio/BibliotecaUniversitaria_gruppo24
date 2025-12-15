package bibliotecauniversitaria.controllers.login;

import bibliotecauniversitaria.models.UserArchive;
import bibliotecauniversitaria.utils.StageHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * @brief Controller per la schermata iniziale di selezione Login/Registrazione.
 *
 * Questa classe è un'interfaccia di benvenuto. Il suo scopo principale è determinare
 * se l'applicazione è al primo avvio e reindirizzare l'utente alla schermata di Registrazione o di Login.
 *
 * @see schermatalogin.fxml Interfaccia utente gestita da questo controller.
 */
public class SchermataLoginController {

    private boolean nuovoAvvio = false;

    @FXML
    private Button button;
    
    /**
     * @brief Inizializza il controller e determina lo stato dell'archivio utente.
     *
     * - controlla se esiste un archivio utente (@ref UserArchive) salvato in precedenza.
     * - imposta il flag `nuovoAvvio` a true se l'archivio non esiste.
     * - aggiorna il testo del pulsante (`button`) in base allo stato: "REGISTRATI" se è il primo avvio, "LOGIN" altrimenti.
     *
     * @throws IOException se si verifica un errore durante la verifica dell'archivio (non gestito internamente).
     */

    @FXML
    public void initialize() throws IOException {
        nuovoAvvio=!UserArchive.exists(UserArchive.NAME);
        button.setText(nuovoAvvio?"REGISTRATI":"LOGIN");
    }
    
    /**
     * @brief Gestisce il click sul pulsante "Login" / "Registrati".
     *
     * Questo metodo viene chiamato al click del pulsante nell'interfaccia. 
     * Il cambio di interfaccia è condizionatodal flag `nuovoAvvio`:
     * - True: reindirizza l'utente alla {@link RegistrazioneController schermata di registrazione}.
     * - False: reindirizza l'utente alla {@link LoginController schermata di login}.
     */

    @FXML
    protected void onLoginButtonPressed() {

        StageHelper.switchToNew((Stage)button.getScene().getWindow(),nuovoAvvio?"registrazione":"login","Schermata di "+(nuovoAvvio?"registrazione":"login"));
        
        
    }
}
