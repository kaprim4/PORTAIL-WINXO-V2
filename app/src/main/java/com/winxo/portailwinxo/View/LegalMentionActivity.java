package com.winxo.portailwinxo.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.winxo.portailwinxo.R;
import com.winxo.portailwinxo.Utilities.Constants;

public class LegalMentionActivity extends AppCompatActivity {

    private WebView legal_mention_txt;
    private FloatingActionButton home_btn;
    private LinearLayout loading_bloc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legal_mention);

        initialiseVars();
        blocUserEventClick();

        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoMenuActivity();
            }
        });

        String url = Constants.WEB_SERVICE_URL + "legal_mention.html";
        legal_mention_txt.loadUrl(url);

        legal_mention_txt.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                unblocUserEventClick();
            }
        });
    }

    private void initialiseVars(){
        legal_mention_txt = findViewById(R.id.legal_mention_txt);
        home_btn = findViewById(R.id.home_btn);
        loading_bloc = findViewById(R.id.loading_bloc);
    }

    private void gotoMenuActivity(){
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void blocUserEventClick() {
        loading_bloc.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void unblocUserEventClick() {
        loading_bloc.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}