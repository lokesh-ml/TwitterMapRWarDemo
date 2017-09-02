package com.mastek.mapr.stream;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class MapRStreamProducer {

	private MapRStreamProducer(){Properties props = new Properties();
	    props.put("key.serializer",
	            "org.apache.kafka.common.serialization.StringSerializer");
	    props.put("value.serializer",
	            "org.apache.kafka.common.serialization.StringSerializer");
	    
	    producer = new KafkaProducer<String, String>(props);
    };
    
	private static MapRStreamProducer producerInstance;
	
	public static MapRStreamProducer getProducerInstance(){
		if(producerInstance == null){
			producerInstance = new MapRStreamProducer();
		}
		return producerInstance;
	}
	
    KafkaProducer producer;
    public String topic = "/twitter:tweets";
	
	public void produce(String key, String line){
		ProducerRecord<String, String> rec = new ProducerRecord<String, String>(topic,key, line);
        producer.send(rec);
	}
	
	public void stopProducer(){
		producer.close();
	}
}
