package com.shipGame.generalObjects;

import com.AppPreferences;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.StringBuilder;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.shipGame.generalObjects.Ship.Ship;
import com.shipGame.TileShipGame;
import com.shipGame.screens.GameScreen;

/**
 * Layer of HUD elements. Is on a FitViewport to keep hud elements scaled correctly.
 */
public class HUD {
    private ExtendViewport HUDScreenLayer;
    private TileShipGame game;
    private Ship playerShip;
    private boolean drawMenu = false;
    private Stage menuOverlay;
    private boolean showDebugHud = true;

    private Label titleLabel;
    private Label volumeMusicLabel;
    private Label volumeSoundLabel;
    private Label musicOnOffLabel;
    private Label soundOnOffLabel;

    public HUD(TileShipGame game) {
        this.game = game;
        this.playerShip = game.getPlayerShip();

        HUDScreenLayer = new ExtendViewport(TileShipGame.defaultViewportSizeX, TileShipGame.defaultViewportSizeY);
        createMenuOverlay();
    }

    /**
     * Called to draw the HUD
     * Has own internal batch draw call for hud to use HUD veiwport
     */
    public void draw(){
        StringBuilder stringBuilder = new StringBuilder();
        if(showDebugHud){
            stringBuilder = buildDebugUI();
        }

        //Secondly draw the Hud
        game.batch.setProjectionMatrix(HUDScreenLayer.getCamera().combined); //set the spriteBatch to draw what our stageViewport sees

        game.batch.begin();

        if(showDebugHud){
            game.font.draw(game.batch, stringBuilder.toString() , 2,
                    (HUDScreenLayer.getWorldHeight() - 2)); // Worldheight gives the extendVeiwport hight
        }

        if(this.drawMenu()){
            menuOverlay.act(Gdx.graphics.getDeltaTime());
            menuOverlay.draw();
        }

        game.batch.end();
    }

    private void createMenuOverlay() {
        menuOverlay = new Stage(HUDScreenLayer);
        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(AppPreferences.getAppPreferences().getIsDebug());
        menuOverlay.addActor(table);

        Skin skin = new Skin(Gdx.files.internal("skin/neon/skin/neon-ui.json"));

        //volume
        final Slider volumeMusicSlider = new Slider( 0f, 1f, 0.1f,false, skin );
        volumeMusicSlider.setValue( game.getPreferences().getMusicVolume() );
        volumeMusicSlider.addListener(event -> {
            game.getPreferences().setMusicVolume( volumeMusicSlider.getValue() );
            return false;
        });
        // sound volume
        final Slider soundMusicSlider = new Slider(0f, 1f, 0.1f, false, skin);
        soundMusicSlider.setValue(game.getPreferences().getSoundVolume());
        soundMusicSlider.addListener(new InputListener() {
            @Override
            public boolean handle(Event event) {
                game.getPreferences().setSoundVolume(soundMusicSlider.getValue());
                // updateVolumeLabel();
                return false;
            }
        });

        // music on/off
        final CheckBox musicCheckbox = new CheckBox(null, skin);
        musicCheckbox.setChecked(game.getPreferences().isMusicEnabled());
        musicCheckbox.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                boolean enabled = musicCheckbox.isChecked();
                System.out.println("Checkbox is returning " + enabled + " for .isChecked()");
                game.getPreferences().setMusicEnabled(enabled);
                return false;
            }
        });

        // sound on/off
        final CheckBox soundEffectsCheckbox = new CheckBox(null, skin);
        soundEffectsCheckbox.setChecked(game.getPreferences().isSoundEffectsEnabled());
        soundEffectsCheckbox.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                boolean enabled = soundEffectsCheckbox.isChecked();
                game.getPreferences().setSoundEffectsEnabled(enabled);
                return false;
            }
        });

        // return to main screen button
        final TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.changeScreen(TileShipGame.MENU);

            }
        });

        // Create labels
        titleLabel = new Label( "Preferences", skin );
        volumeMusicLabel = new Label( "Music Volume", skin );
        volumeSoundLabel = new Label( "Sound Volume", skin );
        musicOnOffLabel = new Label( "Music", skin );
        soundOnOffLabel = new Label( "Sound Effect", skin );


        // Add to table
        table.add(titleLabel).colspan(2);
        table.row().pad(10,0,0,10);
        table.add(volumeMusicLabel).left();
        table.add(volumeMusicSlider);
        table.row().pad(10,0,0,10);
        table.add(musicOnOffLabel).left();
        table.add(musicCheckbox);
        table.row().pad(10,0,0,10);
        table.add(volumeSoundLabel).left();
        table.add(soundMusicSlider);
        table.row().pad(10,0,0,10);
        table.add(soundOnOffLabel).left();
        table.add(soundEffectsCheckbox);
        table.row().pad(10,0,0,10);
        table.add(backButton).colspan(2);
    }


    /**
     * Gets data for debug hud UI stack
     * @return - StringBuilder representing the debug info
     */
    private StringBuilder buildDebugUI() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("FPS " + Gdx.graphics.getFramesPerSecond() + "\n" +
                "ShipTile number : " + playerShip.shipSize());
        if(playerShip.destroyedTileCount > 0){
            stringBuilder.append("\nTiles Destroyed : " + playerShip.destroyedTileCount);
        }

        return stringBuilder;
    }

    /**
     * Get if debug HUD should be rendered
     * @return - true if should show debug hud, else should not
     */
    public boolean isShowDebugHud() {
        return showDebugHud;
    }

    /**
     * Call to toggle seeing the debug hud
     * @param showDebugHud - if true will show debug HUD, else will not
     */
    public void setShowDebugHud(boolean showDebugHud) {
        this.showDebugHud = showDebugHud;
    }

    /**
     * Called on screen resize to keep HUD consistent
     * @param width
     * @param height
     */
    public void update(int width, int height) {
        HUDScreenLayer.update(width, height, true);
    }

    public boolean drawMenu() {
        return drawMenu;
    }
    public void toggleMenu(){
        drawMenu = !drawMenu;
        if(drawMenu){
            Gdx.input.setInputProcessor(this.menuOverlay);
        } else {
            GameScreen gameScreen = (GameScreen) game.getCurrentScreen();
            gameScreen.setFocusToGame();
        }
    }
}
