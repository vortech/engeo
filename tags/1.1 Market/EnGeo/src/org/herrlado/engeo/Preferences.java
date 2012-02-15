package org.herrlado.engeo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import de.ub0r.android.lib.Log;

/**
 * Preferences.
 * 
 * @author Lado Kumsiashvili
 */
public class Preferences extends PreferenceActivity implements
		SharedPreferences.OnSharedPreferenceChangeListener {

	/** Tag for output. */
	public static final String TAG = "engeo.pref";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.addPreferencesFromResource(R.xml.prefs);
		Preference p = this.findPreference("send_logs");
		if (p != null) {
			p.setOnPreferenceClickListener(// .
			new Preference.OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(final Preference preference) {
					Log.collectAndSendLog(Preferences.this);
					return true;
				}
			});
		}
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		final SharedPreferences p = PreferenceManager
				.getDefaultSharedPreferences(// .
				Preferences.this);
	}
}
