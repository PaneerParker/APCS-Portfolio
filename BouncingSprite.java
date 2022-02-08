
public class BouncingSprite extends MobileSprite{

	public BouncingSprite(double x, double y, int width, int height, String image, double vx, double vy) {
		super(x, y, width, height, image, vx, vy);
		// TODO Auto-generated constructor stub
	}
	
	private void bounce()
	{
		if(super.getX() + getVX() >= 600)
		{
			setVX(getVX() * -1);
		}
		
		if(super.getX() + getVX() <= 0)
		{
			setVX(getVX() * -1);
		}
		
		if(super.getY() + getVY() >= 600)
		{
			setVY(getVY() * -1);
		}
		
		if( super.getY() + getVY() <= 0)
		{
			setVY(getVY() * -1);
		}
	}
	
	public void step(World world) {
		bounce();
		double valX = super.getX();
		double valY = super.getY();
		valX += super.getVX();
		valY += super.getVY();
		super.setX(valX);
		super.setY(valY);
	}

}
