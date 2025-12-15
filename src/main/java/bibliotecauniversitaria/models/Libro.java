package bibliotecauniversitaria.models;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.Year;
import java.util.Objects;
import java.util.UUID;

import bibliotecauniversitaria.exceptions.LibroGiaEsistenteException;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @class Libro
 * @brief Rappresenta un libro all'interno del sistema bibliotecario.
 * Questa classe gestisce i metadati del libro (titolo, autore, ISBN, anno),
 * il conteggio delle copie (totali e disponibili) e la logica di validazione dei dati.
 * Implementa Serializable con una gestione personalizzata per le property di JavaFX.
 */
public class Libro implements Serializable {

    private transient StringProperty titolo= new SimpleStringProperty(); 
    private transient StringProperty autore= new SimpleStringProperty(); 
    private transient StringProperty ISBN= new SimpleStringProperty(); 
    private transient IntegerProperty annoPubblicazione= new SimpleIntegerProperty();
    private transient IntegerProperty numeroCopieTotali= new SimpleIntegerProperty();
    private transient IntegerProperty numeroCopieDisponibili= new SimpleIntegerProperty();

    private transient UUID uuid = UUID.randomUUID();

     /**
     * @brief Costruttore della classe Libro.
     * Inizializza un nuovo libro validando i dati in ingresso.
     * Le copie disponibili vengono inizializzate uguali alle copie totali.
     * @post Viene creato un nuovo oggetto Libro con un UUID univoco.
     * @param titolo Il titolo del libro.
     * @param autore L'autore del libro.
     * @param ISBN Il codice ISBN univoco del libro.
     * @param annoPubblicazione L'anno di pubblicazione.
     * @param numeroCopieTotali Il numero iniziale di copie fisiche possedute dalla biblioteca.
     */
    public Libro(String titolo, String autore, String ISBN, int annoPubblicazione, int numeroCopieTotali) {
        setTitolo(titolo);
        setAutore(autore);
        setISBN(ISBN);
        setAnnoPubblicazione(annoPubblicazione); 
        setNumeroCopieTotali(numeroCopieTotali);
        setNumeroCopieDisponibili(numeroCopieTotali);
    }
    
    /**
     * @brief Restituisce il titolo del libro.
     * @return Una stringa contenente il titolo.
     */
    public String getTitolo() {
        return titolo.get();
    }

    /**
     * @brief Imposta il titolo del libro.
     * @param[in] titolo Il nuovo titolo da assegnare.
     */
    public void setTitolo(String titolo) {
        this.titolo.set(titolo);
    }

    /**
     * @brief Restituisce la property JavaFX del titolo.
     * @return L'oggetto StringProperty del titolo.
     */
    public StringProperty titoloProperty() {
        return titolo;
    }

    /**
     * @brief Restituisce l'autore del libro.
     * @return Una stringa contenente il nome dell'autore.
     */
    public String getAutore() {
        return autore.get();
    }

     /**
     * @brief Imposta l'autore del libro.
     * @param autore Il nuovo autore da assegnare.
     */
    public void setAutore(String autore) {
        this.autore.set(autore);
    }

    /**
     * @brief Restituisce la property JavaFX dell'autore.
     * @return L'oggetto StringProperty dell'autore.
     */
    public StringProperty autoreProperty() {
        return autore;
    }

     /**
     * @brief Restituisce il codice ISBN del libro.
     * @return Una stringa contenente l'ISBN.
     */
    public String getISBN() {
        return ISBN.get();
    }

    /**
     * @brief Imposta il codice ISBN del libro.
     * @param ISBN Il nuovo codice ISBN.
     */
    public void setISBN(String ISBN) {
        this.ISBN.set(ISBN);
    }

     /**
     * @brief Restituisce la property JavaFX dell'ISBN.
     * @return L'oggetto StringProperty dell'ISBN.
     */
    public StringProperty ISBNProperty() {
        return ISBN;
    }

