package com.example.desy.instagram_project;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PhotosActivity extends ActionBarActivity {

    public static final String CLIENT_ID = "8b463d30f5c8465ba1b80ecf4e05edeb";
    private ArrayList<InstagramPhotos> photos;
    private InstagramPhotosAdapter aPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        // SEND OUT API REQUEST to POPULAR PHOTOS
        photos = new ArrayList<>();
        //1. Create the adapter linking it to the source
        aPhotos = new InstagramPhotosAdapter(this, photos);
        //2. Find the listview from the layout
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        //3. set the adapter binding it to the ListView
        lvPhotos.setAdapter(aPhotos);

        //fetch the popular photos
        fetchPopularPhotos();

    }

    //Trigger API
    private void fetchPopularPhotos() {
        /*
        instagram client id:     8b463d30f5c8465ba1b80ecf4e05edeb
        -popular:    https://api.instagram.com/v1/media/popular?access_token=ACCESS-TOKEN
        -> Type: {“data”=> [x] => “type”} (“image”or video”)
        -> URL: {“data”=> [x] => “image” => “standard_resolution” => “url"}
        -> Caption: {“data”=> [x] => “caption” => “text"}
        -> Author Name:{“data”=> [x] => “user” => “username"}
        */

        String url ="https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;

        //Create the network client
        AsyncHttpClient client = new AsyncHttpClient();
        //Trigger the Get request
        client.get(url,null,new JsonHttpResponseHandler() {
           //onSuccess (worked)

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //Expecting a JSON object
                //Log.i("DEBUG", response.toString());
                //Iterate each of the photo items and decode the item into a java object
                JSONArray photosJSON = null;
                try{
                    photosJSON = response.getJSONArray("data"); //array of posts
                    //iterate arry of photos
                    for (int i = 0; i < photosJSON.length(); i++) {
                        //get the json object at that position
                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        //decode the attributes of the json into a data model
                        InstagramPhotos photo = new InstagramPhotos();
                        //-> Author Name:{“data”=> [x] => “user” => “username"}
                        photo.username = photoJSON.getJSONObject("user").getString("username");
                        //-> Caption: {“data”=> [x] => “caption” => “text"}
                        photo.caption = photoJSON.getJSONObject("caption").getString("text");
                        //-> Type: {“data”=> [x] => “type”} (“image”or video”)
                        //-> URL: {“data”=> [x] => “image” => “standard_resolution” => “url"}
                        photo.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("height");
                        photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");
                        photos.add(photo);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }

                //callback
                aPhotos.notifyDataSetChanged();
            }

            //onFailure (failed)

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // DO SOMETHING
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
