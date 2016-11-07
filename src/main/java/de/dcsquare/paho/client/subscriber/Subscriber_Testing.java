package de.dcsquare.paho.client.subscriber;


import de.dcsquare.paho.client.util.Utils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * @author Dominik Obermaier
 * @author Christian Götz
 */
public class Subscriber_Testing {

    public static final String BROKER_URL = "tcp://144.76.122.130:1883";

	private static final int CLIENT_TO_SUBSCRIBE = 1000;
	
	//private static final String clientId;

    //We have to generate a unique Client id.
   // String clientId = Utils.getMacAddress() + "-sub";
    private MqttClient mqttClient;
    

    public Subscriber_Testing(String clientId) {

        try {
            mqttClient = new MqttClient(BROKER_URL, clientId);
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
            
            mqttClient.connect();

            //Subscribe to all subtopics of home
            final String topic = "testing/";
            if(!mqttClient.isConnected())
            {
            	mqttClient.subscribe(topic,0);
            }

            System.out.println("Subscriber is now listening to "+topic);

        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String... args) {
    	
    	
    	for(int i=1;i<=CLIENT_TO_SUBSCRIBE;i++){
    		String clientId=""+i;
        final Subscriber_Testing subscriber = new Subscriber_Testing(clientId);
        System.out.println("the broker url is: "+subscriber.BROKER_URL);
        System.out.println("client id is: "+clientId);
        subscriber.start();
    	}
    }

}