package it.unisa.diem.ids.bibliotecauniversitaria;


import java.time.LocalDate;

public class Prestito {

    private Libro libro;

    private Utente utente;

    private LocalDate dataInizio;

    private LocalDate dataRestPrevista;

    private LocalDate dataRestituzione;

    /**
 * @brief Verifica se la data di restituzione effettiva di un prestito supera la data prevista di più di due settimane.
 *
 * Implementa il controllo di validità per il ritardo superiore a 2 settimane (IF-8.2)
 * come parte del processo di registrazione della restituzione, che porta all'aggiunta di una segnalazione
 * all'Utente che ha effettuato la restituzione in ritardo
 *
 * @return {@code true} se il ritardo supera le due settimane; {@code false} altrimenti.
 *
 * @pre La restituzione del libro è avvenuta.
 * @pre La data di restituzione prevista e la data di restituzione effettiva non coincidono.
 *
 * @post Il sistema ha verificato la presenza di un ritardo.
 */
    public boolean verificaRitardoMaggioreDueSettimane() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
 * @brief Verifica se la data di restituzione effettiva di un prestito supera la data prevista di più di tre settimane.
 *
 * Implementa il controllo di validità per il ritardo superiore a 3 settimane (IF-8.3),
 * che porta alla sospensione automatica dell'utente nel caso d'uso "Registrazione Restituzione".
 *
 * @return {@code true} se il ritardo supera le tre settimane; {@code false} altrimenti.
 *
 * @pre La restituzione del prestito è avvenuta.
 * @pre Il sistema ha già verificato l'eventuale ritardo di due settimane.
 *
 * @post Il sistema ha verificato la presenza di un ritardo.
 */
    public boolean verificaRitardoMaggioreTreSettimane() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
