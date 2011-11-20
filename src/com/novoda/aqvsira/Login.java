package com.novoda.aqvsira;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * https://developer.foursquare.com/docs/oauth.html
 * https://foursquare.com/oauth/
 *
 * @author Mark Wyszomierski (markww@gmail.com)
 * @date May 17, 2011
 */
public class Login extends Activity {
    private static final String TAG = "ActivityWebView";

    /**
     * Get these values after registering your oauth app at: https://foursquare.com/oauth/
     */
    public static final String CALLBACK_URL = "http://novoda.com/aqvira";
    public static final String CLIENT_ID = "XGCO5IRLKDI1NJJ1SDDDQZCWJJMQDLGAETTKSKWY1PKQGHFQ";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        String url =
                "https://foursquare.com/oauth2/authenticate" +
                        "?client_id=" + CLIENT_ID +
                        "&response_type=token" +
                        "&redirect_uri=" + CALLBACK_URL;

        WebView webview = (WebView) findViewById(R.id.web_view);

        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient() {
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                String fragment = "#access_token=";
                int start = url.indexOf(fragment);
                if (start > -1) {
                    // You can use the accessToken for api calls now.
                    String accessToken = url.substring(start + fragment.length(), url.length());
                    PreferenceManager.getDefaultSharedPreferences(Login.this).edit().putString("token", accessToken).commit();
                    Log.v(TAG, "OAuth complete, token: [" + accessToken + "].");
                    Toast.makeText(Login.this, "Logged in!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login.this
                            , ListCheckin.class));
                    finish();
                }
            }
        });
        webview.loadUrl(url);
    }
}