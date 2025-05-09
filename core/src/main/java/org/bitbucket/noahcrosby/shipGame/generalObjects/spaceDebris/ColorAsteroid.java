package org.bitbucket.noahcrosby.shipGame.generalObjects.spaceDebris;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import org.bitbucket.noahcrosby.shipGame.ID;
import org.bitbucket.noahcrosby.shipGame.generalObjects.tiles.tileTypes.ArcadeColors;

public class ColorAsteroid extends Asteroid {
    private ArcadeColors color;
    private Texture colorTexture;

    public ColorAsteroid(Vector2 position, Vector2 size, ID id) {
        super(position, size, id);

        color = ArcadeColors.getRandomColor();
        setColor(color);
    }

    @Override
    public Texture getTexture() {
        return colorTexture;
    }

    /**
     * When a color tile is set to a specific color it will update the texture reference
     * @param color
     */
    public void setColor(ArcadeColors color){
        this.color = color;
        this.colorTexture = ArcadeColors.getAsteroidTexture(color);
    }

    public ArcadeColors getColor(){
        return color;
    }
}
