package org.bitbucket.noahcrosby.shipGame;

import com.badlogic.gdx.utils.Array;
import org.bitbucket.noahcrosby.Interfaces.Tutorial;
import org.bitbucket.noahcrosby.shipGame.util.TextBubble;

public class ArcadeModeTutorial implements Tutorial {
    boolean isComplete;
    Array<TextBubble> tutorialText;

    public ArcadeModeTutorial(){
        isComplete = false;
        tutorialText = new Array<>();
    }

    @Override
    public boolean tutorialIsComplete() {
        return isComplete;
    }

    @Override
    public void stepTutorial() {
        tutorialText.forEach(textBubble -> {
            textBubble.render();
            textBubble.update();
        });
    }
}
