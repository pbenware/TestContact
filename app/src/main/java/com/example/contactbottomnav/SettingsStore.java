package com.example.contactbottomnav;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class SettingsStore {

    private static final String LOGID = "com.example.contactbottomnav.SettingsStore";

    // settings version
    private static final int VERSION = 0x0100;

    // internal keyword values
    private static final String KEY_CREATED_ON = "CreatedOn";
    private static final String KEY_VERSION = "Version";

    private static final String KEY_CURRENT_TAB = "CurrentFocusedTab";

    private SharedPreferences _prefs = null;
    private SharedPreferences.Editor _edit = null;
    private Context _context;

    // Default constructor
    public SettingsStore(Context context)
    {
        try {
            // save the context
            _context = context;

            // use default Shared Preferences
            // (NOTE: I know this is now deprecated and there are
            // newer accepted methods for screen preferences, put for speed of implementation I
            // decided to use the default shared preferences to store hidden settings data.)
            _prefs = PreferenceManager.getDefaultSharedPreferences(context);

            // initialize preferences
            initialize(false);

        } catch (Exception e)
        {
            Log.e(LOGID, "SettingsStore exception " + e.getMessage());
        }
    }

    public void initialize(boolean erase)
    {
        // get default values
        long createdOn = System.currentTimeMillis();

        // erase existing settings?
        if (erase) {

            // check for existing contents
            if (_prefs.contains(KEY_CREATED_ON)) {
                // retain existing values where necessary
                createdOn = _prefs.getLong(KEY_CREATED_ON, 0);

                // clear preferences
                _prefs.edit().clear();
                _prefs.edit().commit();
            }
        }

        // write initial preference values
        if (!_prefs.contains(KEY_CREATED_ON) || erase)
        {
            // create editor
            _edit = _prefs.edit();

            // write the "created on" and "version" for this file
            _edit.putLong(KEY_CREATED_ON, createdOn);
            _edit.putInt(KEY_VERSION, VERSION);

            // set default values
            _edit.putInt(KEY_CURRENT_TAB, 0);
        }

        // upgrade?
        int ver = _prefs.getInt(KEY_VERSION, VERSION);
        if (ver < VERSION)
        {
            _edit = _prefs.edit();

            // set new version
            _edit.putInt(KEY_VERSION, VERSION);

            // ---------
            // do what needs to be done to upgrade to new version of settings
            // ---------

            // persist settings
            _edit.commit();
        }
    }

    // get/set current tab
    public int getCurrentFocusedTab()
    {
        return _prefs.getInt(KEY_CURRENT_TAB, 0);
    }
    public void setCurrentFocusedTab(int current)
    {
        if (_edit == null)
        {
            _edit = _prefs.edit();
        }

        _edit.putInt(KEY_CURRENT_TAB, current);
        _edit.commit();
    }
}
