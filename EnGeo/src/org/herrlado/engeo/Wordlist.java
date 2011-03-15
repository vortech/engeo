package org.herrlado.engeo;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class Wordlist extends ListActivity implements OnItemClickListener,
		OnItemLongClickListener, OnSharedPreferenceChangeListener {

	private static final String TAG = "EnGEO";

	private EditText editText;

	/** Conversations. */
	private WordlistAdapter adapter = null;

	// private static final HashMap<Character, Character> to_geo = new
	// HashMap<Character, Character>(
	// 33);
	// private static final HashMap<Character, Character> to_eng = new
	// HashMap<Character, Character>(
	// 33);

	// static {
	// String geo = "აბგდევზთიკლმნოპჟრსტუფქღყშჩცძწჭხჯჰ";
	// String eng = "abgdevzTiklmnopJrstufqRySCcZwWxjh";
	// for (int i = 0; i < geo.length(); ++i) {
	// to_geo.put(eng.charAt(i), geo.charAt(i));
	// }
	// for (int i = 0; i < geo.length(); ++i) {
	// to_eng.put(geo.charAt(i), eng.charAt(i));
	// }
	// }

	// /**
	// * Creates and returns a list adapter for the current list activity
	// *
	// * @return
	// */
	// protected ListAdapter createAdapter(String[] values) {
	// // Create a simple array adapter (of type string) with the test values
	// ListAdapter adapter = new ArrayAdapter<String>(this,
	// android.R.layout.simple_list_item_1, values);
	// return adapter;
	// }

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// int theme = Preferences.getTheme(this);
		// this.setTheme(theme);
		// Thread.setDefaultUncaughtExceptionHandler(new
		// ChewbaccaUncaughtExceptionHandler(
		// getApplication().getBaseContext(), null));
		setContentView(R.layout.wordlist);
		// new DictDownloader(getApplicationContext()).execute();

		final ListView list = this.getListView();
		this.adapter = new WordlistAdapter(this);
		this.setListAdapter(this.adapter);

		list.setOnItemClickListener(this);
		list.setOnItemLongClickListener(this);

		editText = (EditText) this.findViewById(R.id.textEdit);
		editText.addTextChangedListener(adapter);

		registerForContextMenu(list);
		// db = new DataBaseHelper(this);
		// db.openDataBase();
	}

	@Override
	protected void onDestroy() {
		adapter.db.close();// TODO soll selber tun
		super.onDestroy();

	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		return parent.showContextMenu();
		// Intent myIntent = new Intent(view.getContext(), DetailView.class);
		// startActivityForResult(myIntent, 0);
		// return true;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent detailIntent = new Intent(view.getContext(), DetailView.class);
		Cursor cursor = (Cursor) parent.getItemAtPosition(position);
		cursor.getColumnIndex("_id");
		String q = "q";
		String w = null;
		// if(isGeo(content)){
		q = "g";
		// w = content.substring(0, content.indexOf("=")).trim().toLowerCase();
		// } else {
		// w = content.substring(0, content.indexOf("[")).trim().toLowerCase();
		// }

		// detailIntent.putExtra("content", content);
		detailIntent.putExtra("w", w);
		detailIntent.putExtra("q", q);
		startActivityForResult(detailIntent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean onCreateOptionsMenu(final Menu menu) {
		MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		final SharedPreferences p = prefs();
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.share:
			share(info);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	private void share(AdapterContextMenuInfo info) {
		Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Some text");
		shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
				"Some other text");
		startActivity(Intent.createChooser(shareIntent, "Title for chooser"));
	}

	public SharedPreferences prefs() {
		PreferenceManager.getDefaultSharedPreferences(this)
				.registerOnSharedPreferenceChangeListener(this);
		return PreferenceManager.getDefaultSharedPreferences(this);
	}

	@Override
	public final boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item_settings: // start settings activity
			this.startActivity(new Intent(this, Preferences.class));
			return true;
		default:
			return false;
		}
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub

	}
}