package com.mastek.mapr.db;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.ojai.types.ODate;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HashTagCount {

  private String id;
  private String hashtag;
  private long count;

  public HashTagCount() {
  }

  @JsonProperty("_id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @JsonProperty("hashtag")
  public String getHashTag() {
    return hashtag;
  }

  public void setHashTag(String hashtag) {
    this.hashtag = hashtag;
  }

  @JsonProperty("count")
  public long getCount() {
    return count;
  }

  public void setCount(long count) {
    this.count = count;
  }
  
  public void updateCount(long addCount){
	  this.count+=addCount;
  }

  @Override
  public String toString() {
    return "User{" +
            "id='" + id + '\'' +
            ", hashtag='" + hashtag + '\'' +
            ", count=" + count +
            '}';
  }
}
