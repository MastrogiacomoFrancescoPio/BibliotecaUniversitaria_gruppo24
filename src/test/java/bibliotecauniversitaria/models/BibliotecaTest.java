/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bibliotecauniversitaria.models;

import bibliotecauniversitaria.TestHelper;
import bibliotecauniversitaria.utils.Configurazione;
import java.time.LocalDate;
import java.time.Month;
import java.util.UUID;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author franc
 */
public class BibliotecaTest {

    @BeforeAll
    public static void salva() {
        TestHelper.salva();
    }

    @AfterAll
    public static void down() {
        TestHelper.ripristina();
    }

    @BeforeEach
    @AfterEach
    public void reset() {
        Biblioteca.setListaUtenti(FXCollections.observableArrayList());
        Biblioteca.setListaLibri(FXCollections.observableArrayList());
        Biblioteca.setListaPrestiti(FXCollections.observableArrayList());
    }
    
    /**
     * Test of getConfigurazione method, of class Biblioteca.
     */
    @Test
    public void testGetConfigurazione() {
        System.out.println("getConfigurazione");
        Configurazione config = new Configurazione();
        Biblioteca.configurazione = config;

        Biblioteca instance = new Biblioteca();
        Configurazione result = instance.getConfigurazione();

        assertEquals(config, result);

        Biblioteca.configurazione = null;
    }

    /**
     * Test of getListaLibri method, of class Biblioteca.
     */
    @Test
    public void testGetListaLibri() {
        System.out.println("testGetListaLibri");

        ObservableList<Libro> result = Biblioteca.getListaLibri();
        assertNotNull(result, "La lista dei libri non deve essere null dopo l'inizializzazione.");
        assertTrue(result.isEmpty(), "La lista dei libri deve essere inizialmente vuota.");
    }

    /**
     * Test of setListaLibri method, of class Biblioteca.
     */
    @Test
    public void testSetListaLibri() {
        ObservableList<Libro> listaOriginale = FXCollections.observableArrayList();
        listaOriginale.add(new Libro("Titolo", "Autore", " ", 1, 1));

        Biblioteca.setListaLibri(listaOriginale);

        ObservableList<Libro> listaOttenuta = Biblioteca.getListaLibri();
        assertEquals(listaOriginale, listaOttenuta, "Il getter deve restituire esattamente l'oggetto lista che è stato settato.");
    }

    /**
     * Test of getListaUtenti method, of class Biblioteca.
     */
    @Test
    public void testGetListaUtenti() {
        System.out.println("testGetListaUtenti");

        ObservableList<Utente> result = Biblioteca.getListaUtenti();
        assertNotNull(result, "La lista dei utenti non deve essere null dopo l'inizializzazione.");
        assertTrue(result.isEmpty(), "La lista degli utenti deve essere inizialmente vuota.");
    }

    /**
     * Test of setListaUtenti method, of class Biblioteca.
     */
    @Test
    public void testSetListaUtenti() {
        ObservableList<Utente> listaOriginale = FXCollections.observableArrayList();
        listaOriginale.add(new Utente("a", "a", "a", "a@a.a"));

        Biblioteca.setListaUtenti(listaOriginale);

        ObservableList<Utente> listaOttenuta = Biblioteca.getListaUtenti();
        assertEquals(listaOriginale, listaOttenuta, "Il getter deve restituire esattamente l'oggetto lista che è stato settato.");
    }

    /**
     * Test of getListaPrestiti method, of class Biblioteca.
     */
    @Test
    public void testGetListaPrestiti() {
        System.out.println("testGetListaPrestiti");

        ObservableList<Prestito> result = Biblioteca.getListaPrestiti();
        assertNotNull(result, "La lista dei prestiti non deve essere null dopo l'inizializzazione.");
        assertTrue(result.isEmpty(), "La lista dei prestiti deve essere inizialmente vuota.");
    }

    /**
     * Test of setListaPrestiti method, of class Biblioteca.
     */
    @Test
    public void testSetListaPrestiti() {
        ObservableList<Prestito> listaOriginale = FXCollections.observableArrayList();

        listaOriginale.add(new Prestito(UUID.randomUUID(), UUID.randomUUID(), LocalDate.now(), LocalDate.of(2025, Month.DECEMBER, 31)));

        Biblioteca.setListaPrestiti(listaOriginale);

        ObservableList<Prestito> listaOttenuta = Biblioteca.getListaPrestiti();
        assertEquals(listaOriginale, listaOttenuta, "Il getter deve restituire esattamente l'oggetto lista che è stato settato.");
    }

