package com.example.ymwebview.models;


import android.os.CountDownTimer;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.example.ymwebview.BotWebView;
import com.example.ymwebview.YMBotPlugin;
import com.google.gson.Gson;

public class JavaScriptInterface {
    protected BotWebView parentActivity;
    protected WebView mWebView;

    public JavaScriptInterface(BotWebView _activity, WebView _webView)  {
        parentActivity = _activity;
        mWebView = _webView;

    }

    @JavascriptInterface
    public void loadURL(String url) {
        final String u = url;

        parentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mWebView.loadUrl(u);
            }
        });
    }

    @JavascriptInterface
    public void  receiveMessage(String s) {
        BotEventsModel incomingEvent = new Gson().fromJson(s, BotEventsModel.class);
        Log.d("Event from Bot", "receiveMessage: "+incomingEvent.code);
            parentActivity.finish();
        YMBotPlugin.getInstance().emitEvent(incomingEvent);
    }

}
