package com.dubs.whatscooking.whatscooking;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        Bundle bundle = getIntent().getExtras();
        final String url = bundle.getString("url");
        final String recipe = bundle.getString("recipe");
        Toolbar toolbar = (Toolbar) findViewById(R.id.web_toolbae);
        toolbar.setTitle(recipe);
        setSupportActionBar(toolbar);


        WebView recWebView = (WebView) findViewById(R.id.recommendation_webview);
        recWebView.setWebChromeClient(new WebChromeClient());
        recWebView.setWebViewClient(new WebViewClient());
        recWebView.loadUrl(url);
    }

}
