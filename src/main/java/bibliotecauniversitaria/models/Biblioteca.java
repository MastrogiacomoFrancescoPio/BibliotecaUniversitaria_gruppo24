package bibliotecauniversitaria.models;


import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


import bibliotecauniversitaria.exceptions.LibroGiaEsistenteException;
import bibliotecauniversitaria.exceptions.LibroInPrestitoException;
import bibliotecauniversitaria.exceptions.UtenteGiaEsistenteException;
import bibliotecauniversitaria.exceptions.UtenteHaPrestitiException;
import bibliotecauniversitaria.utils.Configurazione;
import bibliotecauniversitaria.utils.Email;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import javax.mail.MessagingException;

/**
 * @class Biblioteca
 * @brief Classe statica che funge da controller principale e repository dei dati.
 * Gestisce le liste globali di libri, utenti e prestiti. Si occupa della persistenza dei dati,
 * della logica di aggiunta/rimozione e delle operazioni di ricerca e ordinamento.
 */
public class Biblioteca implements Serializable {

    private static ObservableList<Libro> listaLibri = FXCollections.observableArrayList();
    private static ObservableList<Utente> listaUtenti = FXCollections.observableArrayList();
    private static ObservableList<Prestito> listaPrestiti = FXCollections.observableArrayList();

    public static Configurazione configurazione;

 /**
     * @brief Restituisce l'oggetto di configurazione globale.
     * @return L'istanza corrente di Configurazione.
     */
    public Configurazione getConfigurazione() {
        return configurazione;
    }

    /**
     * @brief Restituisce la lista osservabile dei libri.
     * @return ObservableList contenente oggetti Libro.
     */
    public static ObservableList<Libro> getListaLibri() {
        return listaLibri;
    }

    /**
     * @brief Imposta la lista dei libri.
     * @param listaLibri La nuova lista di libri.
     */
    public static void setListaLibri(ObservableList<Libro> listaLibri) {
        Biblioteca.listaLibri = listaLibri;
    }

     /**
     * @brief Restituisce la lista osservabile degli utenti.
     * @return ObservableList contenente oggetti Utente.
     */
    public static ObservableList<Utente> getListaUtenti() {
        return listaUtenti;
    }

    /**
     * @brief Imposta la lista degli utenti.
     * @param listaUtenti La nuova lista di utenti.
     */
    public static void setListaUtenti(ObservableList<Utente> listaUtenti) {
        Biblioteca.listaUtenti = listaUtenti;
    }

    /**
     * @brief Restituisce la lista osservabile dei prestiti.
     * @return ObservableList contenente oggetti Prestito.
     */
    public static ObservableList<Prestito> getListaPrestiti() {
        return listaPrestiti;
    }

    /**
     * @brief Imposta la lista dei prestiti.
     * @param listaPrestiti La nuova lista di prestiti.
     */
    public static void setListaPrestiti(ObservableList<Prestito> listaPrestiti) {
        Biblioteca.listaPrestiti = listaPrestiti;
    }

    
    /**
     * @brief Carica tutti i dati della biblioteca dagli archivi su file.
     * Inizializza le liste (Libri, Utenti, Prestiti), carica la configurazione
     * e prepara il sistema di invio email. Se i file non esistono, crea le strutture necessarie.
     * @post Le liste sono popolate con i dati letti da disco.
     */
    public static void carica() {
        listaLibri = Archivio.carica(Archivio.fileLibri);
        listaUtenti = Archivio.carica(Archivio.fileUtenti);
        listaPrestiti = Archivio.carica(Archivio.filePrestiti);
        configurazione = new Configurazione();

        if (!Archivio.cartellaData.exists()) Archivio.cartellaData.mkdir();

        if (!Configurazione.configurazioneFile.exists()) {
            configurazione.salvaDefault(Configurazione.configurazioneFile);
        }

        try {
            configurazione.carica(Configurazione.configurazioneFile);
            Email.carica();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Impossibile caricare la configurazione!").showAndWait();
        }
    }