    /**
     * Test of carica method, of class Biblioteca.
     */
    @Test
    public void testCarica() {
        System.out.println("testCarica");

        Biblioteca.carica();

        assertNotNull(new Biblioteca().getConfigurazione(),
                "La configurazione deve essere stata inizializzata (non deve essere null).");

        Biblioteca.configurazione = null;
    }

    /**
     * Test of togliSospensioni method, of class Biblioteca.
     */
    @Test
    public void testTogliSospensioni() {
       System.out.println("testTogliSospensioni");

       
        Utente utenteDaRevocare = new Utente("M001", "Mario", "Rossi", "m@b.c");
        utenteDaRevocare.setDataFineSospensione(LocalDate.now().minusDays(1));
        utenteDaRevocare.setSospeso(true); 
        
        Utente utenteDaIgnorare = new Utente("M002", "Luca", "Verdi", "l@b.c");

        Utente altroUtenteDaRevocare = new Utente("M003", "Anna", "Neri", "a@b.c");
        altroUtenteDaRevocare.setDataFineSospensione(LocalDate.now().minusDays(5));
         altroUtenteDaRevocare.setSospeso(true);
        
        ObservableList<Utente> listaIniziale = FXCollections.observableArrayList(
                utenteDaRevocare, utenteDaIgnorare, altroUtenteDaRevocare
        );

        Biblioteca.setListaUtenti(listaIniziale);
        Biblioteca.configurazione = new Configurazione();
        Biblioteca.configurazione.valori.put("SMTP_CONFIGURATO", "false");
        Biblioteca.togliSospensioni();

        assertFalse(utenteDaRevocare.isSospensioneScaduta(), "Utente 1: La revocaSospensione doveva essere chiamata e lo stato modificato.");     
        assertFalse(utenteDaIgnorare.isSospensioneScaduta(),"Utente 2: La revocaSospensione NON doveva essere chiamata.");        
        assertFalse(altroUtenteDaRevocare.isSospensioneScaduta(),  "Utente 3: La revocaSospensione doveva essere chiamata e lo stato modificato.");
    }

    /**
     * Test of ottieniUtenteDaID method, of class Biblioteca.
     */
    @Test
    public void testOttieniUtenteDaID() {
    Utente utenteTrovato = new Utente("M001", "Mario", "Rossi", "m@b.c");
   
    UUID idTrovato = utenteTrovato.getUUID();
    
    UUID idSconosciuto = UUID.randomUUID();
    
    Biblioteca.setListaUtenti(FXCollections.observableArrayList(utenteTrovato, new Utente("M002", "Luca", "Verdi", "l@b.c"))); 
    
    Utente risultatoTrovato = Biblioteca.ottieniUtenteDaID(idTrovato);
    assertEquals(utenteTrovato, risultatoTrovato, "Dovrebbe restituire l'utente con l'ID fornito."); 

        
        Utente risultatoNonTrovato = Biblioteca.ottieniUtenteDaID(idSconosciuto);
        assertNull(risultatoNonTrovato, "Dovrebbe restituire null se l'ID non è presente nella lista.");

        Utente risultatoNull = Biblioteca.ottieniUtenteDaID(null);
        assertNull(risultatoNull, "Dovrebbe restituire null quando l'input UUID è null.");
        Biblioteca.setListaUtenti(FXCollections.observableArrayList());
    }

    /**
     * Test of ottieniLibroDaID method, of class Biblioteca.
     */
    @Test
    public void testOttieniLibroDaID() {
    UUID idLibroSconosciuto = UUID.randomUUID();
    
    Libro libroDaTrovare = new Libro("Titolo", "Autore", " ", 1, 1);
    UUID idLibroTrovato = libroDaTrovare.getUUID();

    
    Biblioteca.setListaLibri(FXCollections.observableArrayList(libroDaTrovare, new Libro("Titolo2", "Autore2", " ", 2, 2))); 
    
    Libro risultatoTrovato = Biblioteca.ottieniLibroDaID(idLibroTrovato);
    assertEquals(libroDaTrovare, risultatoTrovato, "Dovrebbe restituire l'oggetto Libro con l'ID fornito."); 

    Libro risultatoNonTrovato = Biblioteca.ottieniLibroDaID(idLibroSconosciuto);
    assertNull(risultatoNonTrovato, "Dovrebbe restituire null se l'ID non è presente nella lista.");

    Libro risultatoNull = Biblioteca.ottieniLibroDaID(null);
    assertNull(risultatoNull, "Dovrebbe restituire null quando l'input UUID è null.");

        Biblioteca.setListaLibri(FXCollections.observableArrayList());
    }

