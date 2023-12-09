package org.bitbucket.noahcrosby.shipGame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.StringBuilder;
import org.bitbucket.noahcrosby.AppPreferences;
import org.bitbucket.noahcrosby.shipGame.LevelData.MapDrawer;
import org.bitbucket.noahcrosby.Shapes.Line;
import org.bitbucket.noahcrosby.shipGame.generalObjects.HUD.HUD;
import org.bitbucket.noahcrosby.shipGame.managers.MapNavManager;

public class MainGameHUD extends HUD {

    private MapNavManager mapNavigator;
    private MapDrawer mapDrawer; // We can put this here because it's the only place we draw the map

    public MainGameHUD(TileShipGame game, MapNavManager mapNavigator) {
        super(game);

        setMapNavigator(mapNavigator);
        this.mapDrawer = new MapDrawer(mapNavigator.currentMap); // Put this into the navigator so the HUD can be less complicated.
    }

    /**
     * Called to draw the HUD
     * Has own internal batch draw call for hud to use HUD veiwport
     */
    @Override
    public void draw(){
        super.draw();// Call basic hud draw before doing specific draws to MainGame
        StringBuilder stringBuilder = new StringBuilder();

        //Secondly draw the Hud
        game.batch.setProjectionMatrix(HUDScreenLayer.getCamera().combined); //set the spriteBatch to draw what our stageViewport sees

        if(AppPreferences.getAppPreferences().getIsDebug()){
        }

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
