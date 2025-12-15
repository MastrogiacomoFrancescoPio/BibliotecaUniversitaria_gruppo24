package bibliotecauniversitaria.models;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * @class Prestito
 * @brief Rappresenta un prestito di un libro a un utente.
 * Questa classe collega un libro e un utente tramite i loro UUID e gestisce
 * le date relative al periodo di prestito (inizio e restituzione prevista).
 * Implementa Serializable con gestione personalizzata per le property JavaFX.
 */
public class Prestito implements Serializable {

    
    private UUID libro;
    private UUID utente;

    private transient ObjectProperty<LocalDate> dataInizio;
    private transient ObjectProperty<LocalDate> dataRestituzionePrevista;

    private transient UUID uuid = UUID.randomUUID();

    /**
     * @brief Costruttore della classe Prestito.
     * Crea una nuova istanza di prestito associando un libro a un utente con date specifiche.
     * @post Viene creato un nuovo oggetto Prestito con un UUID univoco generato automaticamente.
     * @param libro UUID del libro oggetto del prestito.
     * @param utente UUID dell'utente che richiede il prestito.
     * @param dataInizio La data in cui inizia il prestito.
     * @param dataRestituzionePrevista La data entro cui il libro deve essere restituito.
     */    
    public Prestito(UUID libro, UUID utente, LocalDate dataInizio, LocalDate dataRestituzionePrevista) {
        this.libro = libro;
        this.utente = utente;
        this.dataInizio = new SimpleObjectProperty<>(dataInizio);
        this.dataRestituzionePrevista = new SimpleObjectProperty<>(dataRestituzionePrevista);
    }
    /**
     * @brief Recupera l'oggetto Libro associato al prestito.
     * Utilizza l'UUID memorizzato per cercare l'istanza del libro nella lista globale della Biblioteca.
     * @return L'oggetto Libro corrispondente all'UUID, o null se non trovato.
     */
    public Libro getLibro() {
        return Biblioteca.ottieniLibroDaID(libro);
    }
    
    /**
     * @brief Imposta l'UUID del libro in prestito.
     * @param libro Il nuovo UUID del libro.
     */
    public void setLibro(UUID libro) {
        this.libro = libro;
    }
    
    
    /**
     * @brief Restituisce l'identificativo univoco del prestito.
     * @return L'UUID del prestito.
     */
    public UUID getUUID() {
        return uuid;
    }

    /**
     * @brief Recupera l'oggetto Utente associato al prestito.
     * Utilizza l'UUID memorizzato per cercare l'istanza dell'utente nella lista globale della Biblioteca.
     * @return L'oggetto Utente corrispondente all'UUID, o null se non trovato.
     */
    public Utente getUtente() {
        return Biblioteca.ottieniUtenteDaID(utente);
    }
    
    /**
     * @brief Imposta l'UUID dell'utente che ha effettuato il prestito.
     * @param utente Il nuovo UUID dell'utente.
     */
    public void setUtente(UUID utente) {
        this.utente = utente;
    }

    /**
     * @brief Restituisce la data di inizio del prestito.
     * @return Un oggetto LocalDate rappresentante la data di inizio.
     */
    public LocalDate getDataInizio() {
        return dataInizio.get();
    }

     /**
     * @brief Imposta la data di inizio del prestito.
     * @param dataInizio La nuova data di inizio.
     */
    public void setDataInizio(LocalDate dataInizio) {
        this.dataInizio.set(dataInizio);
    }

     /**
     * @brief Restituisce la property JavaFX relativa alla data di inizio.
     * @return L'oggetto ObjectProperty contenente la LocalDate di inizio.
     */
    public ObjectProperty<LocalDate> dataInizioProperty() {
        return dataInizio;
    }

      /**
     * @brief Restituisce la data prevista per la restituzione.
     * @return Un oggetto LocalDate rappresentante la scadenza del prestito.
     */
    public LocalDate getDataRestituzionePrevista() {
        return dataRestituzionePrevista.get();
    }

     /**
     * @brief Imposta la data prevista per la restituzione.
     * 
     * @param dataRestituzionePrevista La nuova data di scadenza.
     */
    public void setDataRestituzionePrevista(LocalDate dataRestituzionePrevista) {
        this.dataRestituzionePrevista.set(dataRestituzionePrevista);
    }

    /**
     * @brief Restituisce la property JavaFX relativa alla data di restituzione prevista.
     * @return L'oggetto ObjectProperty contenente la LocalDate di scadenza.
     */
    public ObjectProperty<LocalDate> dataRestituzionePrevistaProperty() {
        return dataRestituzionePrevista;
    }

    /**
     * @brief Calcola i giorni di ritardo rispetto a una data specifica.
     * Calcola la differenza in giorni tra la data di restituzione prevista e la data fornita.
     * @param data La data di riferimento (es. la data odierna o la data di restituzione effettiva).
     * @return Un intero rappresentante i giorni di differenza. Un valore positivo indica un ritardo.
     */
    public int calcolaRitardo(LocalDate data) {
        return (int) ChronoUnit.DAYS.between(getDataRestituzionePrevista(), data);
    }

     /**
     * @brief Verifica se il prestito è in ritardo oltre una certa soglia.
     * Confronta la data odierna con la data di restituzione prevista.
     * @param giorni Il numero di giorni di tolleranza o soglia per il ritardo.
     * @return true Se il ritardo attuale supera i giorni specificati,
     *   false Se non c'è ritardo o il ritardo è entro la soglia.
     */
    public boolean verificaRitardo(int giorni) {
        return calcolaRitardo(LocalDate.now()) > giorni;
    }

     /**
     * @brief Metodo privato per la serializzazione personalizzata dell'oggetto.
     * Scrive i dati dell'oggetto sullo stream, convertendo le property JavaFX (non serializzabili)
     * nei loro valori primitivi/base.
     * @param oos Lo stream di output su cui scrivere.
     * @throws IOException In caso di errori di I/O.
     */
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeUTF(uuid.toString());
        oos.writeObject(dataInizio.get());
        oos.writeObject(dataRestituzionePrevista.get());
    }

     /**
     * @brief Metodo privato per la deserializzazione personalizzata dell'oggetto.
     * Legge i dati dallo stream e ricostruisce le property JavaFX a partire dai valori salvati.
     * @param ois Lo stream di input da cui leggere.
     * @throws IOException In caso di errori di I/O.
     * @throws ClassNotFoundException Se la classe serializzata non viene trovata.
     */
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        this.uuid = UUID.fromString(ois.readUTF());

        LocalDate dataInizioVal = (LocalDate) ois.readObject();
        LocalDate dataRestituzioneVal = (LocalDate) ois.readObject();

        this.dataInizio = new SimpleObjectProperty<>(dataInizioVal);
        this.dataRestituzionePrevista = new SimpleObjectProperty<>(dataRestituzioneVal);
    }
}
