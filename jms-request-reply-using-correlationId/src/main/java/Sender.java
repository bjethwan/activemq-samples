import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

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
		Queue requestRueue = session.createQueue("EM_TRADE_REQUEST_Q");
		
		//.Session.Queue
		Queue responseQueue = session.createQueue("EM_TRADE_RESPONSE_Q");
		
		//Session.Message
		TextMessage message = session.createTextMessage("BUY APPLE 1000 SHARES");
		message.setJMSReplyTo(responseQueue);
		
		//Session.MessageProducer
		MessageProducer producer = session.createProducer(requestRueue);
		
		
		producer.send(requestRueue, message);
		
		System.out.println("Message sent");
		
		String filter = "JMSCorrelationID = '" + message.getJMSMessageID() +"'";
		
		//.Session.MessageConsumer (with Filter)
		MessageConsumer consumer = session.createConsumer(responseQueue, filter);
		
		
		TextMessage responseMessage = (TextMessage)consumer.receive();
		
		System.out.println("received confirmation = "+ responseMessage.getText());
		
		connection.close();
	}

}
