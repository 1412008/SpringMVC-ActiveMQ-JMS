package web.jms;

import javax.jms.Message;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Listener {

	@Autowired
	private Producer producer;

	@JmsListener(destination = "abc.topic")
	public void receiveMessage(Message jsonMessage) {
		String messageData = null;
		if (jsonMessage instanceof TextMessage) {
			TextMessage textMessage = (TextMessage) jsonMessage;
			try {
				System.out.println("Receive message: " + textMessage.getText());
				messageData = textMessage.getText();
				producer.sendMessage("def.topic", messageData);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

}
