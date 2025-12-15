/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bibliotecauniversitaria.controllers;

import it.unisa.diem.oop.aaaaa.exceptions.UtenteGiaEsistenteException;
import it.unisa.diem.oop.aaaaa.models.Archivio;
import it.unisa.diem.oop.aaaaa.models.Biblioteca;
import it.unisa.diem.oop.aaaaa.models.Prestito;
import it.unisa.diem.oop.aaaaa.models.Utente;
import it.unisa.diem.oop.aaaaa.utils.StageHelper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Region;
import javafx.scene.shape.Arc;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

/**
 *
 * @author ACER
 */
public class GestioneUtentiController {
    @FXML
    private TextField nomeTxt;
    @FXML
    private TextField cognomeTxt;
    @FXML
    private TextField matricolaTxt;
    @FXML
    private TextField emailTxt;


    @FXML
    private TableView<Utente> tabellaUtenti;
    @FXML
    private TableColumn<Utente, String> colonnaNome;
    @FXML
    private TableColumn<Utente, String> colonnaCognome;
    @FXML
    private TableColumn<Utente, String> colonnaMatricola;
    @FXML
    private TableColumn<Utente, String> colonnaEmail;
    @FXML
    private TableColumn<Utente, ArrayList<Prestito>> colonnaPrestiti;
    @FXML
    private TableColumn<Utente, Integer> colonnaSegnalazioni;
    @FXML
    private TableColumn<Utente, LocalDate> colonnaSospensioni;
    @FXML
    private TableColumn<Utente, LocalDate> colonnaFineSospensioni;


