/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bibliotecauniversitaria.utils;

import bibliotecauniversitaria.Main;
import bibliotecauniversitaria.models.Archivio;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

/**
 *
 * @author franc
 */
public class Configurazione {

    public static File configurazioneFile = new File(Archivio.cartellaData,"configurazione");

    public HashMap<String, String> valori = new HashMap<>();

    public void carica(File configurazioneFile) throws IOException {
        try(BufferedReader br = new BufferedReader(new FileReader(configurazioneFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(line.startsWith("#")) continue;
                if(!line.contains("=")) continue;
                String chiave = line.split("=")[0];
                String valore = line.split("=")[1];
                valori.put(chiave, valore);
            }
        }
    }

    public void salvaDefault(File file){
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(Main.class.getResourceAsStream("config/configurazione")));
             FileWriter fileOut = new FileWriter(file)) {

            String line;
            while ((line = reader.readLine()) != null) {
                fileOut.write(line + System.lineSeparator());
            }
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Impossibile creare la configurazione!").showAndWait();
        }
    }

    public String get(String chiave) {
        return valori.get(chiave);
    }

    public int getNumero(String chiave) {
        int n = 0;
        try {
            n = Integer.parseInt(valori.get(chiave));
        } catch (NumberFormatException e) {
            Alert a = new Alert(Alert.AlertType.ERROR, "Nella configurazione, il valore della chiave " + chiave + " deve essere un numero!");
            a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            a.showAndWait();
        }
        return n;
    }

}

