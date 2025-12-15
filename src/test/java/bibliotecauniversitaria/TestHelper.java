package bibliotecauniversitaria;

import bibliotecauniversitaria.models.*;
import bibliotecauniversitaria.utils.Configurazione;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Random;

public class TestHelper {

    public static ObservableList<Prestito> listaPrestiti;
    public static ObservableList<Utente> listaUtenti;
    public static ObservableList<Libro> listaLibri;

    public static void salva(Path temp) {
        Biblioteca.carica();
        listaPrestiti = Biblioteca.getListaPrestiti();
        listaUtenti = Biblioteca.getListaUtenti();
        listaLibri = Biblioteca.getListaLibri();
        Biblioteca.configurazione = new Configurazione();
        if(temp!=null){
            File file = new File(temp.toFile(),"configurazione_test");
            Biblioteca.configurazione.salvaDefault(file);
            try {
                Biblioteca.configurazione.carica(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void ripristina() {
        Archivio.scrivi(listaUtenti,Archivio.fileUtenti);
        Archivio.scrivi(listaLibri,Archivio.fileLibri);
        Archivio.scrivi(listaPrestiti,Archivio.filePrestiti);
    }

    public static String generaStringa(int lunghezza) {
        String s = "";
        String[] possibili = "ABCDEFGHILMNOPQRSTUVZabcdefghilmnopqrstuvz".split("");
        for (int i = 0; i < lunghezza; i++) {
            s += possibili[new Random().nextInt(possibili.length)];
        }
        return s;
    }

}




