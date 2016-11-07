package de.dcsquare.paho.client.publisher;

import de.dcsquare.paho.client.util.Utils;
import org.eclipse.paho.client.mqttv3.*;

/**
 * @author Dominik Obermaier
 * @author Christian Götz
 */
public class Publisher {

    public static final String BROKER_URL = "tcp://127.0.0.1:1883";

    public static final String TOPIC_BRIGHTNESS = "home/brightness";
    public static final String TOPIC_TEMPERATURE = "home/temperature";
    
    public static final String TOPIC_TEST = "testing/";
    public static final int MESSAGE_COUNT =10;

    private MqttClient client;


    public Publisher() {

        //We have to generate a unique Client id.
        String clientId = Utils.getMacAddress() + "-pub";
        System.out.println("publisher id is: "+ clientId);


        try {

            client = new MqttClient(BROKER_URL, clientId);

        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    void start() {

        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(false);
            options.setWill(client.getTopic("testing/LWT"), "I'm gone :(".getBytes(), 0, false);

            client.connect(options);

            //Publish data forever
            
            
            for(int i=1;i<=MESSAGE_COUNT;i++){
            	final MqttTopic testingtopic = client.getTopic(TOPIC_TEST);
            	String message="message no "+i;
            	
            	testingtopic.publish(new MqttMessage(message.getBytes()));
            	 Thread.sleep(20);
            	 System.out.println("Published data. Topic: " + testingtopic.getName() + "  Message: " + message);
            	 
            	 
            	
            }
        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

  

    public static void main(String... args) {
        final Publisher publisher = new Publisher();
//        System.out.println("publisher client id is: "+publisher.clientId);
        publisher.start();
    }
}
