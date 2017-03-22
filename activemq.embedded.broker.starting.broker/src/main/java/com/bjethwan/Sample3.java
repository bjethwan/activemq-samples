package com.bjethwan;


import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Sample3 {
	
	public void start() throws Exception
	{

		ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false&broker.useJmx=true&broker.brokerName=family");	
		
		Connection connection = cf.createConnection();
		connection.start();
		
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue queue = session.createQueue("jethwani.bipin");
		MessageConsumer consumer = session.createConsumer(queue);
		consumer.setMessageListener(new MessageListener() {
			public void onMessage(Message message) {
				try {
					System.out.println("Received a new message.Thanks for sending me some messages!\n"+"Message Text\n"+((TextMessage)message).getText());
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void main(String[] args) throws Exception {
		Sample3 sample3 = new Sample3();
		sample3.start();
	}
}
