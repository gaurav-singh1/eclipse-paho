package de.dcsquare.paho.client.subscriber;


import de.dcsquare.paho.client.util.Utils;
//import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
/**
 * @author Dominik Obermaier
 * @author Christian Götz
 */
public class Subscriber_Testing {

    public static final String BROKER_URL = "tcp://144.76.122.130:1883";

	private static final int CLIENT_TO_SUBSCRIBE = 10000;
	
	//private static final String clientId;

    //We have to generate a unique Client id.
   // String clientId = Utils.getMacAddress() + "-sub";
    private MqttClient mqttClient;
    

    public Subscriber_Testing(String clientId) {
		 MemoryPersistence persistence = new MemoryPersistence();
        try {
            mqttClient = new MqttClient(BROKER_URL, clientId, persistence);
            System.out.println("server uri is: "+mqttClient.getServerURI());
            System.out.println("This is the MQTT ID"+mqttClient.getClientId());


        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void start() {
        try {

            mqttClient.setCallback(new SubscribeCallback());
           MqttConnectOptions options = new MqttConnectOptions();
		options.setCleanSession(true);
 
            mqttClient.connect(options);

            //Subscribe to all subtopics of home
            final String topic = "testing/";
            
            
            	mqttClient.subscribe(topic);
            

           // System.out.println("Subscriber is now listening to "+topic);

        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String... args) {
    	
    	
    	for(int i=1;i<=CLIENT_TO_SUBSCRIBE;i++){
    		String clientId="Client+130+"+i;
        final Subscriber_Testing subscriber = new Subscriber_Testing(clientId);
        System.out.println("the broker url is: "+subscriber.BROKER_URL);
        System.out.println("client id is: "+clientId);
        subscriber.start();
    	}
    }

}
