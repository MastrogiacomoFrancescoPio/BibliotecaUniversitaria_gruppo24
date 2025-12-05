package it.unisa.diem.ids.bibliotecauniversitaria;


import it.unisa.diem.ids.bibliotecauniversitaria.Prestito;
import java.util.List;

public class Utente {

    private String matricola;

    private String nome;

    private String cognome;

    private boolean sospeso;

    private int segnalazioni;

    private List<Prestito> prestitiAttivi;

    public boolean isSospeso() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean verificaPrestitiInRitardo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Prestito aggiungiPrestito(Prestito prestito) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Prestito rimuoviPrestito(Prestito prestito) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void incrementaSegnalazioni() {
    }

    public int controllaSegnalazioni() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int sospendi(int giorni) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
