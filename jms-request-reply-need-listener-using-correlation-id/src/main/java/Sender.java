import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

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

public class Sender {
	
	public static void main(String[] args) throws JMSException 
	{
		
		String msgString = "BUY APPLE 1000 SHARES";
		
		//.Connection
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
		Connection connection = connectionFactory.createConnection();
		connection.start();
		
		//.Connection.Session
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		//.Session.Queue
		Queue queue = session.createQueue("EM_TRADE_REQUEST_Q");
		
		//.Session.Queue
		Queue responseQueue = session.createQueue("EM_TRADE_RESPONSE_Q");
		
		//Session.Message
		Message message = session.createTextMessage(msgString);
		message.setJMSReplyTo(responseQueue);
		
		//Session.MessageProducer
		MessageProducer producer = session.createProducer(queue);
		
		//MessageProducer.send(message)
		producer.send(queue, message);
		
		System.out.println("Message sent: " + msgString);
		
		String filter = "JMSCorrelationID = '" + message.getJMSMessageID() +"'";
		System.out.println("filter: "+filter);
		
		MessageConsumer consumer = session.createConsumer(responseQueue,filter);
		TextMessage responseMessage = (TextMessage)consumer.receive();
		
		if(responseMessage!=null)
			System.out.println("Received Confirmation: "+responseMessage.getText());
		else
			System.out.println("Timeout waiting for response");
		
		connection.close();
	}

}
