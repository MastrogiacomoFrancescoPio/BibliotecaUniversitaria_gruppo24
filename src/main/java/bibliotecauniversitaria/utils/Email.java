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
    
    public static boolean isValida(String email){
        return email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]+$");
    }

    public static void carica() {
        host = Biblioteca.configurazione.get("SMTP_HOST");
        port = Integer.parseInt(Biblioteca.configurazione.get("SMTP_PORT"));
        mail = Biblioteca.configurazione.get("SMTP_USERNAME");
        password = Biblioteca.configurazione.get("SMTP_PASSWORD");
        password = Biblioteca.configurazione.get("SMTP_PASSWORD");
        tls = Biblioteca.configurazione.get("SMTP_TLS").equalsIgnoreCase("true");
    }

    public static boolean isConfigurato() {
        return Biblioteca.configurazione.get("SMTP_CONFIGURATO").equals("true");
    }

    public static void mandaMailPagina(String mail, String soggetto, String nomeDelHtml, HashMap<String, String> chiavi) throws MessagingException {
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

    public static String aStringa() {
        return host + ":" + port + " (" + mail + ":" + password + ") [TLS: " + tls + "]";
    }
    
    
}
