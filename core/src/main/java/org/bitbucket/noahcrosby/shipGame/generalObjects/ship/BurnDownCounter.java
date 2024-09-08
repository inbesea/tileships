package org.bitbucket.noahcrosby.shipGame.generalObjects.ship;

import org.bitbucket.noahcrosby.shipGame.util.generalUtil;

public class BurnDownCounter extends Counter{

    Double burndownAmt;

    /*
     * The plan here is to have a conditional burndown roll each time we move between systems. 
     * We have several outcomes here. We can 
     * 1. Roll fine : the move happens without adverse consequenses. 
     * 2. Roll burndown with room : roll below the threshold and lose some fuel. 
     * 3. Roll burndown without room : roll below the threshold and have no fuel left, stranding you in space. 
     * 
     * visually this can be a segmented gage that we roll on. 
     */
    public BurnDownCounter (Double burnoutThreshold, Double max, Double burndownAmt, Double maxFail){
        super(burnoutThreshold, max);

        this.burndownAmt = burndownAmt;
        
    }

    /**
     * Rolls a check to see if we burndown or not. 
     */
    public void check(){
        Double rolledValue = generalUtil.getRandomNumber(1, super.getCapacity());
        
        // Check if rolled value is good enough
        if(rolledValue <= super.count){
            burnout();
        }
    }

    /**
     * subtract burndownAmt from capacity
     *  */ 
    private void burnout() {
        super.capacity -= burndownAmt;
    }

}
