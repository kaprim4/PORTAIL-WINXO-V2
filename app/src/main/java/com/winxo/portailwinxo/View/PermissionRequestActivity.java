package com.winxo.portailwinxo.View;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.winxo.portailwinxo.Model.SecurePreferences;
import com.winxo.portailwinxo.R;

@RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
public class PermissionRequestActivity extends AppCompatActivity {
    private LinearLayout storage_32, storage_33;
    private ImageView RecordAudio_denied, RecordAudio_granted;
    private ImageView Read_denied, Read_granted, Read_image_denied, Read_image_granted, Read_audio_denied, Read_audio_granted;
    private ImageView Camera_denied, Camera_granted;
    private ImageView FineLocation_denied, FineLocation_granted;
    private ImageView CallPhone_denied, CallPhone_granted;
    private Button check_all_btn;
    private boolean isRecordAudioPermissionGranted = false;
    private boolean isReadPermissionGranted = false;
    private boolean isReadImagePermissionGranted = false;
    private boolean isReadAudioPermissionGranted = false;
    private boolean isCameraPermissionGranted = false;
    private boolean isFineLocationPermissionGranted = false;
    private boolean isCallPhonePermissionGranted = false;
    private boolean isAllPermissionRequested = false;
    private SecurePreferences preferences;

