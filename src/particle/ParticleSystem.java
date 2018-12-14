package particle;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import engine.exception.JavaEngineException;

public class ParticleSystem 
{
	ColorMethod colorMethod;
	
	public boolean generate = true;

	final double NSPRSEC = Math.pow(10, 9);
	long spawnIntervalNs; // pr sec.
	double[] random; // used for generating new random things
	public int x,y; // center
	double[] speedx; // pixels pr ns
	double[] speedy; // pixels pr ns
	double[] xpos;
	double[] ypos;
	
	double[] currentLifeNs;
	
	double timerNs;
	double delayNs;
	
	double accXPrNs;
	double accYPrNs;
	double lifeNs;
	long maxLifeNs;// life in ns
	
	
	int amount;
	
	Color[] cols;
	public int count = 0;
	
	long spawnCountDown = spawnIntervalNs;
	
	
	
	double[] speedXGenerate;
	int speedX_i = 0;

	double[] speedYGenerate;
	int speedY_i = 0;
	
	ParticleConfiguration conf;

	public ParticleSystem(int amount, int x, int y, ParticleConfiguration conf, Color color)
	{
		colorMethod = ColorMethod.COLOR_RANDOM;
		
		this.conf = conf;
		
		cols = new Color[] {color};
		this.amount = amount;
		this.x = x;
		this.y = y;
		
		init();
	}

	public ParticleSystem(int amount, int x, int y, ParticleConfiguration conf)
	{
		colorMethod = ColorMethod.COLOR_RANDOM;
		
		this.conf = conf;
		
		cols = genRandomColors(amount);
		this.amount = amount;
		this.x = x;
		this.y = y;
		
		init();
	}

	public ParticleSystem(int amount, int x, int y, ParticleConfiguration conf, Color fromColor, Color toColor, double gradientFactor)
	{
		colorMethod = ColorMethod.COLOR_INTERPOLATION;

		this.conf = conf;
		
		cols = genColorsInterpolation(amount, fromColor, toColor, gradientFactor);
		this.amount = amount;
		this.x = x;
		this.y = y;
		
		init();
	}
	
	private void init()
	{
		this.speedXGenerate = genRandomDoubles(amount, conf.minSpeedXPrSec / NSPRSEC, conf.maxSpeedXPrSec / NSPRSEC);
		this.speedYGenerate = genRandomDoubles(amount, conf.minSpeedYPrSec / NSPRSEC, conf.maxSpeedYPrSec / NSPRSEC);
		
		xpos = new double[amount];
		ypos = new double[amount];
		currentLifeNs = new double[amount];
		speedx = new double[amount];
		speedy = new double[amount];
		
		
		spawnIntervalNs = (long)(NSPRSEC / conf.spawnPrSec); // pr sec.
		timerNs = conf.maxGenerationTimeSec * NSPRSEC;
		delayNs = conf.delaySec * NSPRSEC;
		
		accXPrNs = conf.accXPrSec / NSPRSEC;
		accYPrNs = conf.accYPrSec / NSPRSEC;
		lifeNs = conf.lifeSecs * NSPRSEC;
		maxLifeNs = (long)(NSPRSEC * conf.lifeSecs);// life in ns
	}
	
	
	private double getSpeedX()
	{
		if (speedX_i > (speedXGenerate.length - 1))
			speedX_i = 0;
		
		return speedXGenerate[speedX_i++];
	}
	
	private double getSpeedY()
	{
		if (speedY_i > (speedYGenerate.length - 1))
			speedY_i = 0;
		
		return speedYGenerate[speedY_i++];
	}
	
	private void initArrs(int i)
	{
			xpos[i] = x;
			ypos[i] = y;
			speedx[i] = getSpeedX();
			speedy[i] = getSpeedY();
			currentLifeNs[i] = lifeNs;
	}
	
	public void update(double ns)
	{
		if (delayNs > 0)
		{
			delayNs -= ns;
			return;
		}
		
		// handle all particles
		double accx = accXPrNs * ns;
		double accy = accYPrNs * ns;
		for (int i = 0; i < count; i++)
		{
			// move
			xpos[i] += speedx[i] * ns;
			ypos[i] += speedy[i] * ns;
			
			// adjust speed
			speedx[i] += accx;
			speedy[i] += accy;
			
			// life update
			currentLifeNs[i] -= ns;
			
			if (currentLifeNs[i] < 0)
			{
				xpos[i] = xpos[count-1];
				ypos[i] = ypos[count-1];
				speedx[i] = speedx[count-1];
				speedy[i] = speedy[count-1];
				currentLifeNs[i] = currentLifeNs[count-1];
				
				count--;
			}
		}
		
		
		// handle generation
		if (!generate) return;
		
		if (conf.maxGenerationTimeSec == -1 || timerNs > 0)
		{
			if (conf.maxGenerationTimeSec != -1)
				timerNs -= ns;
			
			if (ns > spawnCountDown)
			{
				while (ns > spawnCountDown && count < (amount-1))
				{
					ns -= spawnCountDown;
					
					spawnCountDown = spawnIntervalNs;
					count++;
					
					initArrs(count-1);
				}
			}
			else
			{
				// spawn new particles
				spawnCountDown -= ns;
				
			}
			
		}
		
	}
	
	public void render(Graphics2D g) throws JavaEngineException
	{
		int x;
		int y;
		int colorChoice;
		for (int i = 0; i < count; i++)
		{
			if (currentLifeNs[i] > 0)
			{
				if (colorMethod == ColorMethod.COLOR_INTERPOLATION)
					colorChoice = (int)((1.0 - currentLifeNs[i] / lifeNs) * (double)amount);
				else if (colorMethod == ColorMethod.COLOR_RANDOM)
					colorChoice = i;
				else if (colorMethod == ColorMethod.COLOR_FIXED)
					colorChoice = 0;
				else
					throw new JavaEngineException("Color method not handled ["+colorMethod+"]");
					
				g.setColor(cols[colorChoice]);
				x = (int)(xpos[i]);
				y = (int)(ypos[i]);
				g.fillRect(x, y, conf.partSize, conf.partSize);				
			}
		}		
	}
	
	public double[] genRandomDoubles(int amount, double low, double high)
	{
		Random r = new Random();
		
		double[] result = new double[amount];
		
		for (int i = 0; i < amount; i++)
		{
			result[i] = low + (r.nextDouble() * (high - low));
		}
		
		return result;
	}

	public Color[] genRandomColors(int amount)
	{
		Random r = new Random();
		
		Color[] result = new Color[amount];
		
		for (int i = 0; i < amount; i++)
		{
			result[i] = new Color((int)(r.nextDouble()*255),(int)(r.nextDouble()*255),(int)(r.nextDouble()*255));
		}
		
		return result;
	}
	
	public Color[] genColorsInterpolation(int amount, Color from, Color to, double gradientFactor)
	{
		Color[] result = new Color[amount];

		double stepR = (to.getRed() - from.getRed()) / (double)amount;
		double stepG = (to.getGreen() - from.getGreen()) / (double)amount;
		double stepB = (to.getBlue() - from.getBlue()) / (double)amount;
		
		double factor;
		for (int i = 0; i < amount; i++)
		{
			factor = (amount - i) * gradientFactor + i; 
			result[i] = new Color((int)(from.getRed() + stepR * factor),(int)(from.getGreen() + stepG * factor),(int)(from.getBlue() + stepB * factor));
		}
		
		return result;
	}

}
