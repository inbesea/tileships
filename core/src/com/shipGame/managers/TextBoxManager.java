package com.shipGame.managers;

import com.Interfaces.TextBoxInterface;
import com.shipGame.generalObjects.GameObject;

import java.util.ArrayList;

public class TextBoxManager implements Manager{

    ArrayList<TextBoxInterface> textBoxes;

    public TextBoxManager(){
        textBoxes = new ArrayList<>();
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
}
