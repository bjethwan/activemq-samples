import java.util.concurrent.TimeUnit;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
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
		
		TextMessage message = (TextMessage)consumer.receive();
		
		System.out.println("processing message: "+ message.getText());
		
		TimeUnit.SECONDS.sleep(10); //Simulate Processing Time 
	
		String confirmation = "EQ-12345";
		System.out.println("trade confirmation: "+ confirmation);
		
		connection.close();
	}

}
