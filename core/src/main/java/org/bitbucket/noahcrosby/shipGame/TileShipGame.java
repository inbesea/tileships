package org.bitbucket.noahcrosby.shipGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.bitbucket.noahcrosby.AppPreferences;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import org.bitbucket.noahcrosby.shipGame.generalObjects.GameObject;
import org.bitbucket.noahcrosby.shipGame.generalObjects.ship.Ship;
import org.bitbucket.noahcrosby.shipGame.arcadeMode.ArcadeModeScreen;
import org.bitbucket.noahcrosby.shipGame.screens.GameScreen;
import org.bitbucket.noahcrosby.shipGame.screens.MainMenu;
import org.bitbucket.noahcrosby.shipGame.screens.MainMenuScreen;
import org.bitbucket.noahcrosby.shipGame.screens.PreferencesScreen;
import org.bitbucket.noahcrosby.javapoet.Resources;

/**
 * Entry point for libGDX framework to run the game.
 * We need the logic to be referenced from this point.
 */
public class TileShipGame extends Game {
    public static Skin defaultSkin = null;
    private AppPreferences appPreferences;
	public static float zoomMax = 5;
	public static float zoomMin = 0.5f;
	Screen currentScreen;
	public static SpriteBatch batch; // Draws the textures and fonts etc.
	public static BitmapFont font;
	private Ship playerShip;
	public static int zoomSpeed = 5;
	public static float defaultViewportSizeX = 700f, defaultViewportSizeY = 500f;
    public static float spawnRadius = 700f;
	public static float meterLength = 64f;
	private Array<GameObject> gameObjects = new Array<>();

    private Screen mainGameScreen;
    private Screen mainMenuScreen;
    private Screen mainMenu;
    private ArcadeModeScreen arcadeModeScreen;
    private Screen preferencesScreen;

    private static OrthographicCamera currentCamera;

    public final static int MENU = 6;
    public final static int OLD_MENU = 0;
    public final static int PREFERENCES = 1;
    public final static int CLASSIC_MODE = 2;
    public final static int GAMEOVER = 3;
    public final static int LOADING = 4;
    public final static int ARCADE_MODE = 5;


    /**
     * Initialization of the game stuff
     */
    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();

        appPreferences = AppPreferences.getAppPreferences();

        this.changeScreen(MENU);
    }

    /**
     * Switch screens using only int pointer/static var references.
     * Can be called from anywhere with game instance reference.
     * @param screen - Int representing screen to switch to.
     */
    public void changeScreen(int screen) {
        Gdx.app.log("Screen Change", "Changing screen to " + screen);
        switch (screen){
            case OLD_MENU:
                if(mainMenuScreen == null) mainMenuScreen = new MainMenuScreen(this);
                this.setGameScreen(mainMenuScreen);
                break;
            case MENU:
                if(mainMenu == null) mainMenu = new MainMenu(this);
                this.setGameScreen(mainMenu);
                break;
            case PREFERENCES:
                if(preferencesScreen == null) preferencesScreen = new PreferencesScreen(this);
                this.setGameScreen(preferencesScreen);
                break;
            case CLASSIC_MODE:
                if(mainGameScreen == null) mainGameScreen = new GameScreen(this);
                this.setGameScreen(mainGameScreen);
                break;
            case ARCADE_MODE:
                if(arcadeModeScreen == null) arcadeModeScreen = new ArcadeModeScreen(this);
                this.setGameScreen(arcadeModeScreen);
                break;
            case LOADING:
                break;
            case GAMEOVER:
                break;
            default :
                throw new RuntimeException();
        }
    }

    /**
     * Method to convert a pixel length to meters.
     *
     * @param pixelLength - pixels to divide by meterLength as defined in tileShipGame
     * @return - float representing meters from passed pixel length
     */
    public static float convertPixelsToMeters(float pixelLength) {
        return pixelLength / meterLength; // Gives an easy way to swap pixel lengths for the physics simulation
    }

    @Override
    public void dispose() {
        font.dispose();
        batch.dispose();
        Resources.assetManager.dispose();
        currentScreen.dispose();
    }

    /**
     * Used to set current screen in game context
     * Should only be used via the change screen method
     *
     * @param screen - Screen object to set in game object
     */
    private void setGameScreen(Screen screen) {
        if(this.currentScreen != null) this.currentScreen.pause();
        this.currentScreen = screen;
        setScreen(this.currentScreen);
        this.currentScreen.resume();
    }

    public Screen getCurrentScreen(){
        return this.currentScreen;
    }

    public void setPlayerShip(Ship playerShip) {
        this.playerShip = playerShip;
    }

    public Ship getPlayerShip() {
        return playerShip;
    }


    public Array<GameObject> getGameObjects() {
        return gameObjects;
    }

    public void setGameObjects(Array<GameObject> gameObjects) {
        this.gameObjects = gameObjects;
    }

    public AppPreferences getPreferences(){
        return this.appPreferences;
    }

    public static OrthographicCamera getCurrentCamera() {
        return TileShipGame.currentCamera;
    }

    public static void setCurrentCamera(OrthographicCamera newCamera){
        TileShipGame.currentCamera = newCamera;
    }
}
