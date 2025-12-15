package bibliotecauniversitaria.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;


class UserArchiveTest {

    @TempDir
    Path tempDir;

    @Test
    void testSaveAndLoad() throws IOException, ClassNotFoundException {
        System.out.println("Test: Salvataggio e Caricamento");

        String filename = "test_user_save.bbl";
        String testEmail = "test@example.com";
        String testHash = "hash12345";
        
        File file = new File(tempDir.toFile(), filename);
        UserArchive archiveToSave = new UserArchive(testHash, testEmail);
        archiveToSave.saveTo(file.getPath());
        
        assertTrue(file.exists(), "Il file dovrebbe essere stato creato su disco.");

        UserArchive loadedArchive = UserArchive.loadFrom(file.getPath());

        assertNotNull(loadedArchive, "L'oggetto caricato non dovrebbe essere null.");
        assertEquals(testEmail, loadedArchive.email, "L'email caricata deve corrispondere a quella salvata.");
        assertEquals(testHash, loadedArchive.hashedPassword, "La password hashata deve corrispondere.");
    }

    @Test
    void testExists_ReturnsTrue() throws IOException {
        System.out.println("Test: Exists (True)");

        UserArchive archive = new UserArchive("pass", "mail@mail.com");
        
        File fTemp = new File(tempDir.toFile(), "test_user.bbl");
        archive.saveTo(fTemp.getPath());

        boolean exists = UserArchive.exists(fTemp.getPath());

        assertTrue(exists, "Il metodo exists() dovrebbe restituire TRUE se il file è presente.");
    }

    @Test
    void testExists_ReturnsFalse() {
        System.out.println("Test: Exists (False)");

        File fTemp = new File(tempDir.toFile(), "test_user.bbl");
        boolean exists = UserArchive.exists(fTemp.getPath());

        assertFalse(exists, "Il metodo exists() dovrebbe restituire FALSE se il file non esiste.");
    }

    @Test
    void testLoadFrom_FileNotFound() {
        System.out.println("Test: LoadFrom File Inesistente");

        String fakeFile = "non_esiste.bbl";

        assertThrows(IOException.class, () -> {
            UserArchive.loadFrom(fakeFile);
        }, "Dovrebbe lanciare IOException o FileNotFoundException se il file manca.");
    }
}