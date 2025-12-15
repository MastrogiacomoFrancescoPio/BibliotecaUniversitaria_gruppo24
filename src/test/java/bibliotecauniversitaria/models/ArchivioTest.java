/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bibliotecauniversitaria.models;

import java.io.File;
import java.nio.file.Path;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.io.TempDir;

/**
 *
 * @author franc
 */
public class ArchivioTest {
    
    @TempDir
    Path tempPath;
    
    public ArchivioTest() {
    }
    
    @Test
    public void testCarica() {
        System.out.println("testCarica");
        
        File nomeFileInesistente = new File(tempPath.toFile(),"fileCheNonEsiste.bbl");
     
        ObservableList<?> result = Archivio.carica(nomeFileInesistente);
       
        assertNotNull(result, "Il risultato non deve essere null.");
        assertTrue(result.isEmpty(), "Il caricamento di un file inesistente deve restituire una lista vuota.");
    }
    
    @Test
    public void testScrivi() {
        System.out.println("scrivi");
        ObservableList<String> listaProva = FXCollections.observableArrayList(
            "Elemento Uno", "Elemento Due"
        );
        File fileProva = new File(tempPath.toFile(), "test_scrivi.bbl");
        Archivio.scrivi(listaProva, fileProva);
        assertTrue(fileProva.exists(), "Il file deve esistere dopo la scrittura.");
       
        ObservableList<String> listaCaricata = Archivio.carica(fileProva); 
        assertEquals(listaProva, listaCaricata, "La lista caricata non corrisponde alla lista scritta.");
    }
}
