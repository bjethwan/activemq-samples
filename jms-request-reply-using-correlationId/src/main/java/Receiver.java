import java.util.concurrent.TimeUnit;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;


public class Receiver {
	
	public static void main(String[] args) throws JMSException, InterruptedException 
	{
		//.Connection
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
		Connection connection = connectionFactory.createConnection();
		connection.start();
		
		//.Connection.Session
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		//.Session.Queue
		Queue queue = session.createQueue("EM_TRADE_REQUEST_Q");
		
		//Session.MessageConsumer
		MessageConsumer consumer = session.createConsumer(queue);
		
		//.Consumer.Receive()
		TextMessage requestMessage = (TextMessage)consumer.receive();
		
		System.out.println("processing message: "+ requestMessage.getText());
		
		
		//**
		//** Simulate Processing Time
			TimeUnit.SECONDS.sleep(5); 
			String confirmation = "EQ-12345";
		//**
		 //**
		
	
		
		//Session.MessageProducer
		MessageProducer producer = session.createProducer(requestMessage.getJMSReplyTo());
		
		//.Session.Message
		TextMessage responseMessage = (TextMessage)session.createTextMessage(confirmation);
		responseMessage.setJMSCorrelationID(requestMessage.getJMSMessageID());
		
		//.Producer.Send(Response)
		producer.send(responseMessage);
		
		System.out.println("Sent trade confirmation: "+ confirmation);
		
		connection.close();
	}

}
