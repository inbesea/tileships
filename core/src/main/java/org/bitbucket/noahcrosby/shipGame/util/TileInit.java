package org.bitbucket.noahcrosby.shipGame.util;

import com.badlogic.gdx.math.Vector2;
import org.bitbucket.noahcrosby.shipGame.ID;

public class TileInit {
    public static final int ORIGIN_X = 0;
    public static final int ORIGIN_Y = 0;
    ID id;
    int x;
    int y;

    public TileInit(ID id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public ID getId() {
        return id;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
}
