package bibliotecauniversitaria.models;

import bibliotecauniversitaria.utils.Email;
import javafx.beans.property.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

/**
 * @class Utente
 * @brief Rappresenta un utente della biblioteca universitaria.
 * <p>
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
     * @param matricola La matricola univoca dell'utente.
     * @param nome      Il nome dell'utente.
     * @param cognome   Il cognome dell'utente.
     * @param email     L'indirizzo email dell'utente.
     * @brief Costruttore della classe Utente.
     * <p>
     * Inizializza un nuovo utente impostando i dati base e azzerando segnalazioni e sospensioni.
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
     * @return La stringa contenente la matricola.
     * @brief Restituisce la matricola dell'utente.
     */
    public String getMatricola() {
        return matricola.get();
    }

    /**
     * @param matricola La nuova matricola da assegnare.
     * @brief Imposta la matricola dell'utente.
     */
    public void setMatricola(String matricola) {
        this.matricola.set(matricola);
    }

    /**
     * @return L'oggetto StringProperty della matricola.
     * @brief Restituisce la property JavaFX relativa alla matricola.
     */
    public StringProperty matricolaProperty() {
        return matricola;
    }

    /**
     * @return La stringa contenente il nome.
     * @brief Restituisce il nome dell'utente.
     */
    public String getNome() {
        return nome.get();
    }

    /**
     * @param nome Il nuovo nome da assegnare.
     * @brief Imposta il nome dell'utente.
     */
    public void setNome(String nome) {
        this.nome.set(nome);
    }

    /**
     * @return L'oggetto StringProperty del nome.
     * @brief Restituisce la property JavaFX relativa al nome.
     */
    public StringProperty nomeProperty() {
        return nome;
    }

    /**
     * @return La stringa contenente il cognome.
     * @brief Restituisce il cognome dell'utente.
     */
    public String getCognome() {
        return cognome.get();
    }

    /**
     * @param cognome Il nuovo cognome da assegnare.
     * @brief Imposta il cognome dell'utente.
     */
    public void setCognome(String cognome) {
        this.cognome.set(cognome);
    }

    /**
     * @return L'oggetto StringProperty del cognome.
     * @brief Restituisce la property JavaFX relativa al cognome.
     */
    public StringProperty cognomeProperty() {
        return cognome;
    }

    /**
     * @return La stringa contenente l'email.
     * @brief Restituisce l'email dell'utente.
     */
    public String getEmail() {
        return email.get();
    }

    /**
     * @param email La nuova email.
     * @throws IllegalArgumentException Se l'email non è valida e non è vuota.
     * @brief Imposta l'email dell'utente con validazione.
     * @pre L'email deve essere valida secondo il pattern definito in Email.isValida, oppure una stringa vuota.
     */
    public void setEmail(String email) {
        if (!Email.isValida(email) && !email.equals("")) {
            throw new IllegalArgumentException("E-mail non valida");
        }
        emailProperty().set(email);
    }

    /**
     * @return L'oggetto StringProperty dell'email.
     * @brief Restituisce la property JavaFX relativa all'email.
     */
    public StringProperty emailProperty() {
        return email;
    }

    /**
     * @return Un intero rappresentante il numero di segnalazioni.
     * @brief Restituisce il numero di segnalazioni attive.
     */
    public int getNumeroSegnalazioni() {
        return numeroSegnalazioni.get();
    }

    /**
     * @param numeroSegnalazioni Il nuovo numero di segnalazioni.
     * @throws IllegalArgumentException Se il numero è negativo.
     * @brief Imposta il numero di segnalazioni.
     * @pre Il numero di segnalazioni deve essere >= 0.
     */
    public void setNumeroSegnalazioni(int numeroSegnalazioni) throws IllegalArgumentException {
        if (numeroSegnalazioni < 0) {
            throw new IllegalArgumentException("Numero segnalazioni non può essere minore di 0!");
        }
        this.numeroSegnalazioni.set(numeroSegnalazioni);
    }

    /**
     * @return L'oggetto IntegerProperty delle segnalazioni.
     * @brief Restituisce la property JavaFX relativa alle segnalazioni.
     */
    public IntegerProperty numeroSegnalazioniProperty() {
        return numeroSegnalazioni;
    }

    /**
     * @return true se sospeso, false altrimenti.
     * @brief Verifica se l'utente è attualmente sospeso.
     */
    public boolean isSospeso() {
        return sospeso.get();
    }

    /**
     * @param sospeso true per sospendere, false per attivare.
     * @brief Imposta lo stato di sospensione.
     */
    public void setSospeso(boolean sospeso) {
        this.sospeso.set(sospeso);
    }

    /**
     * @return L'oggetto BooleanProperty della sospensione.
     * @brief Restituisce la property JavaFX relativa allo stato di sospensione.
     */
    public BooleanProperty sospesoProperty() {
        return sospeso;
    }

    /**
     * @return Oggetto LocalDate o null se non sospeso.
     * @brief Restituisce la data di inizio sospensione.
     */
    public LocalDate getDataSospensione() {
        return dataSospensione.get();
    }

    /**
     * @param dataSospensione La data di inizio.
     * @brief Imposta la data di inizio sospensione.
     */
    public void setDataSospensione(LocalDate dataSospensione) {
        this.dataSospensione.set(dataSospensione);
    }

    /**
     * @return ObjectProperty contenente LocalDate.
     * @brief Restituisce la property JavaFX relativa alla data di sospensione.
     */
    public ObjectProperty<LocalDate> dataSospensioneProperty() {
        return dataSospensione;
    }

    /**
     * @return Oggetto LocalDate o null se non sospeso.
     * @brief Restituisce la data di fine sospensione.
     */
    public LocalDate getDataFineSospensione() {
        return dataFineSospensione.get();
    }

    /**
     * @param dataFineSospensione La data in cui terminerà la sospensione.
     * @brief Imposta la data di fine sospensione.
     */
    public void setDataFineSospensione(LocalDate dataFineSospensione) {
        this.dataFineSospensione.set(dataFineSospensione);
    }

    /**
     * @return ObjectProperty contenente LocalDate.
     * @brief Restituisce la property JavaFX relativa alla data di fine sospensione.
     */
    public ObjectProperty<LocalDate> dataFineSospensioneProperty() {
        return dataFineSospensione;
    }

    /**
     * @return Oggetto UUID.
     * @brief Restituisce l'UUID univoco dell'istanza.
     */
    public UUID getUUID() {
        return uuid;
    }

    /**
     * @return ArrayList di oggetti Prestito associati all'utente corrente.
     * @brief Recupera tutti i prestiti associati a questo utente.
     * Scansiona la lista globale dei prestiti nella classe Biblioteca.
     */
    public ArrayList<Prestito> getPrestiti() {
        ArrayList<Prestito> prestiti = new ArrayList<>();
        for (Prestito p : Biblioteca.getListaPrestiti()) {
            if (p.getUtente() == this) prestiti.add(p);
        }
        return prestiti;
    }

    /**
     * @return Il numero intero di prestiti.
     * @brief Conta il numero di prestiti attivi dell'utente.
     */
    public int conteggioPrestiti() {
        return getPrestiti().size();
    }

    /**
     * @return true se il limite è stato raggiunto o superato, false altrimenti.
     * @brief Verifica se l'utente ha raggiunto il limite massimo di prestiti consentiti.
     * Confronta il numero attuale di prestiti con la configurazione globale MAX_PRESTITI.
     */
    public boolean verificaLimitePrestitiRaggiunto() {
        return conteggioPrestiti() >= Biblioteca.configurazione.getNumero("MAX_PRESTITI");
    }

    /**
     * @brief Incrementa di 1 il contatore delle segnalazioni dell'utente.
     * @post Il valore di numeroSegnalazioni viene aumentato di 1.
     */
    public void incrementaSegnalazioni() {
        numeroSegnalazioni.set(numeroSegnalazioni.get() + 1);
    }

    /**
     * @brief Azzera il contatore delle segnalazioni.
     * @post Il valore di numeroSegnalazioni diventa 0.
     */
    public void resetSegnalazioni() {
        numeroSegnalazioni.set(0);
    }

    /**
     * @param massime     Il numero massimo di segnalazioni tollerate.
     * @param sospensione I giorni di sospensione da applicare se il limite è superato.
     * @brief Controlla se le segnalazioni superano la soglia e, in tal caso, sospende l'utente.
     * @post Se numeroSegnalazioni >= massime, l'utente viene sospeso.
     */
    public void controllaSegnalazioni(int massime, int sospensione) {
        if (numeroSegnalazioni.get() >= massime) sospendi(sospensione, true);
    }

    /**
     * @param giorni Durata della sospensione in giorni.
     * @param email  Flag per decidere se inviare l'email di notifica (true = invia).
     * @brief Sospende l'utente per un determinato numero di giorni.
     * Imposta lo stato a sospeso, calcola le date e invia una mail di notifica asincrona.
     * @post L'utente risulta sospeso e le segnalazioni vengono azzerate.
     */
    public void sospendi(int giorni, boolean email) {
        sospeso.set(true);
        dataSospensione.set(LocalDate.now());
        dataFineSospensione.set(LocalDate.now().plusDays(giorni));
        resetSegnalazioni();

        HashMap<String, String> sostituzioni = new HashMap<>();
        sostituzioni.put("giorni", String.valueOf(giorni));
        sostituzioni.put("di", dataSospensione.get().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        sostituzioni.put("df", dataFineSospensione.get().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        if (email) {
            Runnable runnable = () -> {
                try {
                    Email.mandaMailPagina(getEmail(), "Sei stato sospeso!", "sospendi", sostituzioni);
                } catch (MessagingException ignored) {
                }
            };
            runnable.run();
        }

    }

    /**
     * @param email Flag per decidere se inviare l'email di notifica (true = invia).
     * @brief Revoca la sospensione dell'utente.
     * Ripristina lo stato attivo, azzera le date di sospensione e invia email di notifica.
     * @post L'utente non risulta più sospeso.
     */
    public void revocaSospensione(boolean email) {
        sospeso.set(false);
        dataSospensione.set(null);
        dataFineSospensione.set(null);

        if (email) {
            Runnable runnable = () -> {
                try {
                    Email.mandaMailPagina(getEmail(), "Non sei più sospeso!", "toglisospendi", null);
                } catch (MessagingException ignored) {
                }
            };
            runnable.run();
        }
    }

    /**
     * @return true se l'utente è sospeso MA la data odierna è successiva alla data di fine sospensione.
     * @brief Verifica se il periodo di sospensione è terminato.
     */
    public boolean isSospensioneScaduta() {
        LocalDate fine = dataFineSospensione.get();
        return sospeso.get() && fine != null && !fine.isAfter(LocalDate.now());
    }

    /**
     * @param email Flag per l'invio dell'email in caso di revoca.
     * @brief Aggiorna lo stato di sospensione verificando se è scaduta.
     * Se la sospensione è scaduta, chiama il metodo revocaSospensione.
     */
    public void aggiornaSospensione(boolean email) {
        if (isSospensioneScaduta()) revocaSospensione(email);
    }

    /**
     * @return Intero rappresentante l'hash code.
     * @brief Calcola l'hash code dell'oggetto basato sulla matricola.
     */
    @Override
    public int hashCode() {
        return Objects.hash(matricola.get().toLowerCase());
    }

    /**
     * @param obj L'oggetto da confrontare.
     * @return true se gli oggetti sono uguali, false altrimenti.
     * @brief Confronta due oggetti Utente per uguaglianza.
     * <p>
     * Due utenti sono considerati uguali se hanno la stessa matricola (case insensitive).
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Utente other = (Utente) obj;
        return matricola.get() != null && matricola.get().trim().equalsIgnoreCase(other.matricola.get().trim());
    }

    /**
     * @param oos Lo stream di output su cui scrivere.
     * @throws IOException In caso di errori di I/O.
     * @brief Metodo privato per la serializzazione personalizzata dell'oggetto.
     * Necessario perché le JavaFX Properties non sono serializzabili di default.
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
     * @param ois Lo stream di input da cui leggere.
     * @throws IOException            In caso di errori di I/O.
     * @throws ClassNotFoundException Se la classe non viene trovata.
     * @brief Metodo privato per la deserializzazione personalizzata dell'oggetto.
     * Ricostruisce le JavaFX Properties leggendo i valori primitivi dallo stream.
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