    /**
     * Test of aggiungiLibro method, of class Biblioteca.
     */


    @Test
    public void testAggiungiLibro() {
        System.out.println("testAggiungiLibro");


        Libro nuovoLibro = new Libro("Titolo", "Autore", "a", 1, 1);

        assertTrue(Biblioteca.getListaLibri().isEmpty(), "La lista deve essere vuota prima del test.");

        boolean result = Biblioteca.aggiungiLibro(nuovoLibro);

        assertTrue(result, "Il risultato di add() dovrebbe essere true per un'aggiunta valida.");

        ObservableList<Libro> listaFinale = Biblioteca.getListaLibri();
        assertFalse(listaFinale.isEmpty(), "La lista non deve essere vuota.");
        assertTrue(listaFinale.contains(nuovoLibro), "La lista deve contenere il libro aggiunto.");
    }

    @Test
    public void testAggiungiLibro_AggiuntaNull() {
        boolean result = Biblioteca.aggiungiLibro(null);
        assertTrue(result, "L'aggiunta di null in una lista standard Java dovrebbe restituire true.");
    }

    /**
     * Test of rimuoviLibro method, of class Biblioteca.
     */

    @Test
    public void testRimuoviLibro_Successo() {
        System.out.println("testRimuoviLibro - Successo");

        Libro libroDaRimuovere = new Libro("Titolo", "Autore", "a", 1, 1);

        Biblioteca.setListaLibri(FXCollections.observableArrayList(libroDaRimuovere, new Libro("Titolo2", "Autor2", "b", 2, 2)));
        int sizeIniziale = Biblioteca.getListaLibri().size();

        boolean result = Biblioteca.rimuoviLibro(libroDaRimuovere);

        assertTrue(result, "Il risultato di remove() dovrebbe essere true per un elemento presente.");
        assertEquals(sizeIniziale - 1, Biblioteca.getListaLibri().size(), "La dimensione della lista deve diminuire di uno.");

        assertFalse(Biblioteca.getListaLibri().contains(libroDaRimuovere), "La lista non deve più contenere il libro rimosso.");
    }

    @Test
    public void testRimuoviLibro_ElementoAssente() {
        System.out.println("testRimuoviLibro - Assente");

        Biblioteca.setListaLibri(FXCollections.observableArrayList(new Libro("Titolo", "Autore", "a", 1, 1), new Libro("Titolo2", "Autor2", "b", 2, 2)));
        Libro libroAssente = new Libro("Titolo3", "Autore3", "c", 3, 3);

        boolean result = Biblioteca.rimuoviLibro(libroAssente);

        assertFalse(result, "Il risultato di remove() dovrebbe essere false per un elemento assente.");
    }

    /**
     * Test of ordinaLibriTitolo method, of class Biblioteca.
     */
    @Test
    public void testOrdinaLibriTitolo() {
        System.out.println("testOrdinaLibriTitolo - Verifica ordinamento");

        Libro libroA = new Libro("A", "", "", 1, 1);
        Libro libroC = new Libro("C", "", "", 3, 3);
        Libro libroB = new Libro("B", "", "", 2, 2);

        ObservableList<Libro> listaOriginale = FXCollections.observableArrayList(libroC, libroB, libroA);

        ObservableList<Libro> listaOrdinata = Biblioteca.ordinaLibriTitolo(listaOriginale);

        assertEquals(listaOriginale.size(), listaOrdinata.size(), "La dimensione della lista deve essere la stessa.");
        assertEquals(libroC, listaOriginale.get(0), "La lista originale NON deve essere stata modificata.");
        assertEquals(libroA, listaOrdinata.get(0), "Il primo elemento deve essere il libro 'A'.");
        assertEquals(libroB, listaOrdinata.get(1), "Il secondo elemento deve essere il libro 'B'.");
        assertEquals(libroC, listaOrdinata.get(2), "Il terzo elemento deve essere il libro 'C'.");
    }

