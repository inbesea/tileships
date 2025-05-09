package org.bitbucket.noahcrosby;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import static com.badlogic.gdx.Application.LOG_DEBUG;
import static com.badlogic.gdx.Application.LOG_INFO;

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
    private static final String IS_DEBUG_MODE = "debug.mode";
    private static final String IS_FULL_SCREEN = "fullscreen";
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

    /**
     * Calls the IS_FULL_SCREEN location and returns the value if set, else returns default value.
     * Sets the Gdx.graphics.screen mode depending on the passed boolean.
     * @param isFullScreen
     */
    public void setIsFullScreen(boolean isFullScreen) {
        if(isFullScreen){
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        } else {
            Gdx.graphics.setWindowedMode(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
        getPrefs().putBoolean(IS_FULL_SCREEN, isFullScreen);
        getPrefs().flush();
    }

    /**
     * Returns true if the game is in full screen mode else false
     * @return - boolean representing fullscreen status
     */
    public boolean isFullScreen() {
        return getPrefs().getBoolean(IS_FULL_SCREEN, false);
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

    public boolean getIsDebug() {
        return getPrefs().getBoolean(IS_DEBUG_MODE, false);
    }
    public void setIsDebug(boolean isDebug) {
        getPrefs().putBoolean(IS_DEBUG_MODE, isDebug);
        getPrefs().flush();
    }

    /**
     * Switches app to debug mode
     * Also affects the logging level.
     */
    public void toggleIsDebug() {
        getPrefs().putBoolean(IS_DEBUG_MODE, !getIsDebug());
        getPrefs().flush();
        if(getIsDebug()){
            Gdx.app.setLogLevel(LOG_DEBUG);
        } else {
            Gdx.app.setLogLevel(LOG_INFO);
        }
    }


    public static void initLogging() {
        if(getAppPreferences().getIsDebug()){
            Gdx.app.setLogLevel(LOG_DEBUG);
        } else {
            Gdx.app.setLogLevel(LOG_INFO);
        }
    }
}
