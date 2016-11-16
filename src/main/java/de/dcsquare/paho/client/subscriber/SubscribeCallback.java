package de.dcsquare.paho.client.subscriber;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.eclipse.paho.client.mqttv3.*;

/**
 * @author Dominik Obermaier
 * @author Christian Götz
 */
public class SubscribeCallback implements MqttCallback {

	public void connectionLost(Throwable cause) {
		// This is called when the connection is lost. We could reconnect here.
	}

	public void messageArrived(String topic, MqttMessage message) throws Exception {
		System.out.println("Message arrived. Topic: " + topic + "  Message: " + message.toString());

		File file = new File("/home/rq/rocq/logs/BrokerMessages.txt");
		if (!file.exists()) {
			file.createNewFile();
		}
//		System.out.println("control in callback subscriber");
//		
//		System.out.println("file absolute path :" + file.getAbsoluteFile());

		FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
		BufferedWriter bw = new BufferedWriter(fw);
		String h = message.toString();
		bw.write(h);
		bw.write("\n");
		bw.close();

//		System.out.println("Done");

		if ("testing/LWT".equals(topic)) {
			System.err.println("Sensor gone!");
		}
	}

	public void deliveryComplete(IMqttDeliveryToken token) {
		// no-op
	}
}
