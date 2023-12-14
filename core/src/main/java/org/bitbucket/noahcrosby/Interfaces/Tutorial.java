package org.bitbucket.noahcrosby.Interfaces;

/**
 * This will be implemented to tutorial objects
 *
 * When implementing, objects related to the game will need to have a good number of control methods for the tutorial to work with.
 * This is because the tutorial will need to know about the game it's working with to control the flow of the game.
 */
public interface Tutorial {
    // Game will check this to see if tutorial is complete
    public boolean tutorialIsComplete();
    // Game will call this method to step the tutorial
    public void stepTutorial();
}
