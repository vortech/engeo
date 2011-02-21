package org.herrlado.engeo;

import de.ub0r.android.lib.Log;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * Preferences.
 * 
 * @author lado
 */
public class Preferences extends PreferenceActivity implements
		SharedPreferences.OnSharedPreferenceChangeListener {

	public static final String USE_TRANSLATEGE = "use_translatege";
	
	public static final String USE_OFFLINE = "use_offline";
	

	/** Preference's name: theme. */
	private static final String PREFS_THEME = "theme";
	/** Theme: black. */
	private static final String THEME_BLACK = "black";
	/** Theme: light. */
	private static final String THEME_LIGHT = "light";

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
						public boolean onPreferenceClick(
								final Preference preference) {
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
	
	
	/**
	 * Get Theme from Preferences.
	 * 
	 * @param context
	 *            {@link Context}
	 * @return theme
	 */
	static final int getTheme(final Context context) {
		final SharedPreferences p = PreferenceManager
				.getDefaultSharedPreferences(context);
		final String s = p.getString(PREFS_THEME, THEME_LIGHT);
		if (s != null && THEME_LIGHT.equals(s)) {
			return android.R.style.Theme_Light;
		}
		return android.R.style.Theme_Black;
	}

}