    /**
     * Test of ordinaLibriAutore method, of class Biblioteca.
     */
    @Test
    public void testOrdinaLibriAutore() {
        System.out.println("testOrdinaLibriAutore - Verifica ordinamento");

        Libro libroAutoreZ = new Libro("", "Zola", "", 3, 3);
        Libro libroAutoreA = new Libro("", "Austen", "", 1, 1);
        Libro libroAutoreM = new Libro("", "Manzoni", "", 2, 2);

        ObservableList<Libro> listaOriginale = FXCollections.observableArrayList(libroAutoreZ, libroAutoreA, libroAutoreM);

        ObservableList<Libro> listaOrdinata = Biblioteca.ordinaLibriAutore(listaOriginale);
        assertEquals(libroAutoreZ, listaOriginale.get(0), "La lista originale NON deve essere stata modificata.");
        assertEquals(libroAutoreA, listaOrdinata.get(0), "Il primo elemento deve essere il libro di 'Austen'.");
        assertEquals(libroAutoreM, listaOrdinata.get(1), "Il secondo elemento deve essere il libro di 'Manzoni'.");
        assertEquals(libroAutoreZ, listaOrdinata.get(2), "Il terzo elemento deve essere il libro di 'Zola'.");
    }

    /**
     * Test of ordinaLibriISBN method, of class Biblioteca.
     */
    @Test
    public void testOrdinaLibriISBN() {
        System.out.println("testOrdinaLibriISBN - Verifica ordinamento");

        Libro libroISBN_c = new Libro("", "", "c", 3, 3); // Alto
        Libro libroISBN_a = new Libro("", "", "a", 1, 1); // Basso
        Libro libroISBN_b = new Libro("", "", "b", 2, 2); // Medio

        ObservableList<Libro> listaOriginale = FXCollections.observableArrayList(libroISBN_c, libroISBN_a, libroISBN_b);

        ObservableList<Libro> listaOrdinata = Biblioteca.ordinaLibriISBN(listaOriginale);

        assertEquals(libroISBN_c, listaOriginale.get(0), "La lista originale NON deve essere stata modificata.");
        assertEquals(libroISBN_a, listaOrdinata.get(0), "Il primo elemento deve essere il libro con ISBN 'A'.");
        assertEquals(libroISBN_b, listaOrdinata.get(1), "Il secondo elemento deve essere il libro con ISBN 'M'.");
        assertEquals(libroISBN_c, listaOrdinata.get(2), "Il terzo elemento deve essere il libro con ISBN 'Z'.");
    }

    /**
     * Test of cercaLibro method, of class Biblioteca.
     */
    @Test
    public void testCercaLibro_Titolo() {
        System.out.println("testCercaLibro: Filtro Titolo");

        Libro libro1_match = new Libro("Harry Potter - Pietra Filosofale", "", "A", 1, 1);
        Libro libro2_match = new Libro("Harry Potter - Camera dei Segreti", "", "B", 1, 1);
        Libro libro3_noMatch = new Libro("Il Signore degli Anelli", "", "C", 1, 1);

        Biblioteca.setListaLibri(FXCollections.observableArrayList(libro1_match, libro2_match, libro3_noMatch));

        Libro criterioRicerca = new Libro("Potter", "", "", 1, 1);

        ObservableList<Libro> risultati = Biblioteca.cercaLibro(criterioRicerca);

        assertEquals(2, risultati.size(), "Dovrebbero essere restituiti esattamente 2 libri.");

        assertTrue(risultati.stream().anyMatch(u -> u.getTitolo().equals("Harry Potter - Pietra Filosofale")), "Deve contenere il primo libro di Harry Potter.");
        assertTrue(risultati.stream().anyMatch(u -> u.getTitolo().equals("Harry Potter - Camera dei Segreti")), "Deve contenere il secondo libro di Harry Potter.");
        assertFalse(risultati.stream().anyMatch(u -> u.getTitolo().equals("Il Signore degli Anelli")), "NON deve contenere Il Signore degli Anelli.");
    }

    @Test
    public void testCercaLibro_Autore() {
        Libro l1_r = new Libro("Java Tutorial #1","Peppe","",1,1);
        Libro l2_dummy = new Libro("Java Tutorial #2","Giggino","",1,1);
        Libro l1_s = new Libro("","Peppe","",0,0);

        Biblioteca.setListaLibri(FXCollections.observableArrayList(l1_r,l2_dummy));
        ObservableList<Libro> trovati = Biblioteca.cercaLibro(l1_s);
        assertEquals(l1_r.getUUID(), trovati.get(0).getUUID());
        assertEquals(1, trovati.size());
    }

    @Test
    public void testCercaLibro_null() {
        System.out.println("testCercaLibro : Input Null");

        Biblioteca.setListaLibri(FXCollections.observableArrayList(new Libro("A", "", "", 1, 1), new Libro("B", "", "", 1, 1)));

        ObservableList<Libro> result = Biblioteca.cercaLibro(null);
        assertNotNull(result, "Viene visualizzato l'alert");
    }

