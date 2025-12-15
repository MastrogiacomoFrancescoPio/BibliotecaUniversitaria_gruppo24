/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bibliotecauniversitaria.models;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LibroTest {
    private Libro libro;
    
    private final String TITOLO_INIZIALE = "Ingegneria del software"; 
    private final String NUOVO_TITOLO = "UML distilled"; 
    private final String AUTORE_INIZIALE = "I. Sommerville";
    private final String NUOVO_AUTORE = "M. Flower";
    private final String ISBN_INIZIALE = "978-ABC-123-XYZ";
    private final String NUOVO_ISBN = "ISBN-ABC-123";
    private final int ANNO_INIZIALE = 2017;
    private final int NUOVO_ANNO = 2018;
    private final int COPIE_TOTALI_INIZIALI = 5;
    private final int NUOVE_COPIE_TOTALI = 7;
    private final int COPIE_DISPONIBILI=3;
    
    public LibroTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
        libro = new Libro(TITOLO_INIZIALE, AUTORE_INIZIALE, ISBN_INIZIALE, ANNO_INIZIALE, COPIE_TOTALI_INIZIALI);
    }
    
    @AfterEach
    public void tearDown() {
    }
    
    //test costruttore con anno maggiore del corrente
    @Test
    public void testCostruttore_AnnoMaggioreCorrente() {
        assertThrows(IllegalArgumentException.class, () -> {
            // Tentativo di creare un libro con anno futuro
            new Libro(TITOLO_INIZIALE, AUTORE_INIZIALE, ISBN_INIZIALE, 2030, COPIE_TOTALI_INIZIALI);
        });
    }

    /**
     * test costruttore con copie totali negative 
     */
    @Test
    public void testCostruttore_CopieTotaliNegative() {
        assertThrows(IllegalArgumentException.class, () -> {
            // Tentativo di creare un libro con copie totali negative
            new Libro(TITOLO_INIZIALE, AUTORE_INIZIALE, ISBN_INIZIALE, ANNO_INIZIALE, -1);
        });
    }
    /**
     * Test of getTitolo method, of class Libro.
     */
    @Test
    public void testGetTitolo() {
        assertEquals(TITOLO_INIZIALE, libro.getTitolo());
    }

    /**
     * Test of setTitolo method, of class Libro.
     */
    @Test
    public void testSetTitolo() {
        libro.setTitolo(NUOVO_TITOLO);
        assertEquals(NUOVO_TITOLO, libro.getTitolo());
    }




    /**
     * Test of getAutore method, of class Libro.
     */
    @Test
    public void testGetAutore() {
        assertEquals(AUTORE_INIZIALE, libro.getAutore());
    }

    /**
     * Test of setAutore method, of class Libro.
     */
    @Test
    public void testSetAutore() {
        libro.setAutore(NUOVO_AUTORE);
        assertEquals(NUOVO_AUTORE, libro.getAutore());
    }



    /**
     * Test of getISBN method, of class Libro.
     */
    @Test
    public void testGetISBN() {
        assertEquals(ISBN_INIZIALE, libro.getISBN());
    }

    /**
     * Test of setISBN method, of class Libro.
     */
    @Test
    public void testSetISBN() {
        libro.setISBN(NUOVO_ISBN);
        assertEquals(NUOVO_ISBN, libro.getISBN());
    }



    /**
     * Test of getAnnoPubblicazione method, of class Libro.
     */
    @Test
    public void testGetAnnoPubblicazione() {
        assertEquals(ANNO_INIZIALE, libro.getAnnoPubblicazione());
    }

    /**
     * Test of setAnnoPubblicazione method, of class Libro.
     */
    @Test
    public void testSetAnnoPubblicazione_Valido() {
        libro.setAnnoPubblicazione(NUOVO_ANNO);
        assertEquals(NUOVO_ANNO, libro.getAnnoPubblicazione());
    }
    
    @Test
    public void testSetAnnoPubblicazione_MaggioreCorrente() {
        assertThrows(IllegalArgumentException.class, ()->{
            libro.setAnnoPubblicazione(2030);
        });
    }



    /**
     * Test of getNumeroCopieTotali method, of class Libro.
     */
    @Test
    public void testGetNumeroCopieTotali() {
        assertEquals(COPIE_TOTALI_INIZIALI, libro.getNumeroCopieTotali());
    }

    /**
     * Test of setNumeroCopieTotali method, of class Libro.
     */
    @Test
    public void testSetNumeroCopieTotali_Valido() {
        libro.setNumeroCopieTotali(NUOVE_COPIE_TOTALI);
        assertEquals(NUOVE_COPIE_TOTALI, libro.getNumeroCopieTotali());
    }
    
    @Test
    public void testSetNumeroCopieTotali_MinimoValido() {
        libro.setNumeroCopieDisponibili(0);
        libro.setNumeroCopieTotali(0);
        assertEquals(0, libro.getNumeroCopieTotali());
    }
    
    @Test
    public void testSetNumeroCopieTotali_Negativo() {
        assertThrows(IllegalArgumentException.class, ()->{
            libro.setNumeroCopieTotali(-1);
        });
    }
    
    @Test
    public void testSetNumeroCopieTotali_MinoreDisponibili() {
        libro.setNumeroCopieDisponibili(COPIE_DISPONIBILI);
        assertThrows(IllegalArgumentException.class, ()->{
            libro.setNumeroCopieTotali(COPIE_DISPONIBILI-1);
        });
    }



    /**
     * Test of getNumeroCopieDisponibili method, of class Libro.
     */
    @Test
    public void testGetNumeroCopieDisponibili() {
        assertEquals(COPIE_TOTALI_INIZIALI, libro.getNumeroCopieDisponibili(),"All'inizio le copie disponibili devono essere uguali alle totali");
    }

    /**
     * Test of setNumeroCopieDisponibili method, of class Libro.
     */
    @Test
    public void testSetNumeroCopieDisponibili_Valido() {
        libro.setNumeroCopieDisponibili(3);
        assertEquals(3, libro.getNumeroCopieDisponibili());
    }

    @Test
    public void testSetNumeroCopieDisponibili_MinimoValido() {
        libro.setNumeroCopieDisponibili(0);
        assertEquals(0, libro.getNumeroCopieDisponibili());
    }

    @Test
    public void testSetNumeroCopieDisponibili_MassimoValido() {
        libro.setNumeroCopieDisponibili(COPIE_TOTALI_INIZIALI);
        assertEquals(COPIE_TOTALI_INIZIALI, libro.getNumeroCopieDisponibili());
    }
    
    @Test
    public void testSetNumeroCopieDisponibili_Negativo() {
        assertThrows(IllegalArgumentException.class, ()->{
            libro.setNumeroCopieDisponibili(-1);
        });
    }
    
    @Test
    public void testSetNumeroCopieDisponibili_MaggioreDiCopieTotali() {
        assertThrows(IllegalArgumentException.class, ()->{
            libro.setNumeroCopieDisponibili(COPIE_TOTALI_INIZIALI+1);
        });
    }


    /**
     * Test of getUUID method, of class Libro.
     */
    @Test
    public void testGetUUID_NonNull() {
        assertNotNull(libro.getUUID());
    }

    @Test
    public void testGetUUID_Unico() {
        Libro libro2 = new Libro(TITOLO_INIZIALE, AUTORE_INIZIALE, NUOVO_ISBN, ANNO_INIZIALE, COPIE_TOTALI_INIZIALI);
        assertNotEquals(libro.getUUID(), libro2.getUUID());
    }
    /**
     * Test of incrementaCopieDisponibili method, of class Libro.
     */
    @Test
    public void testIncrementaCopieDisponibili_Valido() {
        libro.setNumeroCopieDisponibili(COPIE_DISPONIBILI);
        libro.incrementaCopieDisponibili();
        assertEquals(COPIE_DISPONIBILI+1,libro.getNumeroCopieDisponibili());
    }
    
    @Test
    public void testIncrementaCopieDisponibili_CopieDisponibiliUgualiTotali() {
        libro.setNumeroCopieDisponibili(COPIE_TOTALI_INIZIALI);
        libro.incrementaCopieDisponibili();
        assertEquals(COPIE_TOTALI_INIZIALI,libro.getNumeroCopieDisponibili());
    }

    /**
     * Test of decrementaCopieDisponibili method, of class Libro.
     */
    @Test
    public void testDecrementaCopieDisponibili_Valido() {
        libro.setNumeroCopieDisponibili(COPIE_DISPONIBILI);
        libro.decrementaCopieDisponibili();
        assertEquals(COPIE_DISPONIBILI-1,libro.getNumeroCopieDisponibili());
    }
    
    @Test
    public void testDecrementaCopieDisponibili_CopieDisponibiliUgualiZero() {
        libro.setNumeroCopieDisponibili(0);
        libro.decrementaCopieDisponibili();
        assertEquals(0,libro.getNumeroCopieDisponibili());
    }
    /**
     * Test of haCopieDisponibili method, of class Libro.
     */
    @Test
    public void testHaCopieDisponibili_Vero() {
        libro.setNumeroCopieDisponibili(COPIE_DISPONIBILI);
        assertTrue(libro.haCopieDisponibili());
    }
    
    @Test
    public void testHaCopieDisponibili_Falso() {
        libro.setNumeroCopieDisponibili(0);
        assertFalse(libro.haCopieDisponibili());
    }

    /**
     * Test of hashCode method, of class Libro.
     */
    @Test
    public void testHashCode_StessoLibro() {
        assertEquals(libro.hashCode(), libro.hashCode());
    }

    @Test
    public void testHashCode_StessoISBN() {
        Libro libro2=new Libro("", "", libro.getISBN(), 0, 0);
        assertEquals(libro.hashCode(), libro2.hashCode());
    }
    
    @Test
    public void testHashCode_CaseInsensitive() {
        Libro libro2=new Libro("", "", libro.getISBN().toLowerCase(), 0, 0);
        Libro libro3=new Libro("", "", libro.getISBN().toUpperCase(), 0, 0);
        assertEquals(libro2.hashCode(), libro3.hashCode());
    }
    
    @Test
    public void testHashCode_DiversoISBN() {
        Libro libro2=new Libro("", "", libro.getISBN()+"a", 0, 0);
        assertNotEquals(libro.hashCode(), libro2.hashCode());
    }
    /**
     * Test of equals method, of class Libro.
     */
    

    
    @Test
    public void testEquals_ObjNull() {
        assertFalse(libro.equals(null));
    }
    
    @Test
    public void testEquals_ClasseDiversa() {
        assertFalse(libro.equals("Stringa"));
    }
    
    @Test
    public void testEquals_StessoLibro() {
        assertTrue(libro.equals(libro));
    }

    @Test
    public void testEquals_StessoISBN() {
        Libro libro2=new Libro("", "", libro.getISBN(), 0, 0);
        assertTrue(libro.equals(libro2));
        assertTrue(libro2.equals(libro));
    }
    
    @Test
    public void testEquals_ISBNCaseInsensitive() {
        Libro libro2 = new Libro("", "", libro.getISBN().toUpperCase(), 0, 0);
        Libro libro3 = new Libro("", "", libro.getISBN().toLowerCase(), 0, 0);
        assertTrue(libro2.equals(libro3));
    }
    
    @Test
    public void testEquals_DiversoISBN() {
        Libro libro2=new Libro("", "", libro.getISBN()+"a", 0, 0);
        assertFalse(libro.equals(libro2));
        assertFalse(libro2.equals(libro));
    }
    
    @Test
    public void testSerializzazione() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(libro);
        oos.close();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        Libro libroDeserializzato = (Libro) ois.readObject();
        ois.close();

        assertAll("Verifica deserializzazione",
            () -> assertEquals(libro.getTitolo(), libroDeserializzato.getTitolo()),
            () -> assertEquals(libro.getAutore(), libroDeserializzato.getAutore()),
            () -> assertEquals(libro.getISBN(), libroDeserializzato.getISBN()),
            () -> assertEquals(libro.getAnnoPubblicazione(), libroDeserializzato.getAnnoPubblicazione()),
            () -> assertEquals(libro.getNumeroCopieTotali(), libroDeserializzato.getNumeroCopieTotali()),
            () -> assertEquals(libro.getNumeroCopieDisponibili(), libroDeserializzato.getNumeroCopieDisponibili()),
            () -> assertEquals(libro.getUUID(), libroDeserializzato.getUUID())
        );
    }
    
}
