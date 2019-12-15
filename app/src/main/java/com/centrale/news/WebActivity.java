package com.centrale.news;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class WebActivity extends AppCompatActivity {

    String url;

    WebView webview;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        Log.d("WebViewAct", "Created");

        webview = (WebView) findViewById(R.id.webView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);

        Bundle b = getIntent().getExtras();

        url = "";
        if (b != null)
            url = b.getString("url");

        webview.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        launchWebView();
    }

    private void launchWebView(){
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.d("webViewAct", "Start Loading : " + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d("webViewAct", "Finish Loading");
                webview.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Log.e("webViewAct", "network error : " + error.getDescription());
                popNetworkError();
            }
        });
        webview.loadUrl(url);
    }

    private void popNetworkError() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Erreur de connexion");
        alertBuilder.setMessage("Les informations n'ont pas pu être récupérées.\nAssurez vous d'être bien connecté à un réseau");

        alertBuilder.setPositiveButton("Réessayer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                launchWebView();
            }
        });

        alertBuilder.show();
    }
}
