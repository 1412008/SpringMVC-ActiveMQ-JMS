package web.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import web.models.Switch;

@Component
public class Producer {
	@Autowired
	private JmsTemplate jmsTemplate;

	public void sendMessage(final String destinationName, final String message) {
		try {
			jmsTemplate.send(destinationName, new MessageCreator() {
				public Message createMessage(Session session) throws JMSException {
					System.out.println("Sending message to " + destinationName);
					return session.createTextMessage(message);
				}
			});
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void sendSwitchMessage(final String destinationName, final Switch message, final String correlation) {
		try {
			jmsTemplate.send(destinationName, new MessageCreator() {
				public Message createMessage(Session session) throws JMSException {
					System.out.println("Sending message to " + destinationName + " - " + correlation);
					Message rs = session.createObjectMessage(message);
					rs.setJMSCorrelationID(correlation);
					return rs;
				}
			});
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
