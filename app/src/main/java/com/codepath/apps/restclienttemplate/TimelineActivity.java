package com.codepath.apps.restclienttemplate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.TweetDao;
import com.codepath.apps.restclienttemplate.models.TweetWithUser;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;
import okhttp3.Request;

public class TimelineActivity extends AppCompatActivity {
    public static final String  TAG = "TimelineActivity";
    TwitterClient client;

private final int REQUEST_CODE = 20;

    RecyclerView rvTweets;
    List<Tweet>tweets;
    TweetsAdapter adapter;
    SwipeRefreshLayout swipeContainer;
    TweetDao tweetDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

         client = TwitterApp.getRestClient(this);
         tweetDao =  ((TwitterApp) getApplicationContext()).getMyDatabase().tweetDao();

         swipeContainer = findViewById(R.id.swipeContainer);

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG,"fetching new data!");
                populateHomeTimeLine();
            }
        });

         rvTweets = findViewById(R.id.rvTweets);
         tweets = new ArrayList<>();
         adapter= new TweetsAdapter(this,tweets);
         rvTweets.setLayoutManager(new LinearLayoutManager(this));
         rvTweets.setAdapter(adapter);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG,"Showing database data");
                List<TweetWithUser> tweetWithUsers = tweetDao.recentItems();
                List <Tweet> tweetsfromDB = TweetWithUser.getTweetList(tweetWithUsers);
                adapter.clear();
                adapter.addAll(tweetsfromDB);
            }
        });
         populateHomeTimeLine();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.compose){

            //IF Compose item is hit
            //Toast.makeText(this,"compose", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,ComposeActivity.class);

           // intent.putExtra("mode", 2);

            //registerForActivityResult(intent, REQUEST_CODE);
            startActivityForResult(intent, REQUEST_CODE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==REQUEST_CODE && resultCode== RESULT_OK){
            Tweet tweet= Parcels.unwrap(data.getParcelableExtra("tweet"));
            tweets.add(0,tweet);
            adapter.notifyItemChanged(0);
            rvTweets.smoothScrollToPosition(0);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void populateHomeTimeLine() {

        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess" + json.toString());
                JSONArray jsonArray = json.jsonArray;
                try {
                    List <Tweet> tweetsFromNetwork = Tweet.fromJsonArray(jsonArray);
                    List<User>  usersFromNetwork = User.fromJsonTweetArray(tweetsFromNetwork);
                    adapter.clear();
                    adapter.addAll(tweetsFromNetwork);
                    //adapter.notifyDataSetChanged();
                    swipeContainer.setRefreshing(false);

            /*        AsyncTask.execute(new Runnable() {

                        @Override
                        public void run() {


                            Log.i(TAG,"Saving database data");

                            tweetDao.insertModel(usersFromNetwork.toArray(new User[0]));
                            tweetDao.insertModel(tweetsFromNetwork.toArray(new Tweet[0]));

                        }
                    });*/

                } catch (JSONException e) {
                    Log.e(TAG, "Json exception", e);
                    //e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure"+ response,throwable);
            }
        });

    }
}