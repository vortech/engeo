package org.herrlado.engeo;

import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.herrlado.engeo.db.DataBaseHelper;

import android.content.Context;
import android.database.Cursor;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

public class WordlistAdapter extends ResourceCursorAdapter implements
		TextWatcher {

	private static final String SQL_ENG = "SELECT t1.id as _id, t1.eng as original, t1.transcription, t2.geo as translate, t4.name, t4.abbr FROM eng t1, geo t2, geo_eng t3, types t4 WHERE t1.eng >= ? AND t3.eng_id=t1.id AND t2.id=t3.geo_id AND t4.id=t2.type ORDER BY t1.eng LIMIT 25";

	private static final String SQL_GEO = "SELECT t2.id as _id, t1.eng as translate, t1.transcription, t2.geo as original, t4.name, t4.abbr FROM eng t1, geo t2, geo_eng t3, types t4 WHERE t2.geo >= ? AND t3.eng_id=t1.id AND t2.id=t3.geo_id AND t4.id=t2.type ORDER BY t2.geo LIMIT 25";

	private static final String TAG = Wordlist.class.getSimpleName();
	
	private static final String geo = "http://translate.ge/g.aspx?w=";
	
	private static final String eng = "http://translate.ge/q.aspx?w=";
	
	DataBaseHelper db;

	private static boolean isGeo(CharSequence w) {
		int c = w.charAt(0);
		return c > 4304 && c < 4337;
	}

	public WordlistAdapter(Wordlist wordlist) {
		super(wordlist, android.R.layout.simple_list_item_2, null, true);
		db = new DataBaseHelper(wordlist);
		db.openDataBase();
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		final TextView orig = (TextView) view.findViewById(android.R.id.text1);

		String original = cursor.getString(cursor.getColumnIndex("original"));
		orig.setText(original);

		final TextView trans = (TextView) view.findViewById(android.R.id.text2);
		// trans.setTextAppearance(context, android.R.attr.textAppearanceLarge);

		String tra = cursor.getString(cursor.getColumnIndex("translate"));
		trans.setText(tra);

	}

	public final HttpGet ENG_GET = new HttpGet(eng);

	public final HttpGet GEO_GET = new HttpGet(geo);

	DefaultHttpClient client = new DefaultHttpClient();

	public String loadTranslateGe(CharSequence w) {

		String url;
		if (isGeo(w)) {
			url = geo + w;
		} else {
			url = eng + w;
		}

		HttpGet get = new HttpGet(url);
		//get.getParams().setParameter("w", w);
		
		HttpResponse response = null;
		try {
			response = client.execute(get);

			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				Log.w("EnGEO", response.getStatusLine().toString());
				return "";
			}

			String content = Utils
					.stream2str(response.getEntity().getContent()).trim();

			LineNumberReader reader = new LineNumberReader(new StringReader(
					content));
			String line = reader.readLine();
			
			return line;
		} catch (Exception e) {
			Log.w(TAG, "Error on client.execute", e);
			return "";
		}

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(final CharSequence s, int start, int before,
			int count) {

		if (s.length() == 0) {

			changeCursor(null);
			return;

		}

		final String SQL;
		final String[] args;

		if (isGeo(s)) {
			SQL = SQL_GEO;
			args = new String[] { s.toString().trim() + "%" };
		} else {
			SQL = SQL_ENG;
			args = new String[] { s.toString().toLowerCase().trim() + "%" };
		}

		Cursor c = db.myDataBase.rawQuery(SQL, args);
		changeCursor(c);
		
	}

	@Override
	public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
		return super.runQueryOnBackgroundThread(constraint);
	}

	@Override
	public void afterTextChanged(Editable s) {

	}

}
