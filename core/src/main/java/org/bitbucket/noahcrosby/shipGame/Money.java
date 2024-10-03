package org.bitbucket.noahcrosby.shipGame;

public class Money {
    public static Float money = 0f;
    public static void addCoin(){
        money += 0.25f;
    }
    public static Float getMoney(){
        return money;
    }
}
