package activemq.samples;

import java.util.ArrayList;
import java.util.List;

import org.apache.activemq.broker.BrokerPlugin;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.security.AuthenticationUser;
import org.apache.activemq.security.SimpleAuthenticationPlugin;

public class Sample2 {

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
			broker.setBrokerName("OEM");
			broker.setPlugins(new BrokerPlugin[]{authentication});
			broker.addConnector("tcp://localhost:61616");
			broker.start();
			broker.waitUntilStarted();

			System.out.println("Secure ActiveMQ started");
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

	public static void main(String[] args) {
		Sample2 sample2 = new Sample2();
		sample2.start();
	}
}

