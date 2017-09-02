package solution

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.{ SparkConf, SparkContext }
import org.apache.spark.SparkContext._
import org.apache.spark.streaming._
import org.apache.spark.streaming.dstream.{ DStream, InputDStream }
import org.apache.spark.streaming.kafka.v09.KafkaUtils
import org.apache.spark.streaming.{ Seconds, StreamingContext }
import org.apache.spark.sql.functions.avg
import org.apache.spark.sql.SQLContext
import twitter4j.Status
import com.mastek.mapr.db.MapRDBConsumer

object SparkStreaming extends Serializable {

  val timeout = 10 // Terminate after N seconds
  val batchSeconds = 2 // Size of batch intervals

  def main(args: Array[String]): Unit = {

    val brokers = "maprdemo:9092" // not needed for MapR Streams, needed for Kafka
    val groupId = "testgroup"
    val offsetReset = "earliest"
    val batchInterval = "2"
    val pollTimeout = "5000"
    val topics = "/twitter:tweets"

    val sparkConf = new SparkConf().setAppName("TwitterMapRSparkStream")

    val ssc = new StreamingContext(sparkConf, Seconds(batchInterval.toInt))

    // Create direct kafka stream with brokers and topics
    val topicsSet = topics.split(",").toSet
    val kafkaParams = Map[String, String](
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> brokers,
      ConsumerConfig.GROUP_ID_CONFIG -> groupId,
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG ->
        "org.apache.kafka.common.serialization.StringDeserializer",
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG ->
        "org.apache.kafka.common.serialization.StringDeserializer",
      ConsumerConfig.AUTO_OFFSET_RESET_CONFIG -> offsetReset,
      ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG -> "false",
      "spark.kafka.poll.time" -> pollTimeout
    )

    val tags = KafkaUtils.createDirectStream[String, String](ssc, kafkaParams, topicsSet)
    	.map(_._2).flatMap {t => t.split(" ").filter(_.startsWith("#")).map(_.toLowerCase)}
	
	tags.countByValue()
	    .foreachRDD { rdd =>            
	        println("count received " + rdd.count)
			//rdd.foreach(println)
	      
	        var newRdd = rdd.sortBy(_._2)	          
			newRdd.foreach(x => {
				val maprdbObj = MapRDBConsumer.getInstance();
				maprdbObj.run(x._1, x._2)
			})
			//newRdd.saveAsTextFile("/user/mapr/live_tweets")
		}

    println("start spark streaming")
    ssc.start()
    ssc.awaitTermination()
  }
}