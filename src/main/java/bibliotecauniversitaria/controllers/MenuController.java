
package bibliotecauniversitaria.controllers;


import bibliotecauniversitaria.utils.StageHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * @brief Controller per l'interfaccia Menu.
 * Questa classe gestisce i pulsanti e la navigazione dell'interfaccia Menu.
 * Le sue funzionalità principali consistono nell'instradare l'utente verso le varie
 * schermate di gestione (Libri, Utenti, Prestiti) o nella schermata di Logout/Login.
 * * @see menu.fxml Interfaccia utente gestita da questo controller.
 */

public class MenuController {
    @FXML
    private Button logoutBtn;
    
    /**
     * @brief Gestisce la navigazione alla schermata di gestione dei Libri.
     * Questo metodo viene chiamato al click del pulsante "Gestione Libri" nell'interfaccia Menu.
     * Utilizza la classe @ref StageHelper per effettuare il cambio di scena verso l'interfaccia 
     * di {@link GestioneLibriController gestione Libri}.
     * @see Gestlibri.fxml Interfaccia di gestione libri.
     */
    
    @FXML
    public void onGestioneLibri(){
        StageHelper.switchToNew((Stage)logoutBtn.getScene().getWindow(), "Gestlibri", "Schermata Di Gestione Libri");
    }
    
    /**
     * @brief Gestisce la navigazione alla schermata di gestione degli Utenti.
     * Questo metodo viene chiamato al click del pulsante "Gestione Utenti" nell'interfaccia Menu.
     * Utilizza la classe @ref StageHelper per effettuare il cambio di scena verso l'interfaccia 
     * di {@link GestioneUtentiController gestione Utenti}.
     * @see Gestutenti.fxml Interfaccia di gestione utenti.
     */
    @FXML
    public void onGestioneUtenti(){
        StageHelper.switchToNew((Stage)logoutBtn.getScene().getWindow(), "Gestutenti", "Schermata Di Gestione Utenti");
    }
    
    /**
     * @brief Gestisce la navigazione alla schermata di gestione dei Prestiti.
     * Questo metodo viene chiamato al click del pulsante "Gestione Prestiti" nell'interfaccia Menu.
     * Utilizza la classe @ref StageHelper per effettuare il cambio di scena verso l'interfaccia 
     * di {@link GestionePrestitiController gestione Prestiti}.
     * @see Gestprestiti.fxml Interfaccia di gestione prestiti.
     */
    @FXML
    public void onGestionePrestiti(){
        StageHelper.switchToNew((Stage) logoutBtn.getScene().getWindow(), "Gestprestiti", "Schermata Di Gestione Prestiti");
    }
    
    /**
     * @brief Esegue il logout e torna alla schermata di accesso.
     * Questo metodo viene chiamato al click del pulsante "Logout" nell'interfaccia Menu.
     * Utilizza la classe @ref StageHelper per effettuare il cambio di scena verso l'interfaccia 
     * di {@link SchermataLoginController schermata login}.
     * * @see schermatalogin.fxml Interfaccia di Login.
     */
    @FXML
    public void onLogOut(){
        StageHelper.switchToNew((Stage) logoutBtn.getScene().getWindow(),"schermatalogin","Schermata d'accesso");
    }
}
