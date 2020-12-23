package pokemon;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class SendMail {
	
	public String fromAddress = ""; 
	public String emailPassword = "";
	public String toAddress = "";

	public SendMail(String fileName) {
		
		
		try {
			Properties props = new Properties();
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.port", "587");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			
			
			Session session = Session.getInstance(props, 
					new Authenticator() {
				protected PasswordAuthentication
				getPasswordAuthentication() {
					return new PasswordAuthentication(fromAddress, emailPassword);
				}
			});
			
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromAddress, ""));
			
			message.setReplyTo(
					new Address[]{new InternetAddress(toAddress)});
			message.setRecipients(
					Message.RecipientType.TO , 
					InternetAddress.parse(toAddress));
			message.setSubject("gpxデータ");
			
		
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			
			messageBodyPart.setText("");
			
			MimeBodyPart attachFile = new MimeBodyPart();
			FileDataSource fds = new FileDataSource(fileName);
			
			attachFile.setDataHandler(new DataHandler(fds));
			
			attachFile.setFileName(MimeUtility.encodeWord(fds.getName()));
			
			Multipart multipart = new MimeMultipart(); 
			
			multipart.addBodyPart(messageBodyPart);
			multipart.addBodyPart(attachFile);
			message.setHeader("Content-Transfer-Encoding", "base64");
			
			
			
			
			System.out.println("送信します");
			message.setContent(multipart);
			Transport.send(message);
			

		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
			System.out.println("送信失敗");
		} catch (MessagingException e) {
			
			e.printStackTrace();
			System.out.println("送信失敗");
		}finally {
			System.out.println("完了");
		}
		
	}
}



