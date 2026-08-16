package com.example.napthesms;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Fullscreen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().setDecorFitsSystemWindows(false);
        }

        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webView);

        WebSettings settings = webView.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);

        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);

        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);

        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);

        settings.setMediaPlaybackRequiresUserGesture(false);

        webView.setWebChromeClient(new WebChromeClient());

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(
                    WebView view,
                    WebResourceRequest request) {

                String url = request.getUrl().toString();

                try {

                    if (url.startsWith("tel:")) {

                        Intent intent =
                                new Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse(url));

                        startActivity(intent);
                        return true;
                    }

                    if (url.startsWith("sms:")) {

                        Intent intent =
                                new Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse(url));

                        startActivity(intent);
                        return true;
                    }

                    if (url.startsWith("http://")
                            || url.startsWith("https://")) {

                        Intent browser =
                                new Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse(url));

                        startActivity(browser);
                        return true;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return false;
            }
        });

        // Load file HTML
        webView.loadUrl(
                "file:///android_asset/index.html"
        );

        // Nút Back
        getOnBackPressedDispatcher().addCallback(
                this,
                new OnBackPressedCallback(true) {

                    @Override
                    public void handleOnBackPressed() {

                        if (webView.canGoBack()) {
                            webView.goBack();
                        } else {
                            finish();
                        }
                    }
                }
        );
    }

    @Override
    protected void onDestroy() {

        if (webView != null) {
            webView.destroy();
        }

        super.onDestroy();
    }
}