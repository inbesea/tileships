package org.bitbucket.noahcrosby.shipGame.util;

import com.badlogic.gdx.math.MathUtils;

/**
 * f = vibe frequency/response to changes in input (Can be )
 * z = dampening - 0 < z <= 1 underdamped, will stop vibrating. z > 1 no vibration, slow x approach.
 * r = initial responsiveness -> r = 0 delayed response. 0 < r <= 1 responds quicker. r > 1 overshoots x. r < 0 anticipates action
 */
public class SecondOrderDynamics {
    private Float xp; // previous input
    private Float y, yd; // State variables
    private Float k1, k2, k3; // Dynamics constants

    private Float f;
    private Float z;
    private Float r;
    private Float x0;

    public SecondOrderDynamics(Float f, Float z, Float r, Float x0){
        this.f = f;
        this.z = z;
        this.r = r;
        this.x0 = x0;

        computeConstants();
        initVars();
    }

    /**
     * Calculates three constants k1, k2, and k3 used in second-order dynamics based on the values of z, f, and r.
     * Can be re-run if f, z, or r are changed to update behavior.
     */
    private void computeConstants(){
        // Compute constants
        k1 = z / (MathUtils.PI * f);
        k2 = 1 / ((2 * MathUtils.PI * f) * (2 * MathUtils.PI * f));
        k3 = r * z / (2 * MathUtils.PI * f);
    }

    /**
     * Initialize state variables
     * Should only be called once.
     */
    private void initVars(){
        // Initialize variables
        xp = x0;
        y = x0;
        yd = (float) 0;
    }

    /**
     * Initialize state variables
     * Can be called multiple times with new x0.
     */
    private void initVars(Float x0){
        // Initialize variables
        xp = x0;
        y = x0;
        yd = (float) 0;
    }

    /**
     *
     * @param T = time diff
     * @param x - target location
     * @param xd - change in target location (velocity)
     * @return
     */
    public Float Update(Float T, Float x, Float xd){
        if(xd == null){ // Estimate Velocity
            xd = (x - xp) / T;
            xp = x;
        }
        y = y + T * yd; // integrate position by velocity
        yd = yd + T * (x + k3*xd - y - k1*yd) / k2; // integrate velocity by acceleration
        return y;
    }

    public Float getXp() {
        return xp;
    }

    /**
     * Set previous input
     * @param xp - new previous input
     */
    public void setXp(Float xp) {
        this.xp = xp;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }
    public Float getF() {
        return f;
    }

    public void setF(Float f) {
        this.f = f;
        computeConstants();
    }

    public Float getZ() {
        return z;
    }

    public void setZ(Float z) {
        this.z = z;
        computeConstants();
    }

    public Float getR() {
        return r;
    }

    public void setR(Float r) {
        this.r = r;
        computeConstants();
    }
}
