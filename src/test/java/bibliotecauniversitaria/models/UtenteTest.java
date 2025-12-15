/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bibliotecauniversitaria.models;

import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author ACER
 */
public class UtenteTest {
    
    private final String MATRICOLA = "M001";
    private final String NOME = "Mario";
    private final String COGNOME = "Rossi";
    private final String EMAIL = "mariorossi@hotmail.com";

    @BeforeAll
    public static void setUpClass() {
        TestHelper.salva();
    }

    @AfterAll
    public static void tearDownClass() {
        TestHelper.ripristina();
    }

    private Utente u;

    @BeforeEach
    public void setUp() {
        u = new Utente(MATRICOLA, NOME, COGNOME, EMAIL);
        Biblioteca.setListaPrestiti(FXCollections.observableArrayList());
        Biblioteca.setListaUtenti(FXCollections.observableArrayList());
        Biblioteca.setListaLibri(FXCollections.observableArrayList());
    }

    @AfterEach
    public void tearDown() {

    }

    @Test
    public void testGetMatricola() {
        assertEquals(MATRICOLA, u.getMatricola());
    }

    @Test
    public void testSetMatricola() {
        u.setMatricola("M002");
        assertEquals("M002", u.getMatricola());
    }

    @Test
    public void testMatricolaProperty() {
        assertEquals(MATRICOLA, u.matricolaProperty().get());

        u.matricolaProperty().set("M002");
        assertEquals("M002", u.matricolaProperty().get());
    }

    @Test
    public void testGetNome() {
        assertEquals(NOME, u.getNome());
    }

    @Test
    public void testSetNome() {
        u.setNome("CIAO");
        assertEquals("CIAO", u.getNome());
    }

    @Test
    public void testGetCognome() {
        assertEquals(COGNOME, u.getCognome());
    }

    @Test
    public void testSetCognome() {
        u.setCognome("CIAO");
        assertEquals("CIAO", u.getCognome());
    }

    @Test
    public void testGetEmail() {
        assertEquals(EMAIL, u.getEmail());
    }

    @Test
    public void testSetEmail_Valida() {
        u.setEmail("test@hotmail.com");
        assertEquals("test@hotmail.com", u.getEmail());
    }

    @Test
    public void testSetEmail_NonValida() {
        String[] strings={"test", "@",".", "@.", "user@test", "user.test"};
        for(String email: strings){
            assertThrows(IllegalArgumentException.class, () -> {
                u.setEmail(email);
            });
        }
    }

    @Test
    public void testGetNumeroSegnalazioni() {
        assertEquals(0, u.getNumeroSegnalazioni());
    }

    @Test
    public void testSetNumeroSegnalazioni() {
        u.setNumeroSegnalazioni(1);
        assertEquals(1, u.getNumeroSegnalazioni());
    }

    @Test
    public void testSetNumeroSegnalazioni_Negativo() {
        assertThrows(IllegalArgumentException.class, () -> u.setNumeroSegnalazioni(-1));
    }

    @Test
    public void testSospeso_True() {
        u.setSospeso(true);
        assertTrue(u.isSospeso());
    }

    @Test
    public void testIsSospeso_False() {
        assertFalse(u.isSospeso());
    }

    @Test
    public void testSetSospeso_False() {
        u.setSospeso(false);
        assertFalse(u.isSospeso());
    }

    @Test
    public void testGetDataSospensione_AppenaCreato() {
        assertNull(u.getDataSospensione());
    }

    @Test
    public void testSetDataSospensione() {
        u.setDataSospensione(LocalDate.now());
        assertEquals(LocalDate.now(), u.getDataSospensione());
    }

    @Test
    public void testGetDataFineSospensione_AppenaCreato() {
        assertNull(u.getDataFineSospensione());
    }

    @Test
    public void testGetDataFineSospensione_Modificato() {
        u.setDataFineSospensione(LocalDate.now());
        assertEquals(LocalDate.now(), u.getDataFineSospensione());
    }

    @Test
    public void testGetUUID_NonNullo() {
        assertNotNull(u.getUUID());
    }

    @Test
    public void testGetUUID_Unico() {
        Utente u2 = new Utente(MATRICOLA + "A", NOME, COGNOME, EMAIL + "A");
        assertNotEquals(u.getUUID(), u2.getUUID());
    }
    
    @Test
    void testGetPrestiti_ListaVuota() {
        assertTrue(u.getPrestiti().isEmpty());
    }

    @Test
    public void testGetPrestiti() {
        Libro l = new Libro("A", "B", "C", 1, 1);
        Prestito p = new Prestito(l.getUUID(), u.getUUID(), LocalDate.now(), LocalDate.now().plusDays(1));
        Biblioteca.aggiungiUtente(u);
        Biblioteca.aggiungiLibro(l);
        Biblioteca.aggiungiPrestito(p,false);
        assertEquals(1, u.getPrestiti().size());
        assertEquals(p, u.getPrestiti().get(0));
    }