    @FXML
    public void initialize() {
        colonnaNome.setCellValueFactory(new PropertyValueFactory("nome"));
        colonnaCognome.setCellValueFactory(new PropertyValueFactory("cognome"));
        colonnaMatricola.setCellValueFactory(new PropertyValueFactory("matricola"));
        colonnaEmail.setCellValueFactory(new PropertyValueFactory("email"));
        colonnaPrestiti.setCellValueFactory(new PropertyValueFactory("prestiti"));
        colonnaSegnalazioni.setCellValueFactory(new PropertyValueFactory("numeroSegnalazioni"));
        colonnaSospensioni.setCellValueFactory(new PropertyValueFactory("dataSospensione"));
        colonnaFineSospensioni.setCellValueFactory(new PropertyValueFactory("dataFineSospensione"));

        colonnaNome.setCellFactory(TextFieldTableCell.forTableColumn());
        colonnaCognome.setCellFactory(TextFieldTableCell.forTableColumn());
        colonnaMatricola.setCellFactory(TextFieldTableCell.forTableColumn());
        colonnaEmail.setCellFactory(TextFieldTableCell.forTableColumn());
        colonnaSegnalazioni.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        colonnaSospensioni.setCellValueFactory(cellData -> cellData.getValue().dataSospensioneProperty());

        colonnaFineSospensioni.setCellValueFactory(cellData -> cellData.getValue().dataFineSospensioneProperty());


        colonnaSospensioni.setCellFactory(column -> {
            return new TableCell<Utente, LocalDate>() {
                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null) {
                        setText(null);
                    } else {
                        setText(item.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                        Archivio.scrivi(Biblioteca.getListaUtenti(), Archivio.fileUtenti);
                    }
                }
            };
        });
        colonnaFineSospensioni.setCellFactory(column -> {
            return new TableCell<Utente, LocalDate>() {
                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                        Archivio.scrivi(Biblioteca.getListaUtenti(), Archivio.fileUtenti);
                    }
                }
            };
        });
        colonnaPrestiti.setCellFactory(tc -> new TableCell<Utente, ArrayList<Prestito>>() {
            @Override
            protected void updateItem(ArrayList<Prestito> item, boolean empty) {
                super.updateItem(item, empty);
                if(empty || item == null) {
                    setText(null);
                } else {
                    setText(item.size() + "[" + item.stream().map(i -> i.getLibro().getTitolo()).collect(Collectors.joining(", ")) + "]");
                }
            }
        });

        colonnaSospensioni.setEditable(false);
        tabellaUtenti.setEditable(true);

        colonnaNome.setOnEditCommit(e -> {
            e.getRowValue().setNome(e.getNewValue());
            Archivio.scrivi(Biblioteca.getListaUtenti(), Archivio.fileUtenti);
            Archivio.scrivi(Biblioteca.getListaPrestiti(), Archivio.filePrestiti);
        });

        colonnaCognome.setOnEditCommit(e -> {
            e.getRowValue().setCognome(e.getNewValue());
            Archivio.scrivi(Biblioteca.getListaUtenti(), Archivio.fileUtenti);
        });

        colonnaMatricola.setOnEditCommit(e -> {
            e.getRowValue().setMatricola(e.getNewValue());
            Archivio.scrivi(Biblioteca.getListaUtenti(), Archivio.fileUtenti);
            Archivio.scrivi(Biblioteca.getListaPrestiti(), Archivio.filePrestiti);
            System.out.println(Biblioteca.getListaPrestiti().stream().map(aa -> aa.toString()).collect(Collectors.toList()));
        });

        colonnaEmail.setOnEditCommit(e -> {
            try {
                e.getRowValue().setEmail(e.getNewValue());
            } catch (IllegalArgumentException ex) {
                new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
                tabellaUtenti.refresh();
            }
            Archivio.scrivi(Biblioteca.getListaUtenti(), Archivio.fileUtenti);
        });

        tabellaUtenti.setItems(Biblioteca.getListaUtenti());
        //Funzione che colora la riga di rosso in caso di ban
        tabellaUtenti.setRowFactory(tv -> {
            return new TableRow<Utente>() {
                @Override
                protected void updateItem(Utente item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setStyle("");
                    } else if (item.isSospeso()) {
                        setStyle("-fx-background-color: #e53632;");
                    } else {
                        setStyle("");
                    }
                }
            };
        });
        Biblioteca.togliSospensioni();
        tabellaUtenti.setItems(Biblioteca.getListaUtenti());
    }


    @FXML
    public void onMenu() {
        StageHelper.switchToNew((Stage) nomeTxt.getScene().getWindow(), "menu", "Menu");
    }

    @FXML
    public void onAggiungi() {
        if (nomeTxt.getText().equals("")) {
            new Alert(Alert.AlertType.ERROR, "Il campo nome non può essere vuoto.").showAndWait();
            return;
        }
        if (cognomeTxt.getText().equals("")) {
            new Alert(Alert.AlertType.ERROR, "Il campo cognome non può essere vuoto.").showAndWait();
            return;
        }
        if (matricolaTxt.getText().equals("")) {
            new Alert(Alert.AlertType.ERROR, "Il campo matricola non può essere vuoto.").showAndWait();
            return;
        }

        if (emailTxt.getText().equals("")) {
            new Alert(Alert.AlertType.ERROR, "Il campo email non può essere vuoto.").showAndWait();
            return;
        }
        try{
            Utente u = new Utente(matricolaTxt.getText(), nomeTxt.getText(), cognomeTxt.getText(), emailTxt.getText());
            Biblioteca.aggiungiUtente(u);
        }catch(UtenteGiaEsistenteException | IllegalArgumentException e){
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
            return;
        }
        tabellaUtenti.setItems(Biblioteca.getListaUtenti());
        matricolaTxt.clear();
        nomeTxt.clear();
        cognomeTxt.clear();
        emailTxt.clear();

    }

    @FXML
    public void onRimuovi() {
        Utente selezionato = tabellaUtenti.getSelectionModel().getSelectedItem();
         if (selezionato == null) {
            new Alert(Alert.AlertType.WARNING, "Seleziona un utente da rimuovere.")
                .showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Sicuro di voler rimuovere " + selezionato.getNome() + " " + selezionato.getCognome() + "?", ButtonType.YES, ButtonType.NO);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            try{
                Biblioteca.rimuoviUtente(selezionato);
            }catch(UtenteGiaEsistenteException e){
                new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
            }
                tabellaUtenti.setItems(Biblioteca.getListaUtenti());
        }
    }

    @FXML
    public void onRicerca() {
        ObservableList<Utente> utentiTrovati = Biblioteca.cercaUtente(new Utente(matricolaTxt.getText(), nomeTxt.getText(), cognomeTxt.getText(), emailTxt.getText()));

        if (utentiTrovati.isEmpty()) {
            tabellaUtenti.setItems(Biblioteca.getListaUtenti());
            new Alert(Alert.AlertType.INFORMATION, "Nessun utente trovato corrispondente ai criteri inseriti.").showAndWait();
        } else {
            tabellaUtenti.setItems(utentiTrovati);
        }
    }
    
    @FXML
    public void onSospendi() {
        Utente selezionato = tabellaUtenti.getSelectionModel().getSelectedItem();

        if (selezionato == null) {
            new Alert(Alert.AlertType.WARNING, "Selezionare prima un utente dalla tabella da sospendere.").showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Sicuro di voler " + (selezionato.isSospeso() ? "revocare la sospensione a " : "sospendere ") + selezionato.getNome() + " " + selezionato.getCognome() + "?", ButtonType.YES, ButtonType.NO);
        alert.setResizable(true);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        Optional<ButtonType> result = alert.showAndWait();
        if (!result.isPresent() || result.get() != ButtonType.YES) return;
        if (selezionato.isSospeso()) {
            selezionato.revocaSospensione(true);
            tabellaUtenti.setItems(Biblioteca.getListaUtenti());
            tabellaUtenti.refresh();
            Archivio.scrivi(Biblioteca.getListaUtenti(), Archivio.fileUtenti);
        } else {
            selezionato.sospendi(30,true);
            tabellaUtenti.setItems(Biblioteca.getListaUtenti());
            tabellaUtenti.refresh();
            Archivio.scrivi(Biblioteca.getListaUtenti(), Archivio.fileUtenti);
        }
    }


    @FXML
    public void onOrdinaCognome() {
        tabellaUtenti.setItems(Biblioteca.ordinaUtentiCognome(tabellaUtenti.getItems()));
    }

    @FXML
    public void onOrdinaNome() {
        tabellaUtenti.setItems(Biblioteca.ordinaUtentiNome(tabellaUtenti.getItems()));
    }

    @FXML
    public void onOrdinaMatricola() {
        tabellaUtenti.setItems(Biblioteca.ordinaUtentiMatricola(tabellaUtenti.getItems()));
    }
}
