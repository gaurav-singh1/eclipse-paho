package de.dcsquare.paho.client.subscriber;


import de.dcsquare.paho.client.util.Utils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * @author Dominik Obermaier
 * @author Christian Götz
 */
public class Subscriber_Testing {

	//144.76.122.130			127.0.0.1
    public static final String BROKER_URL = "tcp://127.0.0.1:1883";

	private static final int CLIENT_TO_SUBSCRIBE = 1;
	
	
	
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
//		appSecret+"/"+"dynSeg"+i
            //   12528479b0/dynSeg5
            //Subscribe to all subtopics of home
           // final String topic = "testing/";
            
            final String topic = "12528479b0/dynSeg8";
            mqttClient.subscribe(topic);

            System.out.println("Subscriber is now listening to "+topic);

        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String args[]) {
    	
    	System.out.println("hello");
    	for(int i=1;i<=CLIENT_TO_SUBSCRIBE;i++){
    		String clientId=""+i;
        final Subscriber_Testing subscriber = new Subscriber_Testing(clientId);
        System.out.println("the broker url is......: "+Subscriber_Testing.BROKER_URL);
        System.out.println("client id is: "+clientId);
        subscriber.start();
    	}
    }

}
