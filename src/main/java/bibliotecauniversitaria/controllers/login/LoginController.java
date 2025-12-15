package bibliotecauniversitaria.controllers.login;

import bibliotecauniversitaria.models.UserArchive;
import bibliotecauniversitaria.utils.Email;
import bibliotecauniversitaria.utils.PasswordHelper;
import bibliotecauniversitaria.utils.StageHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.control.CheckBox;

public class LoginController {

    public PasswordField passwordField;
    public TextField passwordTextField;
    public CheckBox mostraPassword;

    @FXML
    private UserArchive user;

    @FXML
    protected void initialize() {
        try {
            user = UserArchive.loadFrom(UserArchive.NAME);
        } catch (IOException e) {
            StageHelper.switchToNew((Stage) mostraPassword.getScene().getWindow(),"registrazione","Schermata di registrazione");
        }
        passwordField.textProperty().bindBidirectional(passwordTextField.textProperty());
    }

    @FXML
    protected void onPasswordShowButton() {
        passwordTextField.setVisible(mostraPassword.isSelected());
        passwordField.setVisible(!mostraPassword.isSelected());

    }

    @FXML
    protected void onPasswordDimenticata() {
        if(!Email.isConfigurato()) {
            new Alert(Alert.AlertType.WARNING, "Servizio SMTP non configurato.").showAndWait();
            return;
        }
        String codice = Email.mandaReset(user.email);
        if(!codice.equals("")){
            FXMLLoader loader = StageHelper.switchToNew((Stage)mostraPassword.getScene().getWindow(),"inseriscicodice","Inserisci codice");
            ((InserireCodiceController)loader.getController()).codice = codice;
        }
    }

    @FXML
    protected void onLogin() {
        if(passwordField.getText().equals("")){
            new Alert(Alert.AlertType.ERROR,"La password non può essere vuota").showAndWait();
            return;
        }
        if(PasswordHelper.checkpw(user.hashedPassword,passwordField.textProperty().get())){
            StageHelper.switchToNew((Stage) passwordField.getScene().getWindow(),"menu","Menu");
        } else {
            new Alert(Alert.AlertType.ERROR, "Password errata!").showAndWait();
        }
    }

}
