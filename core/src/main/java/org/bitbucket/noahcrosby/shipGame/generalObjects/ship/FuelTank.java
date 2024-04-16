package org.bitbucket.noahcrosby.shipGame.generalObjects.ship;

import org.bitbucket.noahcrosby.shipGame.util.generalUtil;

// TODO: Pull functionality into a bucket class that can be inherited
// Probably could make the tracked thing generic making it even more extensible
public class FuelTank {
    Integer fuel = 0;
    Integer fuelCapacity = 5;

    public FuelTank(Integer fuel) {
        this.fuel = fuel;
    }

    public FuelTank(Integer fuel, Integer fuelCapacity) {
        this.fuel = fuel;
        this.fuelCapacity = fuelCapacity;
    }

    public Integer getFuelCapacity() {
        return fuelCapacity;
    }

    public void setFuelCapacity(Integer fuelCapacity) {
        this.fuelCapacity = fuelCapacity;
    }

    public void setFuel(Integer fuel) {
        this.fuel = fuel;
    }

    public Integer getFuel() {
        return fuel;
    }

    public void addFuel(Integer amount) {
        fuel += amount;
        generalUtil.clamp(fuel, 0, fuelCapacity);
    }

    public void consumeFuel(Integer amount) {
        fuel -= amount;
        generalUtil.clamp(fuel, 0, fuelCapacity);
    }
}
