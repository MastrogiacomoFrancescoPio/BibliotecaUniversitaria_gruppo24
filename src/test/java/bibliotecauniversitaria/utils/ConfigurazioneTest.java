package bibliotecauniversitaria.utils;

import bibliotecauniversitaria.models.Biblioteca;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;


public class ConfigurazioneTest {
    
    @TempDir
    Path temp;

    /**
     * Test of carica method, of class Configurazione.
     * @throws java.io.IOException
     */
    @Test
    public void testConfig() throws IOException {
        File testConfig = new File(temp.toFile(), "test_configurazione");
        Biblioteca.configurazione = new Configurazione();
        assertFalse(testConfig.exists(), "Configurazione test NON dovrebbe esistere.");
        Biblioteca.configurazione.salvaDefault(testConfig);
        assertTrue(testConfig.exists(), "Configurazione test dovrebbe esistere");
        Biblioteca.configurazione.carica(testConfig);
        assertEquals("false",Biblioteca.configurazione.get("SMTP_CONFIGURATO"),"Dovrebbe ritornare false");
    }

}