package particle;

import java.io.Serializable;

public class ParticleConfiguration implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7497116673320238811L;
	
	// a delay before start spawning particles
	public double delaySec;
	// the time in which to spawn particles. -1 = infinite.
	public double maxGenerationTimeSec;

	// how many particles to spawn pr sec
	public double spawnPrSec;
	// how many seconds a particle exists before removed
	public double lifeSecs;
	
	
	// the maximum speed to spawn particles (+ = right,- = left)
	public double maxSpeedXPrSec;
	public double minSpeedXPrSec;
	
	// the maximum speed to spawn particles (+ = down,- = up)
	public double maxSpeedYPrSec;
	public double minSpeedYPrSec;
	
	// the acceleration on each axis (gravity)
	public double accXPrSec;
	public double accYPrSec;

	// size of the particles (rectangular)
	public int partSize;

	// repeat particle generation if 0 count (and if not infinite)
	public boolean repeat = false;
	
	
	public ParticleConfiguration(double delaySec, double maxGenerationTimeSec,
			double spawnPrSec, double lifeSecs, double maxSpeedXPrSec,
			double minSpeedXPrSec, double maxSpeedYPrSec,
			double minSpeedYPrSec, double accXPrSec, double accYPrSec,
			int partSize,
			boolean repeat) 
	{
		this.delaySec = delaySec;
		this.maxGenerationTimeSec = maxGenerationTimeSec;
		this.spawnPrSec = spawnPrSec;
		this.lifeSecs = lifeSecs;
		this.maxSpeedXPrSec = maxSpeedXPrSec;
		this.minSpeedXPrSec = minSpeedXPrSec;
		this.maxSpeedYPrSec = maxSpeedYPrSec;
		this.minSpeedYPrSec = minSpeedYPrSec;
		this.accXPrSec = accXPrSec;
		this.accYPrSec = accYPrSec;
		this.partSize = partSize;
		this.repeat = repeat;
	}



	public final static ParticleConfiguration CONF1 = new ParticleConfiguration(1.0,-1,500.0,14.0,0.0,-400.0,50.0,-50.0,0.0000001,0.0,10, false);
	
	public final static ParticleConfiguration SPRAY = new ParticleConfiguration(
																				1.0,          // a delay before start spawning particles
																				-1,			  // the time in which to spawn particles. -1 = infinite.
																				1000.0,       // how many particles to spawn pr sec
																				4.0,          // how many seconds a particle exists before removed
																				-40.0,        // the maximum x speed to spawn particles (+ = right,- = left)
																				-400.0,
																				50.0,         // the maximum speed to spawn particles (+ = down,- = up)
																				-50.0,       
																				0.0000000,    // the acceleration on x axis (gravity)
																				0.0000001,    // the acceleration on y axis (gravity)
																				2,            // size of the particles (rectangular)
																				false		  // repeat animation
																				);

	

	public final static ParticleConfiguration CONF3 = new ParticleConfiguration
	(
			1.0,          // a delay before start spawning particles
			-1,			  // the time in which to spawn particles. -1 = infinite.
			1000.0,       // how many particles to spawn pr sec
			4.0,          // how many seconds a particle exists before removed
			 100.0,        // the maximum x speed to spawn particles (+ = right,- = left)
			-100.0,
			 100.0,         // the maximum speed to spawn particles (+ = down,- = up)
			-100.0,       
			0.0000000,    // the acceleration on x axis (gravity)
			0.0000000,    // the acceleration on y axis (gravity)
			20,            // size of the particles (rectangular)
			false		  // repeat animation
	);
	
	
	public final static ParticleConfiguration FOUNTAIN = new ParticleConfiguration(
			1.0,          // a delay before start spawning particles
			-1,			  // the time in which to spawn particles. -1 = infinite.
			1000.0,       // how many particles to spawn pr sec
			6.0,          // how many seconds a particle exists before removed
			-40.0,        // the maximum x speed to spawn particles (+ = right,- = left)
			40.0,
			-50.0,         // the maximum speed to spawn particles (+ = down,- = up)
			-500.0,       
			0.0000000,    // the acceleration on x axis (gravity)
			0.0000001,    // the acceleration on y axis (gravity)
			2,            // size of the particles (rectangular)
			false		  // repeat animation
			);

	public final static ParticleConfiguration BLOOD_SPATTER_EAST = new ParticleConfiguration(
			0.0,          // a delay before start spawning particles
			0.1,		  // the time in which to spawn particles. -1 = infinite.
			500.0,       // how many particles to spawn pr sec
			0.5,          // how many seconds a particle exists before removed
			100.0,        // the maximum x speed to spawn particles (+ = right,- = left)
			200.0,
			-150,         // the maximum speed to spawn particles (+ = down,- = up)
			150,       
			0.0000000,    // the acceleration on x axis (gravity)
			0.0000004,    // the acceleration on y axis (gravity)
			3,           // size of the particles (rectangular)
			false		  // repeat animation
			);

	public final static ParticleConfiguration BLOOD_SPATTER_WEST = new ParticleConfiguration(
			0.0,          // a delay before start spawning particles
			0.1,		  // the time in which to spawn particles. -1 = infinite.
			500.0,       // how many particles to spawn pr sec
			0.5,          // how many seconds a particle exists before removed
			-100.0,        // the maximum x speed to spawn particles (+ = right,- = left)
			-200.0,
			-150,         // the maximum speed to spawn particles (+ = down,- = up)
			150,       
			0.0000000,    // the acceleration on x axis (gravity)
			0.0000004,    // the acceleration on y axis (gravity)
			3,           // size of the particles (rectangular)
			false		  // repeat animation
			);

	public final static ParticleConfiguration BLOOD_SPATTER_NORTH = new ParticleConfiguration(
			0.0,          // a delay before start spawning particles
			0.1,		  // the time in which to spawn particles. -1 = infinite.
			500.0,       // how many particles to spawn pr sec
			0.5,          // how many seconds a particle exists before removed
			-150.0,        // the maximum x speed to spawn particles (+ = right,- = left)
			150.0,
			-100,         // the maximum speed to spawn particles (+ = down,- = up)
			-200,       
			0.0000000,    // the acceleration on x axis (gravity)
			0.0000004,    // the acceleration on y axis (gravity)
			3,           // size of the particles (rectangular)
			false		  // repeat animation
			);

	public final static ParticleConfiguration BLOOD_SPATTER_SOUTH = new ParticleConfiguration(
			0.0,          // a delay before start spawning particles
			0.1,		  // the time in which to spawn particles. -1 = infinite.
			500.0,       // how many particles to spawn pr sec
			0.5,          // how many seconds a particle exists before removed
			-150.0,        // the maximum x speed to spawn particles (+ = right,- = left)
			150.0,
			100,         // the maximum speed to spawn particles (+ = down,- = up)
			200,       
			0.0000000,    // the acceleration on x axis (gravity)
			0.0000004,    // the acceleration on y axis (gravity)
			3,           // size of the particles (rectangular)
			false		  // repeat animation
			);

	/**
		// ***** CONFIGURATION BEGIN ****** //
		
		// a delay before start spawning particles
		public final double delaySec = 1.0;
		// the time in which to spawn particles. -1 = infinite.
		public final double maxGenerationTimeSec = -1;

		// how many particles to spawn pr sec
		public final double spawnPrSec = 500.0;
		// how many seconds a particle exists before removed
		public final double lifeSecs = 14.0;
		
		
		// the maximum speed to spawn particles (+ = right,- = left)
		public final double maxSpeedXPrSec = 0.0;
		public final double minSpeedXPrSec = -400.0;
		
		// the maximum speed to spawn particles (+ = down,- = up)
		public final double maxSpeedYPrSec = 50.0;
		public final double minSpeedYPrSec = -50.0;
		
		// the acceleration on each axis (gravity)
		public final double accXPrSec = 0.0000001;
		public final double accYPrSec = 0.0000000;

		// size of the particles (rectangular)
		public int partSize = 10;

		
		// ****** CONFIGURATION END ******* //
	**/
}
