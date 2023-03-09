package py.com.fermar.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;

public class EmailUtils {
	
	private JavaMailSender mailSender;
	
	public static final String PROCESS_NAME = "EmailUtils";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailUtils.class.getName());
	
	public EmailUtils(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	public EmailUtils() {
		super();
	}

	@Async
	public void sendMailWithAttachment(String destinatario, String asunto, String texto, 
			Attachment ... attachments) 
		{
		// plantilla para el envío de email
		final MimeMessage mimeMessage = mailSender.createMimeMessage();
		try {
	
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			validateMail(destinatario);
			helper.setTo(destinatario);
			helper.setSubject(asunto);
			helper.setText(texto);
									
			// adjuntando los ficheros
			if (attachments != null) {
				for (int i = 0; i < attachments.length; i++) {
					if(attachments[i] != null) {
						helper.addAttachment(attachments[i].getName(), 
								new ByteArrayResource(attachments[i].getContent()));
					}
					else {
				    	LOGGER.error("{} - attachments {} vacio", PROCESS_NAME, i);
					}
				}
			}		
	        mailSender.send(mimeMessage);
	        
	    } catch (Exception e) {
	    	LOGGER.error(PROCESS_NAME + " - sendMail: " + e.getMessage(), e);
	    }
	}
	
	private static void validateMail(String email) throws IllegalArgumentException {

        // Patrón para validar el email
        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

        Matcher mather = pattern.matcher(email);

        if (!mather.find()) {
        	throw new IllegalArgumentException("El email Ingresado es Inválido.");
        }
    }
	
	public class Attachment{
		byte[] content; 
		String name; 
		String type;
		
		/**
		 * 
		 * @param content, contenido byte del archivo
		 * @param name, nombre del archivo
		 * @param type, tipo del archivo
		 */
		public Attachment(byte[] content, String name, String type) {
			this.content = content;
			this.name = name;
			this.type = type;
		}

		public byte[] getContent() {
			return content;
		}

		public void setContent(byte[] content) {
			this.content = content;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}
	}

	public void sendMailWithAttachment(String destinatario, String destinatarioCC, String asunto, String texto,
			Attachment[] attachments) {
		// plantilla para el envío de email
		final MimeMessage mimeMessage = mailSender.createMimeMessage();
		try {

			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			validateMail(destinatario);
			helper.setTo(destinatario);
			helper.setCc(destinatarioCC);
			helper.setSubject(asunto);
			helper.setText(texto);
						
			// adjuntando los ficheros
			if (attachments != null) {
				for (int i = 0; i < attachments.length; i++) {
					if (attachments[i] != null) {
						helper.addAttachment(attachments[i].getName(),
								new ByteArrayResource(attachments[i].getContent()));
					} else {
						LOGGER.error("{} - attachments {} vacio", PROCESS_NAME, i);
					}
				}
			}
			mailSender.send(mimeMessage);

		} catch (Exception e) {
			LOGGER.error(PROCESS_NAME + " - sendMail: " + e.getMessage(), e);
		}
	}
}
