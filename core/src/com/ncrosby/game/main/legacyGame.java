package com.ncrosby.game.main;

import com.ncrosby.game.*;
import com.ncrosby.game.Window;
import com.ncrosby.game.tiles.ShipTile;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.Random;

public class legacyGame extends Canvas implements Runnable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5662816000099174207L;

	private long window;

	public static final int WIDTH = 640, HEIGHT = WIDTH / 12 * 9;
	private Thread thread;
	private boolean running = false;
	
	@SuppressWarnings("unused")
	private Random r;
	private Handler handler;
	private static final Camera cam = new Camera(0,0);
	//private ShipHandler shiphandler = new ShipHandler(cam); 
	private HUD hud;
	
	// Main runs this first. This is the root of the entire program.
	public legacyGame() {

		System.out.println("Hello LWJGL " +  "!");


		init();
		loop();


		hud = new HUD();
		handler = new Handler();
		//shiphandler = new ShipHandler();
		this.addKeyListener(new KeyInput(handler));

		new Window(WIDTH, HEIGHT, "Let's build a game!", this);
				
		r = new Random();
		
		// Adding player and enemy
//		Player player = new Player(WIDTH/2 - 32, HEIGHT/2 - 32, ID.Player, cam);
//		Ship playerSh = player.getPlayerShip();
		//playerSh.addTileByCoord(WIDTH/2 - 32, HEIGHT/2 - 32, null, getBackground(), cam);


//		handler.addObject(player);
//		handler.addObject(player.getPlayerShip());
//		handler.addObject(new BasicEnemy(WIDTH/2 - 22, HEIGHT/2 - 22, ID.BasicEnemy, player.getPlayerShip(), cam));
//
//		this.addMouseListener(new ClickLocationListener(player, cam));
//		this.addMouseMotionListener(new MouseLocationGetter(handler, player.getPlayerShip(), cam));
	}

	/**
	 * Handles thread creation - sets running to true
	 */
	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
		running = true;
	}
	
	public synchronized void stop() {
		try {
			thread.join();
			running = false;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	// Game loop
	public void run() {
		this.requestFocus();
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while(running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1) {
				tick();
				delta--;
			}
			if(running)
				render();
			frames++;
			
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				//System.out.println("FPS: " + frames);
				frames = 0;
			}
		}
		stop();
	}
	
	// Update things
	private void tick() {
		handler.tick();
		hud.tick();
	}
	
	// Draws game, runs the buffer
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);
				
		// Order decides what gets drawn on top/bottom. 
		// Player renders the ship it owns.
		handler.render(g);
		


		hud.render(g);
		
		g.dispose();
		bs.show();
	}
	
	public static void main(String[] args) {
		
		new legacyGame();

	}
	
	public int getWindowWidth() {
		return WIDTH;
	}
	
	/**
	 * Returns the game camera object 
	 * @return
	 */
	public static Camera getCam() {
		return cam;
	}

	/**
	 * returnIndex returns the index of a tilelocation from an x,y location.
	 * Can translates negative coords to negative indexes for tile init.
	 * 
	 * @param x - The x location in the jframe to adjust by cam
	 * @param y - The y location in the jframe to adjust by cam
	 * @return
	 */
	public static int[] returnIndex(int x, int y) {
		
		boolean yNegative = (y  + cam.y <= -1);
		boolean xNegative = (x  + cam.x <= -1);

		
		int XYresult[] = new int[2];
		if (xNegative) {
			if(yNegative) {
				// x, y negative
				// get index and subtract one.
				XYresult[0] = ((x + cam.x) / ShipTile.TILESIZE) - 1;
				XYresult[1] = ((y + cam.y) / ShipTile.TILESIZE) - 1;
			}
			else {
				// only x negative
				XYresult[0] = ((x + cam.x) / ShipTile.TILESIZE) - 1;
				XYresult[1] = ((y + cam.y) / ShipTile.TILESIZE);
			}
		}
		else if (yNegative) {
			// only Y negative
			XYresult[0] = ((x + cam.x) / ShipTile.TILESIZE);
			XYresult[1] = ((y + cam.y) / ShipTile.TILESIZE) - 1;
		}
		else {
			XYresult[0] = (x + cam.x) / ShipTile.TILESIZE;
			XYresult[1] = (y + cam.y) / ShipTile.TILESIZE;
		}
		
		if(XYresult.length == 2) {
			return XYresult;
		}
		else{
			throw new ArithmeticException("Unexpected number of indexes : " + XYresult.length );
		}
	}

	private void init() {

	}

	private void loop() {

	}
}
