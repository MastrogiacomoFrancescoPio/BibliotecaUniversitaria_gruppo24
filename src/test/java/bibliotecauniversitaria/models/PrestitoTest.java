/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bibliotecauniversitaria.models;

import bibliotecauniversitaria.TestHelper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.UUID;
import javafx.collections.FXCollections;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author ACER
 */
public class PrestitoTest {
    
    private Prestito prestito;
    private Libro libro;
    private Utente utente;

    private final LocalDate DATA_INIZIO = LocalDate.of(2023, 1, 1);
    private final LocalDate DATA_RESTITUZIONE = LocalDate.of(2023, 1, 30);

    private final LocalDate NUOVA_DATA_INIZIO = LocalDate.of(2023, 2, 1);
    private final LocalDate NUOVA_DATA_RESTITUZIONE = LocalDate.of(2023, 2, 28);

    public PrestitoTest() {
    }


    @BeforeAll
    public static void salva() {
        TestHelper.salva(null);
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
    
    @BeforeEach
    public void setUp() {
        libro = new Libro("A","A","A",4,4);
        utente = new Utente("A","A","A","a@a.com");
        prestito = new Prestito(libro.getUUID(), utente.getUUID(), DATA_INIZIO, DATA_RESTITUZIONE);
        Biblioteca.aggiungiUtente(utente);
        Biblioteca.aggiungiLibro(libro);
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testGetLibro() {
        assertEquals(libro, prestito.getLibro());
    }

    @Test
    public void testSetLibro() {
        Libro l = new Libro("A","B","C",2,3);
        Biblioteca.aggiungiLibro(l);
        prestito.setLibro(l.getUUID());
        assertEquals(l, prestito.getLibro());
    }

    @Test
    public void testGetUtente() {
        assertEquals(utente, prestito.getUtente());
    }

    @Test
    public void testSetUtente() {
        Utente utente1 = new Utente("M","E","T","A@b.com");
        Biblioteca.aggiungiUtente(utente1);
        prestito.setUtente(utente1.getUUID());
        assertEquals(utente1, prestito.getUtente());
    }

    @Test
    public void testGetDataInizio() {
        assertEquals(DATA_INIZIO, prestito.getDataInizio());
    }

    @Test
    public void testSetDataInizio() {
        prestito.setDataInizio(NUOVA_DATA_INIZIO);
        assertEquals(NUOVA_DATA_INIZIO, prestito.getDataInizio());
    }
    
    @Test
    public void testGetDataRestituzionePrevista() {
        assertEquals(DATA_RESTITUZIONE, prestito.getDataRestituzionePrevista());
    }

    @Test
    public void testSetDataRestituzionePrevista() {
        prestito.setDataRestituzionePrevista(NUOVA_DATA_RESTITUZIONE);
        assertEquals(NUOVA_DATA_RESTITUZIONE, prestito.getDataRestituzionePrevista());
    }


    @Test
    public void testGetUUID_NonNullo() {
        assertNotNull(prestito.getUUID());
    }

    @Test
    public void testGetUUID_Unico() {
        Prestito prestito2 = new Prestito(UUID.randomUUID(), UUID.randomUUID(), DATA_INIZIO, DATA_RESTITUZIONE);
        assertNotEquals(prestito.getUUID(), prestito2.getUUID());
    }

    @Test
    public void testCalcolaRitardo_Positivo() {
        LocalDate dataVerifica = LocalDate.of(2023, 2, 4);
        int giorniRitardo = prestito.calcolaRitardo(dataVerifica);
        assertEquals(5, giorniRitardo);
    }

    @Test
    public void testCalcolaRitardo_Zero() {
        int giorniRitardo = prestito.calcolaRitardo(DATA_RESTITUZIONE);
        assertEquals(0, giorniRitardo);
    }

    @Test
    public void testCalcolaRitardo_Negativo() {
        LocalDate dataVerifica = LocalDate.of(2023, 1, 20);
        int giorniRitardo = prestito.calcolaRitardo(dataVerifica);
        assertEquals(-10, giorniRitardo);
    }

    @Test
    public void testVerificaRitardo_Vero() {
        prestito.setDataRestituzionePrevista(LocalDate.now().minusDays(10));
        assertTrue(prestito.verificaRitardo(5));
    }

    @Test
    public void testVerificaRitardo_Falso() {
        prestito.setDataRestituzionePrevista(LocalDate.now().minusDays(2));
        assertFalse(prestito.verificaRitardo(5));
    }

    @Test
    public void testVerificaRitardo_NessunRitardo() {
        prestito.setDataRestituzionePrevista(LocalDate.now().plusDays(1));
        assertFalse(prestito.verificaRitardo(0));
    }
    
    @Test
    public void testSerializzazione() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(prestito);
        oos.close();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        Prestito prestitoDeserializzato = (Prestito) ois.readObject();
        ois.close();

        assertAll("Verifica deserializzazione",
                () -> assertEquals(prestito.getLibro(), prestitoDeserializzato.getLibro()),
                () -> assertEquals(prestito.getUtente(), prestitoDeserializzato.getUtente()),
                () -> assertEquals(prestito.getUUID(), prestitoDeserializzato.getUUID()),
                () -> assertEquals(prestito.getDataInizio(), prestitoDeserializzato.getDataInizio()),
                () -> assertEquals(prestito.getDataRestituzionePrevista(), prestitoDeserializzato.getDataRestituzionePrevista())         
        );
    }
}