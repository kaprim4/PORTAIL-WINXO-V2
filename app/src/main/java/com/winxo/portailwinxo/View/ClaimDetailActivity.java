package com.winxo.portailwinxo.View;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.winxo.portailwinxo.Adapter.ClaimImageAdapter;
import com.winxo.portailwinxo.Model.ForegroundService;
import com.winxo.portailwinxo.Model.SessionManager;
import com.winxo.portailwinxo.Model.VolleySingleton;
import com.winxo.portailwinxo.R;
import com.winxo.portailwinxo.Requests.ClaimRequest;
import com.winxo.portailwinxo.Utilities.Constants;
import com.winxo.portailwinxo.Utilities.Util;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClaimDetailActivity extends AppCompatActivity implements Runnable {

    private Toolbar toolbar;
    private TextView number, sitename, ref, date_claim, description;
    private LinearLayout audio_list_bloc, media_list_bloc, ly_gallery;
    private LinearLayout loading_bloc;
    private ListView claim_listView;
    private ArrayList<String> imageArray;
    private Map<Bitmap, String> img_list = new HashMap<>();
    private ClaimImageAdapter claimImageAdapter;
    private SessionManager sessionManager;
    private RequestQueue queue;
    private ClaimRequest request;
    private FloatingActionButton home_btn;
    private Button refresh_btn;
    private String id_claim;
    private String audioFileName = null;
    private boolean mStartPlaying = false;
    private MediaPlayer player = null;
    private final String audio_extension = "ogg";
    private TextView seekBar_time;
    private SeekBar seekBar;
    private ImageButton play_audio_btn;
    public Thread thread_refresh;
    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    private PointF start = new PointF();
    private PointF mid = new PointF();
    private float oldDist = 1f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_detail);

        initialiseVars();

        if (getIntent().hasExtra("id_claim")) {
            id_claim = getIntent().getStringExtra("id_claim");
        } else {
            gotoClaimListActivity();
        }

        sessionManager = new SessionManager(this);
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        request = new ClaimRequest(this, queue);
        img_list.clear();
        imageArray = new ArrayList<String>();

        blocUserEventClick();
        request.getClaimById(id_claim, new ClaimRequest.getClaimByIdCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(String id_claim, String id_site, String reference, String _date_claim, String time_claim, String _description, String audio, String image1, String image2, String image3, String image4, String status, String site_name) {
                number.setText(id_claim);
                sitename.setText(site_name);
                ref.setText(reference);
                date_claim.setText(_date_claim + " à " + time_claim);
                description.setText(_description);

                if (!audio.equals("")) {
                    audioFileName = Constants.WEB_SERVICE_URL + "audio/" + audio + "." + audio_extension;
                    Log.e("getClaimById", "audio: " + audio);
                    Log.e("getClaimById", "audioFileName: " + audioFileName);
                } else {
                    audio_list_bloc.setVisibility(View.INVISIBLE);
                }

                if (!image1.equals("") && image1 != null) {
                    media_list_bloc.setVisibility(View.VISIBLE);
                    imageArray.add(image1);
                } else {
                    media_list_bloc.setVisibility(View.INVISIBLE);
                }

                if (!image2.equals("") && image2 != null) {
                    imageArray.add(image2);
                }

                if (!image3.equals("") && image3 != null) {
                    imageArray.add(image3);
                }

                if (!image4.equals("") && image4 != null) {
                    imageArray.add(image4);
                }

                for (String img_link : imageArray) {
                    if(!img_link.equals("")) {
                        ImageView img_elm = new ImageView(getApplicationContext());
                        Glide.with(ClaimDetailActivity.this)
                                .asBitmap()
                                .load(Constants.CLAIM_IMG + img_link)
                                .apply(new RequestOptions()
                                        .fitCenter()
                                        .centerCrop()
                                        .format(DecodeFormat.PREFER_ARGB_8888)
                                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                )
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .placeholder(R.drawable.favicon_apag_new)
                                .error(R.drawable.favicon_apag_new)
                                .into(new CustomTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                        img_elm.setImageBitmap(resource);
                                        final Bitmap[] finalResource = {resource};
                                        img_elm.setOnClickListener(new View.OnClickListener() {
                                            @SuppressLint("ClickableViewAccessibility")
                                            @Override
                                            public void onClick(View v) {
                                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                                finalResource[0] = Util.resize(finalResource[0], 1000, 1000);
                                                finalResource[0].compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

                                                ViewGroup viewGroup = findViewById(android.R.id.content);
                                                View dialogView = LayoutInflater.from(ClaimDetailActivity.this).inflate(R.layout.dialog_zoom, viewGroup, false);
                                                AlertDialog.Builder builder = new AlertDialog.Builder(ClaimDetailActivity.this);
                                                builder.setView(dialogView);
                                                AlertDialog alertDialog = builder.create();
                                                ImageView img_elm_zm = dialogView.findViewById(R.id.img_elm_zm);
                                                img_elm_zm.setImageBitmap(finalResource[0]);
                                                alertDialog.show();
                                                alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                    @Override
                                                    public void onDismiss(DialogInterface dialog) {

                                                    }
                                                });
                                            }
                                        });
                                    }

                                    @Override
                                    public void onLoadCleared(@Nullable Drawable placeholder) {
                                        img_elm.setImageDrawable(placeholder);
                                        unblocUserEventClick();
                                    }

                                    @Override
                                    public void onLoadStarted(@Nullable Drawable placeholder) {
                                        img_elm.setImageDrawable(placeholder);
                                        blocUserEventClick();
                                    }

                                    @Override
                                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                        img_elm.setImageDrawable(errorDrawable);
                                        unblocUserEventClick();
                                    }
                                });
                        int ly_width = (ly_gallery.getWidth() - 10) / 4;
                        CardView cv = new CardView(getApplicationContext());
                        cv.setCardElevation(5);
                        cv.setRadius(15);
                        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ly_width, ly_width);
                        lp.setMargins(5, 5, 5, 5);
                        cv.setLayoutParams(lp);
                        img_elm.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        cv.addView(img_elm);
                        ly_gallery.addView(cv);
                    }
                }
            }

            @Override
            public void onError(String message) {
                unblocUserEventClick();
                Toast.makeText(ClaimDetailActivity.this, message, Toast.LENGTH_SHORT).show();
                gotoClaimListActivity();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekBar_time.setVisibility(View.VISIBLE);
            }

            @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBar_time.setVisibility(View.VISIBLE);
                if (fromUser) {
                    if (player != null) {
                        player.seekTo(seekBar.getProgress());
                        seekBar.setProgress(progress);
                        seekBar_time.setText(Util.convertSeekBarDuration(player.getDuration()));
                    }
                } else {
                    seekBar_time.setVisibility(View.VISIBLE);
                    int x = (int) Math.ceil(progress / 1000f);
                    if (x < 10)
                        seekBar_time.setText("0:0" + x);
                    else
                        seekBar_time.setText("0:" + x);

                    double percent = progress / (double) seekBar.getMax();
                    int offset = seekBar.getThumbOffset();
                    int seekWidth = seekBar.getWidth();
                    int val = (int) Math.round(percent * (seekWidth - 2 * offset));
                    int labelWidth = seekBar_time.getWidth();
                    seekBar_time.setX(offset + seekBar.getX() + val
                            - Math.round(percent * offset)
                            - Math.round(percent * labelWidth / 2));

                    if (progress > 0 && player != null && !player.isPlaying()) {
                        play_audio_btn.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.round_button));
                        play_audio_btn.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorOrange));
                        play_audio_btn.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.ic_baseline_play_arrow_24));
                        clearMediaPlayer();
                        seekBar.setProgress(0);
                    }
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (player != null && player.isPlaying()) {
                    player.seekTo(seekBar.getProgress());
                    seekBar_time.setText(Util.convertSeekBarDuration(player.getDuration()));
                }
            }
        });

        play_audio_btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                onPlay(mStartPlaying);
            }
        });

        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoMenuActivity();
            }
        });
    }

    private void initialiseVars() {
        toolbar = findViewById(R.id.toolbar);
        number = findViewById(R.id.number);
        sitename = findViewById(R.id.sitename);
        ref = findViewById(R.id.ref);
        date_claim = findViewById(R.id.date_claim);
        description = findViewById(R.id.description);
        audio_list_bloc = findViewById(R.id.audio_list_bloc);
        seekBar = findViewById(R.id.seekBar);
        seekBar_time = findViewById(R.id.seekBar_time);
        play_audio_btn = findViewById(R.id.play_audio_btn);
        media_list_bloc = findViewById(R.id.media_list_bloc);
        claim_listView = findViewById(R.id.claim_listView);
        ly_gallery = findViewById(R.id.ly_gallery);
        home_btn = findViewById(R.id.home_btn);
        refresh_btn = findViewById(R.id.refresh_btn);
        loading_bloc = findViewById(R.id.loading_bloc);
    }

    private void gotoMenuActivity() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();
    }

    private void gotoClaimListActivity() {
        Intent intent;
        if (sessionManager.getId_profile().equals("2")) {
            intent = new Intent(getApplicationContext(), ClaimListActivity.class);
        } else {
            intent = new Intent(getApplicationContext(), ClaimSupervisorActivity.class);
        }
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void onPlay(boolean start) {
        startPlaying();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void startPlaying() {
        try {
            if (player != null && player.isPlaying()) {
                clearMediaPlayer();
                seekBar.setProgress(0);
                mStartPlaying = true;
                play_audio_btn.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.round_button));
                play_audio_btn.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorOrange));
                play_audio_btn.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.ic_baseline_play_arrow_24));
            }
            if (!mStartPlaying) {
                if (player == null) {
                    player = new MediaPlayer();
                }
                player.setDataSource(audioFileName);
                player.prepare();
                player.setVolume(0.5f, 0.5f);
                player.setLooping(false);
                seekBar.setMax(player.getDuration());
                seekBar_time.setText(Util.convertSeekBarDuration(player.getDuration()));
                player.start();
                play_audio_btn.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.round_button));
                play_audio_btn.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorWood));
                play_audio_btn.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.ic_baseline_pause_24));
                new Thread(this).start();
            }
            mStartPlaying = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        clearMediaPlayer();
        clearThread();
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        stopService(serviceIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearMediaPlayer();
        clearThread();
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        stopService(serviceIntent);
    }

    private void clearMediaPlayer() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
            mStartPlaying = false;
        }
    }

    private void clearThread() {
        try {
            if (thread_refresh.isAlive())
                thread_refresh.interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        int currentPosition = player.getCurrentPosition();
        int total = player.getDuration();
        while (player != null && player.isPlaying() && currentPosition < total) {
            try {
                Thread.sleep(1000);
                currentPosition = player.getCurrentPosition();
            } catch (Exception e) {
                return;
            }
            seekBar.setProgress(currentPosition);
        }
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