package org.bitbucket.noahcrosby.shipGame.generalObjects.Background;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import org.apache.commons.lang3.tuple.Pair;
import org.bitbucket.noahcrosby.javapoet.Resources;
import org.bitbucket.noahcrosby.shipGame.TileShipGame;

public class GalaxyBackGround {
    private Boolean[][] stars;
    Pair fieldSize;
    float density;
    public GalaxyBackGround(int x, int y, float percentStars){
        fieldSize = Pair.of(x, y);
        stars = new Boolean[x][y];
        this.density = percentStars;
        populateArray();
    }

    private void populateArray(){
        // Take each element and set to 1 or 0, 1 being a star, and 0 being nothing
        for(int i = 0; i < (int) fieldSize.getLeft() ; i++){
            for (int n = 0; n < (int) fieldSize.getRight() ; n++){
                if(stars[i][n] != null) continue; // Don't repopulate

                if(Math.random() < density){ // If the likelyhood is bigger than a random amount then get stuff.
                    stars[i][n] = true; // is a star. If density was 0 we would never get anything.
                } else {
                    stars[i][n] = false;
                }
            }
        }
        return;
    }

    /**
     * Simple draw statement that loops the stars giving them all the same texture and size lol
     * @param matrix4
     */
    public void draw(Matrix4 matrix4){
        TileShipGame.batch.begin();
        for(int i = 0; i < (int) fieldSize.getLeft() ; i++){
            for (int n = 0; n < (int) fieldSize.getRight() ; n++){
                Vector2 size = new Vector2(1,1);
                if(stars[i][n]){
                    // Terrible star drawing method. Uses the silver asteroid texture
                    TileShipGame.batch.draw(Resources.AsteroidSilverTexture, i, n, size.x, size.y);
                }
            }
        }
        TileShipGame.batch.end();
    }

    public void update(int width, int height) {
        Gdx.app.log("", "Resizing the galaxybackground " + width + "," + height);
        // First updating the bounds if they're bigger
        Pair newSize = Pair.of(this.fieldSize);
        boolean newWidthBigger = width > (int)this.fieldSize.getLeft();
        boolean newHightBigger = height > (int)this.fieldSize.getRight();
        if(newWidthBigger){ // Resizing the bounds, and trying to generate new values
            newSize = Pair.of(width, newSize.getRight());
        }
        if(newHightBigger){
            newSize = Pair.of(newSize.getLeft(), height);
        }

        if(this.fieldSize.getRight() == newSize.getRight() &&
        this.fieldSize.getLeft() == newSize.getLeft())return; // No changes needed if the new size is not different

        this.fieldSize = newSize;

        Boolean newStars[][] = new Boolean[(int)newSize.getLeft()][(int)newSize.getRight()];

        for(int i = 0 ; i < stars.length ; i++){
            System.arraycopy(stars[i], 0 , newStars[i], 0, stars[i].length);
        }
        stars = newStars;

        populateArray(); // Repopulate for new null spaces
    }
}
