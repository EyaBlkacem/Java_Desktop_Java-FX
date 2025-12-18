package presentation;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import entities.Flight;
import entities.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import metier.ReservationService;
import metier.Session; // <-- ta session utilisateur

import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

public class PaymentController {

    @FXML private TextField nameField, cardField, expiryField, cvvField;
    @FXML private Label msgLabel;

    private String selectedSeat;
    private int flightId;
    private Flight flight;
    private double price;

    private final ReservationService reservationService = new ReservationService();

    // 🔹 Paramètres Mailtrap
    private final String fromEmail = "eyafatma@polytechnicien.com"; 
    private final String mailtrapUsername = "ecc0ce1d777e5e"; 
    private final String mailtrapPassword = "da4dfba24b8ad9"; 

    public void setData(int flightId, Flight flight, String selectedSeat) {
        this.flightId = flightId;
        this.flight = flight;
        this.selectedSeat = selectedSeat;
        this.price = flight.getPrice();
    }

    @FXML
    public void pay() {
        if (nameField.getText().isEmpty()
                || cardField.getText().isEmpty()
                || expiryField.getText().isEmpty()
                || cvvField.getText().isEmpty()) {
            msgLabel.setText("Please fill all fields ❗");
            return;
        }
     // ✅ Contrôle numéro de carte
        if (!cardField.getText().matches("\\d{16}")) {
            msgLabel.setText("Numéro de carte invalide !");
            return;
        }

        // ✅ Contrôle CVV
        if (!cvvField.getText().matches("\\d{3,4}")) {
            msgLabel.setText("CVV invalide !");
            return;
        }

        // ✅ Contrôle date d'expiration
        if (!expiryField.getText().matches("(0[1-9]|1[0-2])/\\d{2}")) {
            msgLabel.setText("Date d'expiration invalide !");
            return;
        }

        try {
            User user = Session.getUser(); // <-- ta session utilisateur
            if (user == null) {
                msgLabel.setText("Veuillez vous connecter ❌");
                return;
            }

            reservationService.saveReservation(user.getId(), flightId, selectedSeat, price);

            String reservationNumber = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            String pdfFile = "Ticket_" + reservationNumber + ".pdf";
            String pdfPath = System.getProperty("user.dir") + File.separator + pdfFile;

            createPDF(user.getUsername(), flight, selectedSeat, price, reservationNumber, pdfPath);

            sendEmailWithAttachment(
                    user.getEmail(),
                    "Confirmation de réservation FlightBooking",
                    "Bonjour " + user.getUsername() + ",\n\n" +
                            "Votre réservation est confirmée.\n\n" +
                            "Vol : " + flight.getAirline() + " " +
                            flight.getDeparture() + " → " +
                            flight.getDestination() + "\n" +
                            "Siège : " + selectedSeat + "\n" +
                            "Prix : $" + price + "\n" +
                            "Numéro : " + reservationNumber + "\n\n" +
                            "Merci pour votre confiance.\nFlightBooking",
                    pdfPath
            );

            msgLabel.setStyle("-fx-text-fill: green;");
            msgLabel.setText("Paiement réussi ✅ PDF + Email envoyés !");

            Stage stage = (Stage) nameField.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/presentation/ConfirmationView.fxml"));
            stage.setScene(new Scene(loader.load()));

        } catch (Exception e) {
            msgLabel.setStyle("-fx-text-fill:red;");
            msgLabel.setText("Erreur lors du paiement ❌");
            e.printStackTrace();
        }
    }

    @FXML
    public void back() {
        try {
            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/presentation/SeatView.fxml"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createPDF(String username, Flight flight, String seat,
                           double price, String reservationNumber, String filePath)
            throws IOException, DocumentException {

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        document.add(new Paragraph("========== FlightBooking Ticket =========="));
        document.add(new Paragraph("Utilisateur : " + username));
        document.add(new Paragraph("Vol : " + flight.getAirline() + " " + flight.getDeparture() + " → " + flight.getDestination()));
        document.add(new Paragraph("Siège : " + seat));
        document.add(new Paragraph("Prix : $" + price));
        document.add(new Paragraph("Numéro de réservation : " + reservationNumber));
        document.add(new Paragraph("Merci pour votre confiance !"));
        document.add(new Paragraph("========================================"));

        document.close();
        System.out.println("PDF créé : " + filePath);
    }

    private void sendEmailWithAttachment(String to, String subject, String body, String pdfPath) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.mailtrap.io");
            props.put("mail.smtp.port", "2525");

            // ⚠️ Utiliser javax.mail.Session pour l’email
            javax.mail.Session mailSession = javax.mail.Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(mailtrapUsername, mailtrapPassword);
                }
            });

            Message message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);

            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(body);

            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(new File(pdfPath));

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);

            Transport.send(message);
            System.out.println("Email envoyé à " + to + " avec PDF attaché (via Mailtrap)");

        } catch (Exception e) {
            e.printStackTrace();
            msgLabel.setStyle("-fx-text-fill:red;");
            msgLabel.setText("Erreur lors de l'envoi du mail ❌");
        }
    }
    
}
