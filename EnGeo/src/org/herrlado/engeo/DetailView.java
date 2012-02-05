package org.herrlado.engeo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class DetailView extends Activity {

	private static final String geo = "http://translate.ge/g.aspx?w=";
	private static final String eng = "http://translate.ge/q.aspx?w=";
	WebView wbView;
	TextView tv;
	ProgressDialog dialog;
	CharSequence[] extras;
	String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.detail);

		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(this);
		;

		tv = (TextView) findViewById(R.id.detail);
		Intent sender = getIntent();
		extras = sender.getExtras().getCharSequenceArray("extras");

		if (Utils.isGeo(extras[0])) {
			url = geo + Utils.urlEncode(extras[0], "UTF-8");
		} else {
			url = eng + Utils.urlEncode(extras[0], "UTF-8");
		}

		// String str = "";
		// for (int i = 0; i < extras.length; i++) {
		// str += extras[i] + "\n";
		// }
		// tv.setText(str);

		tv.setText(extras[0] + "\n" + "\t" + extras[1] + "\n" + "\t" + "\t"
				+ extras[2].toString().toLowerCase() + "\n"
				+ extras[3].toString().replaceAll(", ", "\n"));

		if (sharedPref.getBoolean("enable_translatege", false) != true) {

			return;

		} else {
			wbView = (WebView) findViewById(R.id.translatege);
			dialog = ProgressDialog.show(this, "Loading",
					"Loading data from translate.ge...", true, true);
			wbView.setWebViewClient(new WebViewClient() {
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					view.loadUrl(url);
					return true;
				}

				@Override
				public void onPageFinished(WebView view, String url) {
					if (dialog.isShowing()) {
						dialog.dismiss();
					}
				}

			});

			wbView.loadUrl(url);
		}

	}
}
