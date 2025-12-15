package bibliotecauniversitaria;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import bibliotecauniversitaria.TestHelper;
import bibliotecauniversitaria.models.Biblioteca;
import bibliotecauniversitaria.models.Libro;
import bibliotecauniversitaria.models.Prestito;
import bibliotecauniversitaria.models.Utente;
import java.time.LocalDate;
import java.util.Random;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Nunzia
 */
public class TestScalabilita {
    
    public TestScalabilita() {
    }
    
    @BeforeAll
    public static void setUpClass() {
        TestHelper.salva(null);
    }
    
    @AfterAll
    public static void tearDownClass() {
        TestHelper.ripristina();
    }    

     @Test
    public void testScalabilitaBiblioteca() {
        int UTENTI=25000;
        int LIBRI=75000;
        int PRESTITI=25000;
        ObservableList<Utente> utenti = FXCollections.observableArrayList();
        ObservableList<Libro> libri = FXCollections.observableArrayList();
        ObservableList<Prestito> prestiti = FXCollections.observableArrayList();
        for(int i=0;i<UTENTI;i++) {
            String nc = TestHelper.generaStringa(5);
            String ISBN = TestHelper.generaStringa(15);
            Utente u = new Utente(ISBN,nc,nc,String.format("%s@%s.%s",TestHelper.generaStringa(4),TestHelper.generaStringa(4),TestHelper.generaStringa(4)));
            utenti.add(u);
        }
        System.out.println("Creati utenti: " + utenti.size());
        for(int i=0;i<LIBRI;i++) {
            Libro l = new Libro(String.valueOf(Math.random()*10000000),String.valueOf(Math.random()*10000000),String.valueOf(Math.random()*10000000),2025,10000);
            libri.add(l);
        }
        System.out.println("Creati libri: " + libri.size());
        Biblioteca.setListaLibri(libri);
        Biblioteca.setListaUtenti(utenti);
        Utente ur = null;
        Libro ul = null;
        Prestito up = null;
        for(int i=0;i<PRESTITI;i++) {
            Libro l = Biblioteca.getListaLibri().get(new Random().nextInt(Biblioteca.getListaLibri().size()));
            Utente u = Biblioteca.getListaUtenti().get(new Random().nextInt(Biblioteca.getListaUtenti().size()));
            if(ur==null) ur=u;
            if(ul==null) ul=l;
            Prestito p = new Prestito(l.getUUID(), u.getUUID(), LocalDate.now(), LocalDate.now().plusWeeks(2));
            if(up==null) up=p;
            prestiti.add(p);
        }
        System.out.println("Creati prestiti: " + prestiti.size());
        Biblioteca.setListaPrestiti(prestiti);
        assertEquals(ul,up.getLibro(),"Il libro del prestito deve essere quello dato.");
        assertEquals(ur,up.getUtente(),"L'utente del prestito deve essere quello dato.");
        assertEquals(PRESTITI,Biblioteca.getListaPrestiti().size());
        assertEquals(UTENTI, Biblioteca.getListaUtenti().size());
        assertEquals(LIBRI, Biblioteca.getListaLibri().size());
    }
}