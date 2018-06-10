package com.example.jy.scv;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;
import java.util.TimerTask;

public class CastActivity extends AppCompatActivity {

    FirebaseDatabase fbfb = FirebaseDatabase.getInstance();
    DatabaseReference rfrf = fbfb.getReference("appNum");
    private WebView webview;
    private Button exitBtn;
    private final Handler handler = new Handler();
    private String btnStr;
    private int count = 25;
    private int timer_sec = 0;
    private int temp=0;
    public CountDownTimer countDownTimer;



    @Override
    public void onBackPressed(){

        super.onBackPressed();
    }


    @Override
    public void onDestroy() {
        //countDownTimer = null;
        Log.e("sext", "화면돌아갔다 씨발노마");
        Toast.makeText(this, "화면돌아씨발아", Toast.LENGTH_SHORT).show();
        //temp=1;
        if(webview != null) {
            countDownTimer.cancel();
            webview.destroy();
         //   countDownTimer = null;
        }
        super.onDestroy();

        //Log.e("ONDESTROY","한다!");
        //try{
        //    Log.e("TRY","한다!");
        //    countDownTimer.cancel();
        //        //} catch (Exception e) {}
        //countDownTimer=null;
    }






    private ProgressBar progressBar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cast);

        Intent intent = getIntent();
        int position  = intent.getExtras().getInt("position");
        if(position == 0 || position == 2){ // angrybird, sonyujunsun
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        exitBtn = (Button) findViewById(R.id.quitBtn);
        btnStr = exitBtn.getText().toString();
        //countDownTimer = null;
        if(temp==0) {
            //temp=1;
            countDownTimer = new CountDownTimer(26 * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    count = (int) (millisUntilFinished / 1000);
                    int min = (int) ((count) / 60);
                    int sec = (count) % 60;
                    Log.e("Why??", millisUntilFinished + ", " + sec + "");
                    String dueTime = min + ":" + sec;
                    exitBtn.setText(btnStr + dueTime);
                    if (sec < 11 && min == 0) {
                        if (sec % 2 == 0)
                            exitBtn.setBackgroundColor(Color.RED);
                        else if (sec % 2 == 1)
                            exitBtn.setBackgroundColor(Color.GREEN);
                    }

                }


                @Override
                public void onFinish() {

                    CastActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            webview.removeAllViews();
                            webview.clearHistory();
                            webview.clearCache(true);
                            webview.destroy();
                            webview = null;
                            CastActivity.this.finish();
                            rfrf.setValue("end");
                            Log.e("SecSec", "SSS");
                            countDownTimer.cancel();
                            //countDownTimer=null;
                            Intent res = new Intent();
                            setResult(RESULT_OK, res);
                            temp=0;
                            finish();
                        }
                    });
                }
            };
        }

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("앙앙앙학하강","시바시바");
                CastActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("고노고노고노앙앙앙학하강","스스스시바시바");
                        countDownTimer.onFinish();
                        temp=0;
/*                        webview.removeAllViews();
                        webview.clearHistory();
                        webview.clearCache(true);
                        webview.destroy();
                        webview=null;
                        CastActivity.this.finish();
                        rfrf.setValue("end");
                        Log.e("SecSec","SSS");
                        countDownTimer.cancel();
                        //countDownTimer=null;
                        Intent res = new Intent();
                        setResult(RESULT_OK, res);
                        finish();*/
                    }
                });
                /*
                CastActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        countDownTimer.cancel();
                        ////countDownTimer=null;

                        webview.removeAllViews();
                        webview.clearHistory();
                        webview.clearCache(true);
                        webview.destroy();
                        webview=null;
                        CastActivity.this.finish();
                        rfrf.setValue("end");
                        Log.e("SEXSEX","XXX");
                        Intent res = new Intent();
                        setResult(RESULT_OK, res);
                        finish();

                    }
                });*/
            }
        });

        webview = (WebView) findViewById(R.id.webView);

        webview.setWebViewClient(new WebViewClient());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webview.getSettings().setDomStorageEnabled(true);
        webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                progressBar.setProgress(progress);
                if (progress == 100) {
                    progressBar.setVisibility(View.GONE);
                    exitBtn.setVisibility(View.VISIBLE);
                    countDownTimer.start();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        } );
        webview.loadUrl("http://192.168.7.3:8000/#/remote-control/192.168.7.3");

        //webview.loadUrl("http://www.naver.com");


    }
    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        Log.e("onConf","키키키");
    }

}