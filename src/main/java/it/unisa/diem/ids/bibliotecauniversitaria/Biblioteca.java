package it.unisa.diem.ids.bibliotecauniversitaria;


import java.time.LocalDate;
import java.util.List;

public class Biblioteca {

    private List<Libro> libri;

    private List<Utente> utenti;

    private List<Prestito> prestiti;

    public Libro aggiungiLibro(Libro libro) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String rimuoviLibro(String isbn) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String ricercaLibroPerISBN(String isbn) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String ricercaLibroPerTitolo(String titolo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String ricercaLibroPerAutore(String autore) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Libro> ottieniLibriOrdinatiPerISBN() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Libro> ottieniLibriOrdinatiPerTitolo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Libro> ottieniLibriOrdinatiPerAutore() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Utente aggiungiUtente(Utente utente) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String rimuoviUtente(String matricola) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String cercaUtentePerMatricola(String matricola) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String cercaUtentePerCognome(String cognome) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Utente> ottieniUtentiOrdinatiPerCognomeNome() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Utente> ottieniUtentiOrdinatiPerMatricola() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public LocalDate registraPrestito(Libro libro, Utente utente, LocalDate dataInizio, LocalDate dataRestitPrevista) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public LocalDate registraRestituzione(Prestito prestito, LocalDate dataRestituzione) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Prestito> ottieniPrestitiOrdinatiPerDatalnizio() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Prestito> ottieniPrestitiOrdinatiPerDataRestituzione() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void salvaBiblioteca() {
    }

    public void caricaBiblioteca() {
    }
}
