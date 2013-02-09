package org.herrlado.engeo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.ScrollingMovementMethod;
import android.view.Window;
import android.webkit.WebView;
import android.widget.TextView;

public class DetailView extends Activity {

	private String url = "http://translate.ge/Main/Translate?text=";
	WebView wbView;
	TextView orig, transcript, name, trans;
	TextView translate;
	ProgressDialog dialog;
	String[] extras;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.detail);

		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(this);

		orig = (TextView) findViewById(R.id.origin);
		transcript = (TextView) findViewById(R.id.transcript);
		name = (TextView) findViewById(R.id.name);
		trans = (TextView) findViewById(R.id.trans);
		translate = (TextView) findViewById(R.id.textView1);
		translate.setMovementMethod(new ScrollingMovementMethod());
		translate.setText("");
		
		Intent sender = getIntent();
		extras = sender.getExtras().getStringArray("extras");

		orig.setText(extras[0]);
		transcript.setText(extras[1]);
		name.setText(extras[2].toString().toLowerCase());
		trans.setText(extras[3].toString());
		
		if (Utils.isGeo(extras[0])) {
			url += Utils.urlEncode(extras[0], "UTF-8") + "&lang=ge&";
		} else {
			url += Utils.urlEncode(extras[0], "UTF-8") + "&lang=en&";
		}

		if (!sharedPref.getBoolean(getString(R.string.use_translatege), false)) {

			return;

		} else {
			
			translate.setText(JSONParser.GetStringFromJson(url));

		}
	}
}
