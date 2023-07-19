package com.shipGame.util;

import com.Interfaces.TextBoxInterface;
import com.Shapes.Line;
import com.Shapes.Tentacle;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.javapoet.Resources;
import com.shipGame.TileShipGame;
import com.shipGame.generalObjects.Ship.tiles.tileTypes.ShipTile;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.util.ArrayList;

public class SoundTextBubble extends TextBubble implements TextBoxInterface {
    private ArrayList<Sound> letterSounds;
    ShapeDrawer shapeDrawer;
    Tentacle tentacle;
    Vector2 target;
    private boolean firstUpdate = true;

    private int arrowSegments = 75;

    //TODO : Fix this shit class hiarchy. The tentacle thing needs to be ripped out of the constructor, or made optional. Builder much???
    public SoundTextBubble(String text, int millisecondsBetweenLetters, ArrayList<Sound> letterSounds, Vector2 target) {
        super(text, millisecondsBetweenLetters);
        this.letterSounds = letterSounds;

        if(target != null){
            this.target = target;
            tentacle = new Tentacle(arrowSegments, 2, 1 , 20 , Color.WHITE, Color.WHITE);
            shapeDrawer = createShapeDrawer();
        }
    }

    public SoundTextBubble(String text, int millisecondsBetweenLetters, Sound letterSounds, Vector2 target) {
        super(text, millisecondsBetweenLetters);
        this.letterSounds = new ArrayList<>();
        this.letterSounds.add(letterSounds);

        if(target != null){
            this.target = target;
            tentacle = new Tentacle(arrowSegments, 2, 1 , 20 , Color.WHITE, Color.WHITE);
            shapeDrawer = createShapeDrawer();
        }
    }

    private ShapeDrawer createShapeDrawer() {
        ShapeDrawer shapeDrawer1 = new ShapeDrawer(TileShipGame.batch);
        shapeDrawer1.setTextureRegion(new TextureRegion(Resources.ShipTileStrongTexture, 20, 20, 20, 20));
        return shapeDrawer1;
    }

    // When using this we want to be able to call this method, pass text, and crawl speed.
    // sounds are the new stuff.
    // a lot of the super logic is half-baked I would say.

    @Override
    public void update(Vector2 textBoxLocation){
        if(dead){return;} // Dont print text that's expired
        if(firstUpdate){
            begin = System.currentTimeMillis(); // First print will cause the beginning to be set
//            if(tentacle != null){
//                tentacle.setPosition(textBoxLocation.x,textBoxLocation.y - ShipTile.TILE_SIZE/2);
//                tentacle.lock();
//            }
            //
            //Line[] lines = tentacle.getLines().setPosition(textBoxLocation);
            firstUpdate = false;
        }

        deltaFromStart = System.currentTimeMillis() - begin;

        if(letterSounds != null){letterSounds();}

        lastChar = getLastChar();
        intermediateString = this.text.substring(0, lastChar);

        if(tentacle != null){
//            tentacle.translateX(textBoxLocation.x);
//            tentacle.translateY(textBoxLocation.y - ShipTile.TILE_SIZE/2);
            tentacle.follow(target);
            tentacle.inverseFollow(textBoxLocation);
            tentacle.draw(shapeDrawer);
        }

        print(textBoxLocation);
    }

    /**
     * Creates placement sounds from list
     */
    private void letterSounds() {
        if(lastChar != Math.min((int) (deltaFromStart / millisecondsBetweenLetters), text.length())){
            letterSounds.get((int) generalUtil.getRandomNumber(0, letterSounds.size())).play(25);
        }
    }
}
