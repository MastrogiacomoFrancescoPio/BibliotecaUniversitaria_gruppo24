package bibliotecauniversitaria.models;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import bibliotecauniversitaria.utils.Email;
import javafx.beans.property.*;

import javax.mail.MessagingException;

/**
 * @class Utente
 * @brief Rappresenta un utente della biblioteca universitaria.
 *
 * Questa classe gestisce i dati anagrafici, lo stato (sospeso/attivo),
 * le segnalazioni e la logica di business relativa ai prestiti dell'utente.
 * Implementa Serializable con logica custom per gestire le JavaFX Properties.
 */
public class Utente implements Serializable {

    private transient StringProperty matricola = new SimpleStringProperty();
    private transient StringProperty nome = new SimpleStringProperty();
    private transient StringProperty cognome = new SimpleStringProperty();
    private transient StringProperty email = new SimpleStringProperty();
    private transient IntegerProperty numeroSegnalazioni = new SimpleIntegerProperty();
    private transient BooleanProperty sospeso = new SimpleBooleanProperty();
    private transient ObjectProperty<LocalDate> dataSospensione = new SimpleObjectProperty<>();
    private transient ObjectProperty<LocalDate> dataFineSospensione = new SimpleObjectProperty<>();

    private transient UUID uuid = UUID.randomUUID();

 /**
     * @brief Costruttore della classe Utente.
     *
     * Inizializza un nuovo utente impostando i dati base e azzerando segnalazioni e sospensioni.
     *
     * @param matricola La matricola univoca dell'utente.
     * @param nome      Il nome dell'utente.
     * @param cognome   Il cognome dell'utente.
     * @param email     L'indirizzo email dell'utente.
     * @post Viene creato un utente attivo con 0 segnalazioni.
     */
    public Utente(String matricola, String nome, String cognome, String email) {
        setMatricola(matricola);
        setNome(nome);
        setCognome(cognome);
        setEmail(email);
        setNumeroSegnalazioni(0);
        setSospeso(false);
        setDataSospensione(null);
        setDataFineSospensione(null);
    }

 /**
     * @brief Restituisce la matricola dell'utente.
     * @return La stringa contenente la matricola.
     */
    public String getMatricola() { return matricola.get(); }
   /**
     * @brief Imposta la matricola dell'utente.
     * @param matricola La nuova matricola da assegnare.
     */
    public void setMatricola(String matricola) { this.matricola.set(matricola); }
     /**
     * @brief Restituisce la property JavaFX relativa alla matricola.
     * @return L'oggetto StringProperty della matricola.
     */
    public StringProperty matricolaProperty() { return matricola; }
/**
     * @brief Restituisce il nome dell'utente.
     * @return La stringa contenente il nome.
     */
    public String getNome() { return nome.get(); }
    /**
     * @brief Imposta il nome dell'utente.
     * @param nome Il nuovo nome da assegnare.
     */
    public void setNome(String nome) { this.nome.set(nome); }
    /**
     * @brief Restituisce la property JavaFX relativa al nome.
     * @return L'oggetto StringProperty del nome.
     */
    public StringProperty nomeProperty() { return nome; }
/**
     * @brief Restituisce il cognome dell'utente.
     * @return La stringa contenente il cognome.
     */
    public String getCognome() { 
        return cognome.get(); 
    }
     /**
     * @brief Imposta il cognome dell'utente.
     * @param cognome Il nuovo cognome da assegnare.
     */
    public void setCognome(String cognome) { 
        this.cognome.set(cognome); 
    }
     /**
     * @brief Restituisce la property JavaFX relativa al cognome.
     * @return L'oggetto StringProperty del cognome.
     */
    public StringProperty cognomeProperty() { 
        return cognome; 
    }
 /**
     * @brief Restituisce l'email dell'utente.
     * @return La stringa contenente l'email.
     */
    public String getEmail() { 
        return email.get(); 
    }
     /**
     * @brief Imposta l'email dell'utente con validazione.
     *
     * @pre L'email deve essere valida secondo il pattern definito in Email.isValida, oppure una stringa vuota.
     * @param email La nuova email.
     * @throws IllegalArgumentException Se l'email non è valida e non è vuota.
     */
    public void setEmail(String email) {
        if(!Email.isValida(email)&&!email.equals("")){
            throw new IllegalArgumentException("E-mail non valida");
        }
        emailProperty().set(email);
    }
    /**
     * @brief Restituisce la property JavaFX relativa all'email.
     * @return L'oggetto StringProperty dell'email.
     */
    public StringProperty emailProperty() { 
        return email; 
    }
/**
     * @brief Restituisce il numero di segnalazioni attive.
     * @return Un intero rappresentante il numero di segnalazioni.
     */
    public int getNumeroSegnalazioni() { return numeroSegnalazioni.get(); }
    /**
     * @brief Imposta il numero di segnalazioni.
     *
     * @pre Il numero di segnalazioni deve essere >= 0.
     * @param numeroSegnalazioni Il nuovo numero di segnalazioni.
     * @throws IllegalArgumentException Se il numero è negativo.
     */
    public void setNumeroSegnalazioni(int numeroSegnalazioni) throws IllegalArgumentException {
        if(numeroSegnalazioni < 0){
            throw new IllegalArgumentException("Numero segnalazioni non può essere minore di 0!");
        }
        this.numeroSegnalazioni.set(numeroSegnalazioni);
    }
   /**
     * @brief Restituisce la property JavaFX relativa alle segnalazioni.
     * @return L'oggetto IntegerProperty delle segnalazioni.
     */
    public IntegerProperty numeroSegnalazioniProperty() { return numeroSegnalazioni; }
 /**
     * @brief Verifica se l'utente è attualmente sospeso.
     * @return true se sospeso, false altrimenti.
     */
    public boolean isSospeso() { return sospeso.get(); }
      /**
     * @brief Imposta lo stato di sospensione.
     * @param sospeso true per sospendere, false per attivare.
     */
    public void setSospeso(boolean sospeso) { this.sospeso.set(sospeso); }
    