    /**
     * Test of aggiungiUtente method, of class Biblioteca.
     */
    @Test
    public void testAggiungiUtente() {
       System.out.println("testAggiungiUtente");

        Utente nuovoUtente = new Utente("M001", "Mario", "Rossi", "m@b.c"); 
        
        assertTrue(Biblioteca.getListaUtenti().isEmpty(), "La lista deve essere vuota prima del test.");

        boolean result = Biblioteca.aggiungiUtente(nuovoUtente);

        assertTrue(result, "Il risultato di add() dovrebbe essere true per un'aggiunta valida.");

        ObservableList<Utente> listaFinale = Biblioteca.getListaUtenti();
        assertFalse(listaFinale.isEmpty(), "La lista non deve essere vuota.");
        assertTrue(listaFinale.contains(nuovoUtente), "La lista deve contenere l'utente aggiunto.");
    }
    @Test
    public void testAggiungiUtente_AggiuntaNull() {
        boolean result = Biblioteca.aggiungiUtente(null);
        assertFalse(result, "L'aggiunta di null in una lista standard Java dovrebbe restituire true."); 
    }

    /**
     * Test of rimuoviUtente method, of class Biblioteca.
     */

    @Test
    public void testRimuoviUtente_Successo() {
        System.out.println("testRimuoviUtente - Successo");

        Utente utenteDaRimuovere = new Utente("M001", "Mario", "Rossi", "m@b.c");

        Biblioteca.setListaUtenti(FXCollections.observableArrayList(utenteDaRimuovere, new Utente("M002", "Luca", "Verdi", "l@v.c")));
        int sizeIniziale = Biblioteca.getListaUtenti().size();

        boolean result = Biblioteca.rimuoviUtente(utenteDaRimuovere);

        assertTrue(result, "Il risultato di remove() dovrebbe essere true per un elemento presente.");
        assertEquals(sizeIniziale - 1, Biblioteca.getListaUtenti().size(), "La dimensione della lista deve diminuire di uno.");

        assertFalse(Biblioteca.getListaUtenti().contains(utenteDaRimuovere), "La lista non deve più contenere l'utente rimosso.");
    }

    @Test
    public void testRimuoviUtente_ElementoAssente() {
        System.out.println("testRimuoviUtente - Assente");

        Biblioteca.setListaUtenti(FXCollections.observableArrayList(new Utente("M001", "Mario", "Rossi", "m@b.c"), new Utente("M002", "Luca", "Verdi", "l@v.c")));
        Utente utenteAssente = new Utente("M003", "Maria", "Rossa", "a@r.c");

        boolean result = Biblioteca.rimuoviUtente(utenteAssente);

        assertFalse(result, "Il risultato di remove() dovrebbe essere false per un elemento assente.");
    }

    /**
     * Test of trovaDaEmail method, of class Biblioteca.
     */
    @Test
    public void testTrovaDaEmail() {
        System.out.println("testTrovaDaEmail");
        
    final String EMAIL_TROVATA = "mario.rossi@biblio.it";
    final String EMAIL_ASSENTE = "assente@biblio.it";

    Utente utenteDaTrovare = new Utente("M001", "Mario", "Rossi", "mario.rossi@biblio.it");
    
    Utente altroUtente = new Utente("M002", "Luca", "Verdi", "l@v.c");
    
    Biblioteca.setListaUtenti(FXCollections.observableArrayList(utenteDaTrovare, altroUtente)); 

    ObservableList<Utente> risultatiTrovati = Biblioteca.trovaDaEmail(EMAIL_TROVATA);
    assertFalse(risultatiTrovati.isEmpty(), "Deve essere restituita almeno una corrispondenza.");
    assertEquals(1, risultatiTrovati.size(), "Dovrebbe esserci un solo risultato per l'email unica.");
    assertEquals(utenteDaTrovare, risultatiTrovati.get(0), "L'oggetto Utente deve essere quello atteso.");

        ObservableList<Utente> risultatiAssenti = Biblioteca.trovaDaEmail(EMAIL_ASSENTE);
        assertNotNull(risultatiAssenti, "Il risultato non deve mai essere null.");
        assertTrue(risultatiAssenti.isEmpty(), "Se l'email non è presente, deve essere restituita una lista vuota.");

        ObservableList<Utente> risultatiVuoti = Biblioteca.trovaDaEmail("");
        assertNotNull(risultatiVuoti, "Il risultato non deve essere null anche per stringa vuota.");
        assertTrue(risultatiVuoti.isEmpty(), "Se non ci sono utenti con email vuota, la lista deve essere vuota.");

        Biblioteca.setListaUtenti(FXCollections.observableArrayList());
    }

