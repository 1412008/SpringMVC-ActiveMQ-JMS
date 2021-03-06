package web.configs;

import java.util.Arrays;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

@Configuration
@EnableJms
@ComponentScan("web")
public class JmsConfig {

	private static final String URL = "tcp://localhost:61616";
	private static final String USERNAME = "admin";
	private static final String PASSWORD = "admin";

	@Bean
	public ActiveMQConnectionFactory connectionFactory() {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
		connectionFactory.setBrokerURL(URL);
		connectionFactory.setUserName(USERNAME);
		connectionFactory.setPassword(PASSWORD);
		connectionFactory.setTrustedPackages(Arrays.asList("web"));
		return connectionFactory;
	}

	@Bean
	public JmsTemplate jmsTopicTemplate() {
		JmsTemplate template = new JmsTemplate();
		template.setConnectionFactory(connectionFactory());
		template.setDeliveryPersistent(true);
		template.setPubSubDomain(false);
		template.setExplicitQosEnabled(true);
		template.setTimeToLive(5*60*60*1000);
		return template;
	}

	@Bean
	public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory());
		factory.setConcurrency("1-1");
		
		factory.setPubSubDomain(false);

		return factory;
	}
}
