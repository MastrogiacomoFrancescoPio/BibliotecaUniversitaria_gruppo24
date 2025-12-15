/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bibliotecauniversitaria.utils;

import bibliotecauniversitaria.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StageHelper {


    public static FXMLLoader switchToNew(Stage stage, String name, String title){
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(name+".fxml"));
        try {
            if(stage.getScene()==null) {
                stage.setScene(new Scene(loader.load()));
            } else {
                stage.getScene().setRoot(loader.load());
            }
            stage.setTitle(title);
        } catch (IOException ex) {
            Logger.getLogger(StageHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return loader;
    }

}