    /**
     * @brief Restituisce l'anno di pubblicazione.
     * @return Un intero rappresentante l'anno.
     */
    public int getAnnoPubblicazione() {
        return annoPubblicazione.get();
    }

    /**
     * @brief Imposta l'anno di pubblicazione con validazione.
     * @pre L'anno di pubblicazione non deve essere nel futuro rispetto all'anno corrente di sistema.
     * @post Il valore dell'anno viene aggiornato se valido.
     * @param annoPubblicazione L'anno da impostare.
     * @throws IllegalArgumentException Se l'anno è successivo all'anno corrente.
     */
    public void setAnnoPubblicazione(int annoPubblicazione) {
        int annoCorrente = Year.now().getValue();
        if (annoPubblicazione > annoCorrente) {
            throw new IllegalArgumentException("L'anno di pubblicazione (" + annoPubblicazione + ") non può essere successivo all'anno corrente (" + annoCorrente + ").");
        }
        this.annoPubblicazione.set(annoPubblicazione);
    }
    
    /**
     * @brief Restituisce la property JavaFX dell'anno di pubblicazione.
     * @return L'oggetto IntegerProperty dell'anno.
     */
    public IntegerProperty annoPubblicazioneProperty() {
        return annoPubblicazione;
    }

     /**
     * @brief Restituisce il numero totale di copie possedute.
     * @return Un intero rappresentante le copie totali.
     */
    public int getNumeroCopieTotali() {
        return numeroCopieTotali.get();
    }

    /**
     * @brief Imposta il numero totale di copie.
     * @pre Il numero di copie totali deve essere >= 0.
     * @pre Il nuovo totale non può essere inferiore al numero di copie attualmente disponibili (incoerenza logica).
     * @param numeroCopieTotali Il nuovo numero totale di copie.
     * @throws IllegalArgumentException Se il valore è negativo o inferiore alle copie disponibili.
     */
    public void setNumeroCopieTotali(int numeroCopieTotali) {
        if (numeroCopieTotali < 0) {
            throw new IllegalArgumentException("Il numero di copie totali non può essere negativo.");
        } 
        if (getNumeroCopieDisponibili() > numeroCopieTotali) {
            throw new IllegalArgumentException("Impossibile ridurre le copie totali a " + numeroCopieTotali + " perché ci sono ancora " + getNumeroCopieDisponibili() + " copie disponibili.");
        }
        this.numeroCopieTotali.set(numeroCopieTotali);
    }

     /**
     * @brief Restituisce la property JavaFX delle copie totali.
     * @return L'oggetto IntegerProperty delle copie totali.
     */
    public IntegerProperty numeroCopieTotaliProperty() {
        return numeroCopieTotali;
    }

    /**
     * @brief Restituisce il numero di copie attualmente disponibili per il prestito.
     * @return Un intero rappresentante le copie disponibili.
     */
    public int getNumeroCopieDisponibili() {
        return numeroCopieDisponibili.get();
    }

     /**
     * @brief Imposta manualmente il numero di copie disponibili.
     * @param copie Il nuovo numero di copie disponibili.
     * @throws IllegalArgumentException Se le copie sono negative o superiori al totale.
     */
    public void setNumeroCopieDisponibili(int copie) {
        if (copie < 0) {
            throw new IllegalArgumentException("Il numero di copie disponibili non può essere negativo.");
        } else if (copie >numeroCopieTotali.get()) {
            throw new IllegalArgumentException("Il numero di copie disponibili (" + copie + ") non può superare il totale (" + numeroCopieTotali.get() + ").");
        } else {
            this.numeroCopieDisponibili.set(copie);
        }
    }

     /**
     * @brief Restituisce la property JavaFX delle copie disponibili.
     * @return L'oggetto IntegerProperty delle copie disponibili.
     */
    public IntegerProperty numeroCopieDisponibiliProperty() {
        return numeroCopieDisponibili;
    }