    /**
     * @brief Restituisce la property JavaFX relativa allo stato di sospensione.
     * @return L'oggetto BooleanProperty della sospensione.
     */
    public BooleanProperty sospesoProperty() { return sospeso; }
    
    /**
     * @brief Restituisce la data di inizio sospensione.
     * @return Oggetto LocalDate o null se non sospeso.
     */
    public LocalDate getDataSospensione() { return dataSospensione.get(); }
    
    /**
     * @brief Imposta la data di inizio sospensione.
     * @param dataSospensione La data di inizio.
     */
    public void setDataSospensione(LocalDate dataSospensione) { this.dataSospensione.set(dataSospensione); }
    
    /**
     * @brief Restituisce la property JavaFX relativa alla data di sospensione.
     * @return ObjectProperty contenente LocalDate.
     */
    public ObjectProperty<LocalDate> dataSospensioneProperty() { return dataSospensione; }
    
    /**
     * @brief Restituisce la data di fine sospensione.
     * @return Oggetto LocalDate o null se non sospeso.
     */
    public LocalDate getDataFineSospensione() { return dataFineSospensione.get(); }
    
    /**
     * @brief Imposta la data di fine sospensione.
     * @param dataFineSospensione La data in cui terminerà la sospensione.
     */
    public void setDataFineSospensione(LocalDate dataFineSospensione) { this.dataFineSospensione.set(dataFineSospensione); }
     
    /**
     * @brief Restituisce la property JavaFX relativa alla data di fine sospensione.
     * @return ObjectProperty contenente LocalDate.
     */
    public ObjectProperty<LocalDate> dataFineSospensioneProperty() { return dataFineSospensione; }
    
    /**
     * @brief Restituisce l'UUID univoco dell'istanza.
     * @return Oggetto UUID.
     */
    public UUID getUUID() { return uuid; }

 /**
     * @brief Recupera tutti i prestiti associati a questo utente.
     * Scansiona la lista globale dei prestiti nella classe Biblioteca.
     * @return ArrayList di oggetti Prestito associati all'utente corrente.
     */
    public ArrayList<Prestito> getPrestiti() {
        ArrayList<Prestito> prestiti = new ArrayList<>();
        for (Prestito p : Biblioteca.getListaPrestiti()) {
            if (p.getUtente() == this) prestiti.add(p);
        }
        return prestiti;
    }
/**
     * @brief Conta il numero di prestiti attivi dell'utente.
     * @return Il numero intero di prestiti.
     */
    public int conteggioPrestiti() { return getPrestiti().size(); }
     /**
     * @brief Verifica se l'utente ha raggiunto il limite massimo di prestiti consentiti.
     * Confronta il numero attuale di prestiti con la configurazione globale MAX_PRESTITI.
     * @return true se il limite è stato raggiunto o superato, false altrimenti.
     */
    public boolean verificaLimitePrestitiRaggiunto() {
        return conteggioPrestiti() >= Biblioteca.configurazione.getNumero("MAX_PRESTITI");
    }

    /**
     * @brief Incrementa di 1 il contatore delle segnalazioni dell'utente.
     * @post Il valore di numeroSegnalazioni viene aumentato di 1.
     */
    public void incrementaSegnalazioni() { numeroSegnalazioni.set(numeroSegnalazioni.get() + 1); }
    
    /**
     * @brief Azzera il contatore delle segnalazioni.
     * @post Il valore di numeroSegnalazioni diventa 0.
     */
    public void resetSegnalazioni() { numeroSegnalazioni.set(0); }
    
    /**
     * @brief Controlla se le segnalazioni superano la soglia e, in tal caso, sospende l'utente.
     *
     * @param massime Il numero massimo di segnalazioni tollerate.
     * @param sospensione I giorni di sospensione da applicare se il limite è superato.
     * @post Se numeroSegnalazioni >= massime, l'utente viene sospeso.
     */
    public void controllaSegnalazioni(int massime, int sospensione) {
        if (numeroSegnalazioni.get() >= massime) sospendi(sospensione,true);
    }

