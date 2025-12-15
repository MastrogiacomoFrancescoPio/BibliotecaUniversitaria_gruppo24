/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bibliotecauniversitaria.exceptions;

/**
 *
 * @author franc
 */
public class LibroGiaEsistenteException extends RuntimeException {
    public LibroGiaEsistenteException(String message){
        super(message);
    }
}
