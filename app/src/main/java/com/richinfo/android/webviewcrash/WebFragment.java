package com.richinfo.android.webviewcrash;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class WebFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
         View view=inflater.inflate(R.layout.activity_main,container,false);


        WebView webView=view.findViewById(R.id.mail_webView);
        setWebSettings(webView);
        webView.loadUrl("https://blog.csdn.net/hxy_blog/article/details/110927892");
         return view;

    }
    private void setWebSettings(WebView mailWebView) {
        WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG);
        WebSettings settings = mailWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSavePassword(false);
        settings.setSupportZoom(true);
//        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
//        reqMap.put("userAgent", String.format("%s MOA.ZQ.%s", android.webkit.WebSettings.getDefaultUserAgent(this), BuildConfig.VERSION_NAME));
//        settings.setUserAgentString(DataConstants.MOA_USERAGENT);
        settings.setAppCacheEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setDatabaseEnabled(true);
        //多媒体自动播放
        settings.setMediaPlaybackRequiresUserGesture(false);
        //自适应屏幕
        settings.setUseWideViewPort(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }

        /**设置缓存模式
         * LOAD_CACHE_ONLY:  不使用网络，只读取本地缓存数据
         LOAD_DEFAULT:  根据cache-control决定是否从网络上取数据。
         LOAD_CACHE_NORMAL: API level 17中已经废弃, 从API level 11开始作用同LOAD_DEFAULT模式
         LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
         LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
         LOAD_DEFAULT下，无论如何都会从网络上取数据，如果没有网络，就会出现错误页面
         */
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启 DOM storage API 功能
        settings.setDomStorageEnabled(true);
        mailWebView.setWebViewClient(new MyWebViewClient());
    }

    private class MyWebViewClient extends WebViewClient {


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("mailto:") || url.contains("mailto:")) {
                return true;
            } else {
                return super.shouldOverrideUrlLoading(view, url);
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
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
