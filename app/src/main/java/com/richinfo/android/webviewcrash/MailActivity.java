package com.richinfo.android.webviewcrash;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MailActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testmail);


        WebView.setWebContentsDebuggingEnabled(true);
        new AsyncTask<Object,Object,String>(){

            @Override
            protected String doInBackground(Object... objects) {
                StringBuilder stringBuilder = new StringBuilder();
                try {
                    //获取assets资源管理器
                    AssetManager assetManager = MailActivity.this.getAssets();
                    //通过管理器打开文件并读取
                    BufferedReader bf = new BufferedReader(new InputStreamReader(
                            assetManager.open("mailcontent.html")));
                    String line;
                    while ((line = bf.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return stringBuilder.toString();
            }

            @Override
            protected void onPostExecute(String content) {
                super.onPostExecute(content);
                WebView webView=findViewById(R.id.mail_webView);
                setWebSettings(content);
                //also crash
//                webView.loadUrl("file:///android_asset/mailcontent.html");
                webView.loadDataWithBaseURL(null,content,"text/html", "utf-8", null);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
        }.execute();

    }
    private void setWebSettings(String htmlUrl) {
        WebView mailWebView=findViewById(R.id.mail_webView);
        WebSettings settings = mailWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mailWebView.removeJavascriptInterface("searchBoxJavaBridge_");//addJavascript漏洞，移除多余的js
        mailWebView.removeJavascriptInterface("accessibility");
        mailWebView.removeJavascriptInterface("accessibilityTraversal");
        mailWebView.setVerticalScrollBarEnabled(false);
        mailWebView.setVerticalScrollbarOverlay(false);
        mailWebView.setHorizontalScrollBarEnabled(false);
        mailWebView.setHorizontalScrollbarOverlay(false);

        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);
        settings.setDefaultTextEncodingName("UTF-8");
        if (htmlUrl.contains("img") || htmlUrl.contains("table")) {
            settings.setUseWideViewPort(true);
            settings.setTextZoom(130);
        } else {
//            settings.setUseWideViewPort(true);
//            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//            settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
//            settings.setDefaultFixedFontSize(50);
            settings.setLoadWithOverviewMode(true);
            settings.setTextZoom(100);
        }

//        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        DisplayMetrics metrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        int mDensity = metrics.densityDpi;
//
//        if (mDensity == 120) {
//            mailWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
//        } else if (mDensity == 160) {
//            mailWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
//        } else if (mDensity == 240) {
//            mailWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
//        }

        mailWebView.setWebViewClient(new MyWebViewClient());
    }

    boolean paused= false;

    @Override
    protected void onResume() {
        super.onResume();
        paused  = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        paused = true;
    }

    private class MyWebViewClient extends WebViewClient {


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            if (url.startsWith("mailto:") || url.contains("mailto:")) {
//                return true;
//            } else {
                return super.shouldOverrideUrlLoading(view, url);
//            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean onRenderProcessGone(WebView view, RenderProcessGoneDetail detail) {

            return super.onRenderProcessGone(view, detail);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
//            Log.e("omg","page finished");
//            if (!paused &&!isFinishing()) {
//                findViewById(R.id.mail_header).postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (!paused &&!isFinishing()) {
//                            Log.e("omg","call visiable finished");
//                            findViewById(R.id.mail_header).setVisibility(View.VISIBLE);
//                            findViewById(R.id.mail_header_line).setVisibility(View.VISIBLE);
//                        }
//                    }
//                },1000);
//            }

        }

        //忽略Https SSL验证
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }

//        public void onScaleChanged(WebView view, float oldScale, float newScale) {
//            if (isf){
//                isf = false;
//                return;
//            }
//            Intent intent = new Intent(MailDetailActivity.this,MailContentDetailActivity.class);
//            String content = getHtmlData(message.getHtml().getContent());
//            intent.putExtra("mailcontent",content);
//            startActivity(intent);
//            overridePendingTransition(R.anim.my_scale_action, R.anim.my_alpha_action);
//            view.setInitialScale((int)oldScale * 100);
//        }

    }
}