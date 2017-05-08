package com.bjethwan;

import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.pool.PooledConnectionFactory;

public class Main {

	private final String connectionUri = "vm://bipin-emm?create=false";
	private final String brokerConfig = "xbean:emm_activemq.xml";

	private BrokerService broker;

	private ActiveMQConnectionFactory connectionFactory;
	private PooledConnectionFactory pooledFactory;

	private final int NUM_DEVICES = 40;
	private final int TOMCAT_THREADS = 20;

	private CountDownLatch done;
	private int msgCount;




	public static void main(String[] args) throws Exception {

		Main sample = new Main();

		sample.before();
		sample.runPerformanceTest();
		//sample.after();
	}



	public void before() throws Exception{

		startEmbeddedBrokerWithGivenConfig(brokerConfig);

		connectionFactory = new ActiveMQConnectionFactory(connectionUri);
		pooledFactory = new PooledConnectionFactory(connectionFactory);
	}

	private void startEmbeddedBrokerWithGivenConfig(String config) throws Exception{

		broker = BrokerFactory.createBroker(new URI(config));
		broker.start();
	}



	public void runPerformanceTest() throws Exception{

		//Tomcat threads
		ExecutorService service = Executors.newFixedThreadPool(TOMCAT_THREADS);

		boolean keepMoving = true;
		while(keepMoving){
			this.done = new CountDownLatch(NUM_DEVICES);

			for(int i=1; i<=NUM_DEVICES; i++){
				service.execute(new Runnable(){
					@Override
					public void run() {
						try {
							Connection connection = pooledFactory.createConnection();
							Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
							Queue queue = session.createQueue("hrtBeatQueue");
							MessageProducer producer = session.createProducer(queue);
							producer.send(session.createTextMessage("Biggggggggggggggggggggggggggggggggggg gggggggggggggg ggggggggggggg gggggggggg Test Message"));
							producer.close();
							session.close();
							connection.close();
							msgCount++;
						} catch (JMSException e) {
							e.printStackTrace();
						} finally {
							done.countDown();
						}
					}
				});
			}

			done.await();
			System.out.println("Number of messages posted by devices: " + msgCount);
			TimeUnit.SECONDS.sleep(2);
		}

		service.shutdown();

	}

	public void after() throws Exception {
		pooledFactory.clear();
		broker.stop();
	}






}
