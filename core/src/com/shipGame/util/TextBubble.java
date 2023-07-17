package com.shipGame.util;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.shipGame.TileShipGame;
import com.shipGame.player.PlayerInput;

public class TextBubble {
    Long begin;
    long delta;
    int millisecondsBetweenLetters;
    int lastChar;

    Sound letterSound;
    Sound[] letterSounds;

    String text;
    String intermediateString;
    public TextBubble(String text, int millisecondsBetweenLetters){
        begin = System.currentTimeMillis();
        this.text = text;
        this.millisecondsBetweenLetters = millisecondsBetweenLetters;
    }

    public TextBubble(String text, int millisecondsBetweenLetters, Sound letterSound){
        begin = System.currentTimeMillis();
        this.text = text;
        this.millisecondsBetweenLetters = millisecondsBetweenLetters;
        this.letterSound = letterSound;
    }

    public TextBubble(String text, int millisecondsBetweenLetters, Sound[] letterSounds){
        begin = System.currentTimeMillis();
        this.text = text;
        this.millisecondsBetweenLetters = millisecondsBetweenLetters;
        this.letterSounds = letterSounds;
    }

    /**
     * Call each render to print value
     */
    public void print(Vector2 location){
        if(begin == null)begin = System.currentTimeMillis();

        delta = System.currentTimeMillis() - begin;

        if(letterSound != null){letterSound();}
        else if(letterSounds != null){letterSounds();}


        lastChar = Math.min((int) (delta/ millisecondsBetweenLetters), text.length());
        intermediateString = this.text.substring(0, lastChar);
        TileShipGame.font.draw(TileShipGame.batch, intermediateString, location.x, location.y);
    }

    /**
     * Creates placement sounds from list
     */
    private void letterSounds() {
        if(lastChar != Math.min((int) (delta/ millisecondsBetweenLetters), text.length())){
            letterSounds[(int) generalUtil.getRandomNumber(0, letterSounds.length)].play(25);
        }
    }

    /**
     * Creates single sound
     */
    private void letterSound() {
        if(lastChar != Math.min((int) (delta/ millisecondsBetweenLetters), text.length())){
            letterSound.play(0.25f);
        }
    }
}