    private final String TAG = "Permissions_handle";
    private String spref_permissions_handle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_request);

        initialiseVars();

        preferences = new SecurePreferences(getApplicationContext(), true);
        spref_permissions_handle = preferences.getString("permissions_handle");
        if (spref_permissions_handle == null) {
            preferences.put("permissions_handle", "0");
            spref_permissions_handle = preferences.getString("permissions_handle");
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            storage_32.setVisibility(View.VISIBLE);
            storage_33.setVisibility(View.GONE);
        } else {
            storage_32.setVisibility(View.GONE);
            storage_33.setVisibility(View.VISIBLE);
        }

        check_all_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAllPermissionRequested){
                    //Toast.makeText(PermissionRequestActivity.this, "Granted all permissions...", Toast.LENGTH_SHORT).show();
                    preferences.put("permissions_handle", "1");
                    gotoLoginActivity();
                }else {
                    preferences.put("permissions_handle", "0");
                    requestPermissions();
                }
            }
        });

        requestPermissions();
    }

    private void requestPermissions() {
        if (!isRecordAudioPermissionGranted) {
            recordAudioRequestPermissions();
        }
    }

    private void initialiseVars() {
        storage_32 = findViewById(R.id.storage_32);
        storage_33 = findViewById(R.id.storage_33);
        RecordAudio_denied = findViewById(R.id.RecordAudio_denied);
        RecordAudio_granted = findViewById(R.id.RecordAudio_granted);
        Read_denied = findViewById(R.id.Read_denied);
        Read_granted = findViewById(R.id.Read_granted);
        Read_image_denied = findViewById(R.id.Read_image_denied);
        Read_image_granted = findViewById(R.id.Read_image_granted);
        Read_audio_denied = findViewById(R.id.Read_audio_denied);
        Read_audio_granted = findViewById(R.id.Read_audio_granted);
        Camera_denied = findViewById(R.id.Camera_denied);
        Camera_granted = findViewById(R.id.Camera_granted);
        FineLocation_denied = findViewById(R.id.FineLocation_denied);
        FineLocation_granted = findViewById(R.id.FineLocation_granted);
        CallPhone_denied = findViewById(R.id.CallPhone_denied);
        CallPhone_granted = findViewById(R.id.CallPhone_granted);
        check_all_btn = findViewById(R.id.check_all_btn);
    }

    private void recordAudioRequestPermissions() {
        if (ContextCompat.checkSelfPermission(PermissionRequestActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, Manifest.permission.RECORD_AUDIO + " granted.");
            isRecordAudioPermissionGranted = true;
            RecordAudio_denied.setVisibility(View.GONE);
            RecordAudio_granted.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                if (!isReadPermissionGranted) {
                    readRequestPermissions();
                }
            } else {
                if (!isReadImagePermissionGranted) {
                    readImageRequestPermissions();
                }
            }
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                Log.e(TAG, Manifest.permission.RECORD_AUDIO + " 2nd time don't allows handle here.");
            } else {
                Log.e(TAG, Manifest.permission.RECORD_AUDIO + " don't allows handle here.");
            }
            request_permission_luncher_record_audio.launch(Manifest.permission.RECORD_AUDIO);
        }
    }

    private void readRequestPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(PermissionRequestActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, Manifest.permission.READ_EXTERNAL_STORAGE + " granted.");
                isReadPermissionGranted = true;
                Read_denied.setVisibility(View.GONE);
                Read_granted.setVisibility(View.VISIBLE);
                if (!isCameraPermissionGranted) {
                    cameraRequestPermissions();
                }
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                    Log.e(TAG, Manifest.permission.READ_EXTERNAL_STORAGE + " 2nd time don't allows handle here.");
                } else {
                    Log.e(TAG, Manifest.permission.READ_EXTERNAL_STORAGE + " don't allows handle here.");
                }
                request_permission_luncher_read.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
    }

    private void readImageRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(PermissionRequestActivity.this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, Manifest.permission.READ_MEDIA_IMAGES + " granted.");
                isReadImagePermissionGranted = true;
                Read_image_denied.setVisibility(View.GONE);
                Read_image_granted.setVisibility(View.VISIBLE);
                if (!isReadAudioPermissionGranted) {
                    readAudioRequestPermissions();
                }
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                    Log.e(TAG, Manifest.permission.READ_MEDIA_IMAGES + " 2nd time don't allows handle here.");
                } else {
                    Log.e(TAG, Manifest.permission.READ_MEDIA_IMAGES + " don't allows handle here.");
                }
                request_permission_luncher_read_image.launch(Manifest.permission.READ_MEDIA_IMAGES);
            }
        }
    }

    private void readAudioRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(PermissionRequestActivity.this, Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, Manifest.permission.READ_MEDIA_AUDIO + " granted.");
                isReadAudioPermissionGranted = true;
                Read_audio_denied.setVisibility(View.GONE);
                Read_audio_granted.setVisibility(View.VISIBLE);
                if (!isCameraPermissionGranted) {
                    cameraRequestPermissions();
                }
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                    Log.e(TAG, Manifest.permission.READ_MEDIA_AUDIO + " 2nd time don't allows handle here.");
                } else {
                    Log.e(TAG, Manifest.permission.READ_MEDIA_AUDIO + " don't allows handle here.");
                }
                request_permission_luncher_read_audio.launch(Manifest.permission.READ_MEDIA_AUDIO);
            }
        }
    }

    private void cameraRequestPermissions() {
        if (ContextCompat.checkSelfPermission(PermissionRequestActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, Manifest.permission.CAMERA + " granted.");
            isCameraPermissionGranted = true;
            Camera_denied.setVisibility(View.GONE);
            Camera_granted.setVisibility(View.VISIBLE);
            if (!isFineLocationPermissionGranted) {
                locationRequestPermissions();
            }
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                Log.e(TAG, Manifest.permission.CAMERA + " 2nd time don't allows handle here.");
            } else {
                Log.e(TAG, Manifest.permission.CAMERA + " don't allows handle here.");
            }
            request_permission_luncher_camera.launch(Manifest.permission.CAMERA);
        }
    }

    private void locationRequestPermissions() {
        if (ContextCompat.checkSelfPermission(PermissionRequestActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, Manifest.permission.ACCESS_FINE_LOCATION + " granted.");
            isFineLocationPermissionGranted = true;
            FineLocation_denied.setVisibility(View.GONE);
            FineLocation_granted.setVisibility(View.VISIBLE);
            if (!isCallPhonePermissionGranted) {
                callPhoneRequestPermissions();
            }
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                Log.e(TAG, Manifest.permission.ACCESS_FINE_LOCATION + " 2nd time don't allows handle here.");
            } else {
                Log.e(TAG, Manifest.permission.ACCESS_FINE_LOCATION + " don't allows handle here.");
            }
            request_permission_luncher_location.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void callPhoneRequestPermissions() {
        if (ContextCompat.checkSelfPermission(PermissionRequestActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, Manifest.permission.CALL_PHONE + " granted.");
            isCallPhonePermissionGranted = true;
            CallPhone_denied.setVisibility(View.GONE);
            CallPhone_granted.setVisibility(View.VISIBLE);
            allCheckPermissionsFinalCall();
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                Log.e(TAG, Manifest.permission.CALL_PHONE + " 2nd time don't allows handle here.");
            } else {
                Log.e(TAG, Manifest.permission.CALL_PHONE + " don't allows handle here.");
            }
            request_permission_luncher_call_phone.launch(Manifest.permission.CALL_PHONE);
        }
    }

    private final ActivityResultLauncher<String> request_permission_luncher_record_audio = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            Log.e(TAG, Manifest.permission.RECORD_AUDIO + " allowed.");
            isRecordAudioPermissionGranted = true;
            RecordAudio_denied.setVisibility(View.GONE);
            RecordAudio_granted.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                if (!isReadPermissionGranted) {
                    readRequestPermissions();
                }
            } else {
                if (!isReadImagePermissionGranted) {
                    readImageRequestPermissions();
                }
            }
        } else {
            Log.e(TAG, Manifest.permission.RECORD_AUDIO + " not allowed.");
            isRecordAudioPermissionGranted = false;
            RecordAudio_denied.setVisibility(View.VISIBLE);
            RecordAudio_granted.setVisibility(View.GONE);
        }
    });

    private final ActivityResultLauncher<String> request_permission_luncher_read = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            if (isGranted) {
                Log.e(TAG, Manifest.permission.READ_EXTERNAL_STORAGE + " allowed.");
                isReadPermissionGranted = true;
                Read_denied.setVisibility(View.GONE);
                Read_granted.setVisibility(View.VISIBLE);
                if (!isCameraPermissionGranted) {
                    cameraRequestPermissions();
                }
            } else {
                Log.e(TAG, Manifest.permission.READ_EXTERNAL_STORAGE + " not allowed.");
                isReadPermissionGranted = false;
                Read_denied.setVisibility(View.VISIBLE);
                Read_granted.setVisibility(View.GONE);
            }
        }
    });

    private final ActivityResultLauncher<String> request_permission_luncher_read_image = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (isGranted) {
                Log.e(TAG, Manifest.permission.READ_MEDIA_IMAGES + " allowed.");
                isReadImagePermissionGranted = true;
                Read_image_denied.setVisibility(View.GONE);
                Read_image_granted.setVisibility(View.VISIBLE);
                if (!isReadAudioPermissionGranted) {
                    readAudioRequestPermissions();
                }
            } else {
                Log.e(TAG, Manifest.permission.READ_MEDIA_IMAGES + " not allowed.");
                isReadImagePermissionGranted = false;
                Read_image_denied.setVisibility(View.VISIBLE);
                Read_image_granted.setVisibility(View.GONE);
            }
        }
    });

    private final ActivityResultLauncher<String> request_permission_luncher_read_audio = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (isGranted) {
                Log.e(TAG, Manifest.permission.READ_MEDIA_AUDIO + " allowed.");
                isReadAudioPermissionGranted = true;
                Read_audio_denied.setVisibility(View.GONE);
                Read_audio_granted.setVisibility(View.VISIBLE);
                if (!isCameraPermissionGranted) {
                    cameraRequestPermissions();
                }
            } else {
                Log.e(TAG, Manifest.permission.READ_MEDIA_AUDIO + " not allowed.");
                isReadAudioPermissionGranted = false;
                Read_audio_denied.setVisibility(View.VISIBLE);
                Read_audio_granted.setVisibility(View.GONE);
            }
        }
    });

    private final ActivityResultLauncher<String> request_permission_luncher_camera = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            Log.e(TAG, Manifest.permission.CAMERA + " allowed.");
            isCameraPermissionGranted = true;
            Camera_denied.setVisibility(View.GONE);
            Camera_granted.setVisibility(View.VISIBLE);
            if (!isFineLocationPermissionGranted) {
                locationRequestPermissions();
            }
        } else {
            Log.e(TAG, Manifest.permission.CAMERA + " not allowed.");
            isCameraPermissionGranted = false;
            Camera_denied.setVisibility(View.VISIBLE);
            Camera_granted.setVisibility(View.GONE);
        }
    });

    private final ActivityResultLauncher<String> request_permission_luncher_location = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            Log.e(TAG, Manifest.permission.ACCESS_FINE_LOCATION + " allowed.");
            isFineLocationPermissionGranted = true;
            FineLocation_denied.setVisibility(View.GONE);
            FineLocation_granted.setVisibility(View.VISIBLE);
            if (!isCallPhonePermissionGranted) {
                callPhoneRequestPermissions();
            }
        } else {
            Log.e(TAG, Manifest.permission.ACCESS_FINE_LOCATION + " not allowed.");
            isFineLocationPermissionGranted = false;
            FineLocation_denied.setVisibility(View.VISIBLE);
            FineLocation_granted.setVisibility(View.GONE);
        }
    });

    private final ActivityResultLauncher<String> request_permission_luncher_call_phone = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            Log.e(TAG, Manifest.permission.CALL_PHONE + " allowed.");
            isCallPhonePermissionGranted = true;
            CallPhone_denied.setVisibility(View.GONE);
            CallPhone_granted.setVisibility(View.VISIBLE);
            allCheckPermissionsFinalCall();
        } else {
            Log.e(TAG, Manifest.permission.CALL_PHONE + " not allowed.");
            isCallPhonePermissionGranted = false;
            CallPhone_denied.setVisibility(View.VISIBLE);
            CallPhone_granted.setVisibility(View.GONE);
        }
    });

    private boolean allPermissionResultChecked() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            return isRecordAudioPermissionGranted && isReadImagePermissionGranted && isReadAudioPermissionGranted && isCameraPermissionGranted && isFineLocationPermissionGranted && isCallPhonePermissionGranted;
        else
            return isRecordAudioPermissionGranted && isReadPermissionGranted && isCameraPermissionGranted && isFineLocationPermissionGranted && isCallPhonePermissionGranted;
    }

    private void allCheckPermissionsFinalCall() {
        isAllPermissionRequested = true;
        if (allPermissionResultChecked()) {
            //Toast.makeText(this, "Granted all permissions...", Toast.LENGTH_SHORT).show();
            preferences.put("permissions_handle", "1");
            gotoLoginActivity();
        } else {
            sendToSettingDialog();
            preferences.put("permissions_handle", "0");
        }
    }

    private void sendToSettingDialog() {
        new AlertDialog.Builder(PermissionRequestActivity.this)
                .setTitle("Alerte pour autorisation")
                .setMessage("Accéder aux paramètres d'autorisation")
                .setPositiveButton("Paramètre", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        dialog.dismiss();
                    }
                }).show();
    }

    private void gotoLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}