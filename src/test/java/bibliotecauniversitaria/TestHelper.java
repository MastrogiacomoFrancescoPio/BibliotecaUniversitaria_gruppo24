package bibliotecauniversitaria;

import bibliotecauniversitaria.models.*;
import javafx.collections.ObservableList;

import java.util.Random;

public class TestHelper {

    public static ObservableList<Prestito> listaPrestiti;
    public static ObservableList<Utente> listaUtenti;
    public static ObservableList<Libro> listaLibri;

    public static void salva() {
        Biblioteca.carica();
        listaPrestiti = Biblioteca.getListaPrestiti();
        listaUtenti = Biblioteca.getListaUtenti();
        listaLibri = Biblioteca.getListaLibri();
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