    /**
     * Test of ordinaUtentiCognome method, of class Biblioteca.
     */
    @Test
    public void testOrdinaUtentiCognome() {
        System.out.println("testOrdinaUtentiCognome - Verifica ordinamento");

        Utente utenteA = new Utente("", "", "Anni", "");
        Utente utenteV = new Utente("", "", "Verdi", "");
        Utente utenteZ = new Utente("", "", "Zeni", "");

        ObservableList<Utente> listaOriginale = FXCollections.observableArrayList(utenteA, utenteV, utenteZ);

        ObservableList<Utente> listaOrdinata = Biblioteca.ordinaUtentiCognome(listaOriginale);

        assertEquals(utenteA, listaOriginale.get(0), "La lista originale NON deve essere stata modificata.");

        assertEquals(utenteA, listaOrdinata.get(0), "Primo: Anni");
        assertEquals(utenteV, listaOrdinata.get(1), "Secondo: Verdi");
        assertEquals(utenteZ, listaOrdinata.get(2), "Terzo: Zeni");
    }

    /**
     * Test of ordinaUtentiNome method, of class Biblioteca.
     */
    @Test
    public void testOrdinaUtentiNome() {
        System.out.println("testOrdinaUtentiCognome - Verifica ordinamento");

        Utente utenteA = new Utente("", "Anna", "", "");
        Utente utenteL = new Utente("", "Luca", "", "");
        Utente utenteZ = new Utente("", "Zeno", "", "");

        ObservableList<Utente> listaOriginale = FXCollections.observableArrayList(utenteA, utenteL, utenteZ);

        ObservableList<Utente> listaOrdinata = Biblioteca.ordinaUtentiCognome(listaOriginale);

        assertSame(utenteA, listaOriginale.get(0), "La lista originale NON deve essere stata modificata.");

        assertSame(utenteA, listaOrdinata.get(0), "Primo: Anna");
        assertSame(utenteL, listaOrdinata.get(1), "Secondo: Luca");
        assertSame(utenteZ, listaOrdinata.get(2), "Terzo: Zeno");
    }

    /**
     * Test of ordinaUtentiMatricola method, of class Biblioteca.
     */
    @Test
    public void testOrdinaUtentiMatricola() {
        System.out.println("testOrdinaUtentiMatricola - Verifica ordinamento per Matricola");

        Utente utenteZ = new Utente("ZZZ999", "", "", "");
        Utente utenteA = new Utente("AAA111", "", "", "");
        Utente utenteM = new Utente("MMM555", "", "", "");

        ObservableList<Utente> listaOriginale = FXCollections.observableArrayList(utenteZ, utenteA, utenteM);

        ObservableList<Utente> listaOrdinata = Biblioteca.ordinaUtentiMatricola(listaOriginale);

        assertSame(utenteZ, listaOriginale.get(0), "La lista originale NON deve essere stata modificata (deve rimanere ZZZ999 al primo posto).");

        assertSame(utenteA, listaOrdinata.get(0), "Il primo elemento deve essere la matricola 'AAA111'.");
        assertSame(utenteM, listaOrdinata.get(1), "Il secondo elemento deve essere la matricola 'MMM555'.");
        assertSame(utenteZ, listaOrdinata.get(2), "Il terzo elemento deve essere la matricola 'ZZZ999'.");
    }

    /**
     * Test of cercaUtente method, of class Biblioteca.
     */

    @Test
    public void testCercaUtente() {
        System.out.println("testCercaUtente - Filtro Cognome");

        Utente utente1_match = new Utente("", "", "Rossi", "");
        Utente utente2_match = new Utente("", "", "ROSSINI", "");
        Utente utente3_noMatch = new Utente("", "", "Bianchi", "");

        Biblioteca.setListaUtenti(FXCollections.observableArrayList(utente1_match, utente2_match, utente3_noMatch));

        Utente criterioRicerca = new Utente("", "", "ross", "");

        ObservableList<Utente> risultati = Biblioteca.cercaUtente(criterioRicerca);


        assertEquals(2, risultati.size(), "Dovrebbero essere restituiti esattamente 2 utenti.");

        assertTrue(risultati.stream().anyMatch(u -> u.getCognome().equals("Rossi")), "Deve contenere un utente con cognome 'Rossi'");

        assertTrue(risultati.stream().anyMatch(u -> u.getCognome().equals("ROSSINI")), "Deve contenere un utente con cognome 'ROSSINI'");

        assertFalse(risultati.stream().anyMatch(u -> u.getCognome().equals("Bianchi")), "NON deve contenere un utente con cognome 'Bianchi'");
    }

