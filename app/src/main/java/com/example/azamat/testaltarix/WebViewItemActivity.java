package com.example.azamat.testaltarix;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.util.ArrayList;

public class WebViewItemActivity extends AppCompatActivity {
    private WebView mWebView;
    private ProgressDialog pg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_item);
        pg = ProgressDialog.show(WebViewItemActivity.this,"","Загрузка...");
        mWebView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setBuiltInZoomControls(true);
        mWebView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(WebViewItemActivity.this, description, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                pg.show();
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                pg.dismiss();

                String webUrl = mWebView.getUrl();

            }

        });
        Intent intent = getIntent();
        mWebView.loadUrl("https://ru.m.wikipedia.org/wiki/" + intent.getStringExtra("item"));
    }


}
