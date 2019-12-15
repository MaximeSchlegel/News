package com.centrale.news;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.centrale.news.DataModels.Source;
import com.centrale.news.DataModels.SourcesList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StartActivity extends AppCompatActivity {

    private SourcesList sourcesList;

    static final String SOURCESURL = "https://newsapi.org/v2/sources?language=fr&apiKey=42db7c5e940c422690b7333887757ce7";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Log.d("StartAct", "Created");

        sourcesList = SourcesList.getInstance();

        retrieveSources();
    }

    private void retrieveSources() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a json response from the provided URL.
        JsonObjectRequest req = new JsonObjectRequest(SOURCESURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response.has("sources")){
                            try {
                                JSONArray receivedSources = response.getJSONArray("sources");

                                sourcesList.clear();

                                for(int i = 0; i < receivedSources.length(); i++){
                                    JSONObject sourceJSON = receivedSources.getJSONObject(i);
                                    Source source = new Source(sourceJSON);
                                    sourcesList.add(source);
                                }

                                Log.d("StartAct", "retrieveSources - done");

                                // launch the main activity
                                Intent i = new Intent(StartActivity.this, MainActivity.class);
                                startActivity(i);

                            } catch (JSONException e) {
                                Log.e("StartAct", "retrieveSources - parsing error : " + e.getMessage());
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("StartAct", "retrieveSources - network error : " + error.getMessage());
                        popNetworkError();
                    }
                });

        queue.add(req);
    }

    private void popNetworkError() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Erreur de connexion");
        alertBuilder.setMessage("Les informations n'ont pas pu être récupérées.\nAssurez vous d'être bien connecté à un réseau");

        alertBuilder.setPositiveButton("Réessayer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                retrieveSources();
            }
        });

        alertBuilder.show();
    }
}
