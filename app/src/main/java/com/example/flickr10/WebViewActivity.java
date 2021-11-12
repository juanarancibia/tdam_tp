package com.example.flickr10;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        url = getIntent().getExtras().getString("Url");

        webView = (WebView) findViewById(R.id.web_flickr);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);

    }
}