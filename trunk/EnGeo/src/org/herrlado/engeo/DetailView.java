package org.herrlado.engeo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

public class DetailView extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_PROGRESS);
		String w = getIntent().getExtras().getString("w");
		String q = getIntent().getExtras().getString("q");
		String content = getIntent().getExtras().getString("content");
		setContentView(R.layout.detail);
		TextView lv = (TextView) findViewById(R.id.detail);
		lv.setText(content);
		WebView webview = (WebView) this.findViewById(R.id.translatege);
		StringBuilder sb = new StringBuilder();
		sb.append("http://translate.ge/").append(q).append(".aspx?w=")
				.append(w);

		// webview.getSettings().setJavaScriptEnabled(true);

		final Activity activity = this;
		webview.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int progress) {
				// Activities and WebViews measure progress with different
				// scales.
				// The progress meter will automatically disappear when we reach
				// 100%
				activity.setProgress(progress * 100);
			}
		});
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Toast.makeText(activity, "Oh no! " + description,
						Toast.LENGTH_SHORT).show();
			}
		});

		webview.loadUrl(sb.toString());
	}

}
