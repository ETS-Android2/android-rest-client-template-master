package com.codepath.apps.restclienttemplate.models;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Tweet {

   // private static Tweet tweet = new Tweet();
    public String body;
    public String createdAt;
    public User user;
    public User userHandle;
    public String timestamp;
   // Tweet tweet = new Tweet();

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("text");

        tweet.createdAt = jsonObject.getString("created_at");
        tweet.createdAt = getFormattedTimestamp(jsonObject.getString("created_at"));
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        //tweet.userHandle = User.fromJson(jsonObject.getJSONObject("user_username"));
        tweet.userHandle = User.fromJson(jsonObject.getJSONObject("user"));//jsonObject.getString("user_username");

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


