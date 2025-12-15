/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bibliotecauniversitaria.controllers.login;

import bibliotecauniversitaria.utils.StageHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author franc
 */
public class InserireCodiceController {

    public TextField codiceFld;

    public String codice;

    @FXML
    private Pane pane;

   

    @FXML
    protected void onConferma() {
        if(!codiceFld.getText().equals(codice)){
            new Alert(Alert.AlertType.ERROR, "Codice errato!").showAndWait();
            return;
        }
        StageHelper.switchToNew((Stage) codiceFld.getScene().getWindow(), "registrazione", "Cambio password");
    }


}
