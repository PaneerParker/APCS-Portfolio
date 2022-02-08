import java.awt.event.KeyEvent;

public class ControllableSprite extends MobileSprite{

	public ControllableSprite(double x, double y, int width, int height, String image, double vx, double vy) {
		super(x, y, width, height, image, vx, vy);
		// TODO Auto-generated constructor stub
	}
	
	public void step(World world) {
		
		super.step(world);
		
		if(StdDraw.isKeyPressed(KeyEvent.VK_UP) && super.getY() < 600)
		{
			setVY(5);
		}
		else if(StdDraw.isKeyPressed(KeyEvent.VK_DOWN) && super.getY() > 0)
		{
			setVY(-5);
		}
		else
		{
			setVY(0);
		}
		
		if(StdDraw.isKeyPressed(KeyEvent.VK_LEFT) && super.getX() > 0)
		{
			setVX(-5);
		}
		else if(StdDraw.isKeyPressed(KeyEvent.VK_RIGHT) && super.getX() < 600)
		{
			setVX(5);
		}
		else
		{
			setVX(0);
		}
		
		
		// TODO Auto-generated method stub
		
	}

}