     /**
     * @brief Aggiorna lo stato di sospensione di tutti gli utenti.
     * Verifica per ogni utente se la sospensione è scaduta e la revoca se necessario.
     * Salva le modifiche su file.
     * @post Gli utenti con sospensioni scadute vengono riattivati.
     */
    public static void togliSospensioni() {
        for (Utente u : listaUtenti) {
            u.aggiornaSospensione(true);
        }
        Archivio.scrivi(listaUtenti, Archivio.fileUtenti);
    }

  
     /**
     * @brief Cerca un utente tramite il suo UUID.
     * 
     * @param uuid L'identificativo univoco dell'utente.
     * @return L'oggetto Utente se trovato, altrimenti null.
     */
    public static Utente ottieniUtenteDaID(UUID uuid) {
        for (Utente u : listaUtenti) {
            if (u.getUUID().equals(uuid)) return u;
        }
        return null;
    }

    /**
     * @brief Cerca un libro tramite il suo UUID.
     * 
     * @param uuid L'identificativo univoco del libro.
     * @return L'oggetto Libro se trovato, altrimenti null.
     */
    public static Libro ottieniLibroDaID(UUID uuid) {
        for (Libro l : listaLibri) {
            if (l.getUUID().equals(uuid)) return l;
        }
        return null;
    }

   
    /**
     * @brief Aggiunge un nuovo libro alla biblioteca.
     * @pre Il libro non deve essere già presente nella lista (controllo su ISBN).
     * @post Il libro viene aggiunto alla lista in memoria e salvato su file.
     * @param l L'oggetto Libro da aggiungere.
     * @return true se l'operazione ha successo.
     * @throws LibroGiaEsistenteException Se esiste già un libro con lo stesso ISBN.
     */
    public static boolean aggiungiLibro(Libro l) throws LibroGiaEsistenteException {
        boolean b = listaLibri.add(l);
        Archivio.scrivi(listaLibri, Archivio.fileLibri);
        return b;
    }

    /**
     * @brief Rimuove un libro dalla biblioteca.
     * @pre Il libro non deve avere copie attualmente in prestito.
     * @post Il libro viene rimosso dalla lista e il file aggiornato.
     * @param l Il libro da rimuovere.
     * @return true se l'operazione ha successo.
     * @throws LibroInPrestitoException Se ci sono ancora copie in prestito (totali != disponibili).
     */
    public static boolean rimuoviLibro(Libro l) throws LibroInPrestitoException {
        if (l.getNumeroCopieTotali() != l.getNumeroCopieDisponibili()) {
            int prestiti = l.getNumeroCopieTotali() - l.getNumeroCopieDisponibili();
            throw new LibroInPrestitoException("Impossibile rimuovere " + l.getTitolo() + "\n" + (prestiti) + " copi" + (prestiti == 1 ? "a è" : "e sono") + " ancora in prestito.");
        }
        boolean b = listaLibri.remove(l);
        Archivio.scrivi(listaLibri, Archivio.fileLibri);
        return b;
    }

     /**
     * @brief Restituisce una copia della lista libri ordinata per titolo.
     * @param lista La lista di libri da ordinare.
     * @return Una nuova ObservableList ordinata alfabeticamente per titolo.
     */
    public static ObservableList<Libro> ordinaLibriTitolo(ObservableList<Libro> lista) {
        ObservableList<Libro> copia = FXCollections.observableArrayList(lista);
        FXCollections.sort(copia, Comparator.comparing(l -> l.getTitolo().toLowerCase()));
        return copia;
    }

     /**
     * @brief Restituisce una copia della lista libri ordinata per autore.
     * @param lista La lista di libri da ordinare.
     * @return Una nuova ObservableList ordinata alfabeticamente per autore.
     */
    public static ObservableList<Libro> ordinaLibriAutore(ObservableList<Libro> lista) {
        ObservableList<Libro> copia = FXCollections.observableArrayList(lista);
        FXCollections.sort(copia, Comparator.comparing(Libro::getAutore));
        return copia;
    }

    /**
     * @brief Restituisce una copia della lista libri ordinata per ISBN.
     * @param lista La lista di libri da ordinare.
     * @return Una nuova ObservableList ordinata per codice ISBN.
     */
    public static ObservableList<Libro> ordinaLibriISBN(ObservableList<Libro> lista) {
        ObservableList<Libro> copia = FXCollections.observableArrayList(lista);
        FXCollections.sort(copia, Comparator.comparing(Libro::getISBN));
        return copia;
    }

