package org.bitbucket.noahcrosby.shipGame.generalObjects.background;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.bitbucket.noahcrosby.javapoet.Resources;
import org.bitbucket.noahcrosby.shipGame.levelData.ForegroundObject;
import org.bitbucket.noahcrosby.shipGame.TileShipGame;
import org.bitbucket.noahcrosby.shipGame.physics.box2d.Box2DWrapper;

import java.util.Objects;

public class GalaxyBackGround {
    private Boolean[][] stars;

    Integer fieldSizeX;
    Integer fieldSizeY;
    float density;
    // Objects to draw infront of everything.
    private Array<ForegroundObject> foregroundObjects = new Array<>();
    private final Box2DWrapper physics;

    public GalaxyBackGround(Integer x, Integer y, float percentStars){
        physics = new Box2DWrapper(new Vector2(0,0), true);
        fieldSizeX = x;
        fieldSizeY = y;
        stars = new Boolean[x][y];
        this.density = percentStars;
        populateArray();
    }

    private void populateArray(){
        // Take each element and set to 1 or 0, 1 being a star, and 0 being nothing
        for(int i = 0; i < fieldSizeX; i++){
            for (int n = 0; n < fieldSizeY; n++){
                if(stars[i][n] != null) continue; // Don't repopulate

                // If the likelyhood is bigger than a random amount then get stuff.
                stars[i][n] = Math.random() < density; // is a star. If density was 0 we would never get anything.
            }
        }
    }

    /**
     * Simple draw statement that loops the stars giving them all the same texture and size lol
     */
    public void draw(TileShipGame game){
        TileShipGame.batch.begin();
        Texture starTexture = Resources.AsteroidSilverTexture;
        for(int i = 0; i < fieldSizeX; i++){
            for (int n = 0; n < fieldSizeY; n++){
                Vector2 size = new Vector2(1,1);
                if(stars[i][n]){
                    // Terrible star drawing method. Uses the silver asteroid texture
                    TileShipGame.batch.draw(starTexture, i, n, size.x, size.y);
                }
            }
        }
        ForegroundObject object;
        for(int i = 0; i < foregroundObjects.size ; i++){
            object = foregroundObjects.get(i);
            object.render(game);
        }
        TileShipGame.batch.end();

        physics.stepPhysicsSimulation(Gdx.graphics.getDeltaTime());
    }

    public void update(Integer width, Integer height) {
        Gdx.app.log("", "Resizing the galaxybackground " + width + "," + height);
        // First updating the bounds if they're bigger
        Integer newX = this.fieldSizeX;
        Integer newY = this.fieldSizeY;
        boolean newWidthBigger = width > this.fieldSizeX;
        boolean newHightBigger = height > this.fieldSizeY;
        if(newWidthBigger){ // Resizing the bounds, and trying to generate new values
            newX = width;
        }
        if(newHightBigger){
            newY = height;
        }

        if(Objects.equals(this.fieldSizeY, newY) &&
            Objects.equals(this.fieldSizeX, newX))return; // No changes needed if the new size is not different

        this.fieldSizeY = newY;
        this.fieldSizeX = newX;

        Boolean[][] newStars = new Boolean[(int)newX][(int)newY];

        for(int i = 0 ; i < stars.length ; i++){
            System.arraycopy(stars[i], 0 , newStars[i], 0, stars[i].length);
        }
        stars = newStars;

        populateArray(); // Repopulate for new null spaces
    }

    /**
     * Updates the foreground objects (Unimplemented)
     * Could make each foreground object update itself.
     */
    public void updateForeground(Array<ForegroundObject> newForegroundObjs) {
        this.foregroundObjects = newForegroundObjs;
        Gdx.app.debug("ForegroundObjects", "Updating foreground objects is unimplemented!");

        for(int i = 0; i < foregroundObjects.size; i++){
            foregroundObjects.get(i).update();
        }
    }

    public void addForegroundObject(ForegroundObject foregroundObject) {
        this.foregroundObjects.add(foregroundObject);
    }
}
