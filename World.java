import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class World
{
	private List<Sprite> sprites;
	private int          width;
	private int          height;

	/** construct a world 600x600 */
	public World() {
		this(600, 600);
	}

	public World(int h, int w)
	{
		height = h;
		width  = w;
		
		sprites = new ArrayList<>();

		StdDraw.setCanvasSize(width, height);
		StdDraw.setXscale(0, width);
		StdDraw.setYscale(0, height);
		StdDraw.clear(Color.BLACK);
		addSprites();
	}
	
	private void addSprites()
	{
	
		StationarySprite Carl = new StationarySprite(100, 100, 100, 100, "square.png");
		sprites.add(Carl);
		
		for(int x = 0; x < 4; x++)
		{
			int a = (int) (Math.random() * 600);
			int b = (int) (Math.random() * 600);
			int c = (int) (Math.random() * 100);
			int d = (int)  (Math.random() * 100);
			
			BouncingSprite bouncer = new BouncingSprite(a, b, c, d, "circle.png", 5, 5);
			
			sprites.add(bouncer);
		}
		
		HeavySprite newphew = new HeavySprite(200, 200, 50, 50, "triangle.png", 5, 5);
		
		sprites.add(newphew);
		
		ControllableSprite Luka = new ControllableSprite(300, 300, 100, 100, "Luka.jpeg", 1, 1);
		
		sprites.add(Luka);
	}

	/** add a sprite to the simulation */
	public void add(Sprite sprite)
	{
		this.sprites.add(sprite);
	}

	/** ask all sprites in simulation to update themselves */
	public void stepAll()
	{
		for (int i = 0; i < sprites.size(); i++)
			this.sprites.get(i).step(this);
	}

	/** get the width of the world */
	public int getWidth()
	{
		return width;
	}

	/** get the height of the world */
	public int getHeight()
	{
		return height;
	}

	/** get the number of sprites in the simulation currently */
	public int getNumSprites()
	{
		return sprites.size();
	}

	/** get the sprite at the given index */
	public Sprite getSprite(int index)
	{
		return sprites.get(index);
	}

	/** run the simulation, i.e. the main game loop */
	public void run()
	{
		while (true)
		{
			this.stepAll();
			this.drawAll();

			StdDraw.show(10); //don't worry about warning if using Eclipse
			StdDraw.clear(Color.BLACK);
			
		}
	}

	/** draw all sprites in the simulation at their current positions */
	public void drawAll() {
		for (Sprite sprite : this.sprites)
			sprite.draw();
	}

	public static void main(String[] args)
	{
		World world = new World(600, 600);
		world.run();
	}
}
