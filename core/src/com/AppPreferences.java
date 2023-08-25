package com;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Handles settings
 *
 * Is set up so changes are saved between sessions
 */
public class AppPreferences {
    private static final String PREF_MUSIC_VOLUME = "volume";
    private static final String PREF_MUSIC_ENABLED = "music.enabled";
    private static final String PREF_SOUND_ENABLED = "sound.enabled";
    private static final String PREF_SOUND_VOL = "sound";
    // Location of preferences
    private static final String PREFS_NAME = "b2dtut";

    private static AppPreferences APP_PREFERENCES;

    /**
     * Singleton to allow universal preference access
     */
    private AppPreferences(){
        if(APP_PREFERENCES != null)throw new RuntimeException("Attempt to instantiate existing singleton - AppPreferences");

        APP_PREFERENCES = this;
    }

    public static AppPreferences getAppPreferences(){
        if(APP_PREFERENCES == null){
            APP_PREFERENCES = new AppPreferences();
        }
        return APP_PREFERENCES;
    }

    protected Preferences getPrefs() {
        return Gdx.app.getPreferences(PREFS_NAME);
    }

    /**
     * Calls the PREF_MUSIC_VOLUME location and returns the value if set, else returns default value
     * @return
     */
    public float getMusicVolume() {
        if(!isMusicEnabled())return 0.0f;
        System.out.println("isMusicEnabled returned true");
        return getPrefs().getFloat(PREF_MUSIC_VOLUME, 0.5f);
    }

    /**
     * Sets the volume to passed float value and calls flush to write to disk
     * @param volume
     */
    public void setMusicVolume(float volume) {
        getPrefs().putFloat(PREF_MUSIC_VOLUME, volume);
        getPrefs().flush();
    }

    public boolean isSoundEffectsEnabled() {
        return getPrefs().getBoolean(PREF_SOUND_ENABLED, true);
    }

    public void setSoundEffectsEnabled(boolean soundEffectsEnabled) {
        getPrefs().putBoolean(PREF_SOUND_ENABLED, soundEffectsEnabled);
        getPrefs().flush();
    }

    public boolean isMusicEnabled() {
        boolean music = getPrefs().getBoolean(PREF_MUSIC_ENABLED, true);
//        System.out.println("Returning " + music + " for isMusicEnabled");
        return music;
    }

    public void setMusicEnabled(boolean musicEnabled) {
        getPrefs().putBoolean(PREF_MUSIC_ENABLED, musicEnabled);
        getPrefs().flush();
    }

    public float getSoundVolume() {
        return getPrefs().getFloat(PREF_SOUND_VOL, 0.5f);
    }

    public void setSoundVolume(float volume) {
        getPrefs().putFloat(PREF_SOUND_VOL, volume);
        getPrefs().flush();
    }
}
