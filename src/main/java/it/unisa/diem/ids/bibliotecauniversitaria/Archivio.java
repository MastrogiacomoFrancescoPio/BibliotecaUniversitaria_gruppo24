package it.unisa.diem.ids.bibliotecauniversitaria;

import java.util.List;

public class Archivio {

    private String percorsoFileLibri;

    private String percorsoFileUtenti;

    private String percorsoFilePrestiti;

    /**
 * @brief Scrive la lista completa dei libri su un file di archivio dedicato ai libri.
 *
 * @param libri La {@code List<Libro>} da scrivere sul file.
 *
 * @pre La lista dei libri non è nulla 
 *
 * @post L'archivio dei libri è stato aggiornato con i dati della lista fornita.
 */
    public void scriviLibri(List<Libro> libri) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

     /**
 * @brief Scrive la lista completa degli utenti su un file di archivio dedicato agli utenti.
 *
 * @param utenti La {@code List<Utenti>} da scrivere sul file.
 *
 * @pre La lista degli utenti non è nulla 
 *
 * @post L'archivio degli utenti è stato aggiornato con i dati della lista fornita.
 */
    public void scriviUtenti(List<Utente> utenti) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

     /**
 * @brief Scrive la lista completa dei prestiti su un file di archivio dedicato ai prestiti.
 *
 * @param prestiti La {@code List<Prestito>} da scrivere sul file.
 *
 * @pre La lista dei prestiti non è nulla 
 *
 * @post L'archivio dei prestiti è stato aggiornato con i dati della lista fornita.
 */
    public void scriviPrestiti(List<Prestito> prestiti) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
 * @brief Legge la lista dei libri da un file di archivio dedicato.
 *
 * @return Una {@code List<Libro>} contenente tutti i libri letti dal file.
 *
 * @pre Il file di archivio per i libri esiste ed è leggibile.
 *
 * @post Il contenuto del file di archivio dei libri è stato caricato in una lista in memoria.
 */
    public List<Libro> leggiLibri() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
 * @brief Legge la lista degli utenti da un file di archivio dedicato.
 *
 * @return Una {@code List<Utente>} contenente tutti gli utenti letti dal file.
 *
 * @pre Il file di archivio per i libri esiste ed è leggibile.
 *
 * @post Il contenuto del file di archivio degli utenti è stato caricato in una lista in memoria.
 */
    public List<Utente> leggiUtenti() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
 * @brief Legge la lista dei prestiti da un file di archivio dedicato.
 *
 * @return Una {@code List<Prestito>} contenente tutti i prestiti letti dal file.
 *
 * @pre Il file di archivio per i prestiti esiste ed è leggibile.
 *
 * @post Il contenuto del file di archivio dei prestiti è stato caricato in una lista in memoria.
 */
    public List<Prestito> leggiPrestiti() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
