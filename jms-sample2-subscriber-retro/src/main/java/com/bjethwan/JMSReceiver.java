package com.bjethwan;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.region.policy.PolicyEntry;
import org.apache.activemq.broker.region.policy.PolicyMap;
import org.apache.activemq.broker.region.policy.StrictOrderDispatchPolicy;

public class JMSReceiver {

	public static void main(String[] args) throws JMSException {
		
		//Connection has to be started manually in JMS 1.1.
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
		Connection connection = connectionFactory.createConnection();
		connection.start();
		
	
		
		//The ack param is ignore when you select "True" for first transaction param.
		Session session = connection.createSession(false,  Session.AUTO_ACKNOWLEDGE);
		
		//Creates a destination.
		//Mostly with all the MOM provider the queue is created if not already present in the system
		//Queue queue = session.createQueue("BIPIN");
		Topic topic = session.createTopic("BIPIN?consumer.retroactive=true");
		
		//Creates a MessageConsumer tied to a destination.
		MessageConsumer messageConsumer = session.createConsumer(topic);
		
		
		//WAIT TILL INFINITY FOR A MESSAGE TO APPEAR !!!!!!
		TextMessage textMsg = (TextMessage)messageConsumer.receive();
		
		//Closing the connection would close the session spawned from it.
		connection.close();
		
		System.out.println("Message: " + textMsg.getText());
		
	}
}
