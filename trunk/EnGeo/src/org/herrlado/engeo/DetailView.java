package org.herrlado.engeo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

		tv = (TextView) findViewById(R.id.detail);
		Intent sender = getIntent();
		extras = sender.getExtras().getCharSequenceArray("extras");

		if (Utils.isGeo(extras[0])) {
			url = geo + Utils.urlEncode(extras[0], "UTF-8");
		} else {
			url = eng + Utils.urlEncode(extras[0], "UTF-8");
		}

		String str = "";
		for (int i = 0; i < extras.length; i++) {
			str += extras[i] + "\n";
		}
		tv.setText(str);
		
		dialog = ProgressDialog.show(this, "Loading", "Loading data from translate.ge...");
		wbView = (WebView) findViewById(R.id.translatege);
		
		wbView.setWebViewClient(new WebViewClient(){
			public boolean shouldOverrideUrlLoading(WebView view, String url){
				view.loadUrl(url);
				return true;
			}
			
			public void onPageFinished(WebView view, String url) {
				if(dialog.isShowing()){
					dialog.dismiss();
				}
			}

		});
		wbView.loadUrl(url);

	}
}