    /**
     * Test of aggiungiPrestito method, of class Biblioteca.
     */


    @Test
    public void testAggiungiPrestito() {
        System.out.println("testAggiungiPrestito - Caso di Successo con UUID");

        Libro libroPrestato = new Libro("Titolo", "Autore", " ", 1, 1);
        Utente utenteChePrende = new Utente("M001", "Mario", "Rossi", "m@b.c");
        int copieIniziali = libroPrestato.getNumeroCopieDisponibili();

        Prestito nuovoPrestito = new Prestito(libroPrestato.getUUID(), utenteChePrende.getUUID(), LocalDate.now(), LocalDate.now().plusWeeks(2));

        Biblioteca.setListaLibri(FXCollections.observableArrayList(libroPrestato));
        Biblioteca.setListaUtenti(FXCollections.observableArrayList(utenteChePrende)); 
        Biblioteca.setListaPrestiti(FXCollections.observableArrayList()); 
        
        boolean result = Biblioteca.aggiungiPrestito(nuovoPrestito,false);

        assertTrue(result, "Il risultato del metodo deve essere TRUE per un'aggiunta valida.");

        assertEquals(copieIniziali - 1, libroPrestato.getNumeroCopieDisponibili(), "Le copie disponibili del libro devono essere state decrementate da 3 a 2.");

        assertTrue(Biblioteca.getListaPrestiti().contains(nuovoPrestito), "Il nuovo prestito deve essere aggiunto alla lista statica.");
    }

    /**
     * Test of rimuoviPrestito method, of class Biblioteca.
     */

    @Test
    public void testRimuoviPrestito_Successo() {
        System.out.println("testRimuoviPrestito - Caso di Successo");
        Libro libroRitorno = new Libro("Titolo", "Autore", " ", 1, 1);
        Utente utenteRitorno = new Utente("M001", "Mario", "Rossi", "m@b.c");
        int copieIniziali = libroRitorno.getNumeroCopieDisponibili();

        Prestito prestitoDaRimuovere = new Prestito(libroRitorno.getUUID(), utenteRitorno.getUUID(), LocalDate.now(), LocalDate.now().plusWeeks(2));

        Biblioteca.setListaLibri(FXCollections.observableArrayList(libroRitorno));
        Biblioteca.setListaPrestiti(FXCollections.observableArrayList(
                prestitoDaRimuovere, new Prestito(UUID.randomUUID(), UUID.randomUUID(), LocalDate.now(), LocalDate.now().plusWeeks(2))));
        int dimensioneInizialePrestiti = Biblioteca.getListaPrestiti().size(); // 2

        boolean result = Biblioteca.rimuoviPrestito(prestitoDaRimuovere);

        assertTrue(result, "Il risultato del metodo deve essere TRUE per una rimozione avvenuta.");

        assertEquals(copieIniziali, libroRitorno.getNumeroCopieTotali(), "Le copie disponibili del libro non devo essere modificate.");

        assertFalse(Biblioteca.getListaPrestiti().contains(prestitoDaRimuovere), "Il prestito deve essere rimosso dalla lista statica.");

        assertEquals(dimensioneInizialePrestiti - 1, Biblioteca.getListaPrestiti().size(), "La dimensione della lista prestiti deve diminuire di uno.");
    }

    /**
     * Test of ordinaPrestitiDataInizio method, of class Biblioteca.
     */
    @Test
    public void testOrdinaPrestitiDataInizio() {
        System.out.println("testOrdinaPrestitiDataInizio - Verifica ordinamento per Data Inizio");


        LocalDate dataAntica = LocalDate.of(2026, 1, 15);
        LocalDate dataFutura = LocalDate.of(2026, 12, 1);
        LocalDate dataIntermedia = LocalDate.of(2026, 6, 1);


        Prestito prestitoFuturo = new Prestito(UUID.randomUUID(), UUID.randomUUID(), dataFutura, dataFutura.plusWeeks(2));
        Prestito prestitoAntico = new Prestito(UUID.randomUUID(), UUID.randomUUID(), dataAntica, dataAntica.plusWeeks(2));
        Prestito prestitoMedio = new Prestito(UUID.randomUUID(), UUID.randomUUID(), dataIntermedia, dataIntermedia.plusWeeks(2));

        ObservableList<Prestito> listaOriginale = FXCollections.observableArrayList(prestitoFuturo, prestitoAntico, prestitoMedio);

        ObservableList<Prestito> listaOrdinata = Biblioteca.ordinaPrestitiDataInizio(listaOriginale);


        assertSame(prestitoFuturo, listaOriginale.get(0), "La lista originale NON deve essere stata modificata (deve rimanere 'Futuro' al primo posto).");

        assertSame(prestitoAntico, listaOrdinata.get(0), "Il primo elemento deve essere il prestito con la data più antica.");
        assertSame(prestitoMedio, listaOrdinata.get(1), "Il secondo elemento deve essere il prestito con la data intermedia.");
        assertSame(prestitoFuturo, listaOrdinata.get(2), "Il terzo elemento deve essere il prestito con la data più futura.");
    }

