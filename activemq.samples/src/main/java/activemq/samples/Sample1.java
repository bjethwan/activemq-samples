package activemq.samples;

import org.apache.activemq.broker.BrokerService;

/*
 * This message broker is embedded
 * @author bjethwan
 */
public class Sample1 
{
	void start()
	{
		try {
			System.out.println("ActiveMQ starting...");
            
			BrokerService broker = new BrokerService();
            broker.addConnector("tcp://localhost:61616");
            broker.start();
            broker.waitUntilStarted();
            
            System.out.println("ActiveMQ started");
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
	}
	
	public static void main(String[] args) 
	{
		Sample1 broker = new Sample1();
		broker.start();
	}

}

