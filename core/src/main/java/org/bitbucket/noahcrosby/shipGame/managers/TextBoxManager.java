package org.bitbucket.noahcrosby.shipGame.managers;

import org.bitbucket.noahcrosby.builders.TextBoxBuilder;
import org.bitbucket.noahcrosby.directors.TextBoxDirector;
import org.bitbucket.noahcrosby.interfaces.TextBoxInterface;
import com.badlogic.gdx.math.Vector2;
import org.bitbucket.noahcrosby.shipGame.generalObjects.GameObject;

import java.util.ArrayList;

/**
 * This class holds, renders, creates, and deletes text bubbles in game.
 *
 * It is flexible enough to be applied to any screen.
 */
public class TextBoxManager implements Manager{

    ArrayList<TextBoxInterface> textBoxes;
    TextBoxDirector boxDirector;
    TextBoxBuilder boxBuilder;

    public TextBoxManager(){
        textBoxes = new ArrayList<>();

        boxBuilder = new TextBoxBuilder();
        boxDirector = new TextBoxDirector(boxBuilder);
        // TODO : Add back these textbubbles with the manager handling updates, and the director creating instances.
//        textBubble = new SoundTextBubble("Hello, this is a test message", 300, Resources.sfxCollectTileSound, player.getPosition());
//
//        textBubble1 = new SoundTextBubble("This is also a test message", 300, Resources.sfxCollectTileSound, player.getPosition());
    }

    @Override
    public boolean deleteMember(GameObject gameObject) {
        try{
            TextBoxInterface textBox = (TextBoxInterface) gameObject;
            textBox.kill();
            return true;
        } catch (ClassCastException cce){
            System.out.println("Failed to cast TextBoxInterface!! - " + cce);
            return false;
        }
    }

    public void render(){
        for(TextBoxInterface textBox : textBoxes){
            textBox.render();
        }
    }

    /**
     * Basic text bubble, timeout, fadeout, etc. all defaulted.
     * @param text
     */
    public void newTextBubble(String text){
        TextBoxInterface textBubble = boxDirector.getTextBubble(text);
        this.textBoxes.add(textBubble);
    }

    /**
     * Creates a text bubble with text and position.
     * @param text - display text
     * @param position - position of the textbubble
     */
    public void newTextBubble(String text, Vector2 position){
        TextBoxInterface textBubble = boxDirector.getTextBubble(text, position);
        this.textBoxes.add(textBubble);
    }

}