     /**
     * @brief Restituisce l'UUID univoco del libro.
     * @return Oggetto UUID.
     */
    public UUID getUUID() {
        return uuid;
    }

     /**
     * @brief Incrementa di uno il numero di copie disponibili.
     * Utilizzato solitamente quando un libro viene restituito.
     * @post Il numero di copie disponibili aumenta di 1, se non supera il totale.
     */
    public void incrementaCopieDisponibili() {
        if (getNumeroCopieDisponibili() < getNumeroCopieTotali()) {
            numeroCopieDisponibili.set(getNumeroCopieDisponibili() + 1);
        }

    }

    /**
     * @brief Decrementa di uno il numero di copie disponibili.
     * Utilizzato solitamente quando un libro viene preso in prestito.
     * @post Il numero di copie disponibili diminuisce di 1, se maggiore di 0.
     */
    public void decrementaCopieDisponibili() {
        if (getNumeroCopieDisponibili() > 0) {
            numeroCopieDisponibili.set(getNumeroCopieDisponibili() - 1);
        }
    }

     /**
     * @brief Verifica se ci sono copie disponibili per il prestito.
     * @return true Se le copie disponibili sono > 0, false altrimenti.
     */
    public boolean haCopieDisponibili() {
        return getNumeroCopieDisponibili() > 0;
    }

    /**
     * @brief Calcola l'hash code del libro basato sull'ISBN.
     * @return Intero rappresentante l'hash code.
     */
    @Override
    public int hashCode() {
        return 29 * 7 + Objects.hashCode((ISBN != null && ISBN.get() != null) ? ISBN.get().trim().toLowerCase() : "");
    }

     /**
     * @brief Confronta due libri per uguaglianza.
     * Due libri sono considerati uguali se hanno lo stesso ISBN (case insensitive).
     * @param obj L'oggetto con cui confrontare il libro corrente.
     * @return true Se gli oggetti sono uguali, false altrimenti.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final Libro l = (Libro) obj;
        if (this.ISBN == null || this.ISBN.get() == null) return false;
        if (l.ISBN == null || l.ISBN.get() == null) return false;
        return this.ISBN.get().trim().equalsIgnoreCase(l.ISBN.get().trim());
    }
    
     /**
     * @brief Metodo privato per la serializzazione personalizzata.
     * Converte le JavaFX Properties in tipi primitivi o serializzabili standard per la scrittura su stream.
     * @param oos Lo stream di output.
     * @throws IOException In caso di errori di I/O.
     */
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();

        oos.writeUTF(uuid.toString());

        oos.writeObject(titolo.get());
        oos.writeObject(autore.get());
        oos.writeObject(ISBN.get());

        oos.writeInt(annoPubblicazione.get());
        oos.writeInt(numeroCopieTotali.get());
        oos.writeInt(numeroCopieDisponibili.get());
    }

    /**
     * @brief Metodo privato per la deserializzazione personalizzata.
     * Legge i dati dallo stream e ricostruisce le JavaFX Properties.
     * @param ois Lo stream di input.
     * @throws IOException In caso di errori di I/O.
     * @throws ClassNotFoundException Se la classe non viene trovata.
     */
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();

        this.uuid = UUID.fromString(ois.readUTF());

        String titoloVal = (String) ois.readObject();
        String autoreVal = (String) ois.readObject();
        String ISBNVal = (String) ois.readObject();

        int annoVal = ois.readInt();
        int totaleVal = ois.readInt();
        int disponibileVal = ois.readInt();

        this.titolo = new SimpleStringProperty(titoloVal);
        this.autore = new SimpleStringProperty(autoreVal);
        this.ISBN = new SimpleStringProperty(ISBNVal);

        this.annoPubblicazione = new SimpleIntegerProperty(annoVal);
        this.numeroCopieTotali = new SimpleIntegerProperty(totaleVal);
        this.numeroCopieDisponibili = new SimpleIntegerProperty(disponibileVal);
    }
}