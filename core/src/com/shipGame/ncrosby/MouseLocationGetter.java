package com.shipGame.ncrosby;

import com.shipGame.ncrosby.generalObjects.Ship;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import com.shipGame.ncrosby.main.legacyGame;

public class MouseLocationGetter extends MouseMotionAdapter {

	private Camera cam;
	private Ship sh;
	private Handler handler;

	public MouseLocationGetter(Handler handler, Ship sh, Camera cam) {
		this.handler = handler;
		this.sh = sh;
		this.cam = cam;
	}
	
    public void mouseMoved(MouseEvent e)
    {
		int xMouse = e.getX();
		int yMouse = e.getY();
		
		//System.out.println(xMouse + ", " + yMouse);

		// Meant to update the shipHandler pointLocation so it can draw a square around the index the mouse is in. 
		int[] index = legacyGame.returnIndex(xMouse, yMouse);
		//int[] index = {xMouse, yMouse};
		//System.out.println(index[0] + ", " + index[1]);

		sh.setPointLocation(index);
    }
}
