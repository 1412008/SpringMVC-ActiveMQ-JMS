package web.jms;

import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class Producer {
	@Autowired
	private JmsTemplate jmsTemplate;

	public void sendMessage(String topicName, final String message) {
		ObjectMapper maper = new ObjectMapper();
		TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
		};
		Map<String, Object> map;
		try {
			map = maper.readValue(message, typeRef);
			final String textMessage = "Hi " + map.get("name");
			System.out.println("Send message: '" + textMessage + "' to topic: " + topicName);
			jmsTemplate.send(topicName, new MessageCreator() {
				public Message createMessage(Session session) throws JMSException {
					System.out.println("sending");
					return session.createTextMessage(textMessage);
				}
			});
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
