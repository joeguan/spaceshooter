/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceshooter;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.Random;

/**
 *
 * @author ralph
 */
public class Game extends Canvas implements Runnable {
    
    // main linked list of objects rendered to screen
    private Handler handler;
    // frame size
    public static final int WIDTH = 800, HEIGHT = WIDTH /12 *9;
    // first thread for game
    private Thread thread;
    // main boolean for if the game is running
    private boolean running = false;
    // test for randoms
    Random r = new Random();
    int increment = -400;
    // heads up display, health and shit
    private HUD hud;
    // spawner for levels
    private Spawner spawner;
    // tracking time dawg
    private int seconds;
    private int minutes;
    private int hours;
    Clock c;
    // contructor for game
    
    public Game(){
        handler = new Handler();
        this.addKeyListener(new KeyInput(handler));
        new Window(WIDTH,HEIGHT,"StarShooter pre-alpha",this);
        hud = new HUD();
        spawner = new Spawner(handler, hud);
        // time stuff
        seconds = 0;
        minutes = 0;
        hours = 0;
        c = new Clock();

        // test
            
         handler.addObject(new Player(WIDTH/2-32,HEIGHT/2-32,ID.Player,handler));
       
         
        // test level 1
       for(int i = 0; i <= 5; i++){
           handler.addObject(new BasicFighter((WIDTH + increment)/2-32,HEIGHT/7,ID.BasicFighter,handler));
         //  handler.addObject(new BasicLaser((WIDTH + increment)/2-16,HEIGHT/7,ID.BasicLaser,handler));
           increment += 200; 
       } 
      
    }
    // start method
    public synchronized void start(){
        thread = new Thread(this);
      //                  System.out.println("this");
        thread.start();
        running = true;
    }
    // stop method
     public synchronized void stop(){
        try{
            // thread.join is to stop the game
            thread.join();
            running = false;
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
     new Game();
    }

    @Override
    public void run() {
        this.requestFocus();

        //game loop
        // nano seconds for frame rate
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while(running){
            long now = System.nanoTime();
            delta += (now - lastTime)/ns;
            lastTime = now;
            while(delta >= 1){
                tick();
                delta--;
            }
            if(running){
                render();
                frames++;
            }
               
            if(System.currentTimeMillis() - timer > 1000){
                timer += 1000;
                System.out.println("FPS: "+ frames);
                frames = 0;
                timer++;
              //  System.out.println(c.deltaSeconds);

            }

            if(timer == 100000){
                System.out.println(timer);
            }     
        }
        
         stop();    
    }
    private void tick(){
        handler.tick();
        hud.tick();
        spawner.tick();
        c.tick();     
    }
    
    private void render(){
        // main color method for rendering to window, graphics
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null){
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        
        
        handler.render(g);
        hud.render(g);
        
        
        g.dispose();
        bs.show();
       
    }
    
    public static int clamp(int var, int min, int max){
        // boundries of window
        
        if(var >= max)return var = max;
        else if(var <= min) return var = min;
        else return var;
        
    }
 
}
