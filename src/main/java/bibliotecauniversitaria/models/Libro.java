package bibliotecauniversitaria.models;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.Year;
import java.util.Objects;
import java.util.UUID;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Libro implements Serializable {

    private transient StringProperty titolo= new SimpleStringProperty(); 
    private transient StringProperty autore= new SimpleStringProperty(); 
    private transient StringProperty ISBN= new SimpleStringProperty(); 
    private transient IntegerProperty annoPubblicazione= new SimpleIntegerProperty();
    private transient IntegerProperty numeroCopieTotali= new SimpleIntegerProperty();
    private transient IntegerProperty numeroCopieDisponibili= new SimpleIntegerProperty();

    private transient UUID uuid = UUID.randomUUID();

    public Libro(String titolo, String autore, String ISBN, int annoPubblicazione, int numeroCopieTotali) {
        setTitolo(titolo);
        setAutore(autore);
        setISBN(ISBN);
        setAnnoPubblicazione(annoPubblicazione); 
        setNumeroCopieTotali(numeroCopieTotali);
        setNumeroCopieDisponibili(numeroCopieTotali);
    }

    public String getTitolo() {
        return titolo.get();
    }

    public void setTitolo(String titolo) {
        this.titolo.set(titolo);
    }

    public StringProperty titoloProperty() {
        return titolo;
    }

    public String getAutore() {
        return autore.get();
    }

    public void setAutore(String autore) {
        this.autore.set(autore);
    }

    public StringProperty autoreProperty() {
        return autore;
    }

    public String getISBN() {
        return ISBN.get();
    }

    public void setISBN(String ISBN) {
        this.ISBN.set(ISBN);
    }

    public StringProperty ISBNProperty() {
        return ISBN;
    }

    public int getAnnoPubblicazione() {
        return annoPubblicazione.get();
    }

    public void setAnnoPubblicazione(int annoPubblicazione) {
        int annoCorrente = Year.now().getValue();
        if (annoPubblicazione > annoCorrente) {
            throw new IllegalArgumentException("L'anno di pubblicazione (" + annoPubblicazione + ") non può essere successivo all'anno corrente (" + annoCorrente + ").");
        }
        this.annoPubblicazione.set(annoPubblicazione);
    }
    public IntegerProperty annoPubblicazioneProperty() {
        return annoPubblicazione;
    }

    public int getNumeroCopieTotali() {
        return numeroCopieTotali.get();
    }

    public void setNumeroCopieTotali(int numeroCopieTotali) {
        if (numeroCopieTotali < 0) {
            throw new IllegalArgumentException("Il numero di copie totali non può essere negativo.");
        } 
        if (getNumeroCopieDisponibili() > numeroCopieTotali) {
            throw new IllegalArgumentException("Impossibile ridurre le copie totali a " + numeroCopieTotali + " perché ci sono ancora " + getNumeroCopieDisponibili() + " copie disponibili.");
        }
        this.numeroCopieTotali.set(numeroCopieTotali);
    }

    public IntegerProperty numeroCopieTotaliProperty() {
        return numeroCopieTotali;
    }

    public int getNumeroCopieDisponibili() {
        return numeroCopieDisponibili.get();
    }

    public void setNumeroCopieDisponibili(int copie) {
        if (copie < 0) {
            throw new IllegalArgumentException("Il numero di copie disponibili non può essere negativo.");
        } else if (copie >numeroCopieTotali.get()) {
            throw new IllegalArgumentException("Il numero di copie disponibili (" + copie + ") non può superare il totale (" + numeroCopieTotali.get() + ").");
        } else {
            this.numeroCopieDisponibili.set(copie);
        }
    }

    public IntegerProperty numeroCopieDisponibiliProperty() {
        return numeroCopieDisponibili;
    }

    public UUID getUUID() {
        return uuid;
    }

    public void incrementaCopieDisponibili() {
        if (getNumeroCopieDisponibili() < getNumeroCopieTotali()) {
            numeroCopieDisponibili.set(getNumeroCopieDisponibili() + 1);
        }

    }

    public void decrementaCopieDisponibili() {
        if (getNumeroCopieDisponibili() > 0) {
            numeroCopieDisponibili.set(getNumeroCopieDisponibili() - 1);
        }
    }

    public boolean haCopieDisponibili() {
        return getNumeroCopieDisponibili() > 0;
    }

    @Override
    public int hashCode() {
        return 29 * 7 + Objects.hashCode((ISBN != null && ISBN.get() != null) ? ISBN.get().trim().toLowerCase() : "");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final Libro l = (Libro) obj;
        if (this.ISBN == null || this.ISBN.get() == null) return false;
        if (l.ISBN == null || l.ISBN.get() == null) return false;
        return this.ISBN.get().trim().equalsIgnoreCase(l.ISBN.get().trim());
    }
    
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