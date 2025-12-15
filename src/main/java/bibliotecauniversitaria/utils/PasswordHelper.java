/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bibliotecauniversitaria.utils;

import org.springframework.security.crypto.bcrypt.BCrypt;


public class PasswordHelper {

    public static String encrypt(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
    
    public static boolean checkpw(String hashed, String password) {
        return BCrypt.checkpw(password,hashed);
    }
    
}
