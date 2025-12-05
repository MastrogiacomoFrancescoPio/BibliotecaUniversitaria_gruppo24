package it.unisa.diem.ids.bibliotecauniversitaria;

import java.time.LocalDate;
import java.util.List;

public class Biblioteca {

    private List<Libro> libri;

    private List<Utente> utenti;

    private List<Prestito> prestiti;

    /**
     * @param libro
 * @brief Aggiunge un nuovo libro al catalogo della biblioteca.
 *
 * Questo metodo implementa il flusso principale del caso d'uso
 * "Inserimento Libro".
 *
 * @pre Il personale ha effettuato l'accesso (UI-1).
 * @pre Il personale visualizza l'interfaccia "Menu" (UI-4).
 *
 * @post Il catalogo dei libri è aggiornato con i dati del libro aggiunto.
 */
    public void aggiungiLibro(Libro libro) {
    }

    /**
     * @param isbn identificativo univoco del libro
 * @brief Rimuove un libro specifico dal catalogo della biblioteca.
 *
 * Questo metodo implementa il flusso principale del caso d'uso
 * "Rimozione Libro". Rimuove il libro selezionato dall'archivio (IF-1).
 *
 * @pre Il personale ha effettuato l'accesso al sistema (UI-1).
 * @pre Il personale visualizza l'interfaccia "Menu" (UI-4).
 * @pre Deve essere stato selezionato un libro da rimuovere.
 *
 * @post Il libro rimosso non compare più nel catalogo libri.
 */
    public void rimuoviLibro(String isbn) {
    }

