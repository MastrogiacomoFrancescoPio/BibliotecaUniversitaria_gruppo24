package bibliotecauniversitaria.models;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import it.unisa.diem.oop.aaaaa.utils.Email;
import javafx.beans.property.*;

import javax.mail.MessagingException;

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


    public String getMatricola() { return matricola.get(); }
    public void setMatricola(String matricola) { this.matricola.set(matricola); }
    public StringProperty matricolaProperty() { return matricola; }

    public String getNome() { return nome.get(); }
    public void setNome(String nome) { this.nome.set(nome); }
    public StringProperty nomeProperty() { return nome; }

    public String getCognome() { 
        return cognome.get(); 
    }
    public void setCognome(String cognome) { 
        this.cognome.set(cognome); 
    }
    public StringProperty cognomeProperty() { 
        return cognome; 
    }

    public String getEmail() { 
        return email.get(); 
    }
    public void setEmail(String email) {
        if(!Email.isValida(email)&&!email.equals("")){
            throw new IllegalArgumentException("E-mail non valida");
        }
        emailProperty().set(email);
    }
    public StringProperty emailProperty() { 
        return email; 
    }

    public int getNumeroSegnalazioni() { return numeroSegnalazioni.get(); }
    public void setNumeroSegnalazioni(int numeroSegnalazioni) throws IllegalArgumentException {
        if(numeroSegnalazioni < 0){
            throw new IllegalArgumentException("Numero segnalazioni non può essere minore di 0!");
        }
        this.numeroSegnalazioni.set(numeroSegnalazioni);
    }
    public IntegerProperty numeroSegnalazioniProperty() { return numeroSegnalazioni; }

    public boolean isSospeso() { return sospeso.get(); }
    public void setSospeso(boolean sospeso) { this.sospeso.set(sospeso); }
    public BooleanProperty sospesoProperty() { return sospeso; }

    public LocalDate getDataSospensione() { return dataSospensione.get(); }
    public void setDataSospensione(LocalDate dataSospensione) { this.dataSospensione.set(dataSospensione); }
    public ObjectProperty<LocalDate> dataSospensioneProperty() { return dataSospensione; }

    public LocalDate getDataFineSospensione() { return dataFineSospensione.get(); }
    public void setDataFineSospensione(LocalDate dataFineSospensione) { this.dataFineSospensione.set(dataFineSospensione); }
    public ObjectProperty<LocalDate> dataFineSospensioneProperty() { return dataFineSospensione; }

    public UUID getUUID() { return uuid; }


    public ArrayList<Prestito> getPrestiti() {
        ArrayList<Prestito> prestiti = new ArrayList<>();
        for (Prestito p : Biblioteca.getListaPrestiti()) {
            if (p.getUtente() == this) prestiti.add(p);
        }
        return prestiti;
    }

    public int conteggioPrestiti() { return getPrestiti().size(); }
    public boolean verificaLimitePrestitiRaggiunto() {
        return conteggioPrestiti() >= Biblioteca.configurazione.getNumero("MAX_PRESTITI");
    }


    public void incrementaSegnalazioni() { numeroSegnalazioni.set(numeroSegnalazioni.get() + 1); }
    public void resetSegnalazioni() { numeroSegnalazioni.set(0); }
    public void controllaSegnalazioni(int massime, int sospensione) {
        if (numeroSegnalazioni.get() >= massime) sospendi(sospensione,true);
    }


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

    public boolean isSospensioneScaduta() {
        LocalDate fine = dataFineSospensione.get();
        return sospeso.get() && fine != null && !fine.isAfter(LocalDate.now());
    }

    public void aggiornaSospensione(boolean email) {
        if (isSospensioneScaduta()) revocaSospensione(email);
    }


    @Override
    public int hashCode() {
        return Objects.hash(matricola.get().toLowerCase());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Utente other = (Utente) obj;
        return matricola.get() != null && matricola.get().trim().equalsIgnoreCase(other.matricola.get().trim());
    }


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
