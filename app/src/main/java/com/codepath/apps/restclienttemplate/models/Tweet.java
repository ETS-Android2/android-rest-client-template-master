package com.codepath.apps.restclienttemplate.models;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
@Entity(foreignKeys = @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "userId"))
public class Tweet {

   // private static Tweet tweet = new Tweet();
   @ColumnInfo
   @PrimaryKey
   public long id;//public User userHandle; //or user

   @ColumnInfo
    public String body;
    @ColumnInfo
    public String createdAt;

    @ColumnInfo
    public long userId;

@Ignore
    public User user;

    public String timestamp;
   // Tweet tweet = new Tweet();

    public Tweet(){} //need for Parceler

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("text");

        tweet.createdAt = jsonObject.getString("created_at");
        tweet.createdAt = getFormattedTimestamp(jsonObject.getString("created_at"));
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.id = jsonObject.getLong("id");//tweet.userHandle = User.fromJson(jsonObject.getJSONObject("user"));//jsonObject.getString("user_username");

        User user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.user = user;
        tweet.userId = user.id;

        //tweet.userHandle = User.fromJson(jsonObject.getJSONObject("user_username"));
        //tweet.timestamp = jsonObject.getString("timestamp");
       //tweet.timestamp = timestamp.;
        return tweet;
    }

    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {

        List<Tweet> tweets = new ArrayList<>();

        for(int i = 0; i<jsonArray.length(); i++){
            tweets.add( fromJson (jsonArray.getJSONObject(i)));
            }
            return tweets;
        }
        public static String getFormattedTimestamp(String tweet1){ //OR public Static TimeFormatter
            return TimeFormatter.getTimeDifference(tweet1);
       }
}


