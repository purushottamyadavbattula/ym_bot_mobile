package com.example.ymwebview;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.ymwebview.models.ConfigDataModel;
import com.example.ymwebview.models.JavaScriptInterface;
import com.google.gson.Gson;
import java.net.URLEncoder;
import java.util.Map;
import im.delight.android.webview.AdvancedWebView;

public class WebviewOverlay extends Fragment implements AdvancedWebView.Listener {
    private final String TAG = "YM WebView Plugin";
    private AdvancedWebView myWebView;
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        progressDialog = new ProgressDialog(getActivity().getApplicationContext());
        progressDialog.setTitle("Please wait.");
        progressDialog.setMessage("The bot is initializing...");
        progressDialog.setCanceledOnTouchOutside(false);
         try {
           progressDialog.show();
       }
       catch (Exception e){
           Log.e(TAG, "YmPlugin: Bot loading dialog ", e );
       }
        myWebView = (AdvancedWebView) preLoadWebView();
        return myWebView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public View preLoadWebView() {
        final Context context = getActivity();
        String botId = ConfigDataModel.getInstance().getConfig("botID");
        Map payload = ConfigDataModel.getInstance().getPayload();
        String payloadJSON = URLEncoder.encode(new Gson().toJson(payload));
        String enableHistory = ConfigDataModel.getInstance().getConfig("enableHistory");
        myWebView = new AdvancedWebView(context);
        myWebView.setListener(getActivity(), this);
        final String botUrl = "https://yellowmessenger.github.io/pages/dominos/mobile.html?botId=" + botId + "&enableHistory=" + enableHistory + "&ym.payload=" + payloadJSON;
//
        Log.d(TAG, "onCreate: " + botUrl);
        myWebView.loadUrl(botUrl);
        myWebView.getSettings().setSupportMultipleWindows(true);
        myWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        myWebView.getSettings().setGeolocationDatabasePath(context.getFilesDir().getPath());
        myWebView.addJavascriptInterface(new JavaScriptInterface((BotWebView) getActivity(), myWebView), "YMHandler");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        myWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                AdvancedWebView newWebView = new AdvancedWebView(context);
                //setContentView(newWebView);
                Log.d("qwerty", resultMsg.toString());
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(newWebView);
                resultMsg.sendToTarget();
//                view.loadUrl(botUrl);
                newWebView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                        browserIntent.setData(Uri.parse(url));
                        startActivity(browserIntent);
                        return true;
                    }
                });
                return true;
            }
        });
        return myWebView;
    }

    //    @Override
//    public void onBackPressed() {
//        //Log.i("appLog",""+myWebView.canGoBack());
//        if (myWebView.canGoBack()) {
//            myWebView.goBack();
//        }
//        else {
//            //super.onBackPressed();
//        }

        public void sendEvent(String s){
            Log.d("Sending Event: ", s);
            myWebView.loadUrl("javascript:sendEvent('"+s+"');");
    }
    @Override
    public void onPageStarted(String url, Bitmap favicon) {
    }

    @Override
    public void onPageFinished(String url) {
        try {
            progressDialog.dismiss();
        }
        catch (Exception e){
            Log.e(TAG, "YmPlugin: Bot loading dialog dismiss ", e );
        }
        myWebView.setVisibility(View.VISIBLE);
    }


    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
        try {
            progressDialog.dismiss();
        }
        catch (Exception e){
            Log.e(TAG, "YmPlugin: Bot loading dialog dismiss ", e );
        }
        Log.e("WebView Error", "onPageError(errorCode = "+errorCode+",  description = "+description+",  failingUrl = "+failingUrl+")");
    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {
    }

    @Override
    public void onExternalPageRequest(String url) {
        //Toast.makeText(BotWebView.this, "onExternalPageRequest(url = "+url+")", Toast.LENGTH_SHORT).show();
    }


}
