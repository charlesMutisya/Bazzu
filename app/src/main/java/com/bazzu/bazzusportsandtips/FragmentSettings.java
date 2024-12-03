package com.bazzu.bazzusportsandtips;

import android.os.Bundle;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class FragmentSettings extends PreferenceFragmentCompat {

    static final String TAG = "SettingsFragmentTag";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        ListPreference themePreference = findPreference("themePref");
        if (themePreference != null) {
            themePreference.setOnPreferenceChangeListener(
                    new Preference.OnPreferenceChangeListener() {
                        @Override
                        public boolean onPreferenceChange(Preference preference, Object newValue) {
                            String themeOption = (String) newValue;
                            ThemeHelper.applyTheme(themeOption);
                            return true;
                        }
                    });
        }
    }
}