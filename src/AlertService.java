import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class AlertService {

	public void sendEmail(String message){

		String to="";
		String from="";

		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.mail.yahoo.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props,new javax.mail.Authenticator()
		{
			@Override
			protected PasswordAuthentication getPasswordAuthentication()
			{
				return new PasswordAuthentication("",""); // fix plain text password
			}
		});

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(from, "Wolf"));
			msg.addRecipient(Message.RecipientType.TO,
					new InternetAddress(to, "Wolf Investments"));
			msg.setSubject("Arbitrage Found " + new Date());
			msg.setText(message);
			Transport.send(msg);
			System.out.println("Email sent successfully...");

		} catch (AddressException e) {
			throw new RuntimeException(e);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

} 