    @Test
    void testConteggioPrestiti_Zero() {
        assertEquals(0, u.conteggioPrestiti());
    }

    @Test
    public void testConteggioPrestiti() {
        Libro l = new Libro("A", "B", "C", 1, 1);
        Prestito p = new Prestito(l.getUUID(), u.getUUID(), LocalDate.now(), LocalDate.now().plusDays(1));
        Biblioteca.aggiungiUtente(u);
        Biblioteca.aggiungiLibro(l);
        Biblioteca.aggiungiPrestito(p,false);
        assertEquals(1, u.conteggioPrestiti());
    }

    @Test
    public void testVerificaLimitePrestitiRaggiunto_Raggiunto() {
        int MAX_PRESTITI = Biblioteca.configurazione.getNumero("MAX_PRESTITI");
        Libro l = new Libro("A", "B", "C", 1, MAX_PRESTITI + 1);
        Biblioteca.aggiungiUtente(u);
        Biblioteca.aggiungiLibro(l);
        for (int i = 0; i < MAX_PRESTITI; i++) {
            Prestito p = new Prestito(l.getUUID(), u.getUUID(), LocalDate.now(), LocalDate.now().plusDays(1));
            Biblioteca.aggiungiPrestito(p,false);
        }
        assertTrue(u.verificaLimitePrestitiRaggiunto());
    }

    @Test
    public void testVerificaLimitePrestitiRaggiunto_Superato() {
        int MAX_PRESTITI = 3;
        Libro l = new Libro("A", "B", "C", 1, MAX_PRESTITI + 1);
        Biblioteca.aggiungiUtente(u);
        Biblioteca.aggiungiLibro(l);
        for (int i = 0; i < MAX_PRESTITI + 1; i++) {
            Prestito p = new Prestito(l.getUUID(), u.getUUID(), LocalDate.now(), LocalDate.now().plusDays(1));
            Biblioteca.aggiungiPrestito(p,false);
        }
        assertTrue(u.verificaLimitePrestitiRaggiunto());
    }

    @Test
    public void testVerificaLimitePrestitiRaggiunto_Inferiore() {
        int MAX_PRESTITI = 3;
        Libro l = new Libro("A", "B", "C", 1, MAX_PRESTITI + 1);
        Biblioteca.aggiungiUtente(u);
        Biblioteca.aggiungiLibro(l);
        for (int i = 0; i < MAX_PRESTITI - 1; i++) {
            Prestito p = new Prestito(l.getUUID(), u.getUUID(), LocalDate.now(), LocalDate.now().plusDays(1));
            Biblioteca.aggiungiPrestito(p,false);
        }
        assertFalse(u.verificaLimitePrestitiRaggiunto());
    }

    @Test
    public void testIncrementaSegnalazioni() {
        u.incrementaSegnalazioni();
        assertEquals(1, u.getNumeroSegnalazioni());
    }
    
    @Test
    void testIncrementaSegnalazioni_Multiple() {
        u.incrementaSegnalazioni();
        u.incrementaSegnalazioni();
        u.incrementaSegnalazioni();
        assertEquals(3, u.getNumeroSegnalazioni());
    }

    @Test
    public void testResetSegnalazioni() {
        u.incrementaSegnalazioni();
        u.resetSegnalazioni();
        assertEquals(0, u.getNumeroSegnalazioni());
    }

    @Test
    public void testControllaSegnalazioni_Vero() {
        u.setNumeroSegnalazioni(3);
        u.controllaSegnalazioni(3, 1);
        assertTrue(u.isSospeso());
    }

    @Test
    public void testControllaSegnalazioni_Falso() {
        u.setNumeroSegnalazioni(2);
        u.controllaSegnalazioni(3, 1);
        assertFalse(u.isSospeso());
    }

    @Test
    public void testSospendi() {
        u.sospendi(30,false);
        assertTrue(u.isSospeso());
        assertEquals(LocalDate.now(), u.getDataSospensione());
        assertEquals(LocalDate.now().plusDays(30), u.getDataFineSospensione());
    }

    @Test
    public void testRevocaSospensione() {
        u.setSospeso(true);
        u.revocaSospensione(false);
        assertFalse(u.isSospeso());
        assertNull(u.getDataSospensione());
        assertNull(u.getDataFineSospensione());
    }

    @Test
    public void testIsSospensioneScaduta_Oggi() {
        u.sospendi(1,false);
        u.setDataSospensione(LocalDate.now().minusDays(1));
        u.setDataFineSospensione(LocalDate.now());
        assertTrue(u.isSospensioneScaduta());
    }

