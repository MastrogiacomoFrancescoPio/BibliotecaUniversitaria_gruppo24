/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bibliotecauniversitaria.utils;

import org.springframework.security.crypto.bcrypt.BCrypt;


public class PasswordHelper {

    /**
     * @param password La password in chiaro da criptare.
     * @return La stringa contenente l'hash BCrypt risultante.
     * @brief Cripta una password in chiaro utilizzando l'algoritmo BCrypt.
     * Genera automaticamente un salt casuale e produce un hash sicuro della password.
     * @pre La stringa 'password' non deve essere null.
     * @post Viene restituita una stringa contenente l'hash (incluso il salt) pronta per essere salvata.
     */
    public static String encrypt(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * @param hashed   L'hash BCrypt precedentemente generato e salvato.
     * @param password La password in chiaro da verificare.
     * @return true se la password è corretta (corrisponde all'hash), false altrimenti.
     * @brief Verifica la corrispondenza tra una password in chiaro e un hash.
     * Utilizzato durante la fase di login per controllare se la password inserita
     * dall'utente corrisponde a quella salvata (criptata) nel sistema.
     */
    public static boolean checkpw(String hashed, String password) {
        return BCrypt.checkpw(password, hashed);
    }

}
