package com.example.ymwebview.models;


import java.util.HashMap;
import java.util.Map;

public class ConfigDataModel{
    private static ConfigDataModel configInstance;
    private final Map<String, String> config;
    private final Map<String, String> payload;

    private ConfigDataModel(){
        config = new HashMap<>();
        payload = new HashMap<>();
    }

    public static  ConfigDataModel getInstance(){
        if (configInstance == null) {
            synchronized (ConfigDataModel.class) {
                if (configInstance == null) {
                    configInstance = new ConfigDataModel();
                }
            }
        }
        return  configInstance;
    }

    public boolean setConfig(Map configMap) {


        if (!configMap.isEmpty()) {
            config.putAll(configMap);
            return true;
        }
        return false;
    }

    public String getConfig(String key) {
        if(config.get(key) != null){
            return  config.get(key);
        }
        else return "";
    }

    public boolean setPayload(Map botPayload) {
        if (botPayload !=null) {
            payload.putAll(botPayload);
            return true;
        }
        return false;
    }

    public boolean emptyPayload() {
        if (payload !=null) {
            payload.clear();
            return true;
        }
        return false;
    }

    public String getPayloadByKey(String key) {
        return payload.get(key);
    }

    public Map<String, String> getPayload() {
        return payload;
    }


}
