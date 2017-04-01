import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;


public class Sender {
	
	public static void main(String[] args) throws JMSException 
	{
		//.Connection
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
		Connection connection = connectionFactory.createConnection();
		connection.start();
		
		//.Connection.Session
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		//.Session.Queue
		Queue queue = session.createQueue("EM_TRADE_REQUEST_Q");
		
		//Session.Message
		Message message = session.createTextMessage("BUY APPLE 1000 SHARES");
		
		//Session.MessageProducer
		MessageProducer producer = session.createProducer(queue);
		

		producer.send(queue, message);
		
		System.out.println("Message sent");
		
		connection.close();
	}

}
