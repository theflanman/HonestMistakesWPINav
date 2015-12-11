package main.gui.frontutil;

import java.io.File;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailSender {

	/*
	 * @author Nick Gigliotti
	 */
    public void sendFromGMail(String[] toEmail, String subject, String body) {
        Properties props = System.getProperties();
    	String fromEmail = "EraOfNavigation";
    	String pass = "HonestMistakes";
    	String myDirectoryPath = "src/data/pathimages";
    	
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", fromEmail);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(fromEmail));
            InternetAddress[] toAddress = new InternetAddress[toEmail.length];

            // To get the array of addresses
            for( int i = 0; i < toEmail.length; i++ ) {
                toAddress[i] = new InternetAddress(toEmail[i]);
            }

            for( int i = 0; i < toAddress.length; i++) {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }

            Multipart multipart = new MimeMultipart();
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);
            multipart.addBodyPart(messageBodyPart);
            
            File dir = new File(myDirectoryPath);
            File[] directoryListing = dir.listFiles();
            if (directoryListing != null) {
            	for (File child : directoryListing) {
            		child.deleteOnExit();
            		MimeBodyPart attachPart = new MimeBodyPart();
            		String attachFile = myDirectoryPath.concat("/".concat(child.getName()));

            		DataSource source = new FileDataSource(attachFile);

            		try {
            			attachPart.setDataHandler(new DataHandler(source));
            			attachPart.setFileName(new File(attachFile).getName());
            			multipart.addBodyPart(attachPart);
            		} catch (MessagingException e) {
            			e.printStackTrace();
            		}
            	}
            }
            
            message.setSubject(subject);
            message.setContent(multipart);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, fromEmail, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            System.out.println("Email Sent Successfuly");
            for (File file : directoryListing) {
            	file.delete();
            }
 
        }
        catch (AddressException ae) {
            ae.printStackTrace();
        }
        catch (MessagingException me) {
            me.printStackTrace();
        }
    }
}
