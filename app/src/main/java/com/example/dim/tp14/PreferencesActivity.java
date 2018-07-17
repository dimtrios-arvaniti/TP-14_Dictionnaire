package com.example.dim.tp14;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;

import com.example.dim.tp14.utils.AppCompatPreferenceActivity;

import java.util.List;
import java.util.Locale;

public class PreferencesActivity extends AppCompatPreferenceActivity {


    //private boolean langFR;
    //private Context baseContext;

    /**
     * Preferences value change listener, update summary
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {

        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            // set preference summary
            preference.setSummary(stringValue);


            return true;
        }
    };

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger preferences value change listener
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), "fr"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();

        Log.i("TEST", "onCreate: " + Locale.getDefault().getDisplayLanguage());
        String pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .getString("lang", "fr");
        Log.i("TEST", "onCreate: " + pref);
        // init local only once
        if (!Locale.getDefault().getDisplayLanguage().toLowerCase().contains(pref)) {
            initLocale(pref.contains("fr") ? Locale.FRENCH // french
                    : pref.contains("en") ? Locale.ENGLISH // english
                    : Locale.FRENCH); // default
        }
        //langFR = false;

       // baseContext = getBaseContext();
    }

    private void initLocale(Locale french) {
        Configuration config = getResources().getConfiguration();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(french);
        } else {
            config.locale = french;
        }

        // update
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        Intent intent = new Intent(PreferencesActivity.this, PreferencesActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.action_settings);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || LanguagePreferenceFragment.class.getName().equals(fragmentName);
    }

    /**
     * Enable MainActivity return from action bar
     *
     * @param item item clicked
     * @return true if home clicked, false otherwise
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(PreferencesActivity.this, MainActivity.class));
            return true;
        }
        return false;
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class LanguagePreferenceFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_lang);
            setHasOptionsMenu(true);

            // summary binding
            bindPreferenceSummaryToValue(findPreference("lang"));

            // update local
            findPreference("lang").setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    if (o.toString().contains("fr")) {
                        changeLocale(Locale.FRENCH);
                        return true;
                    }
                    if (o.toString().contains("en")) {
                        changeLocale(Locale.ENGLISH);
                        return true;
                    }
                    return false;
                }
            });
        }

        private void changeLocale(Locale french) {
            Configuration config = getResources().getConfiguration();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
                config.setLocale(french);
            } else{
                config.locale = french;
            }

            // update
            getActivity().getBaseContext().getResources().updateConfiguration(config,
                    getActivity().getBaseContext().getResources().getDisplayMetrics());

            onConfigurationChanged(config);

            Intent intent = new Intent(getActivity(), PreferencesActivity.class);
            getActivity().startActivity(intent);
            getActivity().finish();
        }
    }


}
