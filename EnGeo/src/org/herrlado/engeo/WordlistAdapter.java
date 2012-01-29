package org.herrlado.engeo;

import org.herrlado.engeo.db.DataBaseHelper;

import android.content.Context;
import android.database.Cursor;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

public class WordlistAdapter extends ResourceCursorAdapter implements
		TextWatcher {

	private static final String SQL_ENG = "SELECT t1.id as _id, t1.eng as original, t1.transcription, t2.geo as translate, t4.name, t4.abbr FROM eng t1, geo t2, geo_eng t3, types t4 WHERE t1.eng >= ? AND t3.eng_id=t1.id AND t2.id=t3.geo_id AND t4.id=t2.type GROUP BY t1.eng LIMIT 25";

	private static final String SQL_GEO = "SELECT t2.id as _id, t1.eng as translate, t1.transcription, t2.geo as original, t4.name, t4.abbr FROM eng t1, geo t2, geo_eng t3, types t4 WHERE t2.geo >= ? AND t3.eng_id=t1.id AND t2.id=t3.geo_id AND t4.id=t2.type GROUP BY t2.geo LIMIT 25";

	private static final String TAG = Wordlist.class.getSimpleName();

	DataBaseHelper db;

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

		String tra = cursor.getString(cursor.getColumnIndex("translate"));
		trans.setText(tra);

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

		if (Utils.isGeo(s)) {
			SQL = SQL_GEO;
			args = new String[] { s.toString().trim() };
		} else {
			SQL = SQL_ENG;
			args = new String[] { s.toString().toLowerCase().trim() };
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
