import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
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
public class Receiver implements MessageListener{

	private static int orderCount = 0;

	private static Session session;

	public static void main(String[] args) throws JMSException, InterruptedException, IOException 
	{
		//.Connection
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
		Connection connection = connectionFactory.createConnection();
		connection.start();

		//.Connection.Session
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		//.Session.Queue
		Queue queue = session.createQueue("EM_TRADE_REQUEST_Q");

		//Session.MessageConsumer
		MessageConsumer consumer = session.createConsumer(queue);
		consumer.setMessageListener(new Receiver());

		System.in.read();

		session.close();
		connection.close();
	}

	@Override
	public void onMessage(Message msg) {

		TextMessage message = (TextMessage)msg;

		try {
			System.out.println("processing message: "+ message.getText());
			System.out.println(message.getJMSMessageID());
			
			TimeUnit.SECONDS.sleep(15); //Simulate Processing Time 
			String confirmation = message.getText() + " --- EQ-"+orderCount++;
			System.out.println("trade confirmation: "+ confirmation);

			MessageProducer producer = session.createProducer(message.getJMSReplyTo());
			TextMessage responseMessage = session.createTextMessage(confirmation);
			responseMessage.setJMSCorrelationID(message.getJMSMessageID());
			producer.send(responseMessage);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 


	}
}