    /**
     * @brief Cerca libri che corrispondono ai criteri specificati.
     * Filtra la lista dei libri in base ai campi non nulli/non vuoti dell'oggetto libro passato come filtro.
     * I criteri (Titolo, Autore, ISBN) vengono combinati in AND.
     * @param libro Un oggetto Libro contenente i termini di ricerca.
     * @return Una ObservableList contenente i libri che soddisfano i criteri.
     */
    public static ObservableList<Libro> cercaLibro(Libro libro) {
        ObservableList<Libro> risultati = FXCollections.observableArrayList(listaLibri);
        if (libro == null) return risultati;

        Iterator<Libro> it = risultati.iterator();
        while (it.hasNext()) {
            Libro l = it.next();
            String titoloRicerca = libro.getTitolo() != null ? libro.getTitolo() : "";
            String autoreRicerca = libro.getAutore() != null ? libro.getAutore() : "";
            String isbnRicerca = libro.getISBN() != null ? libro.getISBN() : "";

            String titoloLibro = l.getTitolo() != null ? l.getTitolo() : "";
            String autoreLibro = l.getAutore() != null ? l.getAutore() : "";
            String isbnLibro = l.getISBN() != null ? l.getISBN() : "";

            boolean titolo = titoloRicerca.isEmpty() || titoloLibro.toLowerCase().contains(titoloRicerca.toLowerCase());
            boolean autore = autoreRicerca.isEmpty() || autoreLibro.toLowerCase().contains(autoreRicerca.toLowerCase());
            boolean isbn = isbnRicerca.isEmpty() || isbnLibro.toLowerCase().contains(isbnRicerca.toLowerCase());
            boolean copie = libro.getNumeroCopieDisponibili() == 0 || l.getNumeroCopieDisponibili() == libro.getNumeroCopieDisponibili();
            boolean anno = libro.getAnnoPubblicazione() == 0 || l.getAnnoPubblicazione() == libro.getAnnoPubblicazione();

            if (!(titolo && autore && isbn && copie && anno)) it.remove();
        }
        return risultati;
    }

  /**
     * @brief Aggiunge un nuovo utente al sistema.
     * Verifica l'univocità della matricola e dell'email. Invia un'email di benvenuto.
     * @pre L'utente non deve esistere (matricola e email uniche).
     * @post Utente aggiunto, email inviata e dati salvati su disco.
     * @param u L'utente da registrare.
     * @return true se l'aggiunta ha successo, false se l'oggetto è null.
     * @throws UtenteGiaEsistenteException Se matricola o email sono duplicati.
     */
    public static boolean aggiungiUtente(Utente u) {
        if (u == null) {
            return false;
        }
        if (listaUtenti.contains(u)) {
            throw new UtenteGiaEsistenteException("E' già presente un utente con matricola " + u.getMatricola());
        }
        if (!trovaDaEmail(u.getEmail()).isEmpty()) {
            throw new UtenteGiaEsistenteException("E' già presente un utente con e-mail " + u.getEmail());
        }
        boolean b = listaUtenti.add(u);

        HashMap<String, String> sostituzioni = new HashMap<>();
        sostituzioni.put("nome", u.getNome());
        sostituzioni.put("cognome", u.getCognome());
        sostituzioni.put("email", u.getEmail());
        sostituzioni.put("matricola", u.getMatricola());
        Runnable runnable = () -> {
            try {
                Email.mandaMailPagina(u.getEmail(), "Benvenuto!", "registrazione", sostituzioni);
            } catch (MessagingException ignored) {
            }
        };
        runnable.run();

        Archivio.scrivi(listaUtenti, Archivio.fileUtenti);
        return b;
    }

    /**
     * @brief Rimuove un utente dal sistema.
     * @pre L'utente non deve avere prestiti attivi.
     * @post Utente rimosso e dati salvati.
     * @param u L'utente da rimuovere.
     * @return true se rimosso con successo.
     * @throws UtenteHaPrestitiException Se l'utente ha ancora libri in prestito.
     */
    public static boolean rimuoviUtente(Utente u) {
        if (u.conteggioPrestiti() != 0) {
            throw new UtenteHaPrestitiException("Non è possibile rimuovere" + u.getNome() + "" + u.getCognome() + " perchè ha " + u.conteggioPrestiti() + "prestit" + ((u.conteggioPrestiti()) == 1 ? "o" : "i") + " attiv" + ((u.conteggioPrestiti()) == 1 ? "o" : "i"));
        }
        boolean b = listaUtenti.remove(u);
        Archivio.scrivi(listaUtenti, Archivio.fileUtenti);
        return b;
    }

