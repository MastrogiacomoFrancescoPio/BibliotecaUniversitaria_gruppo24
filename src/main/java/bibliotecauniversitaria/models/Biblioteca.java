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
import javax.mail.internet.AddressException;

public class Biblioteca implements Serializable {

    private static ObservableList<Libro> listaLibri = FXCollections.observableArrayList();
    private static ObservableList<Utente> listaUtenti = FXCollections.observableArrayList();
    private static ObservableList<Prestito> listaPrestiti = FXCollections.observableArrayList();

    public static Configurazione configurazione;

 
    public Configurazione getConfigurazione() {
        return configurazione;
    }

    public static ObservableList<Libro> getListaLibri() {
        return listaLibri;
    }

    public static void setListaLibri(ObservableList<Libro> listaLibri) {
        Biblioteca.listaLibri = listaLibri;
    }

    public static ObservableList<Utente> getListaUtenti() {
        return listaUtenti;
    }

    public static void setListaUtenti(ObservableList<Utente> listaUtenti) {
        Biblioteca.listaUtenti = listaUtenti;
    }

    public static ObservableList<Prestito> getListaPrestiti() {
        return listaPrestiti;
    }

    public static void setListaPrestiti(ObservableList<Prestito> listaPrestiti) {
        Biblioteca.listaPrestiti = listaPrestiti;
    }

    
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

    public static void togliSospensioni() {
        for (Utente u : listaUtenti) {
            u.aggiornaSospensione(true);
        }
        Archivio.scrivi(listaUtenti, Archivio.fileUtenti);
    }

  
    public static Utente ottieniUtenteDaID(UUID uuid) {
        for (Utente u : listaUtenti) {
            if (u.getUUID().equals(uuid)) return u;
        }
        return null;
    }

    public static Libro ottieniLibroDaID(UUID uuid) {
        for (Libro l : listaLibri) {
            if (l.getUUID().equals(uuid)) return l;
        }
        return null;
    }

   
    public static boolean aggiungiLibro(Libro l) throws LibroGiaEsistenteException {
        if (getListaLibri().contains(l)) {
            throw new LibroGiaEsistenteException("E' già presente un libro con ISBN" + l.ISBNProperty().get());
        }
        boolean b = listaLibri.add(l);
        Archivio.scrivi(listaLibri, Archivio.fileLibri);
        return b;
    }

    public static boolean rimuoviLibro(Libro l) throws LibroInPrestitoException {
        if (l.getNumeroCopieTotali() != l.getNumeroCopieDisponibili()) {
            int prestiti = l.getNumeroCopieTotali() - l.getNumeroCopieDisponibili();
            throw new LibroInPrestitoException("Impossibile rimuovere " + l.getTitolo() + "\n" + (prestiti) + " copi" + (prestiti == 1 ? "a è" : "e sono") + " ancora in prestito.");
        }
        boolean b = listaLibri.remove(l);
        Archivio.scrivi(listaLibri, Archivio.fileLibri);
        return b;
    }

    public static ObservableList<Libro> ordinaLibriTitolo(ObservableList<Libro> lista) {
        ObservableList<Libro> copia = FXCollections.observableArrayList(lista);
        FXCollections.sort(copia, Comparator.comparing(l -> l.getTitolo().toLowerCase()));
        return copia;
    }

    public static ObservableList<Libro> ordinaLibriAutore(ObservableList<Libro> lista) {
        ObservableList<Libro> copia = FXCollections.observableArrayList(lista);
        FXCollections.sort(copia, Comparator.comparing(Libro::getAutore));
        return copia;
    }

    public static ObservableList<Libro> ordinaLibriISBN(ObservableList<Libro> lista) {
        ObservableList<Libro> copia = FXCollections.observableArrayList(lista);
        FXCollections.sort(copia, Comparator.comparing(Libro::getISBN));
        return copia;
    }


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

  
    public static boolean aggiungiUtente(Utente u) {
        if (u == null) {
            return false;
        }
        if (listaUtenti.contains(u)) {
            throw new UtenteGiaEsistenteException("E' già presente un utente con matricola " + u.getMatricola());
        }
        if (u == null) {
            return false;
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

    public static boolean rimuoviUtente(Utente u) {
        if (u.conteggioPrestiti() != 0) {
            throw new UtenteHaPrestitiException("Non è possibile rimuovere" + u.getNome() + "" + u.getCognome() + " perchè ha " + u.conteggioPrestiti() + "prestit" + ((u.conteggioPrestiti()) == 1 ? "o" : "i") + " attiv" + ((u.conteggioPrestiti()) == 1 ? "o" : "i"));
        }
        boolean b = listaUtenti.remove(u);
        Archivio.scrivi(listaUtenti, Archivio.fileUtenti);
        return b;
    }

    public static ObservableList<Utente> trovaDaEmail(String email) {
        return FXCollections.observableArrayList(
                listaUtenti.stream().filter(u -> u.getEmail().equals(email)).collect(Collectors.toList()));
    }

    public static ObservableList<Utente> ordinaUtentiCognome(ObservableList<Utente> lista) {
        ObservableList<Utente> copia = FXCollections.observableArrayList(lista);
        FXCollections.sort(copia, Comparator.comparing(Utente::getCognome).thenComparing(Utente::getNome));
        return copia;
    }

    public static ObservableList<Utente> ordinaUtentiNome(ObservableList<Utente> lista) {
        ObservableList<Utente> copia = FXCollections.observableArrayList(lista);
        FXCollections.sort(copia, Comparator.comparing(Utente::getNome).thenComparing(Utente::getCognome));
        return copia;
    }

    public static ObservableList<Utente> ordinaUtentiMatricola(ObservableList<Utente> lista) {
        ObservableList<Utente> copia = FXCollections.observableArrayList(lista);
        FXCollections.sort(copia, Comparator.comparing(u -> u.getMatricola().toLowerCase()));
        return copia;
    }

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

    public static boolean rimuoviPrestito(Prestito p) {
        p.getLibro().incrementaCopieDisponibili();
        boolean b = listaPrestiti.remove(p);
        Archivio.scrivi(listaLibri, Archivio.fileLibri);
        Archivio.scrivi(listaPrestiti, Archivio.filePrestiti);
        return b;
    }

    public static ObservableList<Prestito> ordinaPrestitiDataInizio(ObservableList<Prestito> lista) {
        ObservableList<Prestito> copia = FXCollections.observableArrayList(lista);
        FXCollections.sort(copia, Comparator.comparing(Prestito::getDataInizio));
        return copia;
    }

    public static ObservableList<Prestito> ordinaPrestitiDataRestituzionePrevista(ObservableList<Prestito> lista) {
        ObservableList<Prestito> copia = FXCollections.observableArrayList(lista);
        FXCollections.sort(copia, Comparator.comparing(Prestito::getDataRestituzionePrevista));
        return copia;
    }

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
