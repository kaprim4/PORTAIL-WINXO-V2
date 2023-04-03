package com.winxo.portailwinxo.Utilities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.FileProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.winxo.portailwinxo.Model.Parameters;
import com.winxo.portailwinxo.Model.SessionManager;
import com.winxo.portailwinxo.Model.Site;
import com.winxo.portailwinxo.Model.Statistic;
import com.winxo.portailwinxo.Model.VolleySingleton;
import com.winxo.portailwinxo.R;
import com.winxo.portailwinxo.Requests.MainRequest;
import com.winxo.portailwinxo.Requests.PriceRequest;
import com.winxo.portailwinxo.Service.PortailWinxoService;

import net.gotev.uploadservice.data.UploadNotificationConfig;
import net.gotev.uploadservice.data.UploadNotificationStatusConfig;
import net.gotev.uploadservice.protocols.multipart.MultipartUploadRequest;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Util {
    private static int screenWidth = 0;
    private static int screenHeight = 0;
    public static Site mainUser;

    @SuppressLint("DefaultLocale")
    public static String convertDuration(long duration) {
        long d = duration / 1000;
        long minutes = d / 60;
        long secondes = d % 60;
        return String.format("%d mins, %d secs", minutes, secondes);
    }

    @SuppressLint("DefaultLocale")
    public static String convertSeekBarDuration(long duration) {
        long d = duration / 1000;
        long minutes = d / 60;
        long secondes = d % 60;
        return String.format("%d:%02d", minutes, secondes);
    }

    @SuppressLint("DefaultLocale")
    public static String printDiffDates(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        return String.format("%d Hrs, %d mins, %d secs", elapsedHours, elapsedMinutes, elapsedSeconds);
    }

    public static String getDate(long millisecond) {
        return DateFormat.format("dd/MM/yyyy", new Date(millisecond * 1000)).toString();
    }

    public static String formatDate(String dateString) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            return fmt.format(fmt.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int getScreenHeight(Context context) {
        if (screenHeight == 0) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenHeight = size.y;
        }
        return screenHeight;
    }

    public static int getScreenWidth(Context context) {
        if (screenWidth == 0) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }
        return screenWidth;
    }

    public static boolean isNetworkAvailable(Context context) {
        boolean status = false;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        if (is3g) {
            status = true;
        } else if (isWifi) {
            status = true;
        }
        return status;
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public static void collapseToolbar(CoordinatorLayout content, AppBarLayout appBarLayout) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        if (behavior != null) {
            behavior.onNestedFling(content, appBarLayout, null, 0, 10000, true);
        }
    }

    public static void expandToolbar(CoordinatorLayout content, AppBarLayout appBarLayout) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        if (behavior != null) {
            behavior.setTopAndBottomOffset(0);
            behavior.onNestedPreScroll(content, appBarLayout, null, 0, 1, new int[2]);
        }
    }

    public static String getSubString(String mainString, String lastString, String startString) {
        String endString = "";
        int endIndex = mainString.indexOf(lastString);
        int startIndex = mainString.indexOf(startString);
        endString = mainString.substring(startIndex, endIndex);
        return endString;
    }

    public static void updateParametersFromDB(Context context) {
        SessionManager sessionManager = new SessionManager(context);
        RequestQueue queue = VolleySingleton.getInstance(context).getRequestQueue();
        MainRequest mainRequest = new MainRequest(context, queue);
        mainRequest.getParameterList(new MainRequest.ParametersCallback() {
            @Override
            public void onSuccess(float PRICE_MIN_SSP, float PRICE_MAX_SSP, float PRICE_MIN_GASOIL, float PRICE_MAX_GASOIL, float PRICE_MIN_2T, float PRICE_MAX_2T, float PRICE_STEP, int INPUT_INTO_DAY, int MONTH_LIMIT_FOR_LISTING, int INTERVAL_BY_SECS, int ADD_COUNT_PER_DAY, int ADD_INTERVAL_PER_DAY, int LOGIN_ATTEMPT, boolean SEND_MAIL, boolean SEND_SMS, String PW_CHECKSUM, boolean AVIS_MAINTENANCE, boolean MAINTENANCE, boolean ENABLE_APAG, boolean ENABLE_ORDERS, boolean ENABLE_CLAIM, int ORDER_ADD_COUNT_PER_DAY, String ORDER_SUSPEND_DAY_FROM, String ORDER_SUSPEND_DAY_TO, String ORDER_ADD_TIME_FROM, String ORDER_ADD_TIME_TO, String ORDER_CANCEL_DELAY, int ORDER_DATE_SHIP_DAY_UPON, int ORDER_CAR_MIN, int ORDER_CAR_MAX, String ORDER_CAR_UNIT) {
                Parameters parameters = new Parameters(PRICE_MIN_SSP, PRICE_MAX_SSP, PRICE_MIN_GASOIL, PRICE_MAX_GASOIL, PRICE_MIN_2T, PRICE_MAX_2T, PRICE_STEP, INPUT_INTO_DAY, MONTH_LIMIT_FOR_LISTING, INTERVAL_BY_SECS, ADD_COUNT_PER_DAY, ADD_INTERVAL_PER_DAY, LOGIN_ATTEMPT, SEND_MAIL, SEND_SMS, PW_CHECKSUM, AVIS_MAINTENANCE, MAINTENANCE, ENABLE_APAG, ENABLE_ORDERS, ENABLE_CLAIM, ORDER_ADD_COUNT_PER_DAY, ORDER_SUSPEND_DAY_FROM, ORDER_SUSPEND_DAY_TO, ORDER_ADD_TIME_FROM, ORDER_ADD_TIME_TO, ORDER_CANCEL_DELAY, ORDER_DATE_SHIP_DAY_UPON, ORDER_CAR_MIN, ORDER_CAR_MAX, ORDER_CAR_UNIT);
                sessionManager.updateParams(parameters);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(context, "Erreur: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void updateSiteInfo(Context context) {
        SessionManager sessionManager = new SessionManager(context);
        RequestQueue queue = VolleySingleton.getInstance(context).getRequestQueue();
        MainRequest mainRequest = new MainRequest(context, queue);
        mainRequest.getSiteByUserName(sessionManager.getUsername(), new MainRequest.SiteCallback() {
            @Override
            public void onSuccess(String _id_site, String _id_profile, String _id_animateur, String _id_animateur_site, String _superviseur_name, String _display_name, String _nb_totale, String _nb_active, String _nb_standby, String _code_sap, String _username, String _password, String _libelle, String _tel, String _email, String _id_city, String _id_company, String _address_ip, String _imei, String _GradeId_list, String _date_upd, String _status, Boolean _hasFusion, String _prix_saisis, String _prix_appliques, String _prix_annules) {
                sessionManager.updateSiteInformations(_code_sap, _username, _display_name, _id_city, _hasFusion);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(context, "Mise à jour impossible.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void updateStatisticsFromDB(Activity _activity, Context context, String id_site) {
        SessionManager sessionManager = new SessionManager(_activity.getApplicationContext());
        RequestQueue queue = VolleySingleton.getInstance(_activity.getApplicationContext()).getRequestQueue();
        PriceRequest priceRequest = new PriceRequest(_activity, context, queue);
        priceRequest.getPriceStatistics(id_site, new PriceRequest.PriceStatisticsCallback() {
            @Override
            public void onSuccess(String prix_saisis, String prix_appliques, String prix_annules) {
                Statistic statistic = new Statistic(prix_saisis, prix_appliques, prix_annules);
                sessionManager.updateCounter(statistic);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(_activity.getApplicationContext(), "Erreur: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static String getGradeName(int gradeId) {
        String retreived_val = "";
        switch (gradeId) {
            case 1:
                retreived_val = "SSP";
                break;
            case 2:
                retreived_val = "GAS";
                break;
            case 3:
                retreived_val = "HUILE";
                break;
            case 4:
                retreived_val = "Mix 0";
                break;
            case 5:
                retreived_val = "Mix 4%";
                break;
            case 6:
                retreived_val = "Mix 5%";
                break;
            case 7:
                retreived_val = "Mix 6%";
                break;
            case 8:
                retreived_val = "Mix 7%";
                break;
            case 9:
                retreived_val = "Mix 2%";
                break;
            case 10:
                retreived_val = "Mix 3%";
                break;
            case 11:
                retreived_val = "Mix 8%";
                break;
        }
        return retreived_val;
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String getHttpResponseCode(int code) {
        String text = "";
        switch (code) {
            case 100:
                text = "Continue";
                break;
            case 101:
                text = "Switching Protocols";
                break;
            case 200:
                text = "OK";
                break;
            case 201:
                text = "Created";
                break;
            case 202:
                text = "Accepted";
                break;
            case 203:
                text = "Non-Authoritative Information";
                break;
            case 204:
                text = "No Content";
                break;
            case 205:
                text = "Reset Content";
                break;
            case 206:
                text = "Partial Content";
                break;
            case 300:
                text = "Multiple Choices";
                break;
            case 301:
                text = "Moved Permanently";
                break;
            case 302:
                text = "Moved Temporarily";
                break;
            case 303:
                text = "See Other";
                break;
            case 304:
                text = "Not Modified";
                break;
            case 305:
                text = "Use Proxy";
                break;
            case 400:
                text = "Bad MainRequest";
                break;
            case 401:
                text = "Unauthorized";
                break;
            case 402:
                text = "Payment Required";
                break;
            case 403:
                text = "Forbidden";
                break;
            case 404:
                text = "Not Found";
                break;
            case 405:
                text = "Method Not Allowed";
                break;
            case 406:
                text = "Not Acceptable";
                break;
            case 407:
                text = "Proxy Authentication Required";
                break;
            case 408:
                text = "MainRequest Time-out";
                break;
            case 409:
                text = "Conflict";
                break;
            case 410:
                text = "Gone";
                break;
            case 411:
                text = "Length Required";
                break;
            case 412:
                text = "Precondition Failed";
                break;
            case 413:
                text = "MainRequest Entity Too Large";
                break;
            case 414:
                text = "MainRequest-URI Too Large";
                break;
            case 415:
                text = "Unsupported Media Type";
                break;
            case 500:
                text = "Internal Server Error";
                break;
            case 501:
                text = "Not Implemented";
                break;
            case 502:
                text = "Bad Gateway";
                break;
            case 503:
                text = "Service Unavailable";
                break;
            case 504:
                text = "Gateway Time-out";
                break;
            case 505:
                text = "HTTP Version not supported";
                break;
            case 700:
                text = "One or more parameters are missing";
                break;
        }
        return text;
    }

    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static File createImageFile(Activity _mActivity, SessionManager sessionManager) throws IOException {
        // Create an image file name
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = sessionManager.getId_site() + "_" + timeStamp + "_";
        File storageDir = _mActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }

    public static Uri createImageUri(Activity _mActivity, String photo_name) {
        return FileProvider.getUriForFile(_mActivity.getApplicationContext(), _mActivity.getApplicationContext().getPackageName() + ".fileprovider", new File(Environment.getExternalStorageDirectory(), photo_name));
    }

    public static Bitmap DecodeFile(String currentPhotoPath) {
        int targetW = 1024;
        int targetH = 576;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        // TODO: 2/25/2019 Update to BitmapFactory.decodeStream()
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        int scaleFactor = 1;
        scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;
        return BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
    }

    public static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > ratioBitmap) {
                finalWidth = (int) ((float) maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float) maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }

    public static void galleryAddPic(Activity _mActivity, String currentPhotoPath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        _mActivity.sendBroadcast(mediaScanIntent);
    }

    public static byte[] convertBitmapToLongblob(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            return stream.toByteArray();
        }
        return null;
    }

    public static String convertLongblobToString(byte[] longblob) {
        if (longblob != null) {
            return Base64.encodeToString(longblob, Base64.DEFAULT);
        }
        return null;
    }

    public static void addVibration(Activity _mActivity, int e) {
        Vibrator v = (Vibrator) _mActivity.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(e);
    }

    public static void setBadge(BottomNavigationView b_nav, int menu_id, int nbr) {
        BadgeDrawable badge = b_nav.getOrCreateBadge(menu_id);
        badge.setVisible(true);
        badge.setNumber(nbr);
    }

    public static void uploadImageToDB(Context context, String page, Bitmap imageBitmap, String[] fields, String[] values) {
        String requestURL = Constants.WEB_SERVICE_URL + page;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, requestURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("uploadImageToDB", "onResponse: " + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int responseCode = jsonObject.getInt("responseCode");
                    String response = jsonObject.getString("response");
                    if (responseCode == 1) {
                        Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                        //progressDialog.dismiss();
                    } else {
                        Toast.makeText(context, "Erreur: " + response, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    Toast.makeText(context, "1. Impossible de procéder à l'upload.", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("uploadImageToDB", "onErrorResponse: " + volleyError.getMessage());
                try {
                    JSONObject jsonObject = new JSONObject(Objects.requireNonNull(volleyError.getMessage()));
                    int responseCode = jsonObject.getInt("responseCode");
                    String response = jsonObject.getString("response");
                    if (responseCode == 1) {
                        Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                        //progressDialog.dismiss();
                    } else {
                        Toast.makeText(context, "Erreur: " + response, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    Toast.makeText(context, "2. Impossible de procéder à l'upload.", Toast.LENGTH_LONG).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String image = toBase64(imageBitmap);
                Map<String, String> params = new Hashtable<>();
                params.put("binary_image", image);
                Log.e("uploadImageToDB", "binary_image: " + image);
                if (fields.length > 0) {
                    for (int i = 0; i < fields.length; i++) {
                        params.put(fields[i], values[i]);
                        Log.e("uploadImageToDB", fields[i] + ": " + values[i]);
                    }
                }
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public static void uploadAudioToDB(Context _context, String id_claim, String audioFileName) {
        String upload_url = Constants.WEB_SERVICE_URL + "UploadAudioToServer.php";
        try {
            String uploadId = UUID.randomUUID().toString();
            MultipartUploadRequest uploadRequest = new MultipartUploadRequest(_context, upload_url)
                    .setUploadID(uploadId)
                    .setMethod("POST")
                    .addFileToUpload(audioFileName, "upload")
                    .addParameter("id_claim", id_claim)
                    .setMaxRetries(3);
            @SuppressLint("ResourceAsColor")
            UploadNotificationConfig notificationConfig = new UploadNotificationConfig(
                    Constants.NOTIFICATION_CHANNEL_ID,
                    true,
                    new UploadNotificationStatusConfig(Constants.UPLOAD_CHANNEL_ID, "L'opération est en cours...", R.drawable.ic_paper_plane, R.color.colorBlue),
                    new UploadNotificationStatusConfig(Constants.UPLOAD_CHANNEL_ID, "La photo a été mise à jour.", R.drawable.ic_check_circle, R.color.colorGreen),
                    new UploadNotificationStatusConfig(Constants.UPLOAD_CHANNEL_ID, "Une erreur s'est produite lors de l'upload.", R.drawable.ic_ban, R.color.colorRed),
                    new UploadNotificationStatusConfig(Constants.UPLOAD_CHANNEL_ID, "Votre opération vient d'être annullée.", R.drawable.ic_baseline_history_24, R.color.colorGold));
            uploadRequest.setNotificationConfig((context, s) -> notificationConfig);
            uploadRequest.startUpload();
        } catch (Exception ex) {
            Log.e("uploadAudioToDB", "AppError: " + ex.getMessage());
        }
    }

    public static String toBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static boolean isMyServiceRunning(Context _context) {
        ActivityManager manager = (ActivityManager) _context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (PortailWinxoService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    public static void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    public static void dumpEvent(MotionEvent event) {
        String[] names = {"DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE", "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?"};
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_").append(names[actionCode]);
        if (actionCode == MotionEvent.ACTION_POINTER_DOWN || actionCode == MotionEvent.ACTION_POINTER_UP) {
            sb.append("(pid ").append(action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
            sb.append(")");
        }
        sb.append("[");
        for (int i = 0; i < event.getPointerCount(); i++) {
            sb.append("#").append(i);
            sb.append("(pid ").append(event.getPointerId(i));
            sb.append(")=").append((int) event.getX(i));
            sb.append(",").append((int) event.getY(i));
            if (i + 1 < event.getPointerCount())
                sb.append(";");
        }
        sb.append("]");
        Log.d("Touch Events ---------", sb.toString());
    }

    /****************************************** SHOW POPUPS *****************************************/
    /************************************************************************************************/
    public static void correctDialog(Activity _a, String txt) {
        final ViewGroup viewGroup = _a.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(_a).inflate(R.layout.dialog_positive, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(_a);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        TextView text_popup = dialogView.findViewById(R.id.text_popup);
        text_popup.setText(txt);
        alertDialog.show();
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
            }
        });
    }

    public static void errorDialog(Activity activity, String txt) {
        final ViewGroup viewGroup = activity.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_nagative, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        TextView dialog_desc = dialogView.findViewById(R.id.text_popup);
        dialog_desc.setText(txt);
        alertDialog.show();
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
            }
        });
    }

    public static void displayPopupAndGoTo(Activity activity, String txt, int counter, boolean redirect, Class<?> cls) {
        final ViewGroup viewGroup = activity.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_positive, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        TextView text_popup = dialogView.findViewById(R.id.text_popup);
        TextView text_counter = dialogView.findViewById(R.id.text_counter);
        text_popup.setText(txt);
        alertDialog.show();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        blocUserEventClick(activity);
       /* new CountDownTimer(counter, 1000) {
            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                text_counter.setText("Basculement en cours...\n" + (millisUntilFinished / 1000) + " (sec)");
            }

            public void onFinish() {
                alertDialog.dismiss();
                unblocUserEventClick(activity);
                if(redirect) {
                    gotoActivity(activity, cls);
                }
            }
        }.start();*/
    }

    private static void gotoActivity(Activity activity, Class<?> cls) {
        Intent intent = new Intent(activity.getApplicationContext(), cls);
        activity.startActivity(intent);
    }

    private static void blocUserEventClick(Activity activity) {
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private static void unblocUserEventClick(Activity activity) {
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sbuf = new StringBuilder();
        for (byte aByte : bytes) {
            int intVal = aByte & 0xff;
            if (intVal < 0x10) sbuf.append("0");
            sbuf.append(Integer.toHexString(intVal).toUpperCase());
        }
        return sbuf.toString();
    }

    public static byte[] getUTF8Bytes(String str) {
        try {
            return str.getBytes(StandardCharsets.UTF_8);
        } catch (Exception ex) {
            return null;
        }
    }

    public static String loadFileAsString(String filename) throws IOException {
        final int BUFLEN = 1024;
        try (BufferedInputStream is = new BufferedInputStream(new FileInputStream(filename), BUFLEN)) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFLEN);
            byte[] bytes = new byte[BUFLEN];
            boolean isUTF8 = false;
            int read, count = 0;
            while ((read = is.read(bytes)) != -1) {
                if (count == 0 && bytes[0] == (byte) 0xEF && bytes[1] == (byte) 0xBB && bytes[2] == (byte) 0xBF) {
                    isUTF8 = true;
                    baos.write(bytes, 3, read - 3); // drop UTF8 bom marker
                } else {
                    baos.write(bytes, 0, read);
                }
                count += read;
            }
            return isUTF8 ? baos.toString("UTF-8") : baos.toString();
        }
    }

    public static String getMACAddress(String interfaceName) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac == null) return "";
                StringBuilder buf = new StringBuilder();
                for (byte aMac : mac) buf.append(String.format("%02X:", aMac));
                if (buf.length() > 0) buf.deleteCharAt(buf.length() - 1);
                return buf.toString();
            }
        } catch (Exception ignored) {

        } // for now eat exceptions
        return "";
        /*try {
            // this is so Linux hack
            return loadFileAsString("/sys/class/net/" +interfaceName + "/address").toUpperCase().trim();
        } catch (IOException ex) {
            return null;
        }*/
    }

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':') < 0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        } // for now eat exceptions
        return "";
    }

    public static String getErrorMessage(Context context, VolleyError error) {
        try {
            if (error instanceof NoConnectionError) {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = null;
                if (cm != null) {
                    activeNetwork = cm.getActiveNetworkInfo();
                }
                if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
                    return "Le serveur n'est pas connecté à Internet.";
                } else {
                    return "Votre appareil n'est pas connecté à Internet.";
                }
            } else if (error instanceof NetworkError
                    || error.getCause() instanceof ConnectException
                    || error.getCause().getMessage() != null
                    && error.getCause().getMessage().contains("connection")) {
                return "Votre appareil n'est pas connecté à Internet.";
            } else if (error.getCause() instanceof MalformedURLException) {
                return "Mauvaise demande.";
            } else if (error instanceof ParseError
                    || error.getCause() instanceof IllegalStateException
                    || error.getCause() instanceof JSONException
                    || error.getCause() instanceof XmlPullParserException) {
                return "Erreur d'analyse (à cause d'un fichier json ou xml non valide).";
            } else if (error.getCause() instanceof OutOfMemoryError) {
                return "Erreur de mémoire insuffisante.";
            } else if (error instanceof AuthFailureError) {
                return "le serveur n'a pas pu trouver la demande authentifiée.";
            } else if (error instanceof ServerError || error.getCause() instanceof ServerError) {
                return "Le serveur ne répond pas.";
            } else if (error instanceof TimeoutError
                    || error.getCause() instanceof SocketTimeoutException
                    || error.getCause() instanceof ConnectTimeoutException
                    || error.getCause() instanceof SocketException
                    || (error.getCause().getMessage() != null
                    && error.getCause().getMessage().contains("Connection timed out"))) {
                return "Erreur de délai de connexion";
            } else {
                return "Une erreur inconnue est survenue.";
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public static long getDateDiff(SimpleDateFormat format, String oldDate, String newDate) {
        try {
            return TimeUnit.MINUTES.convert(format.parse(newDate).getTime() - format.parse(oldDate).getTime(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}