package org.bitbucket.noahcrosby.shipGame.util;

import com.badlogic.gdx.math.MathUtils;
import org.jetbrains.annotations.Nullable;

/**
 * f = vibe frequency/response to changes in input (Can be )
 * z = dampening - 0 < z <= 1 underdamped, will stop vibrating. z > 1 no vibration, slow x approach.
 * r = initial responsiveness -> r = 0 delayed response. 0 < r <= 1 responds quicker. r > 1 overshoots x. r < 0 anticipates action
 */
public class SecondOrderDynamics {
    private Float xp; // previous input
    private Float y, yd; // State variables
    private Float k1, k2, k3; // Dynamics constants
    public SecondOrderDynamics(Float f, Float z, Float r, Float x0){
        // Compute constants
        k1 = z / (MathUtils.PI * f);
        k2 = 1 / ((2 * MathUtils.PI * f) * (2 * MathUtils.PI * f));
        k3 = r * z / (2 * MathUtils.PI * f);
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
    public Float Update(Float T, Float x, @Nullable Float xd){
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
}
