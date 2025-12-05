package it.unisa.diem.ids.bibliotecauniversitaria;

public class Libro {

    private String isbn;

    private String titolo;

    private String autore;

    private int copieDisponibili;

    /**
 * @brief Incrementa il numero di copie disponibili di questo libro nel catalogo.
 *
 * Questo metodo viene invocato tipicamente dopo la registrazione di una restituzione,
 * come postcondizione del caso d'uso "Registrazione Restituzione".
 *
 * @pre Il libro deve essere stato precedentemente prestato, aggiunto e quindi restituito.
 *
 * @post Il conteggio delle copie disponibili del libro è aumentato di una unità.
 */
    public void incrementaCopieDisponibili() {
    }

    /**
 * @brief Decrementa il numero di copie disponibili di questo libro nel catalogo.
 *
 * Questo metodo viene invocato tipicamente dopo la registrazione di un prestito,
 * come postcondizione del caso d'uso "Registrazione Prestito".
 *
 * @pre Il libro deve avere almeno una copia disponibile (verificato da {@code haCopieDisponibili()}).
 *
 * @post Il conteggio delle copie disponibili del libro è diminuito di una unità.
 */
    public void decrementaCopieDisponibili() {
    }

    /**
 * @brief Verifica se è presente almeno una copia disponibile di questo libro per un nuovo prestito.
 *
 * Questo metodo è utilizzato come condizione di validità (IF-6.4)
 * durante la procedura di "Registrazione Prestito".
 *
 * @return {@code true} se è disponibile almeno una copia del libro; {@code false} altrimenti.
 *
 * @pre una copia del libro è già registrato nel sistema.
 *
 * @post Lo stato di disponibilità del libro viene accertato.
 */
    public boolean haCopieDisponibili() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
