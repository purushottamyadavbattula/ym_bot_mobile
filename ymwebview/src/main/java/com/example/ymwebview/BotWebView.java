package com.example.ymwebview;



import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.ymwebview.models.ConfigDataModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Locale;


public class BotWebView extends AppCompatActivity {
    private final String TAG = "YM WebView Plugin";
    WebviewOverlay fh;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bot_web_view);
        fh=new WebviewOverlay();
        FragmentManager fragManager=getSupportFragmentManager();
        fragManager.beginTransaction()
                .add(R.id.container,fh)
                .commit();
        String enableSpeech = ConfigDataModel.getInstance().getConfig("enableSpeech");
        Log.d(TAG, "enableSpeech : "+ enableSpeech);

        if(Boolean.parseBoolean(enableSpeech)){
            FloatingActionButton micButton = findViewById(R.id.floatingActionButton);
            micButton.setVisibility(View.VISIBLE);
            micButton.setOnClickListener(view -> {
                toggleBottomSheet();
            });
        }

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view->{
            this.finish();
        });
    }

    private void speechRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        startActivityForResult(intent, 10);

    }

    public void startListeningWithoutDialog() {
        // Intent to listen to user vocal input and return the result to the same activity.
        Context appContext = getApplicationContext();
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        // Use a language model based on free-form speech recognition.
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                appContext.getPackageName());

        // Add custom listeners.
        CustomRecognitionListener listener = new CustomRecognitionListener();
        SpeechRecognizer sr = SpeechRecognizer.createSpeechRecognizer(appContext);
        sr.setRecognitionListener(listener);
        sr.startListening(intent);
    }

    public void toggleBottomSheet() {
//        Toast.makeText(this, "opening Mic", Toast.LENGTH_SHORT).show();
        fh.sendEvent("Bleh Bleh");
        RelativeLayout voiceArea = findViewById(R.id.voiceArea);

        if(voiceArea.getVisibility() == View.INVISIBLE){
            voiceArea.setVisibility(View.VISIBLE);
//            speechRecognition();
            startListeningWithoutDialog();
        }else voiceArea.setVisibility(View.INVISIBLE);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case 10:
//                    TextView textView = findViewById(R.id.speechTranscription);

//                    textView.setText(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0).toString());

                    break;
            }
        } else {
            Toast.makeText(getApplicationContext(), "Failed to recognize speech!", Toast.LENGTH_LONG).show();
        }
    }
    class CustomRecognitionListener implements RecognitionListener {
        private static final String TAG = "RecognitionListener";

        public void onReadyForSpeech(Bundle params) {
            Log.d(TAG, "onReadyForSpeech");
        }

        public void onBeginningOfSpeech() {
            Log.d(TAG, "onBeginningOfSpeech");
        }

        public void onRmsChanged(float rmsdB) {
            Log.d(TAG, "onRmsChanged");

        }

        public void onBufferReceived(byte[] buffer) {
            Log.d(TAG, "onBufferReceived");
        }

        public void onEndOfSpeech() {
            Log.d(TAG, "onEndofSpeech");
        }

        public void onError(int error) {
            Log.e(TAG, "error " + error);

//            conversionCallaback.onErrorOccured(TranslatorUtil.getErrorText(error));
        }

        public void onResults(Bundle results) {
            ArrayList<String> result = results
                    .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            TextView textView = findViewById(R.id.speechTranscription);
            textView.setText(result.get(0));
        }

        public void onPartialResults(Bundle partialResults) {
            Log.d(TAG, "onPartialResults"+partialResults.toString());

        }

        public void onEvent(int eventType, Bundle params) {
            Log.d(TAG, "onEvent " + eventType);
        }
    }


}

