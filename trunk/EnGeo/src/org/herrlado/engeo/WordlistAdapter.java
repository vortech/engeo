package org.herrlado.engeo;

import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.herrlado.engeo.db.DataBaseHelper;


import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

public class WordlistAdapter extends ResourceCursorAdapter implements TextWatcher {

	
	
	private static final String SQL_ENG = "SELECT t1.id as _id, t1.eng as original, t1.transcription, t2.geo as translate, t4.name, t4.abbr FROM eng t1, geo t2, geo_eng t3, types t4 WHERE t1.eng >= ? AND t3.eng_id=t1.id AND t2.id=t3.geo_id AND t4.id=t2.type ORDER BY t1.eng LIMIT 25";

	private static final String SQL_GEO = "SELECT t2.id as _id, t1.eng as translate, t1.transcription, t2.geo as original, t4.name, t4.abbr FROM eng t1, geo t2, geo_eng t3, types t4 WHERE t2.geo >= ? AND t3.eng_id=t1.id AND t2.id=t3.geo_id AND t4.id=t2.type ORDER BY t2.geo LIMIT 25";

	private static final String TAG = Wordlist.class.getSimpleName();

	private static final HashMap<Character, Character> to_geo = new HashMap<Character, Character>(
			33);
	private static final HashMap<Character, Character> to_eng = new HashMap<Character, Character>(
			33);
	
//
//	private static String to_latin(CharSequence geo) {
//		StringBuilder sb = new StringBuilder();
//		for (int i = 0; i < geo.length(); ++i) {
//			char c = geo.charAt(i);
//			Character cc = to_eng.get(c);
//			if (cc == null) {
//				cc = c;
//			}
//			sb.append(cc);
//		}
//		return sb.toString();
//	}
//	
//	private static String to_geo(CharSequence latin) {
//		StringBuilder sb = new StringBuilder();
//		for (int i = 0; i < latin.length(); ++i) {
//			char c = latin.charAt(i);
//			Character cc = to_geo.get(c);
//			if (cc == null) {
//				cc = c;
//			}
//			sb.append(cc);
//		}
//		return sb.toString();
//	}
	
	DataBaseHelper db;
	
	private static boolean isGeo(CharSequence w) {
		int c = w.charAt(0);
		return c > 4304 && c < 4337;
	}

//	public void a(String s){ 
	

	//boolean u = prefs().getBoolean(Preferences.USE_TRANSLATEGE, false);
	//if(u){
		//loadTranslateGe(s, results);
	//}
//	ListAdapter adapter = createAdapter(results
//			.toArray(new String[] {}));
//	setListAdapter(adapter);
	//}
	
	public WordlistAdapter(Wordlist wordlist){
		super(wordlist, R.layout.wordlist_item, null, true);
		db = new DataBaseHelper(wordlist);
		db.openDataBase();
	}
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		final TextView orig = (TextView) view.findViewById(R.id.word_original);
		final TextView trans = (TextView) view.findViewById(R.id.word_translate);
		
		String original = cursor.getString(cursor.getColumnIndex("original"));
		orig.setText(original);
		String translate = cursor.getString(cursor.getColumnIndex("translate"));
		trans.setText(translate);
	}
	

	public final HttpGet ENG_GET = new HttpGet("http://translate.ge/q.aspx");

	public final HttpGet GEO_GET = new HttpGet("http://translate.ge/g.aspx");

	DefaultHttpClient client = new DefaultHttpClient();

	

	public void loadTranslateGe(CharSequence w, ArrayList<String> results) {

		HttpGet get;
		if (isGeo(w)) {
			get = GEO_GET;
		} else {
			get = ENG_GET;
		}
		get.getParams().setParameter("w", w);
		HttpResponse response = null;
		try {
			response = client.execute(get);

			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				Log.w("EnGEO", response.getStatusLine().toString());
				return;
			}
			
			String content = Utils.stream2str(response.getEntity().getContent()).trim();
			
			LineNumberReader reader = new LineNumberReader(new StringReader(content));
			String line;
			// StringBuilder sb = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				int idx = line.indexOf("-");
				if (idx == -1)
					continue;
				String term = line.substring(0, idx).trim();
				String desc = line.substring(idx, 20).trim();
				results.add(term + " = " + desc + "...");
			}

		} catch (Exception e) {
			Log.w(TAG, "Error on client.execute", e);
			return;
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
			
			
			final String	 SQL;
			final String[] args;
			
			boolean engeo = true;
			
			if (isGeo(s)) {
				SQL = SQL_GEO;
				//args = new String[] { to_latin(s.toString().trim()) + "%"};
				args = new String[] { s.toString().trim() + "%"};
				engeo = false;
			} else {
				SQL = SQL_ENG;
				args = new String[] { s.toString().toLowerCase().trim() +"%" };
			}

			
//			new AsyncTask<Void, Void, Void>(){
//
//				@Override
//				protected Void doInBackground(Void... params) {
					Cursor c = db.myDataBase.rawQuery(SQL, args);
					changeCursor(c);
//					return null;
//				}
//				
//			}.execute();
/*			
			ArrayList<String> results = new ArrayList<String>();
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						if (engeo) {

						} else {

						}
					} while (c.moveToNext());
				}
			}
*/
		}
		
		@Override
		public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
			return super.runQueryOnBackgroundThread(constraint);
		}

		@Override
		public void afterTextChanged(Editable s) {

		}

}