     /**
     * @brief Cerca utenti tramite indirizzo email.
     * @param email L'indirizzo email da cercare (corrispondenza esatta).
     * @return ObservableList contenente gli utenti trovati.
     */
    public static ObservableList<Utente> trovaDaEmail(String email) {
        return FXCollections.observableArrayList(
                listaUtenti.stream().filter(u -> u.getEmail().equals(email)).collect(Collectors.toList()));
    }

     /**
     * @brief Ordina una lista di utenti per Cognome (e poi Nome).
     * @param lista La lista di utenti.
     * @return Nuova lista ordinata.
     */
    public static ObservableList<Utente> ordinaUtentiCognome(ObservableList<Utente> lista) {
        ObservableList<Utente> copia = FXCollections.observableArrayList(lista);
        FXCollections.sort(copia, Comparator.comparing(Utente::getCognome).thenComparing(Utente::getNome));
        return copia;
    }

     /**
     * @brief Ordina una lista di utenti per Nome (e poi Cognome).
     * @param lista La lista di utenti.
     * @return Nuova lista ordinata.
     */
    public static ObservableList<Utente> ordinaUtentiNome(ObservableList<Utente> lista) {
        ObservableList<Utente> copia = FXCollections.observableArrayList(lista);
        FXCollections.sort(copia, Comparator.comparing(Utente::getNome).thenComparing(Utente::getCognome));
        return copia;
    }

     /**
     * @brief Ordina una lista di utenti per Matricola.
     * @param lista La lista di utenti.
     * @return Nuova lista ordinata.
     */
    public static ObservableList<Utente> ordinaUtentiMatricola(ObservableList<Utente> lista) {
        ObservableList<Utente> copia = FXCollections.observableArrayList(lista);
        FXCollections.sort(copia, Comparator.comparing(u -> u.getMatricola().toLowerCase()));
        return copia;
    }

    /**
     * @brief Cerca utenti in base a criteri parziali.
     * Filtra la lista utenti controllando matricola, nome, cognome ed email (match parziale case-insensitive).
     * @param utente Oggetto "dummy" contenente i campi da usare come filtro.
     * @return ObservableList con gli utenti che corrispondono ai criteri.
     */
    public static ObservableList<Utente> cercaUtente(Utente utente) {
        ObservableList<Utente> risultati = FXCollections.observableArrayList(listaUtenti);
        if (utente == null) return risultati;

        Iterator<Utente> it = risultati.iterator();
        while (it.hasNext()) {
            Utente u = it.next();
            boolean matricola = utente.getMatricola().isEmpty() || u.getMatricola().toLowerCase().contains(utente.getMatricola().toLowerCase());
            boolean nome = utente.getNome().isEmpty() || u.getNome().toLowerCase().contains(utente.getNome().toLowerCase());
            boolean cognome = utente.getCognome().isEmpty() || u.getCognome().toLowerCase().contains(utente.getCognome().toLowerCase());
            boolean email = utente.getEmail().isEmpty() || u.getEmail().toLowerCase().contains(utente.getEmail().toLowerCase());

            if (!(matricola && nome && cognome && email)) it.remove();
        }
        return risultati;
    }

