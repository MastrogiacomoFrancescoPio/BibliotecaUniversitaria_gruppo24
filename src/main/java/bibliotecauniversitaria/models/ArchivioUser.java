/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bibliotecauniversitaria.models;

import java.io.*;

public class ArchivioUser implements Serializable {

    public String hashedPassword;
    public String email;

    public static String NAME = new File(Archivio.cartellaData,"user.bbl").getPath();


    public ArchivioUser(String hashedPassword, String email) {
        this.hashedPassword = hashedPassword;
        this.email = email;
    }


    public static Boolean exists(String path) {
        try {
            ArchivioUser.loadFrom(path);
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


    public static ArchivioUser loadFrom(String path) throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(path)))) {
            return (ArchivioUser) ois.readObject();
        } catch (ClassNotFoundException e) {
            System.out.println("Serialized file error.");
            return null;
        }
    }
}
