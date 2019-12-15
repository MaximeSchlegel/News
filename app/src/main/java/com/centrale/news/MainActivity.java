package com.centrale.news;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.centrale.news.DataModels.Article;
import com.centrale.news.DataModels.ArticlesList;
import com.centrale.news.DataModels.Source;
import com.centrale.news.DataModels.SourcesList;
import com.centrale.news.fragments.ArticlePreviewListFragment;
import com.centrale.news.fragments.ArticlePreviewShowFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.centrale.news.fragments.ArticlePreviewListFragment.*;
import static com.centrale.news.fragments.ArticlePreviewShowFragment.*;


public class MainActivity
        extends AppCompatActivity
        implements OnListFragmentInteractionListener, OnShowFragmentInteractionListener {

    static final String ARTICLESURL = "https://newsapi.org/v2/everything";
    static final String API_KEY = "42db7c5e940c422690b7333887757ce7";

    private SourcesList sourcesList;
    private Source selectedSource;

    private ArticlesList articlesList;
    private int page;

    Spinner spinner;
    ArticlePreviewListFragment previewListFragment;
    ArticlePreviewShowFragment previewShowFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("MainAct", "Created");

        sourcesList = SourcesList.getInstance();
        articlesList = ArticlesList.getInstance();

        // Map the activity components
        spinner = (Spinner) findViewById(R.id.sourcesSpinner);
        previewListFragment = new ArticlePreviewListFragment();
        previewShowFragment = null;

        // Create the dropdown menu
        final ArrayList<CharSequence> sources = new ArrayList<>();
        sources.add("Top Headlines");
        for(int i = 0; i < sourcesList.size(); i++){
            sources.add(sourcesList.get(i).getName());
        }

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, sources);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    selectedSource = null;
                    Log.i("MainAct", "Spinner selected: Top Headline");
                } else {
                    position--;
                    selectedSource = sourcesList.get(position);
                    Log.i("MainAct", "Spinner selected: " + selectedSource.getName());
                }
                page = 1;
                articlesList.clear();
                previewListFragment.notifyDataSetChanged();
                retrieveArticles();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        Log.d("MainAct", "Spinner Created");

        // set the selected sources
        if (savedInstanceState != null) {
            selectedSource = SourcesList.getInstance().get(savedInstanceState.getString("source"));
            Log.d("MainAct", "Old selected sources loaded : " + selectedSource.getName());
        } else {
            selectedSource = null;
        }

        // get the articles from the sources
        retrieveArticles();

        // Create the articles list view
        getSupportFragmentManager().beginTransaction()
                .add(R.id.articlePreviewListContainer, previewListFragment)
                .commit();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (selectedSource != null) {
            outState.putString("source", selectedSource.getId());
            Log.d("MainAct", "selected source saved");
        }
        super.onSaveInstanceState(outState);
    }

    private void retrieveArticles() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        String url;
        if (selectedSource != null) {
            url = ARTICLESURL+"?language=fr&sources="+selectedSource.getId()+"&apge="+page+"&apikey="+API_KEY;
        } else {
            url = "https://newsapi.org/v2/top-headlines?country=fr&apge="+page+"&apiKey="+API_KEY;
        }
        Log.d("MainAct", "retrieveArticles url : " + url);

        // Request a json response from the provided URL.
        JsonObjectRequest req = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response.has("articles")){
                            try {
                                JSONArray receivedArticles = response.getJSONArray("articles");

                                for(int i = 0; i < receivedArticles.length(); i++){
                                    JSONObject sourceJSON = receivedArticles.getJSONObject(i);
                                    Article article = new Article(sourceJSON);
                                    articlesList.add(article);
                                    previewListFragment.notifyDataSetChanged();
                                }


                                Log.d("MainAct", "retrieveArticles - done");
                            } catch (JSONException e) {
                                Log.e("MainAct", "retrieveArticles - error parsing : " +e.getMessage());
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("MainAct", "retrieveArticles - network error : " + error.getMessage());
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
                retrieveArticles();
            }
        });

        alertBuilder.show();
    }

    @Override
    public void onArticleClicked(int position) {
        Log.d("MainAct", "article clicked : " + ArticlesList.getInstance().get(position).getSourceId());
        previewShowFragment = ArticlePreviewShowFragment.newInstance(position);
        getSupportFragmentManager().beginTransaction()
                .remove(previewListFragment)
                .commit();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, previewShowFragment)
                .commit();
    }

    @Override
    public void onTop() {
        //refresh the articles
        Log.d("MainAct", "refresh articles");
        page = 1;
        articlesList.clear();
        previewListFragment.notifyDataSetChanged();
        retrieveArticles();
    }

    @Override
    public void onBottom() {
        //load more articles
        if (page <= 5) {
            Log.d("MainAct", "Bottom - Loading more article");
            page ++;
            retrieveArticles();
        } else {
            Log.d("MainAct", "Bottom - Limit reach");
            Toast.makeText(this, "La dernière page a été atteinte", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onOpenArticle(Article article) {
        Intent i = new Intent(MainActivity.this, WebActivity.class);
        i.putExtra("url", article.getArticleUrl());
        startActivity(i);
    }

    @Override
    public void onCloseFragment() {
        Log.d("MainAct", "Close article preview show fragment");
        getSupportFragmentManager().beginTransaction()
                .remove(previewShowFragment)
                .commit();
        previewShowFragment = null;
        getSupportFragmentManager().beginTransaction()
                .add(R.id.articlePreviewListContainer, previewListFragment)
                .commit();
    }
}
