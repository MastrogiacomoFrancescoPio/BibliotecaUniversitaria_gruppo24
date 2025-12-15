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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.scene.control.CheckBox;

/**
 *
 * @author ACER
 */
public class RegistrazioneController {
    public TextField email;
    public Text registrazioneFld;
    public PasswordField passwordField;
    public TextField passwordTextField;
    public PasswordField passwordConfermaField;
    public TextField passwordConfermaTextField;
    public CheckBox mostraPassword;
    
    public UserArchive user=null;

    @FXML
    protected void initialize() {
        try {
            user = UserArchive.loadFrom(UserArchive.NAME);
            registrazioneFld.setText("REIMPOSTA UTENTE");
        } catch (IOException e){
            
        }
        passwordField.textProperty().bindBidirectional(passwordTextField.textProperty());
        passwordConfermaField.textProperty().bindBidirectional(passwordConfermaTextField.textProperty());
        if(user!=null) email.setText(user.email);
    }


    @FXML
    protected void onRegister() {
        if(email.getText().equals("")||!email.getText().contains("@")||!Email.isValida(email.getText())) {
            new Alert(Alert.AlertType.ERROR, "La email non è valida.").showAndWait();
            return;
        }
        if(passwordField.getText().equals("")){
            new Alert(Alert.AlertType.ERROR, "La password non può essere vuota.").showAndWait();
            return;
        }
        if(passwordConfermaField.getText().equals("")){
            new Alert(Alert.AlertType.ERROR, "La conferma password non può essere vuota.").showAndWait();
            return;
        }
        if(!passwordField.getText().equals(passwordConfermaField.getText())) {
            new Alert(Alert.AlertType.ERROR, "Le password non combaciano.").showAndWait();
            return;
        }
        UserArchive user = new UserArchive(PasswordHelper.encrypt(passwordField.getText()),email.getText());
        try {
            user.saveTo(UserArchive.NAME);
            StageHelper.switchToNew((Stage) email.getScene().getWindow(), "schermatalogin", "Schermata di accesso");
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Impossibile salvare l'utente! ("+e.getMessage()+")").showAndWait();
        }
    }
    
    @FXML
    protected void onPasswordShowButton() {
        passwordTextField.setVisible(mostraPassword.isSelected());
        passwordField.setVisible(!mostraPassword.isSelected());
        passwordConfermaTextField.setVisible(mostraPassword.isSelected());
        passwordConfermaField.setVisible(!mostraPassword.isSelected());

    }
}
