package bibliotecauniversitaria.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Archivio {

    public static File cartellaData = new File("dataBiblioteca");

    public static File fileLibri=new File(cartellaData,"archivioLibri.bbl");
    public static File fileUtenti=new File(cartellaData,"archivioUtenti.bbl");
    public static File filePrestiti=new File(cartellaData,"archivioPrestiti.bbl");
    
    public static <T> ObservableList<T> carica(File nomeFile) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(nomeFile))) {
            return FXCollections.observableArrayList((ArrayList<T>)ois.readObject());
        } catch (IOException | ClassNotFoundException ex) {
            return FXCollections.observableArrayList();
        }
    }

    public static <T> void scrivi(ObservableList<T> lista, File file){
        try (ObjectOutputStream oos= new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(new ArrayList<>(lista));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}