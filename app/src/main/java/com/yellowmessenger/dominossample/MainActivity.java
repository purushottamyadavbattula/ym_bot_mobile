package com.yellowmessenger.dominossample;

import android.content.Intent;
import android.os.Bundle;

import com.example.ymwebview.BotEventListener;
import com.example.ymwebview.YMBotPlugin;
import com.example.ymwebview.models.BotEventsModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    String configData = "";
//    LinearLayout dynamicLayout;
//    EditText botnameView,
//            botidView;
//    boolean enableHistory=true;
    String botname;
    String bot_id;
    String enableHistory;

    HashMap<String, Object> payloadData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        dynamicLayout=findViewById(R.id.payloadsDynamic);
//        botidView=findViewById(R.id.botid);
//        botnameView=findViewById(R.id.botname);
        Intent intent=getIntent();
        HashMap<String, String> hashMap = (HashMap<String, String>)intent.getSerializableExtra("bot_details");
//        Log.i("ymbotdetail",hashMap.toString());
//        Log.i("ymbotdetail",hashMap.get("botname"));
        botname=hashMap.get("botname");
        bot_id=hashMap.get("botid");
        enableHistory=hashMap.get("history");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
//            configData = "{" +
//                    "\"botName\": \""+botnameView.getText().toString().trim()+"\"," +
//                    "\"botID\": \""+botidView.getText().toString().trim()+"\"," +
//                    "\"enableHistory\": \"true\"" +
//                    "}";
            configData = "{" +
                    "\"botName\": \""+botname+"\"," +
                    "\"botID\": \""+bot_id+"\"," +
                    "\"enableHistory\": \""+enableHistory+"\"" +
                    "}";
            payloadData=  new HashMap<>();
            YMBotPlugin pluginYM = YMBotPlugin.getInstance();
            try {
                pluginYM.init(configData, new BotEventListener() {

                    @Override
                    public void onSuccess(BotEventsModel botEvent) {

                        switch (botEvent.getCode()) {
                            case "even-name-1":
                                break;
                            case "even-name-2":
                                break;
                            case "even-name-3":
                                break;
                        }
                    }

                    @Override
                    public void onFailure(String error) {
                    }
                });
            } catch (RuntimeException e) {
                Log.w("Plugin Exception", "onCreate: " + e.getMessage());
            }


            payloadData.put("Platform", "Android-App");
            Intent intent=getIntent();
            HashMap<String, String> payloads = (HashMap<String, String>)intent.getSerializableExtra("dynamic_payload");
            payloadData.putAll(payloads);
            Log.i("bot_dynamic_payloads",payloadData.toString());
            pluginYM.setPayload(payloadData);
            pluginYM.startChatBot(this);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
