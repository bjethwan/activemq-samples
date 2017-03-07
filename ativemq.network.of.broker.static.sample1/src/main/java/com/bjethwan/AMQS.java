package com.bjethwan;

import java.util.ArrayList;
import java.util.List;

import org.apache.activemq.broker.BrokerPlugin;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.security.AuthenticationUser;
import org.apache.activemq.security.SimpleAuthenticationPlugin;


/*
 * ProducerTool --verbose --queue sample1 --url=tcp://localhost:61613 --user=admin --password=password
 */
public class AMQS {
	
	void start(){
		try {
			System.out.println("Secure ActiveMQ starting...");

			SimpleAuthenticationPlugin authentication = new SimpleAuthenticationPlugin();	
			List<AuthenticationUser> users =new ArrayList<AuthenticationUser>();
			users.add(new AuthenticationUser(	"admin", 	 "password", "admins,publishers,consumers"));
			users.add(new AuthenticationUser(	"publisher", "password", "publishers,consumers"));
			users.add(new AuthenticationUser(	"consumer",  "password", "consumers"));
			users.add(new AuthenticationUser(	"guest", 	 "password", "guests"));
			authentication.setUsers(users);

			BrokerService broker = new BrokerService();
			broker.setBrokerName("server");
			broker.setPlugins(new BrokerPlugin[]{authentication});
			broker.addConnector("tcp://localhost:61613");
			broker.start();
			broker.waitUntilStarted();

			System.out.println("Secure ActiveMQ started");
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}
	

	public static void main(String[] args) {
		new AMQS().start();
	}
}
