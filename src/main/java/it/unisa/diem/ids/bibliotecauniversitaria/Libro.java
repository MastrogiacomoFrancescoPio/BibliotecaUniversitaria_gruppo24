package it.unisa.diem.ids.bibliotecauniversitaria;

public class Libro {

    private String isbn;

    private String titolo;

    private String autore;

    private int copieDisponibili;

    public void incrementaCopieDisponibili() {
    }

    public void decrementaCopieDisponibili() {
    }

    public boolean haCopieDisponibili() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
