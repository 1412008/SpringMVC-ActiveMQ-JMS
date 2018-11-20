package web.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

@Component
public class Producer {
	@Autowired
	private JmsTemplate jmsTemplate;

	public void sendMessage(String destinationName, final String message) {		
		try {
			jmsTemplate.send(destinationName, new MessageCreator() {
				public Message createMessage(Session session) throws JMSException {
					System.out.println("sending message");
					return session.createTextMessage(message);
				}
			});
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
