package larionovoleksanr.exam.beansConfiguration;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import larionovoleksanr.exam.entities.Dispositivo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static larionovoleksanr.exam.exceptions.ExceptionsHandler.newDateAndHour;

@Component
public class MailGunSender {
    private String mailGunKey;
    private String mailGunDomain;
    private String myEmail;

    public MailGunSender(@Value("${mailgun.apikey}") String mailGunKey,
                         @Value("${mailgun.domainname}") String mailGunDomain,
                         @Value("${myEmail}") String myEmail) {
        this.myEmail = myEmail;
        this.mailGunKey = mailGunKey;
        this.mailGunDomain = mailGunDomain;
    }

    public void sendRegistrationMail(String recipient, Dispositivo device) {
        HttpResponse<JsonNode> response = Unirest.post("https://api.mailgun.net/v3/" + this.mailGunDomain + "/messages")
                .basicAuth("api", this.mailGunKey)
                .queryString("from", "Company Epicode" + this.myEmail)
                .queryString("to", recipient)
                .queryString("subject", "Consegna del Nuovo Dispositivo")
                .queryString("text", "Siamo lieti di informarti che il tuo nuovo dispositivo è stato spedito con successo! Di seguito trovi i dettagli della spedizione:" +
                        "\n Tipo di Dispositivo" + device.getDeviceType() +
                        "\n" + "\n" + "\n" + "Se hai domande o dubbi, non esitare a contattare il nostro team di supporto." +
                        "\n Grazie per la tua collaborazione!" +
                        "\n Cordiali Saluti," +
                        "\n EPICODE STAFF" +
                        "\n" + newDateAndHour())
                .asJson();
    }
}
