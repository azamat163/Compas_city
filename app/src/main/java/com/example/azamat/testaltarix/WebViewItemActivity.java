package com.example.azamat.testaltarix;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.util.ArrayList;

public class WebViewItemActivity extends AppCompatActivity {
    private WebView mWebView;
    private ProgressDialog pg;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_item);
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        getSupportActionBar().setTitle(intent.getStringExtra("item"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        pg = ProgressDialog.show(WebViewItemActivity.this,"","Загрузка...",true);
        mWebView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setBuiltInZoomControls(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error){
                Toast.makeText(WebViewItemActivity.this,"Error :" + error,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {   super.onPageStarted(view,url,favicon);
                pg.show();
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view,url);
                pg.hide();
            }

        });
        mWebView.loadUrl("https://ru.m.wikipedia.org/wiki/" + intent.getStringExtra("item"));
    }


}
