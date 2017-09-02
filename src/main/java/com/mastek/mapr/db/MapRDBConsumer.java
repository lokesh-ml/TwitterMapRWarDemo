package com.mastek.mapr.db;

import com.mapr.db.MapRDB;
import com.mapr.db.Table;
import org.ojai.Document;
import org.ojai.store.DocumentMutation;

import java.io.IOException;

/**
 * This class shows the basic operations of MapR DB
 */
public class MapRDBConsumer {
	
	private static final String TABLE_PATH = "/hashtag_count";
	
	private Table table ;
	
	private static final MapRDBConsumer instance = new MapRDBConsumer();
    
    //private constructor to avoid client applications to use constructor
    private MapRDBConsumer(){}

    public static MapRDBConsumer getInstance(){
        return instance;
    }

	{
		System.out.println("Starting MapR DB Consumer... Checking table...");
		if (!MapRDB.tableExists(TABLE_PATH)) {
			System.out.println("Table doesnt exist... Creating it...");
			table = MapRDB.createTable(TABLE_PATH); // Create the table if not already present
		} else {
			System.out.println("Table already found... Fetching it...");
			table = MapRDB.getTable(TABLE_PATH); // get the table
		}
	}

	public void run(String hashtag, long addCount) throws Exception {
		HashTagCount hashtagCountDbObj = this.queryDocuments(hashtag);
		if (hashtagCountDbObj != null && hashtagCountDbObj.getCount() != 0) {
			hashtagCountDbObj.updateCount(addCount);
			this.updateDocuments(hashtagCountDbObj);
		} else {
			this.createDocuments(hashtag, addCount);
		}
	}

	private void createDocuments(String hashtag, long count)  {
		try {
			// create a new document from a simple bean
			// look at the User class to see how you can use JSON Annotation to
			// drive the format of the document
			HashTagCount hashtagCountDbObj = new HashTagCount();
			hashtagCountDbObj.setId("_" + hashtag);
			hashtagCountDbObj.setHashTag(hashtag);
			hashtagCountDbObj.setCount(count);
			
			System.out.println(hashtagCountDbObj);
			
			Document document = MapRDB.newDocument(hashtagCountDbObj);

			// save document into the table
			table.insert(document);
			table.flush(); // flush to the server
		} catch(Exception e){
			System.err.println("ERROR1>>>>>>>>>" + e);
			e.printStackTrace();
		}

		

	}

	/**
	 * Update record see how to : update existing field can add/remove attribute
	 * to a document append data into lis of values
	 */
	private void updateDocuments(HashTagCount hashtagCountDbObj) {
		try {
			// create a mutation
			System.out.println(hashtagCountDbObj);
			DocumentMutation mutation = MapRDB.newMutation().set("count", hashtagCountDbObj.getCount());
			table.update(hashtagCountDbObj.getId(), mutation);
			table.flush();
		} catch(Exception e){
			System.err.println("ERROR2>>>>>>>>>" + e);
			e.printStackTrace();
		}
		
	}

	/**
	 * Query the record
	 */
	private HashTagCount queryDocuments(String hashtag) {
		try {
			// get single document and map it to the bean
			Document doc = table.findById("_" + hashtag);
			if(doc != null){
				HashTagCount hashtagCountDbObj = doc.toJavaBean(HashTagCount.class);
				System.out.println("hashtag already exists... count="+hashtagCountDbObj.getCount());
				return hashtagCountDbObj;
			} else {
				return null;
			}
			
		} catch(Exception e){
			System.err.println("ERROR3>>>>>>>>>" + e);
			e.printStackTrace();
			return null;
		}
		
	}

}