    /**
     * Test of ordinaPrestitiDataRestituzionePrevista method, of class Biblioteca.
     */
    @Test
    public void testOrdinaPrestitiDataRestituzionePrevista() {
        System.out.println("testOrdinaPrestitiDataInizio - Verifica ordinamento per Data Inizio");


        LocalDate dataAntica = LocalDate.of(2026, 1, 15);
        LocalDate dataFutura = LocalDate.of(2026, 12, 1);
        LocalDate dataIntermedia = LocalDate.of(2026, 6, 1);


        Prestito restituzioneFutura = new Prestito(UUID.randomUUID(), UUID.randomUUID(), dataFutura.minusWeeks(2), dataFutura);
        Prestito restituzioneAntica = new Prestito(UUID.randomUUID(), UUID.randomUUID(), dataAntica.minusWeeks(2), dataAntica);
        Prestito restituzioneMedia = new Prestito(UUID.randomUUID(), UUID.randomUUID(), dataIntermedia.minusWeeks(2), dataIntermedia);

        ObservableList<Prestito> listaOriginale = FXCollections.observableArrayList(restituzioneFutura, restituzioneAntica, restituzioneMedia);

        ObservableList<Prestito> listaOrdinata = Biblioteca.ordinaPrestitiDataInizio(listaOriginale);


        assertSame(restituzioneFutura, listaOriginale.get(0), "La lista originale NON deve essere stata modificata (deve rimanere 'Futuro' al primo posto).");

        assertSame(restituzioneAntica, listaOrdinata.get(0), "Il primo elemento deve essere il prestito con la data più antica.");
        assertSame(restituzioneMedia, listaOrdinata.get(1), "Il secondo elemento deve essere il prestito con la data intermedia.");
        assertSame(restituzioneFutura, listaOrdinata.get(2), "Il terzo elemento deve essere il prestito con la data più futura.");
    }

    /**
     * Test of cercaPrestito method, of class Biblioteca.
     */
    @Test
    public void testCercaPrestito() {
        System.out.println("testCercaPrestito - Filtro per ID Libro");


        LocalDate dataTarget = LocalDate.of(2025, 12, 10);
        LocalDate altraData = LocalDate.of(2025, 11, 25);

        Prestito match1 = new Prestito(UUID.randomUUID(), UUID.randomUUID(), dataTarget, dataTarget.plusWeeks(2));
        Prestito match2 = new Prestito(UUID.randomUUID(), UUID.randomUUID(), dataTarget, dataTarget.plusWeeks(2));
        Prestito noMatch = new Prestito(UUID.randomUUID(), UUID.randomUUID(), altraData, altraData.plusWeeks(2));

        Biblioteca.setListaPrestiti(FXCollections.observableArrayList(match1, match2, noMatch));

        Prestito criterioRicerca = new Prestito(null, null, dataTarget, null);

        ObservableList<Prestito> risultati = Biblioteca.cercaPrestito(criterioRicerca);

        assertEquals(2, risultati.size(), "Dovrebbero essere restituiti esattamente 2 prestiti con la data target.");

        assertTrue(risultati.contains(match1), "Deve contenere il primo prestito che corrisponde alla data.");
        assertTrue(risultati.contains(match2), "Deve contenere il secondo prestito che corrisponde alla data.");
        assertFalse(risultati.contains(noMatch), "NON deve contenere il prestito con data diversa.");
    }

    @Test
    public void testCercaPrestito_InputNull_RestituisceTutti() {
        System.out.println("testCercaPrestito - Input Null Corretto");

        Prestito p1 = new Prestito(UUID.randomUUID(), UUID.randomUUID(), LocalDate.now(), LocalDate.now().plusWeeks(2));
        Biblioteca.setListaPrestiti(FXCollections.observableArrayList(p1));

        ObservableList<Prestito> result = Biblioteca.cercaPrestito(null);

        assertNotNull(result, "Il risultato non deve essere null quando l'input è null.");
        assertEquals(1, result.size(), "Il risultato deve contenere tutti gli elementi della lista statica.");
    }

}
