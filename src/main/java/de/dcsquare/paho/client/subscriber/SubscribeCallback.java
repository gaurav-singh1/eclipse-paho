package de.dcsquare.paho.client.subscriber;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

import org.eclipse.paho.client.mqttv3.*;
import org.json.simple.JSONObject;

import com.htmsl.utilities.bloomfilter.BloomFilter;
/**
 * @author Dominik Obermaier
 * @author Christian Götz
 */
public class SubscribeCallback implements MqttCallback {

	
	public static String BLOOM_FILTER="BloomFilter";
	
	public static String DYNSEG_TOPIC_TOSUBSCRIBE="SOME FILTERSS";
	
	
	public void connectionLost(Throwable cause) {
		// This is called when the connection is lost. We could reconnect here.
	}

	public void messageArrived(String topic, MqttMessage message) throws Exception {
//		System.out.println("Message arrived. Topic: " + topic + "  Message: " + message.getPayload().toString());
//		
//		System.out.println("Message arrived. Topic: " + topic + "  Message: " + message.toString());
		System.out.println("&&&&&&&&&&&&&&&");
		System.out.println(message.getClass());
		/*
		 obj.put("start",startDate);
                                obj.put("end",endDate);
                                obj.put("topicName",topicName_New);
                                obj.put("bf",bf);
		 */
		System.out.println("topic on which data has been received is :"+topic);
		System.out.println("the data packet from the broker has been received in JSON");
		
		
		ByteArrayInputStream baos = new ByteArrayInputStream(message.getPayload());
		System.out.println("after message payload");
		ObjectInputStream outputStream = new ObjectInputStream(baos);
		
		JSONObject datapacket=null;
		try{
			 datapacket=(JSONObject)outputStream.readObject();
		}catch(Exception ex){
			System.out.println("koi to gadbad hai");
			ex.printStackTrace();
			System.out.println();
		}
		
		System.out.println("parsing the data from the json packets.....started");
		BloomFilter bfilter=new BloomFilter(0.01,1000);
		
		try{
			
			byte[] buffer=(byte[]) datapacket.get("bf");
			ByteBuffer bb=ByteBuffer.wrap(buffer);
			
			bfilter.deSerialize(bb);
			
		}catch(Exception ex){
			System.out.println("error in getting the bloom filter");
			ex.printStackTrace();
		}
		System.out.println("bloom filter obtained");
		String TOPIC_TOSUBSCRIBE=(String) datapacket.get("topicName");
		
		String SD=(String) datapacket.get("start");
		String ED=(String) datapacket.get("end");
		
		System.out.println("printing the bloom filter object parametersss....");
		System.out.println(bfilter.getBitVectorSize());
		System.out.println(bfilter.getNumberOfEntries());
		
		
		System.out.println("deserialization object checking=== "+bfilter.contains("random string10000"));
		
		
		
		
		
		File file = new File("/Users/gaurav/Desktop/BrokerMessages.txt");
		if (!file.exists()) {
			file.createNewFile();
		}
	System.out.println("control in callback subscriber");
//		
//		System.out.println("file absolute path :" + file.getAbsoluteFile());

		FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
		BufferedWriter bw = new BufferedWriter(fw);
		String h = message.toString();
		bw.write(h);
		bw.write("\n");
		bw.close();

		System.out.println("Done");

		if ("testing/LWT".equals(topic)) {
			System.err.println("Sensor gone!");
		}
	}

	public void deliveryComplete(IMqttDeliveryToken token) {
		// no-op
	}
}
