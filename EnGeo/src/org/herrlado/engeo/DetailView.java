package org.herrlado.engeo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;
import android.widget.TextView;

public class DetailView extends Activity {

	private static final String geo = "http://translate.ge/g.aspx?w=";
	private static final String eng = "http://translate.ge/q.aspx?w=";
	CharSequence[] extras;
	String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.detail);

		Intent sender = getIntent();
		extras = sender.getExtras().getCharSequenceArray("extras");

		if (Utils.isGeo(extras[0])) {
			url = geo + Utils.urlEncode(extras[0], "UTF-8");
		} else {
			url = eng + Utils.urlEncode(extras[0], "UTF-8");
		}

		TextView tv = (TextView) findViewById(R.id.detail);

		String str = "";
		for (int i = 0; i < extras.length; i++) {
			str += extras[i] + "\n";
		}
		tv.setText(str);

		WebView wbView = (WebView) findViewById(R.id.translatege);
		wbView.loadUrl(url);

	}
}
