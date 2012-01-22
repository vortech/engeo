package org.herrlado.engeo;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class Wordlist extends ListActivity implements OnItemClickListener,
		OnItemLongClickListener, OnSharedPreferenceChangeListener,
		View.OnClickListener {

	private static final String TAG = "EnGEO";

	private EditText editText;

	/** Conversations. */
	private WordlistAdapter adapter = null;

	private Button btnDict = null;

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

		btnDict = (Button) this.findViewById(R.id.dicts);
		btnDict.setOnClickListener(this);

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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		final CharSequence[] dicts = { "English-Georgian", "Georgian-English" };
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("Chose Direction");
		dialog.setSingleChoiceItems(dicts, -1,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						switch (which) {
						case 0:
							btnDict.setText("EN-GE");
							dialog.dismiss();
						case 1:
							btnDict.setText("GE-EN");
							dialog.dismiss();
						}
					}
				});

		AlertDialog alert = dialog.create();
		alert.show();
	}
}