    @Test
    public void testIsSospensioneScaduta_Superata() {
        u.setSospeso(true);
        u.setDataSospensione(LocalDate.now().minusDays(2));
        u.setDataFineSospensione(LocalDate.now().minusDays(1));
        assertTrue(u.isSospensioneScaduta());
    }

    @Test
    public void testIsSospensioneScaduta_NonSuperata() {
        u.setSospeso(true);
        u.setDataSospensione(LocalDate.now().minusDays(1));
        u.setDataFineSospensione(LocalDate.now().plusDays(1));
        assertFalse(u.isSospensioneScaduta());
    }

    @Test
    public void testAggiornaSospensione_Scaduta() {
        u.setSospeso(true);
        u.setDataSospensione(LocalDate.now().minusDays(2));
        u.setDataFineSospensione(LocalDate.now());
        u.aggiornaSospensione(false);
        assertFalse(u.isSospeso());
        assertNull(u.getDataSospensione());
        assertNull(u.getDataFineSospensione());
    }

    @Test
    public void testAggiornaSospensione_NonScaduta() {
        u.setSospeso(true);
        u.setDataSospensione(LocalDate.now().minusDays(2));
        u.setDataFineSospensione(LocalDate.now().plusDays(1));
        u.aggiornaSospensione(false);
        assertTrue(u.isSospeso());
        assertNotNull(u.getDataSospensione());
        assertNotNull(u.getDataFineSospensione());
    }

    @Test
    public void testHashCode_StessoUtente() {
        assertEquals(u.hashCode(), u.hashCode());
    }

    @Test
    public void testHashCode_StessaMatricola() {
        Utente utente2 = new Utente(
                u.getMatricola(),
                "",
                "",
                "x@y.it"
        );
        assertEquals(u.hashCode(), utente2.hashCode());
    }

    @Test
    public void testHashCode_CaseInsensitive() {
        Utente utente2 = new Utente(
                u.getMatricola().toLowerCase(),
                "",
                "",
                "x@y.it"
        );
        Utente utente3 = new Utente(
                u.getMatricola().toUpperCase(),
                "",
                "",
                "x@y.it"
        );
        assertEquals(utente2.hashCode(), utente3.hashCode());
    }

    @Test
    public void testHashCode_MatricolaDiversa() {
        Utente utente2 = new Utente(
                u.getMatricola() + "A",
                "",
                "",
                "x@y.it"
        );
        assertNotEquals(u.hashCode(), utente2.hashCode());
    }

    @Test
    public void testEquals_ObjNull() {
        assertFalse(u.equals(null));
    }

    @Test
    public void testEquals_ClasseDiversa() {
        assertFalse(u.equals("Stringa"));
    }

    @Test
    public void testEquals_StessoUtente() {
        assertTrue(u.equals(u));
    }

    @Test
    public void testEquals_StessaMatricola() {
        Utente utente2 = new Utente(
                u.getMatricola(),
                "",
                "",
                "x@y.it"
        );
        assertTrue(u.equals(utente2));
        assertTrue(utente2.equals(u));
    }

    @Test
    public void testEquals_MatricolaCaseInsensitive() {
        Utente utente2 = new Utente(
                u.getMatricola().toUpperCase(),
                "",
                "",
                "x@y.it"
        );
        Utente utente3 = new Utente(
                u.getMatricola().toLowerCase(),
                "",
                "",
                "x@y.it"
        );
        assertTrue(utente2.equals(utente3));
    }

    @Test
    public void testEquals_MatricolaDiversa() {
        Utente utente2 = new Utente(
                u.getMatricola() + "A",
                "",
                "",
                "x@y.it"
        );
        assertFalse(u.equals(utente2));
        assertFalse(utente2.equals(u));
    }

    @Test
    public void testSerializzazione() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(u);
        oos.close();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        Utente utenteDeserializzato = (Utente) ois.readObject();
        ois.close();

        assertAll("Verifica deserializzazione",
                () -> assertEquals(u.getMatricola(), utenteDeserializzato.getMatricola()),
                () -> assertEquals(u.getNome(), utenteDeserializzato.getNome()),
                () -> assertEquals(u.getCognome(), utenteDeserializzato.getCognome()),
                () -> assertEquals(u.getEmail(), utenteDeserializzato.getEmail()),
                () -> assertEquals(u.getNumeroSegnalazioni(), utenteDeserializzato.getNumeroSegnalazioni()),
                () -> assertEquals(u.isSospeso(), utenteDeserializzato.isSospeso()),
                () -> assertEquals(u.getDataSospensione(), utenteDeserializzato.getDataSospensione()),
                () -> assertEquals(u.getDataFineSospensione(), utenteDeserializzato.getDataFineSospensione()),
                () -> assertEquals(u.getUUID(), utenteDeserializzato.getUUID())
        );
    }


}
