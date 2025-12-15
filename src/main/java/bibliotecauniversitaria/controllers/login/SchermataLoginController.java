package bibliotecauniversitaria.controllers.login;

import bibliotecauniversitaria.models.UserArchive;
import bibliotecauniversitaria.utils.StageHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public class SchermataLoginController {

    private boolean nuovoAvvio = false;

    @FXML
    private Button button;

    @FXML
    public void initialize() throws IOException {
        nuovoAvvio=!UserArchive.exists(UserArchive.NAME);
        button.setText(nuovoAvvio?"REGISTRATI":"LOGIN");
    }

    @FXML
    protected void onLoginButtonPressed() {

        StageHelper.switchToNew((Stage)button.getScene().getWindow(),nuovoAvvio?"registrazione":"login","Schermata di "+(nuovoAvvio?"registrazione":"login"));
        
        
    }
}