    /**
 * @brief Ricerca e restituisce un singolo libro tramite il suo codice ISBN.
 *
 * Implementa una ricerca specifica del caso d'uso "Ricerca Libro" (IF-5.3).
 * Poiché l'ISBN è univoco, restituisce al massimo un singolo risultato.
 *
 * @param isbn Il codice ISBN (International Standard Book Number) del libro da ricercare.
 *
 * @return L'oggetto Libro corrispondente all'ISBN fornito, o {@code null} se non trovato.
 *
 * @pre Il personale ha effettuato l'accesso al sistema (UI-1).
 * @pre Il personale visualizza l'interfaccia "Menu" (UI-4).
 *
 * @post Il personale visualizza i dati del libro ricercato, se trovato.
 */
    public Libro ricercaLibroPerISBN(String isbn) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
/**
 * @brief Ricerca e restituisce una lista di libri corrispondenti a un titolo.
 *
 * Implementa una ricerca generica del caso d'uso "Ricerca Libro"  (IF-5.1).
 * Potrebbe restituire più risultati, dato che titoli identici possono esistere per edizioni diverse.
 *
 * @param titolo Il titolo  del libro da ricercare.
 *
 * @return Una {@code List<Libro>} contenente tutti i libri che corrispondono al titolo inserito.
 * Restituisce una lista vuota se nessun libro corrisponde ai criteri di ricerca.
 *
 * @pre Il personale ha effettuato l'accesso al sistema (UI-1).
 * @pre Il personale visualizza l'interfaccia "Menu" (UI-4).
 *
 * @post Il personale visualizza tutti i libri corrispondenti ai dati inseriti.
 */
    public List<Libro> ricercaLibroPerTitolo(String titolo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
/**
 * @brief Ricerca e restituisce una lista di libri scritti da un autore specifico.
 *
 * Implementa una ricerca generica del caso d'uso "Ricerca Libro" (dato IF-5.2).
 *
 * @param autore Il nome e/o cognome dell'autore da ricercare.
 *
 * @return Una {@code List<Libro>} contenente tutti i libri scritti dall'autore specificato.
 * Restituisce una lista vuota se nessun libro corrisponde ai criteri di ricerca.
 *
 * @pre Il personale ha effettuato l'accesso al sistema (UI-1).
 * @pre Il personale visualizza l'interfaccia "Menu" (UI-4).
 *
 * @post Il personale visualizza tutti i libri corrispondenti ai dati inseriti.
 */
    public List<Libro> ricercaLibroPerAutore(String autore) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
 * @brief Restituisce la lista completa dei libri presenti nel catalogo, ordinata per codice ISBN.
 *
 * Implementa l'ordinamento richiesto nel caso d'uso "Visualizzazione lista libri" (IF-4.3).
 *
 * @return Una {@code List<Libro>} contenente tutti i libri del catalogo, ordinati in base al codice ISBN (crescente).
 *
 * @pre Il personale ha effettuato l'accesso al sistema (UI-1).
 * @pre Il personale visualizza l'interfaccia "Menu" (UI-4).
 *
 * @post Il personale visualizza a schermo la tabella dei libri secondo l'ordine selezionato.
 */
    public List<Libro> ottieniLibriOrdinatiPerISBN() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
 * @brief Restituisce la lista completa dei libri presenti nel catalogo, ordinata per titolo.
 *
 * Implementa l'ordinamento richiesto nel caso d'uso "Visualizzazione lista libri" (IF-4.1).
 *
 * @return Una {@code List<Libro>} contenente tutti i libri del catalogo, ordinati alfabeticamente per titolo.
 *
 * @pre Il personale ha effettuato l'accesso al sistema (UI-1).
 * @pre Il personale visualizza l'interfaccia "Menu" (UI-4).
 *
 * @post Il personale visualizza a schermo la tabella dei libri secondo l'ordine selezionato.
 */
    public List<Libro> ottieniLibriOrdinatiPerTitolo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /**
 * @brief Restituisce la lista completa dei libri presenti nel catalogo, ordinata per autore.
 *
 * Implementa l'ordinamento richiesto nel caso d'uso "Visualizzazione lista libri" (IF-4.2).
 *
 * @return Una {@code List<Libro>} contenente tutti i libri del catalogo, ordinati alfabeticamente per nome dell'autore.
 *
 * @pre Il personale ha effettuato l'accesso al sistema (UI-1).
 * @pre Il personale visualizza l'interfaccia "Menu" (UI-4).
 *
 * @post Il personale visualizza a schermo la tabella dei libri secondo l'ordine selezionato.
 */

    public List<Libro> ottieniLibriOrdinatiPerAutore() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
 * @brief Aggiunge un nuovo utente al sistema e all'archivio della biblioteca.
 *
 * Questo metodo implementa il flusso principale del caso d'uso "Inserimento Utenti". 
 * Il sistema memorizza i dati dell'utente (DF-2) nell'archivio (IF-1).
 *
 * @param utente L'oggetto Utente contenente tutti i campi inerenti all'utente da aggiungere (DF-2).
 *
 * @pre Il personale ha effettuato l'accesso al sistema.
 * @pre Il personale visualizza l'interfaccia "Menu" (UI-4).
 *
 * @post Un nuovo utente è inserito nel sistema.
 */
    public void aggiungiUtente(Utente utente) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
 * @brief Rimuove un utente specifico dal sistema tramite la sua matricola.
 *
 * Questo metodo implementa il flusso principale del caso d'uso "Rimozione Utenti".
 * Prima della rimozione, il sistema esegue un controllo
 * per verificare la presenza di prestiti attivi (IF-9.4).
 * Se non ci sono prestiti attivi, i dati dell'utente (DF-2)
 * vengono rimossi dall'archivio (IF-1).
 *
 * @param matricola La matricola (identificativo univoco) dell'utente da rimuovere.
 *
 * @pre Il personale ha effettuato l'accesso al sistema.
 * @pre Il personale visualizza l'interfaccia "Menu" (UI-4).
 *
 * @post Un utente viene rimosso dal sistema.
 */
    public void rimuoviUtente(String matricola) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
 * @brief Ricerca e restituisce un singolo utente tramite la sua matricola.
 *
 * Implementa una ricerca specifica del caso d'uso "Ricerca Utenti" (IF-11.2).
 * Poiché la matricola è univoca, restituisce al massimo un singolo risultato.
 *
 * @param matricola La matricola (identificativo univoco) dell'utente da cercare.
 *
 * @return L'oggetto Utente corrispondente alla matricola fornita, o {@code null} se non trovato.
 *
 * @pre Il personale ha effettuato l'accesso al sistema.
 * @pre Il personale visualizza l'interfaccia "Menu" (UI-4).
 *
 * @post La riga della tabella degli utenti ricercati appare evidenziata.
 */
    public Utente cercaUtentePerMatricola(String matricola) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
/**
 * @brief Ricerca e restituisce una lista di utenti con un cognome corrispondente.
 *
 * Implementa una ricerca generica del caso d'uso "Ricerca Utenti" IF-11.1).
 * Potrebbe restituire più risultati.
 *
 * @param cognome Il cognome (o una parte di esso) dell'utente da ricercare.
 *
 * @return Una {@code List<Utente>} contenente tutti gli utenti che corrispondono al cognome inserito.
 * Restituisce una lista vuota se nessun utente corrisponde ai criteri di ricerca.
 *
 * @pre Il personale ha effettuato l'accesso al sistema.
 * @pre Il personale visualizza l'interfaccia "Menu" (UI-4).
 *
 * @post La riga della tabella degli utenti ricercati appare evidenziata.
 */
    public List<Utente> cercaUtentePerCognome(String cognome) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
 * @brief Restituisce la lista completa degli utenti presenti nel sistema, ordinata per cognome e nome.
 *
 * Implementa l'ordinamento richiesto nel caso d'uso "Visualizzazione lista Utenti" (IF-10.1).
 *
 * @return Una {@code List<Utente>} contenente tutti gli utenti del sistema, ordinati alfabeticamente per cognome e nome.
 *
 * @pre Il personale ha effettuato l'accesso al sistema.
 * @pre Il personale visualizza l'interfaccia "Menu" (UI-4).
 *
 * @post Il personale visualizza la lista degli utenti in base all'ordine scelto.
 */
    public List<Utente> ottieniUtentiOrdinatiPerCognomeNome() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
 * @brief Restituisce la lista completa degli utenti presenti nel sistema, ordinata per matricola.
 *
 * Implementa l'ordinamento richiesto nel caso d'uso "Visualizzazione lista Utenti" (IF-10.2).
 *
 * @return Una {@code List<Utente>} contenente tutti gli utenti del sistema, ordinati in base alla matricola.
 *
 * @pre Il personale ha effettuato l'accesso al sistema.
 * @pre Il personale visualizza l'interfaccia "Menu" (UI-4).
 *
 * @post Il personale visualizza la lista degli utenti in base all'ordine scelto.
 */
    
    public List<Utente> ottieniUtentiOrdinatiPerMatricola() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
 * @brief Registra un nuovo prestito nel sistema per un libro e un utente specifici, dopo aver effettuato i controlli di validità.
 *
 * Questo metodo implementa il flusso principale del caso d'uso "Registrazione Prestito",
 *
 * @param libro L'oggetto Libro che viene prestato.
 * @param utente L'oggetto Utente a cui viene effettuato il prestito.
 * @param dataInizio La data effettiva di inizio del prestito.
 * @param dataRestitPrevista La data prevista per la restituzione (IF-6.2).
 *
 * @return La data di inizio prestito, come conferma dell'avvenuta registrazione.
 *
 * @pre L'utente e il libro devono essere già registrati nell'archivio.
 * @pre Il libro deve avere copie disponibili (IF-6.4).
 * @pre L'utente non deve avere più di 3 prestiti contemporaneamente (IF-6.5).
 * @pre L'utente non deve avere consegne in ritardo (IF-6.6).
 * @pre L'utente non deve essere sospeso (IF-6.7).
 * @pre Il personale ha effettuato l'accesso al sistema e visualizza il menu (UI-4).
 *
 * @post Il prestito è registrato con data di inizio e di restituzione prevista.
 * @post Il numero di copie disponibili del libro è decrementato.
 * @post Il numero di prestiti dell'utente è incrementato.
 */
    public LocalDate registraPrestito(Libro libro, Utente utente, LocalDate dataInizio, LocalDate dataRestitPrevista) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
 * @brief Registra la restituzione di un prestito, aggiornando il catalogo.
 *
 * Questo metodo implementa il flusso principale del caso d'uso "Registrazione Restituzione".
 *
 * @param prestito L'oggetto {@link Prestito} relativo al libro restituito.
 * @param dataRestituzione La data effettiva in cui il prestito viene restituito.
 *
 * @return La data di restituzione registrata, come conferma dell'avvenuta operazione.
 *
 * @pre Il personale ha effettuato l'accesso al sistema.
 * @pre Il libro deve essere registrato come prestito in precedenza.
 * @pre Il personale visualizza l'interfaccia "Menu" (UI-4).
 *
 * @post Il prestito relativo al libro restituito non compare più nell'elenco dei prestiti attivi.
 * @post Il numero di copie disponibili del libro è incrementato.
 * @post Il numero di prestiti dell'utente è decrementato.
 */
    public LocalDate registraRestituzione(Prestito prestito, LocalDate dataRestituzione) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
 * @brief Restituisce la lista di tutti i prestiti attivi, ordinata in base alla data di inizio del prestito.
 *
 * Implementa l'ordinamento richiesto nel caso d'uso "Visualizzazione lista prestiti" (IF-7.1).
 *
 * @return Una {@code List<Prestito>} contenente tutti i prestiti attivi, ordinati in base alla data di inizio (crescente).
 *
 * @pre Il personale ha effettuato l'accesso al sistema.
 * @pre Il personale visualizza l'interfaccia "Menu" (UI-4).
 *
 * @post Il personale visualizza a schermo la lista dei prestiti attivi secondo l'ordine selezionato.
 */
    public List<Prestito> ottieniPrestitiOrdinatiPerDatalnizio() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
 * @brief Restituisce la lista di tutti i prestiti attivi, ordinata in base alla data di restituzione prevista.
 *
 * Implementa l'ordinamento richiesto nel caso d'uso "Visualizzazione lista prestiti" (IF-7.2).
 *
 * @return Una {@code List<Prestito>} contenente tutti i prestiti attivi, ordinati in base alla data di restituzione prevista (crescente).
 *
 * @pre Il personale ha effettuato l'accesso al sistema.
 * @pre Il personale visualizza l'interfaccia "Menu" (UI-4).
 *
 * @post Il personale visualizza a schermo la lista dei prestiti attivi secondo l'ordine selezionato.
 */
    public List<Prestito> ottieniPrestitiOrdinatiPerDataRestituzione() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
 * @brief Salva lo stato corrente dell'intera biblioteca (libri, utenti, prestiti) su un file di archivio.
 *
 * Questo metodo serializza e persiste tutti i dati gestiti dal sistema in un file,
 * permettendone il recupero in sessioni successive.
 *
 * @pre Il sistema deve fornire tutti i dati per il salvataggio.
 *
 * @post I dati attuali della biblioteca (libri, utenti, prestiti) sono stati scritti sul file di archivio.
 */
    public void salvaBiblioteca() {
    }
    /**
 * @brief Carica lo stato precedente della biblioteca da un file di archivio.
 *
 * Questo metodo deserializza i dati contenuti nel file di archivio e inizializza il sistema
 * (libri, utenti, prestiti) con lo stato salvato.
 *
 * @pre Il file deve contenere dati in un formato valido.
 *
 * @post Il catalogo dei libri, la lista degli utenti e la lista dei prestiti attivi sono stati caricati in memoria.
 */
    public void caricaBiblioteca() {
    }
}
