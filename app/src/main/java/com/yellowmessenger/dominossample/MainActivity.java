package com.yellowmessenger.dominossample;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
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
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    String configData = "";
    //    LinearLayout dynamicLayout;
//    EditText botnameView,
//            botidView;
//    boolean enableHistory=true;
    String botname="Ym chatbot demo";
    String bot_id;
    String enableHistory;
    LinearLayout dynamicLayout;
    EditText botnameView,
            botidView;
    //    boolean enableHistory = true;
    Switch historyEnabler;
    ArrayList<Integer> ids = new ArrayList<Integer>();
    boolean enableHistoryChange;
    HashMap<String, Object> payloadData;
    HashMap<String, String> dynamicContenct;
    FloatingActionButton fabRemover;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fabRemover=findViewById(R.id.remove);
        dynamicLayout = findViewById(R.id.payloadsDynamic);
        botidView = findViewById(R.id.botid);
//        botnameView = findViewById(R.id.botname);
        historyEnabler = findViewById(R.id.switch1);
        historyEnabler.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                enableHistoryChange = b;
            }
        });
        SharedPreferences sp = getSharedPreferences("bot_mem", MODE_PRIVATE);
//        botnameView.setText(sp.getString("botname", ""));
        botidView.setText(sp.getString("botid", ""));
        EditText et = new EditText(this);
        et.setId(0);
        et.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        et.setHint("payload : value");
        et.getBackground().setColorFilter(getResources().getColor(R.color.bot_color), PorterDuff.Mode.SRC_ATOP);
        dynamicLayout.addView(et);
        ids.add(0);
    }

    public void createBot() {
        SharedPreferences sp = getSharedPreferences("bot_mem", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
//        editor.putString("botname", botnameView.getText().toString().trim());
        editor.putString("botid", botidView.getText().toString().trim());
        editor.commit();

//        botname = botnameView.getText().toString().trim();
        bot_id = botidView.getText().toString().trim();
        enableHistory = Boolean.toString(enableHistoryChange);
        dynamicContenct = new HashMap<String, String>();
        for (Integer i : ids) {
            TextView tempTV = dynamicLayout.findViewById(i);
            String[] s = tempTV.getText().toString().trim().split(":");
            if (s.length > 1) {
                dynamicContenct.put(s[0], s[1]);
            }
        }

    }

    @SuppressLint("ResourceAsColor")
    public void addDynamicView(View view) {
        int id = ids.get(ids.size() - 1) + 1;
        EditText et = new EditText(this);
        et.setId(id);
        et.setHint("payload : value");
        et.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        et.getBackground().setColorFilter(getResources().getColor(R.color.bot_color), PorterDuff.Mode.SRC_ATOP);
        dynamicLayout.addView(et);
        ids.add(id);
        if(ids.size()>1)
        {
            fabRemover.setVisibility(View.VISIBLE);
        }
    }

    public void deleteView(View view) {
//        View namebar=dynamicLayout.findViewById(1);
        if (ids.size() > 1) {
            Log.i("bot_arr", ids.toString());
            dynamicLayout.removeView(dynamicLayout.findViewById(ids.get(ids.size() - 1)));
            ids.remove(ids.size() - 1);
            Log.i("bot_arr", ids.toString());
        }
        if(ids.size()<=1)
        {
            fabRemover.setVisibility(View.GONE);
        }
    }

    public void createApp(View view) {
//        configData = "{" +
//                    "\"botName\": \""+botnameView.getText().toString().trim()+"\"," +
//                    "\"botID\": \""+botidView.getText().toString().trim()+"\"," +
//                    "\"enableHistory\": \"true\"" +
//                    "}";
        createBot();
        configData = "{" +
                "\"botName\": \"" + botname + "\"," +
                "\"botID\": \"" + bot_id + "\"," +
                "\"enableHistory\": \"" + enableHistory + "\"" +
                "}";
        Log.i("bot_dynamic_payloads", configData);
        payloadData = new HashMap<>();
        YMBotPlugin pluginYM = YMBotPlugin.getInstance();
        try {
            pluginYM.init(new BotEventListener() {

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

        pluginYM.setconfig(configData); // after init call
        payloadData.put("Platform", "Android-App");
        payloadData.putAll(dynamicContenct);
        Log.i("bot_dynamic_payloads", payloadData.toString());
        pluginYM.setPayload(payloadData);
        pluginYM.startChatBot(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(view -> {
//
//        });
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
