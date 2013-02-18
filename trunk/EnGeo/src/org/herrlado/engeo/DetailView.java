package org.herrlado.engeo;

import org.json.JSONArray;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.ScrollingMovementMethod;
import android.view.Window;
import android.widget.TextView;

public class DetailView extends Activity {

	private String url = "http://translate.ge/Main/Translate?text=";
	private	TextView orig, transcript, name, trans;
	private TextView translate;
	private ProgressDialog dialog;
	private String[] extras;

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
		//		translate.setText("");

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

			new AsyncTask<Void, Void, JSONArray>() {

				@Override
				protected void onPreExecute() {
					dialog = ProgressDialog.show(DetailView.this, "Loading",
							"Loading data from translate.ge...", true, true);
				}

				@Override
				protected JSONArray doInBackground(Void... params) {

					return GetTranslateGE.getFromTranslateGe(url);

				}

				@Override
				protected void onPostExecute(JSONArray result) {

					translate.setText(JSONParser.GetStringFromJson(result));
					if (dialog.isShowing()) {
						dialog.dismiss();
					}
				}
			}.execute();
		}
	}

	private boolean isNetAvailable() {
		ConnectivityManager connection = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetinfo = connection.getActiveNetworkInfo();
		return activeNetinfo != null;
	}
}
