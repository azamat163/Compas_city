package com.example.azamat.testaltarix;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.ArrayList;

public class WebViewItemActivity extends AppCompatActivity {
    private WebView mWebView;
    private ArrayList<GeoDataModel> GeoDataModels;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_item);
        mWebView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setBuiltInZoomControls(true);
        mWebView.setWebViewClient(new CallbackWeb());
        Intent intent = getIntent();
        mWebView.loadUrl("https://ru.m.wikipedia.org/wiki/" + intent.getStringExtra("item"));
    }

    private class CallbackWeb extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return (false);
        }

    }
}
