/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bibliotecauniversitaria.controllers.login;


import bibliotecauniversitaria.utils.StageHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;


/**
 *
 * @author Nunzia
 */
public class MenuController {
    @FXML
    private Button logoutBtn;
    
    @FXML
    public void onGestioneLibri(){
        StageHelper.switchToNew((Stage)logoutBtn.getScene().getWindow(), "Gestlibri", "Schermata Di Gestione Libri");
    }
    
    @FXML
    public void onGestioneUtenti(){
        StageHelper.switchToNew((Stage)logoutBtn.getScene().getWindow(), "Gestutenti", "Schermata Di Gestione Utenti");
    }
    
    @FXML
    public void onGestionePrestiti(){
        StageHelper.switchToNew((Stage) logoutBtn.getScene().getWindow(), "Gestprestiti", "Schermata Di Gestione Prestiti");
    }
    
    @FXML
    public void onLogOut(){
        StageHelper.switchToNew((Stage) logoutBtn.getScene().getWindow(),"schermatalogin","Schermata d'accesso");
    }
}
