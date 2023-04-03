package com.winxo.portailwinxo.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.winxo.portailwinxo.R;
import com.winxo.portailwinxo.Utilities.Constants;

public class PresentationActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private WebView presentation_txt;
    private FloatingActionButton home_btn;
    private LinearLayout loading_bloc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation);

        initialiseVars();
        blocUserEventClick();

        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoMenuActivity();
            }
        });

        String url = Constants.WEB_SERVICE_URL + "presentation.html";
        presentation_txt.loadUrl(url);

        presentation_txt.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                unblocUserEventClick();
            }
        });
    }

    private void initialiseVars() {
        toolbar = findViewById(R.id.toolbar);
        presentation_txt = findViewById(R.id.presentation_txt);
        home_btn = findViewById(R.id.home_btn);
        loading_bloc = findViewById(R.id.loading_bloc);
    }

    private void gotoMenuActivity() {
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