    /**
     * @brief Registra un nuovo prestito.
     * Aggiunge il prestito alla lista, decrementa le copie disponibili del libro,
     * invia una mail di conferma (opzionale) e salva tutto su file.
     * @post Prestito creato, copie libro decrementate.
     * @param p Il prestito da registrare.
     * @param email true per inviare email di conferma all'utente.
     * @return true se l'operazione ha successo.
     */
    public static boolean aggiungiPrestito(Prestito p, boolean email) {
        boolean b = listaPrestiti.add(p);
        if (email) {
            HashMap<String, String> sostituzioni = new HashMap<>();
            sostituzioni.put("id", p.getUUID().toString());
            sostituzioni.put("titolo", p.getLibro().getTitolo());
            sostituzioni.put("di", p.getDataInizio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            sostituzioni.put("dr", p.getDataRestituzionePrevista().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            Runnable runnable = () -> {
                try {
                    Email.mandaMailPagina(p.getUtente().getEmail(), "Prestito registrato con successo!", "prestito", sostituzioni);
                } catch (MessagingException ignored) {

                }
            };
            runnable.run();
        }

        p.getLibro().decrementaCopieDisponibili();
        Archivio.scrivi(listaLibri, Archivio.fileLibri);
        Archivio.scrivi(listaPrestiti, Archivio.filePrestiti);
        return b;
    }

    /**
     * @brief Rimuove (chiude) un prestito esistente.
     * Rimuove il prestito dalla lista attiva e incrementa le copie disponibili del libro restituito.
     * @post Prestito rimosso, copie libro incrementate.
     * @param p Il prestito da rimuovere.
     * @return true se rimosso con successo.
     */
    public static boolean rimuoviPrestito(Prestito p) {
        p.getLibro().incrementaCopieDisponibili();
        boolean b = listaPrestiti.remove(p);
        Archivio.scrivi(listaLibri, Archivio.fileLibri);
        Archivio.scrivi(listaPrestiti, Archivio.filePrestiti);
        return b;
    }

     /**
     * @brief Ordina i prestiti per data di inizio.
     * @param lista La lista di prestiti.
     * @return Nuova lista ordinata cronologicamente.
     */
    public static ObservableList<Prestito> ordinaPrestitiDataInizio(ObservableList<Prestito> lista) {
        ObservableList<Prestito> copia = FXCollections.observableArrayList(lista);
        FXCollections.sort(copia, Comparator.comparing(Prestito::getDataInizio));
        return copia;
    }

     /**
     * @brief Ordina i prestiti per data di restituzione prevista.
     * @param lista La lista di prestiti.
     * @return Nuova lista ordinata per scadenza.
     */
    public static ObservableList<Prestito> ordinaPrestitiDataRestituzionePrevista(ObservableList<Prestito> lista) {
        ObservableList<Prestito> copia = FXCollections.observableArrayList(lista);
        FXCollections.sort(copia, Comparator.comparing(Prestito::getDataRestituzionePrevista));
        return copia;
    }

     /**
     * @brief Ordina i prestiti per ISBN del libro associato.
     * @param lista La lista di prestiti.
     * @return Nuova lista ordinata.
     */
    public static ObservableList<Prestito> ordinaPrestitiISBN(ObservableList<Prestito> lista) {
        ObservableList<Prestito> copia = FXCollections.observableArrayList(lista);
        FXCollections.sort(copia, Comparator.comparing(
                p -> {
                    Libro l = p.getLibro();
                    return l != null ? l.getISBN().toLowerCase() : "";
                }
        ));
        return copia;
    }

     /**
     * @brief Ordina i prestiti per Matricola dell'utente associato.
     * @param lista La lista di prestiti.
     * @return Nuova lista ordinata.
     */
    public static ObservableList<Prestito> ordinaPrestitiMatricola(ObservableList<Prestito> lista) {
        ObservableList<Prestito> copia = FXCollections.observableArrayList(lista);
        FXCollections.sort(copia, Comparator.comparing(
                p -> {
                    Utente u = p.getUtente();
                    return u != null ? u.getMatricola().toLowerCase() : "";
                }
        ));
        return copia;
    }

 /**
     * @brief Cerca prestiti in base a criteri specifici.
     * Filtra la lista controllando corrispondenze esatte su Libro, Utente o date.
     * @param prestito Oggetto contenente i criteri di ricerca.
     * @return ObservableList con i prestiti trovati.
     */
    public static ObservableList<Prestito> cercaPrestito(Prestito prestito) {
        ObservableList<Prestito> risultati = FXCollections.observableArrayList(listaPrestiti);
        if (prestito == null) return risultati;

        Iterator<Prestito> it = risultati.iterator();
        while (it.hasNext()) {
            Prestito p = it.next();
            boolean libro = prestito.getLibro() == null || p.getLibro().equals(prestito.getLibro());
            boolean utente = prestito.getUtente() == null || p.getUtente().equals(prestito.getUtente());
            boolean dataInizio = prestito.getDataInizio() == null || p.getDataInizio().equals(prestito.getDataInizio());
            boolean dataRest = prestito.getDataRestituzionePrevista() == null || p.getDataRestituzionePrevista().equals(prestito.getDataRestituzionePrevista());

            if (!(libro && utente && dataInizio && dataRest)) it.remove();
        }
        return risultati;
    }
}
