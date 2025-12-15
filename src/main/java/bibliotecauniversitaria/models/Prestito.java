package bibliotecauniversitaria.models;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Prestito implements Serializable {

    /* =========================
       CAMPi
       ========================= */
    private UUID libro;
    private UUID utente;

    private transient ObjectProperty<LocalDate> dataInizio;
    private transient ObjectProperty<LocalDate> dataRestituzionePrevista;

    private transient UUID uuid = UUID.randomUUID();

    /* =========================
       COSTRUTTORE
       ========================= */
    public Prestito(UUID libro, UUID utente, LocalDate dataInizio, LocalDate dataRestituzionePrevista) {
        this.libro = libro;
        this.utente = utente;
        this.dataInizio = new SimpleObjectProperty<>(dataInizio);
        this.dataRestituzionePrevista = new SimpleObjectProperty<>(dataRestituzionePrevista);
    }

    public Libro getLibro() {
        return Biblioteca.ottieniLibroDaID(libro);
    }

    public void setLibro(UUID libro) {
        this.libro = libro;
    }

    public UUID getUUID() {
        return uuid;
    }

    public Utente getUtente() {
        return Biblioteca.ottieniUtenteDaID(utente);
    }

    public void setUtente(UUID utente) {
        this.utente = utente;
    }

    public LocalDate getDataInizio() {
        return dataInizio.get();
    }

    public void setDataInizio(LocalDate dataInizio) {
        this.dataInizio.set(dataInizio);
    }

    public ObjectProperty<LocalDate> dataInizioProperty() {
        return dataInizio;
    }

    public LocalDate getDataRestituzionePrevista() {
        return dataRestituzionePrevista.get();
    }

    public void setDataRestituzionePrevista(LocalDate dataRestituzionePrevista) {
        this.dataRestituzionePrevista.set(dataRestituzionePrevista);
    }

    public ObjectProperty<LocalDate> dataRestituzionePrevistaProperty() {
        return dataRestituzionePrevista;
    }

    public int calcolaRitardo(LocalDate data) {
        return (int) ChronoUnit.DAYS.between(getDataRestituzionePrevista(), data);
    }

    public boolean verificaRitardo(int giorni) {
        return calcolaRitardo(LocalDate.now()) > giorni;
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeUTF(uuid.toString());
        oos.writeObject(dataInizio.get());
        oos.writeObject(dataRestituzionePrevista.get());
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        this.uuid = UUID.fromString(ois.readUTF());

        LocalDate dataInizioVal = (LocalDate) ois.readObject();
        LocalDate dataRestituzioneVal = (LocalDate) ois.readObject();

        this.dataInizio = new SimpleObjectProperty<>(dataInizioVal);
        this.dataRestituzionePrevista = new SimpleObjectProperty<>(dataRestituzioneVal);
    }
}
