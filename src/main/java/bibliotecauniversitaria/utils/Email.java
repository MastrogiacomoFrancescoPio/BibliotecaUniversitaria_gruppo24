package bibliotecauniversitaria.utils;

import bibliotecauniversitaria.Main;
import bibliotecauniversitaria.models.Biblioteca;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.security.SecureRandom;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Properties;
import java.util.stream.Collectors;
import javafx.scene.control.Alert;

public class Email {

    public static String host = "";
    public static int port;
    public static String mail = "";
    public static String password = "";
    public static Boolean tls = false;
     
    /**
     * @brief Verifica se una stringa rappresenta un indirizzo email valido.
     * Utilizza una Regular Expression (Regex) per controllare il formato.
     * @param email La stringa da validare.
     * @return true se il formato è valido (es. test@example.com), false altrimenti.
     */
    public static boolean isValida(String email){
        return email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]+$");
    }
    
    /**
     * @brief Carica la configurazione SMTP dalle impostazioni globali.
     * Legge host, porta, username, password e flag TLS dalla classe Biblioteca.configurazione.
     * @pre La configurazione globale deve essere stata inizializzata.
     * @post Le variabili statiche della classe vengono popolate con i dati di connessione.
     */
    public static void carica() {
        host = Biblioteca.configurazione.get("SMTP_HOST");
        port = Integer.parseInt(Biblioteca.configurazione.get("SMTP_PORT"));
        mail = Biblioteca.configurazione.get("SMTP_USERNAME");
        password = Biblioteca.configurazione.get("SMTP_PASSWORD");
        password = Biblioteca.configurazione.get("SMTP_PASSWORD");
        tls = Biblioteca.configurazione.get("SMTP_TLS").equalsIgnoreCase("true");
    }
    
    /**
     * @brief Controlla se il servizio email è abilitato nella configurazione.
     * @return true se la chiave "SMTP_CONFIGURATO" è impostata su "true", false altrimenti.
     */
    public static boolean isConfigurato() {
        return Biblioteca.configurazione.get("SMTP_CONFIGURATO")!=null&&Biblioteca.configurazione.get("SMTP_CONFIGURATO").equals("true");
    }
    
    /**
     * @brief Invia una email formattata utilizzando un template HTML.
     * Carica un file HTML dalle risorse, sostituisce i placeholder {chiave} con i valori
     * forniti nella mappa e invia il messaggio.
     * @pre Il servizio email deve essere configurato (isConfigurato() == true).
     * @pre Il file HTML specificato deve esistere nella cartella delle risorse "html/".
     * @param mail L'indirizzo email del destinatario.
     * @param soggetto L'oggetto dell'email.
     * @param nomeDelHtml Il nome del file template (senza estensione .html).
     * @param chiavi Mappa contenente le coppie chiave-valore per le sostituzioni nel template.
     * @throws MessagingException Se si verifica un errore durante l'invio (es. credenziali errate).
     */
    public static void mandaMailPagina(String mail, String soggetto, String nomeDelHtml, HashMap<String, String> chiavi) throws MessagingException {
        if(!isConfigurato()) return;
        try (InputStream in = Main.class.getResourceAsStream("html/"+nomeDelHtml+".html")) {
            String html = new BufferedReader(new InputStreamReader(in))
                    .lines()
                    .collect(Collectors.joining("\n"));

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            if(chiavi!=null){
                for(String chiave : chiavi.keySet()) {
                    html = html.replace("{"+chiave+"}",chiavi.get(chiave));
                }
            }

            Email.mandaMail(
                    InternetAddress.parse(mail)[0],
                    soggetto,
                    html
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (MessagingException e) {
            throw e;
        }
    }
    
    /**
     * @brief Metodo di basso livello per l'invio dell'email tramite SMTP.
     * Configura la sessione JavaMail, crea l'autenticatore e spedisce il messaggio MIME.
     * @pre Il servizio email deve essere configurato.
     * @param rec Oggetto InternetAddress del destinatario.
     * @param soggetto Oggetto dell'email.
     * @param body Corpo dell'email in formato HTML.
     * @throws MessagingException In caso di fallimento della connessione o dell'invio.
     */
    public static void mandaMail(InternetAddress rec, String soggetto, String body)
            throws MessagingException {
        if(!isConfigurato()) return;
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", String.valueOf(port));
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", String.valueOf(tls));

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mail,password);
            }
        });
        MimeMessage message = new MimeMessage(session);


        try {
            message.setFrom(new InternetAddress(mail, "Biblioteca"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        message.addRecipient(Message.RecipientType.TO, rec);
        message.setSubject(soggetto);
        //message.setText(body);
        message.setContent(body, "text/html; charset=utf-8");

        Transport.send(message);
    }
    
    /**
     * @brief Genera un codice numerico casuale e lo invia via email per il reset della password.
     * Crea un codice a 6 cifre utilizzando SecureRandom e utilizza il template "reset" per l'invio.
     * @param mail L'indirizzo email a cui inviare il codice.
     * @return Una stringa contenente il codice generato (es. "123456"). Restituisce stringa vuota in caso di errore.
     */
    public static String mandaReset(String mail) {
        SecureRandom sr = new SecureRandom();
        String codice = "";
        for(int i = 0;i<6;i++){
            codice+=sr.nextInt(10);
        }
        HashMap<String, String> sostituzioni = new HashMap<>();
        sostituzioni.put("codice",codice);
        try {
            mandaMailPagina(mail,"Reset password","reset",sostituzioni);
        } catch (MessagingException e) {
            codice="";
            new Alert(Alert.AlertType.ERROR, "Impossibile mandare l'email.").showAndWait();
            System.out.println(e.getMessage());
        }
        return codice;
    }
    
}