    /**
     * @brief Sospende l'utente per un determinato numero di giorni.
     * Imposta lo stato a sospeso, calcola le date e invia una mail di notifica asincrona.
     * @param giorni Durata della sospensione in giorni.
     * @param email Flag per decidere se inviare l'email di notifica (true = invia).
     * @post L'utente risulta sospeso e le segnalazioni vengono azzerate.
     */
    public void sospendi(int giorni, boolean email) {
        sospeso.set(true);
        dataSospensione.set(LocalDate.now());
        dataFineSospensione.set(LocalDate.now().plusDays(giorni));
        resetSegnalazioni();

        HashMap<String,String> sostituzioni = new HashMap<>();
        sostituzioni.put("giorni", String.valueOf(giorni));
        sostituzioni.put("di", dataSospensione.get().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        sostituzioni.put("df", dataFineSospensione.get().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        if(email){
            Runnable runnable = () -> {
                try {
                    Email.mandaMailPagina(getEmail(), "Sei stato sospeso!", "sospendi", sostituzioni);
                } catch (MessagingException ignored) {}
            };
            runnable.run();
        }

    }

    /**
     * @brief Revoca la sospensione dell'utente.
     * Ripristina lo stato attivo, azzera le date di sospensione e invia email di notifica.
     * @param email Flag per decidere se inviare l'email di notifica (true = invia).
     * @post L'utente non risulta più sospeso.
     */
    public void revocaSospensione(boolean email) {
        sospeso.set(false);
        dataSospensione.set(null);
        dataFineSospensione.set(null);

        if(email) {
            Runnable runnable = () -> {
                try {
                    Email.mandaMailPagina(getEmail(), "Non sei più sospeso!", "toglisospendi", null);
                } catch (MessagingException ignored) {}
            };
            runnable.run();
        }
    }
    
    /**
     * @brief Verifica se il periodo di sospensione è terminato.
     * @return true se l'utente è sospeso MA la data odierna è successiva alla data di fine sospensione.
     */
    public boolean isSospensioneScaduta() {
        LocalDate fine = dataFineSospensione.get();
        return sospeso.get() && fine != null && !fine.isAfter(LocalDate.now());
    }
    
    /**
     * @brief Aggiorna lo stato di sospensione verificando se è scaduta.
     * Se la sospensione è scaduta, chiama il metodo revocaSospensione.
     * @param email Flag per l'invio dell'email in caso di revoca.
     */
    public void aggiornaSospensione(boolean email) {
        if (isSospensioneScaduta()) revocaSospensione(email);
    }

    /**
     * @brief Calcola l'hash code dell'oggetto basato sulla matricola.
     * @return Intero rappresentante l'hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(matricola.get().toLowerCase());
    }
    
    /**
     * @brief Confronta due oggetti Utente per uguaglianza.
     *
     * Due utenti sono considerati uguali se hanno la stessa matricola (case insensitive).
     *
     * @param obj L'oggetto da confrontare.
     * @return true se gli oggetti sono uguali, false altrimenti.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Utente other = (Utente) obj;
        return matricola.get() != null && matricola.get().trim().equalsIgnoreCase(other.matricola.get().trim());
    }

    /**
     * @brief Metodo privato per la serializzazione personalizzata dell'oggetto.
     * Necessario perché le JavaFX Properties non sono serializzabili di default.
     * @param oos Lo stream di output su cui scrivere.
     * @throws IOException In caso di errori di I/O.
     */
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeUTF(uuid.toString());
        oos.writeObject(matricola.get());
        oos.writeObject(nome.get());
        oos.writeObject(cognome.get());
        oos.writeObject(email.get());
        oos.writeInt(numeroSegnalazioni.get());
        oos.writeBoolean(sospeso.get());
        oos.writeObject(dataSospensione.get());
        oos.writeObject(dataFineSospensione.get());
    }
    
    /**
     * @brief Metodo privato per la deserializzazione personalizzata dell'oggetto.
     * Ricostruisce le JavaFX Properties leggendo i valori primitivi dallo stream.
     * @param ois Lo stream di input da cui leggere.
     * @throws IOException In caso di errori di I/O.
     * @throws ClassNotFoundException Se la classe non viene trovata.
     */
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        this.uuid = UUID.fromString(ois.readUTF());

        this.matricola = new SimpleStringProperty((String) ois.readObject());
        this.nome = new SimpleStringProperty((String) ois.readObject());
        this.cognome = new SimpleStringProperty((String) ois.readObject());
        this.email = new SimpleStringProperty((String) ois.readObject());

        this.numeroSegnalazioni = new SimpleIntegerProperty(ois.readInt());
        this.sospeso = new SimpleBooleanProperty(ois.readBoolean());

        this.dataSospensione = new SimpleObjectProperty<>((LocalDate) ois.readObject());
        this.dataFineSospensione = new SimpleObjectProperty<>((LocalDate) ois.readObject());
    }
}
