package it.unisa.diem.ids.bibliotecauniversitaria;

import java.util.List;

public class Utente {

    private String matricola;

    private String nome;

    private String cognome;

    private boolean sospeso;

    private int segnalazioni;

    private List<Prestito> prestitiAttivi;

    /**
 * @brief Verifica se l'utente è attualmente in uno stato di sospensione.
 *
 * Implementa il controllo di validità IF-6.7.
 *
 * @return {@code true} se l'utente è sospeso (non può prendere libri in prestito); {@code false} altrimenti.
 *
 * @pre L'utente è stato selezionato per un'operazione che richiede che esso non sia sospeso.
 *
 * @post Lo stato di sospensione dell'utente viene accertato.
 */
    public boolean isSospeso() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
 * @brief Verifica se l'utente ha attualmente prestiti la cui data di restituzione prevista è scaduta.
 *
 * Implementa il controllo di validità IF-6.6
 *
 * @return {@code true} se l'utente ha almeno un prestito in ritardo; {@code false} altrimenti.
 *
 * @pre L'utente è stato selezionato per un'operazione che richiede che esso non abbia dei prestiti 
 * già in ritardo.
 *
 * @post Lo stato dei prestiti attivi dell'utente viene verificato rispetto alla data odierna.
 */
    public boolean verificaPrestitiInRitardo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
 * @brief Aggiunge un prestito alla lista dei prestiti attivi associati a questo utente.
 *
 * Corrisponde alla postcondizione e al passo 11 del caso d'uso "Registrazione Prestito",
 * dove il numero di prestiti dell'utente viene incrementato.
 *
 * @param prestito L'oggetto  Prestito appena registrato da associare all'utente.
 *
 * @pre Tutti i controlli di validità (limite prestiti, ritardi, sospensione) per il prestito sono stati superati.
 *
 * @post La lista interna dei prestiti attivi dell'utente include il nuovo prestito.
 */
    public void aggiungiPrestito(Prestito prestito) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
 * @brief Rimuove un prestito dalla lista dei prestiti attivi associati a questo utente.
 *
 * Corrisponde alla postcondizione e al passo 7 del caso d'uso "Registrazione Restituzione",
 * dove il numero di prestiti dell'utente viene decrementato. Questo metodo è anche
 * implicitamente legato al controllo IF-9.4 del caso d'uso "Rimozione Utenti".
 *
 * @param prestito L'oggetto Prestito che è stato restituito e deve essere rimosso.
 *
 * @pre La restituzione del prestito è stata correttamente registrata.
 *
 * @post La lista interna dei prestiti attivi dell'utente non include più il prestito specificato.
 */
    public void rimuoviPrestito(Prestito prestito) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
 * @brief Incrementa il contatore delle segnalazioni associate all'utente.
 *
 * Questo metodo viene invocato quando l'utente restituisce un libro con un ritardo 
 * superiore a due settimane.
 *
 * @pre Il sistema ha verificato un ritardo di restituzione superiore a due settimane.
 *
 * @post Il numero di segnalazioni associate all'utente è aumentato di una unità.
 */
    public void incrementaSegnalazioni() {
    }
    
    /**
 * @brief Restituisce il numero corrente di segnalazioni associate all'utente per ritardi di restituzione.
 *
 * Questo metodo è utilizzato per verificare se il numero di segnalazioni raggiunge la soglia di sospensione (max 3).
 * arrivato a 3 chiamerà il metodo {@code sospendi(int giorni)}
 *
 * @return Il numero intero di segnalazioni accumulate dall'utente.
 *
 * @pre L'utente è stato sottoposto a verifica per un ritardo di restituzione.
 *
 * @post Il numero di segnalazioni dell'utente viene letto e restituito.
 */

    public int controllaSegnalazioni() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
 * @brief Imposta lo stato dell'utente a "sospeso" per un numero specificato di giorni.
 *
 * Questo metodo è chiamato quando l'utente raggiunge 3 segnalazioni per ritardo o in caso di ritardo superiore a 3 settimane..
 *
 * @param giorni Il numero di giorni per cui l'utente deve essere sospeso (30 giorni).
 *
 * @pre L'utente ha superato le soglie di ritardo che richiedono la sospensione.
 * @pre L'utente ha superato di 3 settimane la data di restituzione prevista per il libro che aveva preso in prestito
 *
 * @post L'utente è contrassegnato come "sospeso" nel sistema per il periodo specificato.
 */
    public void sospendi(int giorni) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
