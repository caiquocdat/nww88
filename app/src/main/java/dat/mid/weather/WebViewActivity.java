package dat.mid.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class WebViewActivity extends AppCompatActivity {
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        mapping();
        hideSystemUI();
        setUpWebView();
    }

    private void mapping() {
        webView=findViewById(R.id.webView);
    }

    private void setUpWebView() {


        webView.setWebViewClient(new WebViewClient());

        // Cho phép JavaScript (tuỳ chọn)
        webView.getSettings().setJavaScriptEnabled(true);

        // Đặt URL bạn muốn hiển thị trong WebView
        String url = "http://2xbet.fun/"; // Thay đổi URL này thành trang web bạn muốn hiển thị
        webView.loadUrl(url);
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUI();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}