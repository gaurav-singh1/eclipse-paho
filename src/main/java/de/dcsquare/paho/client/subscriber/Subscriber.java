package de.dcsquare.paho.client.subscriber;

import de.dcsquare.paho.client.util.Utils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * @author Dominik Obermaier
 * @author Christian Götz
 */
public class Subscriber {

    public static final String BROKER_URL = "tcp://127.0.0.1:1883";

    //We have to generate a unique Client id.
    String clientId = Utils.getMacAddress() + "-sub";
    private MqttClient mqttClient;
    

    public Subscriber() {

        try {
            mqttClient = new MqttClient(BROKER_URL, clientId);
            System.out.println("server uri is: "+mqttClient.getServerURI());


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
            final String topic = "home/#";
            mqttClient.subscribe(topic);

            System.out.println("Subscriber is now listening to "+topic);

        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String... args) {
        final Subscriber subscriber = new Subscriber();
        System.out.println("the broker url is: "+subscriber.BROKER_URL);
        System.out.println("client id is: "+subscriber.clientId);
        subscriber.start();
    }

}
