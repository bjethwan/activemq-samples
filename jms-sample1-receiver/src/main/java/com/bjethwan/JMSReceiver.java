package com.bjethwan;

import javax.jms.Connection;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class JMSReceiver {

	public static void main(String[] args) throws Exception {
		
		String msg1 = receiveMsg("BIPIN");
		String msg2 = receiveMsg("EMM");
		
		System.out.println("msg1: " + msg1);
		System.out.println("msg2: " + msg2);
		
	}

	public static String receiveMsg(String queueName) throws Exception{
		
		//Connection has to be started manually in JMS 1.1.
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
		Connection connection = connectionFactory.createConnection();
		connection.start();

		//The ack param is ignore when you select "True" for first transaction param.
		Session session = connection.createSession(false,  Session.AUTO_ACKNOWLEDGE);

		//Creates a destination.
		//Mostly with all the MOM provider the queue is created if not already present in the system
		Queue queue = session.createQueue(queueName);

		//Creates a MessageConsumer tied to a destination.
		MessageConsumer messageConsumer = session.createConsumer(queue);


		//WAIT TILL INFINITY FOR A MESSAGE TO APPEAR !!!!!!
		TextMessage textMsg = (TextMessage)messageConsumer.receive();

		//Closing the connection would close the session spawned from it.
		connection.close();

		return textMsg.getText();
	}
}
