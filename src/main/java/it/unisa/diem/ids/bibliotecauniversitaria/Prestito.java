package it.unisa.diem.ids.bibliotecauniversitaria;


import it.unisa.diem.ids.bibliotecauniversitaria.Libro;
import java.time.LocalDate;

public class Prestito {

    private Libro libro;

    private Utente utente;

    private LocalDate dataInizio;

    private LocalDate dataRestPrevista;

    private LocalDate dataRestituzione;

    public boolean verificaRitardoMaggioreDueSettimane() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean verificaRitardoMaggioreTreSettimane() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
