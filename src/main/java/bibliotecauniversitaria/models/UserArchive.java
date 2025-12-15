/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bibliotecauniversitaria.models;

import java.io.*;

public class UserArchive implements Serializable {

    public String hashedPassword;
    public String email;

    public static String NAME = new File(Archivio.cartellaData,"user.bbl").getPath();


    public UserArchive(String hashedPassword, String email) {
        this.hashedPassword = hashedPassword;
        this.email = email;
    }


    public static Boolean exists(String path) {
        try {
            UserArchive.loadFrom(path);
            return true;
        } catch (IOException e) {
            return false;
        }
    }


    public void saveTo(String path) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(path)))) {
            oos.writeObject(this);
        }
    }


    public static UserArchive loadFrom(String path) throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(path)))) {
            return (UserArchive) ois.readObject();
        } catch (ClassNotFoundException e) {
            System.out.println("Serialized file error.");
            return null;
        }
    }
}
