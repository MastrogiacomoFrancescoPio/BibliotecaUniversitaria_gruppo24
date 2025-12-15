package bibliotecauniversitaria.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordHelperTest {

    @Test
    public void encryptTest_Valid() {
        String password = "password123";
        String hash1 = PasswordHelper.encrypt(password);
        assertTrue(PasswordHelper.checkpw(hash1,password), "La password deve essere corretta.");
    }

    @Test
    public void encryptTest_Invalid() {
        String password = "password1234";
        String hash1 = PasswordHelper.encrypt(password);
        assertFalse(PasswordHelper.checkpw(hash1,password+"5"), "La password deve essere non corretta.");
    }

}