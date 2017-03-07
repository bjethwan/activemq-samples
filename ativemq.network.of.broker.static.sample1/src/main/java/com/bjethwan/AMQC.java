package com.bjethwan;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.network.DiscoveryNetworkConnector;
import org.apache.activemq.network.NetworkConnector;

public class AMQC {
	
	void start(){
		try {
			
			System.out.println("ActiveMQ client starting...");
			BrokerService broker = new BrokerService();
			broker.setBrokerName("client");
			broker.addConnector("tcp://localhost:61612");
			broker.start();
			broker.waitUntilStarted();
			System.out.println("ActiveMQ client started.");
			
			
			System.out.println("Attaching NetworkConnector...");
            NetworkConnector networkConnector = new DiscoveryNetworkConnector(new URI("static://tcp://localhost:61613"));
			networkConnector.setName("one");
			networkConnector = broker.addNetworkConnector(networkConnector);
			networkConnector.setUserName("admin");
			networkConnector.setPassword("password");
			networkConnector.start();
			System.out.println("NetworkConnector Attached.");
			
			 TimeUnit.MINUTES.sleep(10);
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}
	
	
	public static void main(String[] args) {
		new AMQC().start();
	}
}
