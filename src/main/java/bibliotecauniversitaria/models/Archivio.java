package bibliotecauniversitaria.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @class Archivio
 * @brief Classe di utilità per la gestione della persistenza dei dati.
 * Questa classe gestisce il salvataggio (serializzazione) e il caricamento (deserializzazione)
 * delle liste di oggetti (Libri, Utenti, Prestiti) su file binari.
 */
public class Archivio {

    public static File cartellaData = new File("dataBiblioteca");

    public static File fileLibri=new File(cartellaData,"archivioLibri.bbl");
    public static File fileUtenti=new File(cartellaData,"archivioUtenti.bbl");
    public static File filePrestiti=new File(cartellaData,"archivioPrestiti.bbl");
    
    /**
     * @brief Carica una lista di oggetti da un file binario.
     * Metodo generico che tenta di leggere un ArrayList serializzato da file 
     * e lo converte in una ObservableList per l'uso con JavaFX.
     * @tparam T Il tipo di oggetti contenuti nella lista (Libro, Utente, Prestito).
     * @param nomeFile Il file da cui leggere i dati.
     * @return Una ObservableList contenente gli oggetti letti, oppure
     * una lista vuota se il file non esiste, è vuoto o si verifica un errore di lettura/casting.
     */
    public static <T> ObservableList<T> carica(File nomeFile) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(nomeFile))) {
            return FXCollections.observableArrayList((ArrayList<T>)ois.readObject());
        } catch (IOException | ClassNotFoundException ex) {
            return FXCollections.observableArrayList();
        }
    }

     /**
     * @brief Scrive una lista di oggetti su un file binario.
     * Metodo generico che converte la ObservableList in un ArrayList serializzabile
     * e lo scrive sul file specificato.
     * @tparam T Il tipo di oggetti contenuti nella lista.
     * @pre Il percorso del file deve essere valido e scrivibile.
     * @post Il contenuto precedente del file viene sovrascritto con la nuova lista.
     * @param lista La lista di oggetti da salvare.
     * @param file Il file di destinazione.
     */
    public static <T> void scrivi(ObservableList<T> lista, File file){
        try (ObjectOutputStream oos= new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(new ArrayList<>(lista));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}