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
    public BurnDownCounter (Double burnoutThreshold, Double max, Double burndownAmt){
        super(burnoutThreshold, max);

        this.burndownAmt = burndownAmt;

    }

    /**
     * Rolls a check to see if we burndown or not.
     */
    public void check(){
        double rolledValue = generalUtil.getRandomNumber(1, super.getCapacity());

        // Check if rolled value is good enough
        if(rolledValue <= super.count){
            burnout();
        }
    }

    /**
     * Use passed value as check value
     * @param rolledValue - Value to check burnout on
     */
    public boolean check(double rolledValue){
        if(rolledValue <= super.count){
            return burnout();
        }
        return isBurnedOut();
    }

    /**
     * subtract burndownAmt from capacity
     *  */
    private boolean burnout() {
        super.capacity -= burndownAmt;
        return isBurnedOut();
    }

    /**
     *
     * @return
     */
    public boolean isBurnedOut() {
        return super.capacity <= super.count;
    }

    public float burnoutChance(){
        float chance = count.floatValue() / capacity.floatValue();
        return generalUtil.round(chance,2);
    }
}
