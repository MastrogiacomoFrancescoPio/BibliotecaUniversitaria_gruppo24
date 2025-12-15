package bibliotecauniversitaria;


import bibliotecauniversitaria.models.Biblioteca;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Biblioteca.carica(); //carica gli archivi e la configurazione, se non esistono li crea

        new Timer(true).scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Biblioteca.togliSospensioni();
            }
        },0,30*60*1000); // Ogni mezz'ora
        
        
        StageHelper.switchToNew(stage, "schermatalogin", "Schermata d'accesso"); //Imposta la prima schermata
        stage.getIcons().add(new Image(getClass().getResourceAsStream("immagini/icon.png"))); //cambia l'icona del programma
        stage.show();
    }

}
