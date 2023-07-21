package com.Directors;

import com.Builders.TextBoxBuilder;
import com.Interfaces.TextBoxInterface;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;

public class TextBoxDirector {
    protected TextBoxBuilder boxBuilder;
    public TextBoxDirector(TextBoxBuilder boxBuilder){
        this.boxBuilder = boxBuilder;
    }

    public TextBoxInterface getTextBubble(String text){
        boxBuilder.reset();

        boxBuilder.setText(text);

        return boxBuilder.buildProduct();
    }

    public TextBoxInterface getSoundTextBubble(String text, Sound sound){
        boxBuilder.reset();

        boxBuilder.addSounds(sound);
        boxBuilder.setText(text);

        return boxBuilder.buildProduct();
    }

    public TextBoxInterface getSoundTextBubbleWithTimeout(String text, Sound sound, int timeout){
        boxBuilder.reset();

        boxBuilder.addSounds(sound);
        boxBuilder.setText(text);
        boxBuilder.setTimeout(timeout);

        return boxBuilder.buildProduct();
    }

    public TextBoxInterface getAnchoredSoundTextBoxWithPostCrawlTimeout(String text, Sound sound, int timeout, Vector2 Anchor){
        boxBuilder.reset();

        boxBuilder.setText(text);
        boxBuilder.addSounds(sound);
        boxBuilder.setTimeout(timeout);
        boxBuilder.setCountdownAfterCrawl(true);

        return boxBuilder.buildProduct();
    }
}
