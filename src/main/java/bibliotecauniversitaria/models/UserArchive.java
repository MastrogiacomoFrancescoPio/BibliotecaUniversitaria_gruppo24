/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bibliotecauniversitaria.models;

import java.io.*;

/**
 * @class UserArchive
 * @brief Classe per la gestione dell'archiviazione delle credenziali utente.
 * Questa classe viene utilizzata per serializzare e deserializzare le informazioni
 * di accesso (email e password hashata) su file. È utile per mantenere la persistenza
 * dell'utente loggato o delle credenziali di amministratore locale.
 */
public class UserArchive implements Serializable {

    public String hashedPassword;
    public String email;

    public static String NAME = new File(Archivio.cartellaData, "user.bbl").getPath();

    /**
     * @param hashedPassword La stringa contenente l'hash della password.
     * @param email          L'indirizzo email dell'utente.
     * @brief Costruttore della classe UserArchive.
     * Inizializza un oggetto archivio con le credenziali fornite.
     * @post L'oggetto è inizializzato in memoria (non ancora salvato su disco).
     */
    public UserArchive(String hashedPassword, String email) {
        this.hashedPassword = hashedPassword;
        this.email = email;
    }

    /**
     * @param path Il percorso del file da verificare.
     * @return true Se il file esiste e può essere deserializzato correttamente. false altrimenti.
     * @brief Verifica l'esistenza e la validità di un file di archivio.
     * Tenta di caricare il file dal percorso specificato per verificare se esiste
     * ed è un oggetto UserArchive valido.
     */
    public static Boolean exists(String path) {
        try {
            UserArchive.loadFrom(path);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * @param path Il percorso del file di destinazione.
     * @throws IOException Se si verifica un errore durante la scrittura del file.
     * @brief Serializza l'oggetto corrente su file.
     * Scrive l'istanza corrente di UserArchive nel percorso specificato utilizzando ObjectOutputStream.
     * @pre Il percorso specificato deve essere scrivibile.
     * @post Viene creato o sovrascritto un file contenente l'oggetto serializzato.
     */
    public void saveTo(String path) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(path)))) {
            oos.writeObject(this);
        }
    }

    /**
     * @param path Il percorso del file da leggere.
     * @return L'oggetto UserArchive caricato se l'operazione ha successo,
     * null Se la classe serializzata non viene trovata (ClassNotFoundException)..
     * @throws IOException Se si verifica un errore di lettura del file (es. file non trovato).
     * @brief Deserializza un oggetto UserArchive da file.
     * Legge un file dal percorso specificato e tenta di convertirlo in un'istanza di UserArchive.
     * @pre Il file deve esistere al percorso specificato.
     */
    public static UserArchive loadFrom(String path) throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(path)))) {
            return (UserArchive) ois.readObject();
        } catch (ClassNotFoundException e) {
            System.out.println("Serialized file error.");
            return null;
        }
    }
}
