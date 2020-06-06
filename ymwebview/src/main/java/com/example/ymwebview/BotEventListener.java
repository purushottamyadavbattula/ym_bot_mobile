package com.example.ymwebview;

import com.example.ymwebview.models.BotEventsModel;

public interface BotEventListener {
    void onSuccess(BotEventsModel botEvent);
    void onFailure(String error);
}