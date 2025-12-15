/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bibliotecauniversitaria.utils;

import bibliotecauniversitaria.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StageHelper {

    /**
     * @param stage Lo Stage (finestra) su cui applicare il cambio di vista.
     * @param name  Il nome del file FXML da caricare (escluso l'estensione .fxml).
     * @param title Il titolo da assegnare alla finestra dopo il caricamento.
     * @return L'oggetto FXMLLoader utilizzato per caricare la vista (utile per recuperare il controller associato).
     * @brief Carica un file FXML e ne visualizza il contenuto nello stage specificato.
     * <p>
     * Questo metodo gestisce il "cambio pagina". Se lo stage ha già una scena impostata,
     * viene sostituita solo la radice (Root) della scena per mantenere le dimensioni della finestra.
     * Se non c'è una scena, ne viene creata una nuova.
     * @pre Il file FXML corrispondente al parametro 'name' deve esistere nel percorso delle risorse (relativo alla classe Main).
     * @post Il contenuto visualizzato nello stage viene aggiornato e il titolo della finestra modificato.
     */

    public static FXMLLoader switchToNew(Stage stage, String name, String title) {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(name + ".fxml"));
        try {
            if (stage.getScene() == null) {
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
