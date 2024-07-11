package org.bitbucket.noahcrosby.shipGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.StringBuilder;
import com.github.tommyettinger.textra.TypingLabel;
import org.bitbucket.noahcrosby.shipGame.levelData.MapDrawer;
import org.bitbucket.noahcrosby.shapes.Line;
import org.bitbucket.noahcrosby.shipGame.generalObjects.hud.HUD;
import org.bitbucket.noahcrosby.shipGame.managers.MapNavManager;

public class MainGameHUD extends HUD {

    private MapDrawer mapDrawer; // We can put this here because it's the only place we draw the map
    protected TypingLabel storeLabel;
    Skin skin = new Skin(Gdx.files.internal("skin/neon/skin/uiskin.json"));

    public MainGameHUD(TileShipGame game, MapNavManager mapNavigator) {
        super(game);

        setMapNavigator(mapNavigator);
        this.mapDrawer = new MapDrawer(mapNavigator.currentMap); // Put this into the navigator so the hud can be less complicated.

        storeLabel = new TypingLabel("Is Store Location : {VAR=ISSTORE}",
            skin);
        storeLabel.skipToTheEnd();
        HUDTable.add(storeLabel).pad(25);
    }

    /**
     * Called to draw the hud
     * Has own internal batch draw call for hud to use hud veiwport
     */
    @Override
    public void draw(){
        super.draw();// Call basic hud draw before doing specific draws to MainGame
        StringBuilder stringBuilder = new StringBuilder();

        //Secondly draw the Hud
        TileShipGame.batch.setProjectionMatrix(HUDScreenLayer.getCamera().combined); //set the spriteBatch to draw what our stageViewport sees

        storeLabel.setText("Is Store Location : {VAR=ISSTORE}");
        storeLabel.setVariable("ISSTORE",
            mapNavigator.getCurrentNode().isStoreLocation().toString());

        if(this.showingMap()){
            drawMap();
        }
    }

    protected void drawMap() {
        // Opaque block for aesthetics
        Line.drawRectangle(new Vector2(0,0), new Vector2(HUDScreenLayer.getWorldWidth(), HUDScreenLayer.getWorldHeight()),
            new Color(0,0,0,0.5f), true, HUDScreenLayer.getCamera().combined);

        // Get the map and draw it
        mapDrawer.drawMap(HUDScreenLayer.getCamera().combined);

    }
}
