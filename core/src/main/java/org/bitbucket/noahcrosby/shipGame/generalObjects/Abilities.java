package org.bitbucket.noahcrosby.shipGame.generalObjects;

import com.badlogic.gdx.math.Circle;

public class Abilities {

    Circle circleOfAffect = new Circle();

    public Abilities(){
        circleOfAffect.radius = 200;
    }

    public Circle getCircleOfAffect() {
        return circleOfAffect;
    }
    public void setCircleOfAffect(int radius){
        circleOfAffect.radius = radius;
    }
}
