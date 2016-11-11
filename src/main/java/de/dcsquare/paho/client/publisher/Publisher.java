package de.dcsquare.paho.client.publisher;



import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.NoSuchAlgorithmException;











//import de.dcsquare.paho.client.subscriber.Bytebuffer;
import de.dcsquare.paho.client.util.Utils;

import org.eclipse.paho.client.mqttv3.*;
import org.json.simple.JSONObject;

import com.htmsl.utilities.bloomfilter.BloomFilter;

import java.io.Serializable;
import java.nio.ByteBuffer;




/**
 * @author Dominik Obermaier
 * @author Christian Götz
 */
public class Publisher implements Serializable{
	// 144.76.122.130 				127.0.0.1
	public static final String BROKER_URL = "tcp://127.0.0.1:1883";

	public static final String TOPIC_BRIGHTNESS = "home/brightness";
	public static final String TOPIC_TEMPERATURE = "home/temperature";

	public static final String TOPIC_TEST = "12528479b0/dynSeg8";
	public static final int MESSAGE_COUNT = 1;
	
	
	
	public static String BLOOM_FILTER="BloomFilter";
	public static String DYNSEG_TOPIC_TOSUBSCRIBE="DYNSEG_TOPIC_TOSUBSCRIBE";

	private MqttClient client;

	public Publisher() {

		// We have to generate a unique Client id.
		String clientId = Utils.getMacAddress() + "-pub";
		System.out.println("publisher id is: " + clientId);

		try {

			client = new MqttClient(BROKER_URL, clientId);

		} catch (MqttException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	void start() throws NoSuchAlgorithmException, IOException {
		System.out.println("control in start 0");
		try {

			
			MqttConnectOptions options = new MqttConnectOptions();
			System.out.println("control in start 1");
			// options.setCleanSession(false);
			options.setCleanSession(false);
			options.setWill(client.getTopic("testing/LWT"),
					"I'm gone :(".getBytes(), 0, false);

			client.connect(options);

			// Publish data forever

			// bloom filter addition
			BloomFilter bfilter=new BloomFilter(0.01,1000);
			int elements_to_add = 1000;
			

			//System.out.println(bfilter.);
			System.out.println(bfilter.getFalsePositiveProbability(1000));
			System.out.println(bfilter.getBitVectorSize());
			System.out.println(bfilter.getNumberOfEntries());

			System.out
					.println("**************NOW ELEMENT ADDITION STARTS**************");
			String str;
			for (int i = 0; i < elements_to_add; i++) {
				str = "random string" + i;
				bfilter.add(str);
			}
			
			
			JSONObject message=new JSONObject();
			
			
			//ON THIS TOPIC THE DEVICE HAS TO SUBSCRIBE IF IT IS THE BLOOM FILTER
			message.put("topicName", "SOME FILTERS");		
			message.put("start", "SOME START DATE");
			message.put("end", "SOME END DATE");
			
			
			byte[] buffer=bfilter.serialize();
			
			
			message.put("bf", buffer);
			
			
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream outputStream = new ObjectOutputStream(baos);
			outputStream.writeObject(message);
			outputStream.close();

			// for(int i=1;i<=MESSAGE_COUNT;i++){
			final MqttTopic testingtopic = client.getTopic(TOPIC_TEST);
			// String message="message no "+i;
			//String message = "message no " + 1;
			
			testingtopic.publish(new MqttMessage(baos.toByteArray()));
			
		    Thread.sleep(20);
			System.out.println("Published data. Topic: "
					+ testingtopic.getName() + "  Message: " + message);

			// }
		} catch (MqttException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String... args) throws NoSuchAlgorithmException,
			IOException {
		System.out.println("control in start 0");
		final Publisher publisher = new Publisher();
		// System.out.println("publisher client id is: "+publisher.clientId);
		System.out.println("control in main just before start");
		publisher.start();
	}
}
