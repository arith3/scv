package com.example.jy.scv;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CastActivity extends AppCompatActivity {

    private WebView webview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cast);

        webview = (WebView) findViewById(R.id.webView);
        webview.setWebViewClient(new WebViewClient());
        webview .getSettings().setJavaScriptEnabled(true);
        webview .getSettings().setDomStorageEnabled(true);
        webview.loadUrl("http://192.168.0.74:8000/#/remote-control/192.168.0.74");

    }
}