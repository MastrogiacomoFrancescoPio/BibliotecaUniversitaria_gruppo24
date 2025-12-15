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
 /**
     * @brief Carica le impostazioni dal file specificato.
     * Legge il file riga per riga. Le righe che iniziano con '#' vengono ignorate.
     * Le righe contenenti '=' vengono splittate in chiave e valore e inserite nella mappa.
     * @pre Il file deve esistere ed essere leggibile.
     * @post La mappa 'valori' viene popolata con i dati letti.
     * @param configurazioneFile Il file da cui leggere la configurazione.
     * @throws IOException Se si verificano errori di lettura (I/O).
     */
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
 /**
     * @brief Crea il file di configurazione predefinito.
     * Copia il template di configurazione dalle risorse interne del jar (pacchetto 'config')
     * nella cartella dati dell'applicazione.
     * @post Viene creato un nuovo file su disco con le impostazioni di default.
     * @param file Il file di destinazione dove scrivere la configurazione default.
     */
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
/**
     * @brief Recupera il valore stringa associato a una chiave.
     * @param chiave La chiave di configurazione da cercare.
     * @return Il valore corrispondente come stringa, o null se la chiave non esiste.
     */
    public String get(String chiave) {
        return valori.get(chiave);
    }
/**
     * @brief Recupera un valore numerico associato a una chiave.
     * Tenta di convertire il valore stringa associato alla chiave in un intero.
     * Se la conversione fallisce, mostra un Alert di errore all'utente.
     * @param chiave La chiave di configurazione da cercare.
     * @return Il valore intero convertito. Restituisce 0 in caso di errore di formato o chiave inesistente.
     */
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

