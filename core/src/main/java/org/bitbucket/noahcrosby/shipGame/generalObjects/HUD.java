package org.bitbucket.noahcrosby.shipGame.generalObjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import org.bitbucket.noahcrosby.AppPreferences;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.StringBuilder;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import org.bitbucket.noahcrosby.Shapes.Line;
import org.bitbucket.noahcrosby.javapoet.Resources;
import org.bitbucket.noahcrosby.shipGame.TileShipGame;
import org.bitbucket.noahcrosby.shipGame.generalObjects.Ship.Ship;
import org.bitbucket.noahcrosby.shipGame.managers.MapNavManager;
import org.bitbucket.noahcrosby.shipGame.screens.GameScreen;

/**
 * Layer of HUD elements. Is on a FitViewport to keep hud elements scaled correctly.
 */
public class HUD {
    protected ExtendViewport HUDScreenLayer;
    protected ExtendViewport mapScreenLayer;
    protected TileShipGame game;
    protected Ship playerShip;
    protected boolean drawMenu = false;
    boolean drawMap = false;
    protected Stage menuOverlay;
    protected boolean showDebugHud = true;

    protected Label titleLabel;
    protected Label volumeMusicLabel;
    protected Label volumeSoundLabel;
    protected Label musicOnOffLabel;
    protected Label soundOnOffLabel;
    protected MapNavManager mapNavigator;

    public HUD(TileShipGame game) {
        this.game = game;
        this.playerShip = game.getPlayerShip();

        HUDScreenLayer = new ExtendViewport(TileShipGame.defaultViewportSizeX, TileShipGame.defaultViewportSizeY);
        mapScreenLayer = new ExtendViewport(TileShipGame.defaultViewportSizeX, TileShipGame.defaultViewportSizeY);

        createMenuOverlay();
    }

    /**
     * Called to draw the HUD
     * Has own internal batch draw call for hud to use HUD veiwport
     */
    public void draw(){
        StringBuilder stringBuilder = new StringBuilder();

        //Secondly draw the Hud
        game.batch.setProjectionMatrix(HUDScreenLayer.getCamera().combined); //set the spriteBatch to draw what our stageViewport sees

        if(AppPreferences.getAppPreferences().getIsDebug()){
            Line.drawRectangle(new Vector2(0,0),
                new Vector2(HUDScreenLayer.getWorldWidth(), HUDScreenLayer.getWorldHeight()),
                Color.GREEN,
                false,
                HUDScreenLayer.getCamera().combined);
        }

        game.batch.begin();

        if(AppPreferences.getAppPreferences().getIsDebug()){
            stringBuilder.append("\n" + getDebugUIData());
            game.font.draw(game.batch, stringBuilder.toString() , 2,
                    (HUDScreenLayer.getWorldHeight() - 2)); // Worldheight gives the extendVeiwport hight
        }

        if(this.isShowingMenu()){
            menuOverlay.act(Gdx.graphics.getDeltaTime());
            menuOverlay.draw();
        }

        drawControls();

        game.batch.end();

        if(showingMap()){
            mapNavigator.drawMap(this.mapScreenLayer.getCamera().combined);
        }
    }

    /**
     * Draws Player input indicators
     */
    protected void drawControls() {
        TileShipGame.batch.draw(Resources.ConstallationMapTexture, HUDScreenLayer.getWorldWidth() - 100, 20, 64, 64);

        if(playerShip.isCollectingTiles()){
            TileShipGame.batch.draw(Resources.CraftingIconPressedTexture, HUDScreenLayer.getWorldWidth() - 100, 104, 64, 64);
        }else {
            TileShipGame.batch.draw(Resources.CraftingIconTexture, HUDScreenLayer.getWorldWidth() - 100, 104, 64, 64);
        }
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
    protected StringBuilder getDebugUIData() {
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

    /**
     * Checks if the menu should be drawn
     * @return - true if menu should be drawn, else false
     */
    public boolean isShowingMenu() {
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

    /**
     * Switches value of HUD.drawMap
     * @return - true if map should be drawn, else false
     */
    public boolean toggleMap() {
        drawMap = !drawMap;
        mapNavigator.setDrawing(drawMap);
        return drawMap;
    }

    /**
     * Check if the map should be drawn
     * @return - true if map should be drawn, else false
     */
    public boolean showingMap() {
        return drawMap;
    }

    public void setMapNavigator(MapNavManager mapNavigator) {
        this.mapNavigator = mapNavigator;
    }
}
