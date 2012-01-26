package org.herrlado.engeo;

import java.io.LineNumberReader;
import java.io.StringReader;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.sqlite.SQLiteCursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Wordlist extends ListActivity implements OnItemClickListener,
		OnItemLongClickListener, OnSharedPreferenceChangeListener {

	private static final String TAG = "EnGEO";

	private EditText editText;

	/** Conversations. */
	private WordlistAdapter adapter = null;

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
	


	DefaultHttpClient client = new DefaultHttpClient();
	
	private static final String geo = "http://translate.ge/g.aspx?w=";
	
	private static final String eng = "http://translate.ge/q.aspx?w=";

	//public final HttpGet ENG_GET = new HttpGet(eng);

	//public final HttpGet GEO_GET = new HttpGet(geo);
	
	
	public String loadTranslateGe(String w) {

		String url;
		if (Utils.isGeo(w)) {
			url = geo + Utils.urlEncode(w, "UTF-8");
		} else {
			url = eng + Utils.urlEncode(w, "UTF-8");
		}

		HttpGet get = new HttpGet(url);
		//get.getParams().setParameter("w", w);
		
		HttpResponse response = null;
		try {
			response = client.execute(get);

			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				Log.w("EnGEO", response.getStatusLine().toString());
				return null;
			}

			String content = Utils
					.stream2str(response.getEntity().getContent()).trim();

			LineNumberReader reader = new LineNumberReader(new StringReader(
					content));
			String line = reader.readLine();
			
			return line;
		} catch (Exception e) {
			Log.w(TAG, "Error on client.execute", e);
			return null;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {		
		
	
		
		SQLiteCursor c = (SQLiteCursor) adapter.getItem(position);
		//TextView tv = (TextView) findViewById(android.R.id.text1);
		//TextView tv = (TextView)((TwoLineListItem)view).getChildAt(0);
		final String str = c.getString(c.getColumnIndex("original"));

		new AsyncTask<Void, Void, Void>() {
		
			private String result;
			
			protected void onPreExecute() {};
			
			
			protected void onPostExecute(Void result) {
				
				if(this.result == null){
					Toast toast = Toast.makeText(Wordlist.this, "ჩანაწერი არ მოიძებნა", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					return;
				}
				
				new AlertDialog.Builder(Wordlist.this)
					.setTitle(str)//
					.setMessage(this.result.replaceAll("\\<.*?>","").replaceAll("&nbsp;", " "))//
						.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}})//
							.create()//
							.show();
			
			}
			
			
			protected Void doInBackground(Void... params) {
				result = loadTranslateGe(str);
				return null;
			};
		}.execute((Void)null);
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