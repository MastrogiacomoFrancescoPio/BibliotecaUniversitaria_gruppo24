package bibliotecauniversitaria.models;


import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

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
     * @param libro                    UUID del libro oggetto del prestito.
     * @param utente                   UUID dell'utente che richiede il prestito.
     * @param dataInizio               La data in cui inizia il prestito.
     * @param dataRestituzionePrevista La data entro cui il libro deve essere restituito.
     * @brief Costruttore della classe Prestito.
     * Crea una nuova istanza di prestito associando un libro a un utente con date specifiche.
     * @post Viene creato un nuovo oggetto Prestito con un UUID univoco generato automaticamente.
     */
    public Prestito(UUID libro, UUID utente, LocalDate dataInizio, LocalDate dataRestituzionePrevista) {
        this.libro = libro;
        this.utente = utente;
        this.dataInizio = new SimpleObjectProperty<>(dataInizio);
        this.dataRestituzionePrevista = new SimpleObjectProperty<>(dataRestituzionePrevista);
    }

    /**
     * @return L'oggetto Libro corrispondente all'UUID, o null se non trovato.
     * @brief Recupera l'oggetto Libro associato al prestito.
     * Utilizza l'UUID memorizzato per cercare l'istanza del libro nella lista globale della Biblioteca.
     */
    public Libro getLibro() {
        return Biblioteca.ottieniLibroDaID(libro);
    }

    /**
     * @param libro Il nuovo UUID del libro.
     * @brief Imposta l'UUID del libro in prestito.
     */
    public void setLibro(UUID libro) {
        this.libro = libro;
    }


    /**
     * @return L'UUID del prestito.
     * @brief Restituisce l'identificativo univoco del prestito.
     */
    public UUID getUUID() {
        return uuid;
    }

    /**
     * @return L'oggetto Utente corrispondente all'UUID, o null se non trovato.
     * @brief Recupera l'oggetto Utente associato al prestito.
     * Utilizza l'UUID memorizzato per cercare l'istanza dell'utente nella lista globale della Biblioteca.
     */
    public Utente getUtente() {
        return Biblioteca.ottieniUtenteDaID(utente);
    }

    /**
     * @param utente Il nuovo UUID dell'utente.
     * @brief Imposta l'UUID dell'utente che ha effettuato il prestito.
     */
    public void setUtente(UUID utente) {
        this.utente = utente;
    }

    /**
     * @return Un oggetto LocalDate rappresentante la data di inizio.
     * @brief Restituisce la data di inizio del prestito.
     */
    public LocalDate getDataInizio() {
        return dataInizio.get();
    }

    /**
     * @param dataInizio La nuova data di inizio.
     * @brief Imposta la data di inizio del prestito.
     */
    public void setDataInizio(LocalDate dataInizio) {
        this.dataInizio.set(dataInizio);
    }

    /**
     * @return L'oggetto ObjectProperty contenente la LocalDate di inizio.
     * @brief Restituisce la property JavaFX relativa alla data di inizio.
     */
    public ObjectProperty<LocalDate> dataInizioProperty() {
        return dataInizio;
    }

    /**
     * @return Un oggetto LocalDate rappresentante la scadenza del prestito.
     * @brief Restituisce la data prevista per la restituzione.
     */
    public LocalDate getDataRestituzionePrevista() {
        return dataRestituzionePrevista.get();
    }

    /**
     * @param dataRestituzionePrevista La nuova data di scadenza.
     * @brief Imposta la data prevista per la restituzione.
     */
    public void setDataRestituzionePrevista(LocalDate dataRestituzionePrevista) {
        this.dataRestituzionePrevista.set(dataRestituzionePrevista);
    }

    /**
     * @return L'oggetto ObjectProperty contenente la LocalDate di scadenza.
     * @brief Restituisce la property JavaFX relativa alla data di restituzione prevista.
     */
    public ObjectProperty<LocalDate> dataRestituzionePrevistaProperty() {
        return dataRestituzionePrevista;
    }

    /**
     * @param data La data di riferimento (es. la data odierna o la data di restituzione effettiva).
     * @return Un intero rappresentante i giorni di differenza. Un valore positivo indica un ritardo.
     * @brief Calcola i giorni di ritardo rispetto a una data specifica.
     * Calcola la differenza in giorni tra la data di restituzione prevista e la data fornita.
     */
    public int calcolaRitardo(LocalDate data) {
        return (int) ChronoUnit.DAYS.between(getDataRestituzionePrevista(), data);
    }

    /**
     * @param giorni Il numero di giorni di tolleranza o soglia per il ritardo.
     * @return true Se il ritardo attuale supera i giorni specificati,
     * false Se non c'è ritardo o il ritardo è entro la soglia.
     * @brief Verifica se il prestito è in ritardo oltre una certa soglia.
     * Confronta la data odierna con la data di restituzione prevista.
     */
    public boolean verificaRitardo(int giorni) {
        return calcolaRitardo(LocalDate.now()) > giorni;
    }

    /**
     * @param oos Lo stream di output su cui scrivere.
     * @throws IOException In caso di errori di I/O.
     * @brief Metodo privato per la serializzazione personalizzata dell'oggetto.
     * Scrive i dati dell'oggetto sullo stream, convertendo le property JavaFX (non serializzabili)
     * nei loro valori primitivi/base.
     */
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeUTF(uuid.toString());
        oos.writeObject(dataInizio.get());
        oos.writeObject(dataRestituzionePrevista.get());
    }

    /**
     * @param ois Lo stream di input da cui leggere.
     * @throws IOException            In caso di errori di I/O.
     * @throws ClassNotFoundException Se la classe serializzata non viene trovata.
     * @brief Metodo privato per la deserializzazione personalizzata dell'oggetto.
     * Legge i dati dallo stream e ricostruisce le property JavaFX a partire dai valori salvati.
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
