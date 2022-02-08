
public class MobileSprite extends Sprite{
	
	private double vx;
	private double vy;

	public MobileSprite(double x, double y, int width, int height, String image, double vx, double vy) {
		super(x, y, width, height, image);
		this.vx = vx;
		this.vy = vy;
		// TODO Auto-generated constructor stub
	}
	
	public double getVX()
	{
		return vx;
	}
	
	public double getVY()
	{
		return vy;
	}
	
	public void setVX(double setter)
	{
		vx = setter;
	}
	
	public void setVY(double setter)
	{
		vy = setter;
	}

	@Override
	public void step(World world) {
		double valX = getX();
		double valY = getY();
		valX += vx;
		valY += vy;
		setX(valX);
		setY(valY);
		
		// TODO Auto-generated method stub
		
	}

}
