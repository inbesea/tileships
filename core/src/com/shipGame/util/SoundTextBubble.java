package com.shipGame.util;

import com.Shapes.Tentacle;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.javapoet.Resources;
import com.shipGame.TileShipGame;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class SoundTextBubble extends TextBubble {
    private Sound letterSound;
    private Sound[] letterSounds;

    Tentacle tentacle;
    public SoundTextBubble(String text, int millisecondsBetweenLetters, Sound letterSound) {
        super(text, millisecondsBetweenLetters);
        this.letterSound = letterSound;
        tentacle = new Tentacle(40, 2, 1 , 20 , Color.WHITE, Color.WHITE);
    }

    public SoundTextBubble(String text, int millisecondsBetweenLetters, Sound[] letterSounds) {
        super(text, millisecondsBetweenLetters);
        this.letterSounds = letterSounds;
        tentacle = new Tentacle(40, 2, 1 , 20 , Color.WHITE, Color.WHITE);
    }


    // When using this we want to be able to call this method, pass text, and crawl speed.
    // sounds are the new stuff.
    // a lot of the super logic is half-baked I would say.

    @Override
    public void update(Vector2 location){
        if(dead){return;} // Dont print text that's expired
        if(begin == null)begin = System.currentTimeMillis(); // First print will cause the beginning to be set

        deltaFromStart = System.currentTimeMillis() - begin;

        if(letterSound != null){letterSound();}
        else if(letterSounds != null){letterSounds();}

        lastChar = getLastChar();
        intermediateString = this.text.substring(0, lastChar);

        print(location);
    }

    /**
     * Creates placement sounds from list
     */
    private void letterSounds() {
        if(lastChar != Math.min((int) (deltaFromStart / millisecondsBetweenLetters), text.length())){
            letterSounds[(int) generalUtil.getRandomNumber(0, letterSounds.length)].play(25);
        }
    }

    /**
     * Creates single sound
     */
    private void letterSound() {
        int newLastChar = Math.min((int) (deltaFromStart / millisecondsBetweenLetters), text.length());
        System.out.println(newLastChar + ", " + lastChar + " These are the two char vars");
        if(lastChar != newLastChar){
            System.out.println("Playing the sound!!!");
            letterSound.play();
        }
    }
}
