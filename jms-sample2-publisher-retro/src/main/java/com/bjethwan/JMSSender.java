package com.bjethwan;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * Start activemq from terminal
 *  > activemq start
 * 
 * Run below program in eclipse.
 * 
 * Open any browser with url as localhost:8161/admin
 * Browse the queue and check the messages.
 * 
 * @author bjethwan
 *
 */
public class JMSSender {
	
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
		
		//Creates a MessageProducer tied to a destination.
		MessageProducer messageProducer = session.createProducer(topic);
		
		//Creates a message from the session object.
		TextMessage message = session.createTextMessage("Hello Mr. Bipin Jethwani, How are you doing today?");
		
		//Send the message
		messageProducer.send(message);
		
		//Closing the connection would close the session spawned from it.
		connection.close();
		
		System.out.println("Message was sent successfully");
		
	}
}
