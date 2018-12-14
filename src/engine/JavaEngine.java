package engine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.Locale;

import javax.swing.JFrame;

public abstract class JavaEngine extends JFrame implements Runnable, KeyListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3841844401025841597L;
	
	public static int WIDTH, HEIGHT;
	
	public double FPS; // the wanted FPS. If want max (benchmark), set to something like 10000.0
	public double nsPrFrame; 
	
	public JavaEngine (int width, int height, double fps)
	{
		JavaEngine.WIDTH = width;
		JavaEngine.HEIGHT = height;
		this.FPS = fps;
		this.nsPrFrame = Math.pow(10.0, 9.0) / FPS;
		
		addKeyListener(this);
		setPreferredSize(new Dimension(WIDTH,HEIGHT));
		setIgnoreRepaint(true);
		setLocation(0, 0);
		setResizable(false);
		setUndecorated(true);
		//setAlwaysOnTop(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
		
		init();
	}
	
	
	public String getFormattedCurrentFPS()
	{
		return String.format(Locale.US,"FPS: %4.2f", currentFps);
	}
	
	public String getFormattedAvgUpdateTime()
	{
		return String.format(Locale.US,"Update: %4.2f ms", avgUpdateTime);
	}
	
	public String getFormattedAvgRenderTime()
	{
		return String.format(Locale.US,"Render: %4.2f ms", avgRenderTime);
	}

	// implement these methods in concrete game
	public abstract void init();
	public abstract void render(Graphics2D g);
	public abstract void update(double ns);

	boolean running = true;

	double startTime = System.nanoTime();
	double fpsCountPassedTime = 0;
	
	double beginFrame = System.nanoTime();
	double frameTime;
	double lastFrame;
	double endFrame;
	double delta;

	double frameCount = 0;
	double currentFps = -1;
	final double fpsCalcTime = Math.pow(10, 9) * 2; //1 sec in ns.
	final double oneSecNs = Math.pow(10, 9); //1 sec in ns.
	final double tenE6 = Math.pow(10, 6);
	double fpsCalcTimer = fpsCalcTime;
	
	double avgFrameTime = 0;
	
	long idealPassedTime = 0;
	long realPassedTime = 0;

	double sumUpdateTime = 0, sumRenderTime = 0, avgUpdateTime = 0, avgRenderTime = 0, time = 0;

	protected boolean renderStats = false;
	
	public enum GameState {RUNNING, PAUSED};
	
	public GameState state = GameState.RUNNING;

	@Override
	public void run()
	{
		this.createBufferStrategy(2);
		BufferStrategy buf = this.getBufferStrategy();

		Graphics2D g;

		beginFrame = System.nanoTime();
		
		while (running)
		{
			// calculating time diff
			lastFrame = beginFrame;
			beginFrame = System.nanoTime();
			delta = beginFrame - lastFrame;

			if (state == GameState.RUNNING)
			{
				// hook to handle stuff while running
				handlePreCycle();
				
				// hook for handling keys
				handleKeys();
				
				// updating game elements
				update(delta);
				time = System.nanoTime();
				sumUpdateTime += time - beginFrame;
				
				// rendering
				g = (Graphics2D) buf.getDrawGraphics();
				
				render(g);
				
				if (renderStats)
					renderStats(g, WIDTH / 2);

				
				g.dispose();
				sumRenderTime += System.nanoTime() - time;
				
				// painting to screen
				buf.show();				
			}
			
			// more time calculation
			endFrame = System.nanoTime();
			frameTime = endFrame - beginFrame;
			
			// FPS calculation
			frameCount++;
			fpsCalcTimer -= delta;
			fpsCountPassedTime += delta;
			
			idealPassedTime += nsPrFrame;
			realPassedTime += delta;
			
			// show FPS avg every something seconds
			if (fpsCalcTimer < 0)
			{
				fpsCalcTimer = fpsCalcTime;
				currentFps = frameCount / (fpsCountPassedTime / oneSecNs);
				avgUpdateTime = sumUpdateTime / frameCount / tenE6;
				avgRenderTime = sumRenderTime / frameCount / tenE6;
				
				// resetting counters
				frameCount = 0;
				fpsCountPassedTime = 0;
				sumRenderTime = 0;
				sumUpdateTime = 0;
			}
			
			// yield if we have some time to spend
			if (realPassedTime < idealPassedTime)
				Thread.yield();
			
			
			// sleep if we have some time to spend
			long diff = (long) ((idealPassedTime - realPassedTime) / tenE6);
			if (diff > 2)
			{
				
				try {
					long sleep = diff - 1;
					//System.out.println("sleeping for ["+sleep+"] ms ("+(nsPrFrame - frameTime) / tenE6+")");
					//System.out.println(String.format(Locale.US,"Used ms [%10.2f] / [%10.2f]", frameTime / tenE6,nsPrFrame / tenE6));
					//long beforeSleep = System.nanoTime();
					Thread.sleep(sleep);
					//System.out.println("Slept for ["+(System.nanoTime() - beforeSleep) / tenE6+"] ms");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
		}
		
		preExit();
		
		this.dispose();
	}
	
	public abstract void preExit();
	
	protected void handleKeys()
	{
		if (Keys.pull(KeyEvent.VK_ESCAPE))
			running = false;
		else if (Keys.pull(KeyEvent.VK_F1))
			renderStats = !renderStats;
	}
	
	// hook to handle stuff while running
	public abstract void handlePreCycle();
	
	@Override
	public void keyPressed(KeyEvent e)
	{
		Keys.set(e.getKeyCode());
		Keys.setNewEvent(e);
	}
	
	@Override
	public void keyReleased(KeyEvent e)
	{
		Keys.clear(e.getKeyCode());
		Keys.setNewEvent(e);
	}
	
	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	Font font = new Font("Courier New",Font.BOLD,20);

	public void renderStats(Graphics2D g, int xoffset)
	{
		int y = 20;
		int xLeft = 10;
		int xRight = WIDTH - xoffset;
		
		g.setColor(Color.white);
		g.setFont(font);
		g.drawString("[ESC] | [F1]", xLeft, y);
		g.drawString(getFormattedCurrentFPS(), xRight, y);
		g.drawString(getFormattedAvgUpdateTime(), xRight, y + 20);
		g.drawString(getFormattedAvgRenderTime(), xRight, y + 40);
	}
	
}
