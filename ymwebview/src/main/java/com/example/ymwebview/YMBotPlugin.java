package com.example.ymwebview;

import android.content.Context;
import android.content.Intent;

import android.util.Log;

import com.example.ymwebview.models.BotEventsModel;
import com.google.gson.Gson;

import  com.example.ymwebview.models.ConfigDataModel;

import java.util.Map;

public class YMBotPlugin {
    private Context myContext;
    private Intent _intent;
    private BotEventListener listener;
    private static YMBotPlugin botPluginInstance;
    private boolean isInitialized;

    private YMBotPlugin(){}


    public static  YMBotPlugin getInstance(){
        if (botPluginInstance == null) {
            synchronized (YMBotPlugin.class) {
                if (botPluginInstance == null) {
                    botPluginInstance = new YMBotPlugin();
                }
            }
        }
        return  botPluginInstance;
    }

    public void init(String configData, BotEventListener listener){
        if(!isInitialized){
            isInitialized = true;
            if (configData != null && listener != null) {
                ConfigDataModel.getInstance().setConfig(new Gson().fromJson(configData, Map.class));
                this.listener = listener;
            } else {
                throw new RuntimeException("Mandatory arguments not present");
            }
        } else {
            throw new RuntimeException("Cannot initialize " + this.getClass().getName() + " multiple times");
        }
    }
    public void startChatBot(Context context){
        myContext = context;
        _intent = new Intent(myContext, BotWebView.class);
        myContext.startActivity(_intent);
    }


    public void setPayload(Map botPayload){
        ConfigDataModel.getInstance().emptyPayload();
        ConfigDataModel.getInstance().setPayload(botPayload);
    }

    public void emitEvent(BotEventsModel event){
        if(event != null){
            Log.v("WebView Event","From Bot: "+event.getCode());
            listener.onSuccess(event);
        }
        else
            listener.onFailure("An error occurred.");
    }

}

