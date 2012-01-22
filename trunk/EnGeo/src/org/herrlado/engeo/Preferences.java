package org.herrlado.engeo;

import java.util.Locale;

import de.ub0r.android.lib.Log;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * Preferences.
 * 
 * @author Lado Kumsiashvili
 */
public class Preferences extends PreferenceActivity implements
		SharedPreferences.OnSharedPreferenceChangeListener {

	public static final String USE_TRANSLATEGE = "use_translatege";

	public static final String USE_OFFLINE = "use_offline";

	public static final String CHANGE_LANG = "change_lang";

	public static final String LANG_KA = "ქართული";

	/** Preference's name: theme. */
	private static final String PREFS_THEME = "themes";

	/** Theme: light. */
	private static final String THEME_LIGHT = "Light";

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

	public final void localeChange() {
		this.addPreferencesFromResource(R.xml.prefs);
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(this);
		Preference p = this.findPreference(sharedPref.getString(CHANGE_LANG,
				LANG_KA));
		if (p != null && LANG_KA.equals(p)) {
			p.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

				@Override
				public boolean onPreferenceClick(Preference preference) {
					// TODO Auto-generated method stub
					Locale local = new Locale("ka");
					Locale.setDefault(local);
					Configuration config = new Configuration();
					config.locale = local;
					getBaseContext().getResources()
							.updateConfiguration(
									config,
									getBaseContext().getResources()
											.getDisplayMetrics());

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
