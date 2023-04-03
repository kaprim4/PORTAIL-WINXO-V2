package com.winxo.portailwinxo.View;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.winxo.portailwinxo.Adapter.ClaimImageAdapter;
import com.winxo.portailwinxo.BuildConfig;
import com.winxo.portailwinxo.Model.ClaimImage;
import com.winxo.portailwinxo.Model.ForegroundService;
import com.winxo.portailwinxo.Model.SecurePreferences;
import com.winxo.portailwinxo.Model.SessionManager;
import com.winxo.portailwinxo.Model.VolleySingleton;
import com.winxo.portailwinxo.R;
import com.winxo.portailwinxo.Requests.ClaimRequest;
import com.winxo.portailwinxo.Service.PortailWinxoService;
import com.winxo.portailwinxo.Utilities.Constants;
import com.winxo.portailwinxo.Utilities.Util;

import net.gotev.uploadservice.UploadServiceConfig;
import net.gotev.uploadservice.data.RetryPolicyConfig;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClaimActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Runnable {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private CircleImageView avatar;
    private SessionManager sessionManager;
    private TextView navdisplay_name, navid_user, navid_city, code_sap, libelle, id_city, seekBar_time, list_size;
    private Button cancel_btn, confirm_btn, record_audio_btn, take_photo_btn, pick_photo_btn, refresh_btn;
    private EditText detail_value;
    private LinearLayout audio_list_bloc, media_list_bloc;
    private SeekBar seekBar;
    private ImageButton play_audio_btn, delete_audio_btn;
    private BottomNavigationView b_nav_view;
    private FloatingActionButton home_btn;
    private LinearLayout loading_bloc;
    /*********************************************************/
    private RequestQueue queue;
    private ClaimRequest claimRequest;
    public Thread thread_refresh;
    private String spref_username, spref_password, spref_build_version, spref_hashed_pws_db, spref_hashed_pws, spref_remember, spref_fingerprint_action, spref_activate_alerts, spref_activate_gps, spref_activate_notification_push, spref_activate_autosave, spref_id_support, spref_version, spref_imei, password_filled;
    private String filePath, audioFileName = null;
    private boolean audio_recorded, mStartRecording, mStartPlaying = false;
    private MediaRecorder recorder = null;
    private MediaPlayer player = null;
    private final String audio_extension = "ogg";
    private static final int RECORD_AUDIO_PERMISSION_CODE = 200;
    private ListView claim_listView;
    private String currentPhotoPath = null;
    private ClaimImageAdapter claimImageAdapter;
    private int selected_img;
    private int img_count = 4;
    private ArrayList<ClaimImage> imageArray;
    private static final int CAMERA_REQUEST = 1800;
    private static final int GALLERY_REQUEST = 1801;
    private static final int FOREGROUND_SERVICE_REQUEST = 1902;
    private static final int CAMERA_PERMISSION_CODE = 101;
    private static final int FOREGROUND_SERVICE_PERMISSION_CODE = 301;
    private boolean permissionToUseCameraAccepted = false, permissionToUseForegroundAccepted = false;
    private Bitmap current_first_image = null;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim);

        sessionManager = new SessionManager(this);
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        claimRequest = new ClaimRequest(this, queue);

        initialiseVars();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (sessionManager.getIs_logged()) {
            navdisplay_name.setText(sessionManager.getDisplay_name());
            navid_user.setText(sessionManager.getCode_sap());
            navid_city.setText(sessionManager.getId_city());
            code_sap.setText(sessionManager.getCode_sap());
            libelle.setText(sessionManager.getDisplay_name());
            id_city.setText(sessionManager.getId_city());
        }

        SecurePreferences preferences = new SecurePreferences(getApplicationContext(), true);
        spref_username = preferences.getString("username");
        spref_password = preferences.getString("password");
        spref_build_version = preferences.getString("build_version");
        spref_hashed_pws_db = preferences.getString("hashed_pws_db");
        spref_hashed_pws = preferences.getString("hashed_pws");
        spref_remember = preferences.getString("remember");
        spref_id_support = preferences.getString("id_support");
        spref_version = preferences.getString("version");
        spref_imei = preferences.getString("imei");
        spref_fingerprint_action = preferences.getString("fingerprint");
        spref_activate_alerts = preferences.getString("activate_alerts");
        spref_activate_notification_push = preferences.getString("activate_notification_push");
        spref_activate_autosave = preferences.getString("activate_autosave");
        spref_activate_gps = preferences.getString("activate_gps");
        filePath = getExternalCacheDir().getAbsolutePath();
        audioFileName = filePath + "/" + sessionManager.getId_site() + "_audio." + audio_extension;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.FOREGROUND_SERVICE}, FOREGROUND_SERVICE_PERMISSION_CODE);
        }

        //blocUserEventClick();

        UploadServiceConfig.initialize((Application) getApplicationContext(), Constants.NOTIFICATION_CHANNEL_ID, BuildConfig.DEBUG);
        UploadServiceConfig.setRetryPolicy(new RetryPolicyConfig(1, 10, 2, 3));

        imageArray = new ArrayList<ClaimImage>();
        claimImageAdapter = new ClaimImageAdapter(ClaimActivity.this, imageArray, claim_listView, take_photo_btn, pick_photo_btn, list_size);
        claim_listView.setAdapter(claimImageAdapter);
        setListViewHeight(claim_listView);
        claimImageAdapter.notifyDataSetChanged();
        list_size.setText(String.format(list_size.getText().toString(), String.valueOf(imageArray.size()), String.valueOf(img_count)));

        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.updateParametersFromDB(getApplicationContext());
                Util.updateSiteInfo(getApplicationContext());
                Toast.makeText(getApplicationContext(), "Mise à jour en cours...", Toast.LENGTH_SHORT).show();
                unblocUserEventClick();
            }
        });

        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoMenuActivity();
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detail_value.setText("");
            }
        });

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String p_id_site = sessionManager.getId_site();
                final String detail_val = detail_value.getText().toString().trim();
                AlertDialog.Builder builder = new AlertDialog.Builder(ClaimActivity.this);
                builder.setIcon(R.drawable.ic_notification_icon);
                builder.setTitle("Etes-vous sûr de vouloir soumettre ce ticket ?");
                builder.setPositiveButton(R.string.validate_btn, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface diag, int id) {
                        addClaimToDB(p_id_site, detail_val);
                    }
                });
                builder.setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface diag, int id) {
                        diag.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        record_audio_btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                onRecord(mStartRecording);
                if (mStartRecording) {
                    record_audio_btn.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.btn_send_pressed));
                    record_audio_btn.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.ic_baseline_mic_off_24));
                } else {
                    record_audio_btn.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.fab_color_pressed));
                    record_audio_btn.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.ic_baseline_mic_24));
                    getAudioRecorded();
                }
                mStartRecording = !mStartRecording;
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekBar_time.setVisibility(View.VISIBLE);
            }

            @SuppressLint("UseCompatLoadingForDrawables")
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
                    seekBar_time.setX(offset + seekBar.getX() + val - Math.round(percent * offset) - Math.round(percent * labelWidth / 2));

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

        delete_audio_btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                File directory = new File(getExternalCacheDir().getAbsolutePath());
                File[] files = directory.listFiles();
                if (files.length > 0) {
                    for (File file : files) {
                        if (file.getName().equals(sessionManager.getId_site() + "_audio." + audio_extension)) {
                            file.delete();
                            audio_list_bloc.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Fichier supprimé.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        take_photo_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @SuppressLint({"UseCompatLoadingForDrawables", "QueryPermissionsNeeded"})
            @Override
            public void onClick(View view) {
                if (imageArray.size() + 1 <= img_count) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        File photoFile = null;
                        try {
                            photoFile = Util.createImageFile(ClaimActivity.this, sessionManager);
                            currentPhotoPath = photoFile.getAbsolutePath();
                        } catch (IOException ex) {
                            Log.e("takeimg", "IOException: " + ex.getMessage());
                        }
                        if (photoFile != null) {
                            Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".fileprovider", photoFile);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            startActivityForResult(takePictureIntent, CAMERA_REQUEST);
                        }
                    }
                } else {
                    Util.errorDialog(ClaimActivity.this, "Vous avez atteint le nombre maximum de photos.");
                }
            }
        });

        pick_photo_btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                if (imageArray.size() + 1 <= img_count) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    String[] mimeTypes = {"image/jpeg", "image/png"};
                    intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                    startActivityForResult(intent, GALLERY_REQUEST);
                } else {
                    Util.errorDialog(ClaimActivity.this, "Vous avez atteint le nombre maximum de photos.");
                }
            }
        });

        b_nav_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.claim_add:
                        gotoClaimActivity();
                        break;
                    case R.id.claim_list:
                        gotoClaimListActivity();
                        break;
                }
                item.setEnabled(true);
                item.setChecked(true);
                return false;
            }
        });

        getAudioRecorded();

        if (!Util.isMyServiceRunning(getApplicationContext())) {
            Intent service_intent = new Intent(this, PortailWinxoService.class);
            service_intent.putExtra("station_name", sessionManager.getDisplay_name());
            service_intent.putExtra("message", "Interventions techniques");
            service_intent.putExtra("drawable", R.drawable.btn_img_03_);
            service_intent.putExtra("module_name", "CLAIM");
            startService(service_intent);
        }
    }

    private void initialiseVars() {
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        View headerView = navView.getHeaderView(0);
        avatar = headerView.findViewById(R.id.avatar);
        navdisplay_name = headerView.findViewById(R.id.navFullName);
        navid_user = headerView.findViewById(R.id.nav_code_sap);
        navid_city = headerView.findViewById(R.id.navCity);
        code_sap = findViewById(R.id.code_sap);
        libelle = findViewById(R.id.libelle);
        id_city = findViewById(R.id.city);
        detail_value = findViewById(R.id.detail_value);
        record_audio_btn = findViewById(R.id.record_audio_btn);
        take_photo_btn = findViewById(R.id.take_photo_btn);
        pick_photo_btn = findViewById(R.id.pick_photo_btn);
        audio_list_bloc = findViewById(R.id.audio_list_bloc);
        seekBar = findViewById(R.id.seekBar);
        seekBar_time = findViewById(R.id.seekBar_time);
        play_audio_btn = findViewById(R.id.play_audio_btn);
        delete_audio_btn = findViewById(R.id.delete_audio_btn);
        media_list_bloc = findViewById(R.id.media_list_bloc);
        list_size = findViewById(R.id.list_size);
        claim_listView = findViewById(R.id.claim_listView);
        cancel_btn = findViewById(R.id.cancel_btn);
        confirm_btn = findViewById(R.id.confirm_btn);
        b_nav_view = findViewById(R.id.b_nav_view);
        home_btn = findViewById(R.id.home_btn);
        refresh_btn = findViewById(R.id.refresh_btn);
        loading_bloc = findViewById(R.id.loading_bloc);
    }

    private void addClaimToDB(String p_id_site, String detail_val) {
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        claimRequest = new ClaimRequest(this, queue);
        claimRequest.addClaim(p_id_site, detail_val, spref_id_support, spref_version, spref_imei, new ClaimRequest.addClaimCallback() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(String message, String id_claim) {
                if (sessionManager.getSend_sms()) {
                    Toast.makeText(getApplicationContext(), "Vous seriez notifié par SMS du statut de votre demande dans quelques instants.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "La fonctionnalité 'SMS' est désactivée.", Toast.LENGTH_SHORT).show();
                }
                if (audio_recorded) {
                    Util.uploadAudioToDB(getApplicationContext(), id_claim, audioFileName);
                    File file = new File(audioFileName, sessionManager.getId_site() + "_audio." + audio_extension);
                    file.delete();
                }
                String[] fields = new String[1];
                String[] values = new String[1];
                fields[0] = "id_claim";
                values[0] = id_claim;
                if (imageArray.size() > 0) {
                    for (ClaimImage claimImage : imageArray) {
                        ByteArrayInputStream imageStream = new ByteArrayInputStream(claimImage.get_image());
                        Bitmap theImage = BitmapFactory.decodeStream(imageStream);
                        Util.uploadImageToDB(getApplicationContext(), "UploadImageToServer.php", theImage, fields, values);
                        Log.e("img_click", "addClaimToDB: Photo: " + theImage);
                    }
                }
                imageArray = new ArrayList<ClaimImage>();
                setListViewHeight(claim_listView);
                claimImageAdapter.notifyDataSetChanged();
                Util.displayPopupAndGoTo(ClaimActivity.this, message, 3000, true, ClaimListActivity.class);
            }

            @Override
            public void onError(String message) {
                Util.errorDialog(ClaimActivity.this, message);
                unblocUserEventClick();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSION_CODE:
                permissionToUseCameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
            case FOREGROUND_SERVICE_PERMISSION_CODE:
                permissionToUseForegroundAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
    }

    private void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        Log.w("DESIRED WIDTH", String.valueOf(listAdapter.getCount()));
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
            Log.w("HEIGHT" + i, String.valueOf(totalHeight));
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "ClickableViewAccessibility"})
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            try {
                Bitmap photo = null;
                @SuppressLint("SimpleDateFormat") String date = new SimpleDateFormat("dd / MM / yyyy").format(new Date());
                @SuppressLint("SimpleDateFormat") String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
                switch (requestCode) {
                    case CAMERA_REQUEST:
                        Util.galleryAddPic(ClaimActivity.this, currentPhotoPath);
                        photo = Util.DecodeFile(currentPhotoPath);
                        break;
                    case GALLERY_REQUEST:
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        currentPhotoPath = cursor.getString(columnIndex);
                        cursor.close();
                        Util.galleryAddPic(ClaimActivity.this, currentPhotoPath);
                        photo = Util.DecodeFile(currentPhotoPath);
                        break;
                }

                Glide.with(ClaimActivity.this)
                        .asBitmap()
                        .load(photo)
                        .apply(new RequestOptions()
                                .autoClone()
                                .format(DecodeFormat.PREFER_ARGB_8888)
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        )
                        .placeholder(R.drawable.favicon_apag_new)
                        .error(R.drawable.favicon_apag_new)
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                current_first_image = resource;
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                current_first_image.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                byte[] byteArray = stream.toByteArray();

                                @SuppressLint("SimpleDateFormat") String date_capture = new SimpleDateFormat("dd / MM / yyyy").format(new Date());
                                @SuppressLint("SimpleDateFormat") String heure_capture = new SimpleDateFormat("HH:mm:ss").format(new Date());
                                ClaimImage claimImage = new ClaimImage(byteArray);
                                claimImage.setId_claim(claim_listView.getCount() + 1);
                                claimImage.setDate_capture(date_capture);
                                claimImage.setHeure_capture(heure_capture);
                                imageArray.add(claimImage);
                                claimImageAdapter = new ClaimImageAdapter(ClaimActivity.this, imageArray, claim_listView, take_photo_btn, pick_photo_btn, list_size);
                                claim_listView.setAdapter(claimImageAdapter);
                                setListViewHeight(claim_listView);
                                claimImageAdapter.notifyDataSetChanged();
                                media_list_bloc.setVisibility(View.VISIBLE);

                                list_size.setText(String.format(list_size.getText().toString(), String.valueOf(imageArray.size()), String.valueOf(img_count)));
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                                Log.e("img_click", "Glide after 1: onLoadCleared");
                                if (imageArray.size() == img_count) {
                                    take_photo_btn.setEnabled(false);
                                    pick_photo_btn.setEnabled(false);
                                } else {
                                    take_photo_btn.setEnabled(true);
                                    pick_photo_btn.setEnabled(true);
                                }
                            }
                        });
            } catch (Exception e) {
                Log.e("img_click", "onActivityResult Exception:" + e.getMessage());
            }
        }
    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.OGG);
        recorder.setOutputFile(audioFileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.OPUS);
        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e("START RECORDING: ", "prepare() failed");
        }
        recorder.start();
    }

    private void stopRecording() {
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }
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
                play_audio_btn.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.round_button));
                play_audio_btn.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorWood));
                play_audio_btn.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.ic_baseline_pause_24));
                player.setDataSource(audioFileName);
                player.prepare();
                player.setVolume(0.5f, 0.5f);
                player.setLooping(false);
                seekBar.setMax(player.getDuration());
                seekBar_time.setText(Util.convertSeekBarDuration(player.getDuration()));
                player.start();
                new Thread(this).start();
            }
            mStartPlaying = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopPlaying() {
        clearMediaPlayer();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }
        clearMediaPlayer();
        clearThread();
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        stopService(serviceIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }
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
    public void onBackPressed() {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        stopService(serviceIntent);
        gotoMenuActivity();
    }

    private void gotoMenuActivity() {
        if (Util.isMyServiceRunning(getApplicationContext())) {
            Intent service_intent = new Intent(this, PortailWinxoService.class);
            stopService(service_intent);
        }
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();
    }

    private void gotoClaimActivity() {
        Intent intent = new Intent(this, ClaimActivity.class);
        startActivity(intent);
    }

    private void gotoClaimListActivity() {
        Intent intent = new Intent(this, ClaimListActivity.class);
        startActivity(intent);
    }

    public void getAudioRecorded() {
        File directory = new File(filePath);
        File[] files = directory.listFiles();
        Log.e("audio", "filePath: " + filePath);
        audio_list_bloc.setVisibility(View.GONE);
        audio_recorded = false;
        assert files != null;
        if (files.length > 0) {
            Log.e("audio", "files.length: " + files.length);
            for (File file : files) {
                //Log.e("audio", "file: " + file.getName());
                if (file.getName().equals(sessionManager.getId_site() + "_audio." + audio_extension)) {
                    Log.e("audio", "file: " + sessionManager.getId_site() + "_audio." + audio_extension);
                    audio_list_bloc.setVisibility(View.VISIBLE);
                    audio_recorded = true;
                }
            }
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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_presentation) {
            Intent intent = new Intent(getApplicationContext(), PresentationActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_fb) {
            try {
                getApplicationContext().getPackageManager().getPackageInfo("com.facebook.katana", 0);
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/1392305551029582"));
                startActivity(facebookIntent);
            } catch (Exception e) {
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/WinxoOfficiel"));
                startActivity(facebookIntent);
            }
        } else if (id == R.id.nav_website) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.winxo.com/")));
        } else if (id == R.id.nav_mentions_legales) {
            Intent intent = new Intent(getApplicationContext(), LegalMentionActivity.class);
            startActivity(intent);
        }
        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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