import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.UIManager;

/**
 * A class that represents a picture made up of a rectangle of {@link Pixel}s
 */
public class Picture {

    /** The 2D array of pixels that comprise this picture */
	private Pixel[][] pixels;

    /**
     * Creates a Picture from an image file in the "images" directory
     * @param picture The name of the file to load
     */
    public Picture(String picture) {
        File file = new File("./images/"+picture);
        BufferedImage image;
        if (!file.exists()) throw new RuntimeException("No picture at the location "+file.getPath()+"!");
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        pixels = new Pixel[image.getHeight()][image.getWidth()];
        for (int y = 0; y<pixels.length; y++) {
            for (int x = 0; x<pixels[y].length; x++) {
                int rgb = image.getRGB(x, y);
                /*
                 * For the curious - BufferedImage saves an image's RGB info into a hexadecimal integer
                 * The below extracts the individual values using bit-shifting and bit-wise ANDing with all 1's
                 */
                pixels[y][x] = new Pixel((rgb>>16)&0xff, (rgb>>8)&0xff, rgb&0xff);
            }
        }
    }

    /**
     * Creates a solid-color Picture of a given color, width, and height
     * @param red The red value of the color
     * @param green The green value of the color
     * @param blue The blue value of the color
     * @param height The height of the Picture
     * @param width The width of the Picture
     */
    public Picture(int red, int green, int blue, int height, int width) {
        pixels = new Pixel[height][width];
        for (int y = 0; y<pixels.length; y++) {
            for (int x = 0; x<pixels[y].length; x++) {
                pixels[y][x] = new Pixel(red, green, blue);
            }
        }
    }

    /**
     * Creates a solid white Picture of a given width and height
     * @param color The {@link Color} of the Picture
     * @param height The height of the Picture
     * @param width The width of the Picture
     */
    public Picture(int height, int width) {
        this(Color.WHITE, height, width);
    }

    /**
     * Creates a solid-color Picture of a given color, width, and height
     * @param color The {@link Color} of the Picture
     * @param width The width of the Picture
     * @param height The height of the Picture
     */
    public Picture(Color color, int height, int width) {
        this(color.getRed(), color.getGreen(), color.getBlue(), height, width);
    }

    /**
     * Creates a Picture based off of an existing {@link Pixel} 2D array
     * @param pixels A rectangular 2D array of {@link Pixel}s. Must be at least 1x1
     */
    public Picture(Pixel[][] pixels) {
        if (pixels.length==0 || pixels[0].length==0) throw new RuntimeException("Can't have an empty image!");
        int width = pixels[0].length;
        for (int i = 0; i<pixels.length; i++) if (pixels[i].length!=width)
            throw new RuntimeException("Pictures must be rectangles. pixels[0].length!=pixels["+i+"].length!");
        this.pixels = new Pixel[pixels.length][width];
        for (int i = 0; i<pixels.length; i++) {
            for (int j = 0; j<pixels[i].length; j++) {
                this.pixels[i][j] = new Pixel(pixels[i][j].getColor());
            }
        }
    }

    /**
     * Creates a Picture based off of an existing Picture
     * @param picture The Picture to copy
     */
    public Picture(Picture picture) {
        this(picture.pixels);
    }

    /**
     * Gets the width of the Picture
     * @return The width of the Picture
     */
    public int getWidth() {
        return pixels[0].length;
    }

    /**
     * Gets the height of the Picture
     * @return The height of the Picture
     */
    public int getHeight() {
        return pixels.length;
    }

    /**
     * Gets the {@link Pixel} at a given coordinate
     * @param x The x location of the {@link Pixel}
     * @param y The y location of the {@link Pixel}
     * @return The {@link Pixel} at the given location
     */
    public Pixel getPixel(int x, int y) {
        if (x>=getWidth() || y>=getHeight() || x<0 || y<0) throw new RuntimeException("No pixel at ("+x+", "+y+")");
        return pixels[y][x];
    }

    /**
     * Sets the {@link Pixel} at a given coordinate
     * @param x The x location of the {@link Pixel}
     * @param y The y location of the {@link Pixel}
     * @param pixel The new {@link Pixel}
     */
    public void setPixel(int x, int y, Pixel pixel) {
        if (x>=getWidth() || y>=getHeight() || x<0 || y<0) throw new RuntimeException("No pixel at ("+x+", "+y+")");
        if (pixel==null) throw new NullPointerException("Pixel is null"); //guard is required because pixel's value isn't used in this method
        pixels[y][x] = pixel;
    }

    /**
     * Opens a {@link PictureViewer} to view this Picture
     * @return the {@link PictureViewer} viewing the Picture
     */
    public PictureViewer view() {
        return new PictureViewer(this);
    }

	/**
	 * Save the image on disk as a JPEG
	 * Call programmatically on a Picture object, it will prompt you to choose a save location
	 * In the save dialogue window, specify the file AND extension (e.g. "lilies.jpg")
	 * Extension must be .jpg as ImageIO is expecting to write a jpeg
	 */
	public void save()
	{
		try {
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    } 
		catch(Exception e) {
	        e.printStackTrace();
	    }
		
		BufferedImage image = new BufferedImage(this.pixels[0].length, this.pixels.length, BufferedImage.TYPE_INT_RGB);

		for (int r = 0; r < this.pixels.length; r++)
			for (int c = 0; c < this.pixels[0].length; c++)
				image.setRGB(c, r, this.pixels[r][c].getColor().getRGB());

		//user's Desktop will be default directory location
		JFileChooser chooser = new JFileChooser(System.getProperty("user.home") + "/Desktop");

		chooser.setDialogTitle("Select picture save location / file name");

		File file = null;

		int choice = chooser.showSaveDialog(null);

		if (choice == JFileChooser.APPROVE_OPTION)
			file = chooser.getSelectedFile();

		//append extension if user didn't read save instructions
		if (!file.getName().endsWith(".jpg") && !file.getName().endsWith(".JPG") && !file.getName().endsWith(".jpeg") && !file.getName().endsWith(".JPEG"))
			file = new File(file.getAbsolutePath() + ".jpg");
		
		try {
			ImageIO.write(image, "jpg", file);
			System.out.println("File created at " + file.getAbsolutePath());
		}
		catch (IOException e) {
			System.out.println("Can't write to location: " + file.toString());
		}
		catch (NullPointerException|IllegalArgumentException e) {
			System.out.println("Invalid directory choice");
		}
	}
	
	/** return a copy of the reference to the 2D array of pixels that comprise this picture */
	public Pixel[][] getPixels() {
		return pixels;
	}


    /********************************************************
     *************** STUDENT METHODS BELOW ******************
     ********************************************************/

    /** remove all blue tint from a picture */
    public void zeroBlue()
    {
    	for(int x = 0; x < pixels.length; x++)
    	{
    		for(int y = 0; y < pixels[x].length; y++)
    		{
    			pixels [x] [y].setBlue(0);
    		}
    	}
    	
    	//TODO
    }

    /** remove everything BUT blue tint from a picture */
    public void keepOnlyBlue()
    {
    	for(int x = 0; x < pixels.length; x++)
    	{
    		for(int y = 0; y < pixels[x].length; y++)
    		{
    			pixels [x] [y].setRed(0);
    			pixels [x] [y].setGreen(0);
    		}
    	}
    	//TODO
    }

    /** invert a picture's colors */
    public void negate()
    {
    	for(int x = 0; x < pixels.length; x++)
    	{
    		for(int y = 0; y < pixels[x].length; y++)
    		{
    			pixels [x] [y].setRed(255 - pixels [x] [y].getRed());
    			pixels [x] [y].setGreen(255 - pixels [x] [y].getGreen());
    			pixels [x] [y].setBlue(255 - pixels [x] [y].getBlue());
    		}
    	}
    	//TODO
    }

    /** simulate the over-exposure of a picture in film processing */
    public void solarize(int threshold)
    {
    	for(int x = 0; x < pixels.length; x++)
    	{
    		for(int y = 0; y < pixels[x].length; y++)
    		{
    			if(pixels [x] [y] .getRed() < threshold)
    			{
    				pixels [x] [y].setRed(255 - pixels [x] [y].getRed());
    			}
    			
    			if(pixels [x] [y] .getBlue() < threshold)
    			{
    				pixels [x] [y].setBlue(255 - pixels [x] [y].getBlue());
    			}
    			
    			if(pixels [x] [y] .getGreen() < threshold)
    			{
    				pixels [x] [y].setGreen(255 - pixels [x] [y].getGreen());
    			}
    		}
    	}
    	//TODO
    }

    /** convert an image to grayscale */
    public void grayscale()
    {
    	int red = 0;
    	int blue = 0;
    	int green = 0;
    	for(int x = 0; x < pixels.length; x++)
    	{
    		for(int y = 0; y < pixels[x].length; y++)
    		{
    			red = pixels [x] [y].getRed();
    			blue = pixels [x] [y].getBlue();
    			green = pixels [x] [y].getGreen();
    			pixels [x] [y].setRed((red + blue + green)/3);
    			pixels [x] [y].setBlue((red + blue + green)/3);
    			pixels [x] [y].setGreen((red + blue + green)/3);
    		}
    	}
    	
    	//TODO
    }

	/** change the tint of the picture by the supplied coefficients */
	public void tint(double red, double blue, double green)
	{
		int reddy = 0;
    	int bluey = 0;
    	int greeny = 0;
		for(int x = 0; x < pixels.length; x++)
    	{
    		for(int y = 0; y < pixels[x].length; y++)
    		{
    			reddy = pixels [x] [y].getRed();
    			bluey = pixels [x] [y].getBlue();
    			greeny = pixels [x] [y].getGreen();
    			if(reddy * red <= 255)
    			{
    				reddy *= red;
    			}
    			else
    			{
    				reddy = 255;
    			}
    			
    			if(bluey * blue <= 255)
    			{
    				bluey *= blue;
    			}
    			else
    			{
    				bluey = 255;
    			}
    			
    			if(greeny * green <= 255)
    			{
    				greeny *= green;
    			}
    			else
    			{
    				green = 255;
    			}
    			pixels [x] [y].setRed(reddy);
    			pixels [x] [y].setBlue(bluey);
    			pixels [x] [y].setGreen(greeny);
    		}
    	}
		//TODO
	}
	
	/** reduces the number of colors in an image to create a "graphic poster" effect */
	public void posterize(int span)
	{
		for(int x = 0; x < pixels.length; x++)
    	{
    		for(int y = 0; y < pixels[x].length; y++)
    		{
    			pixels [x] [y].setRed(pixels [x] [y].getRed()/span * span);
    			pixels [x] [y].setGreen(pixels [x] [y].getGreen()/span * span);
    			pixels [x] [y].setBlue(pixels [x] [y].getBlue()/span * span);
    		}
    	}
		//TODO
	}

    /** mirror an image about a vertical midline, left to right */
    public void mirrorVertical()
    {
		Pixel leftPixel  = null;
		Pixel rightPixel = null;

		int width = pixels[0].length;

		for (int r = 0; r < pixels.length; r++)
		{
			for (int c = 0; c < width / 2; c++)
			{
				leftPixel  = pixels[r][c];
				rightPixel = pixels[r][(width - 1) - c];

				leftPixel.setColor(rightPixel.getColor());
			}
		}
    }

    /** mirror about a vertical midline, right to left */
    public void mirrorRightToLeft()
    {
    	Pixel leftPixel  = null;
		Pixel rightPixel = null;

		int width = pixels[0].length;

		for (int r = 0; r < pixels.length; r++)
		{
			for (int c = 0; c < width / 2; c++)
			{
				leftPixel  = pixels[r][c];
				rightPixel = pixels[r][(width - 1) - c];

				leftPixel.setColor(rightPixel.getColor());
			}
		}
    	//TODO
    }

    /** mirror about a horizontal midline, top to bottom */
    public void mirrorHorizontal()
    {
    	Pixel topPixel  = null;
		Pixel bottomPixel = null;

		int length = pixels.length;

		for (int r = 0; r < length/2; r++)
		{
			for (int c = 0; c < pixels [r] .length; c++)
			{
				topPixel  = pixels[r][c];
				bottomPixel = pixels[(length - 1) - r][c];

				bottomPixel.setColor(topPixel.getColor());
			}
		}
    	//TODO
    }

    /** flip an image upside down about its bottom edge */
    public void verticalFlip()
    {
    	Pixel topPixel  = null;
		Pixel bottomPixel = null;
		Color temp = null;

		int length = pixels.length;

		for (int r = 0; r < length/2; r++)
		{
			for (int c = 0; c < pixels [r] .length; c++)
			{
				topPixel  = pixels[r][c];
				bottomPixel = pixels[(length - 1) - r][c];
				temp = bottomPixel.getColor();
				bottomPixel.setColor(topPixel.getColor());
				topPixel.setColor(temp);
			}
		}
    	//TODO
    }

    /** fix roof on greek temple */
    public void fixRoof()
    {
    	Pixel leftPixel  = null;
		Pixel rightPixel = null;


		for (int r = 35; r < 97; r++)
		{
			for (int c = 300; c < 488; c++)
			{
				rightPixel  = pixels[r][c];
				leftPixel = pixels[r][pixels[r].length-c];

				rightPixel.setColor(leftPixel.getColor());
			}
		}
    	//TODO
    }

    /** detect and mark edges in an image */
    public void edgeDetection(int dist)
    {
    	double distanceOfColorX = 0;
    	double distanceOfColorY = 0;
    	Pixel edge [] [] = pixels;
    	
    	for(int x = 0; x < pixels.length - 1; x++)
    	{
    		for(int y = 0; y < pixels[x].length - 1; y++)
    		{
    			distanceOfColorX = edge [x] [y].colorDistance(edge [x + 1] [y].getColor());
    			distanceOfColorY = edge [x] [y].colorDistance(edge [x] [y + 1].getColor());
    			
    			if((int) distanceOfColorX > dist)
    			{
    				pixels [x] [y].setRed(0);
    				pixels [x] [y].setBlue(0);
    				pixels [x] [y].setGreen(0);
    			}
    			
    			else
    			{
    				pixels [x] [y].setRed(255);
    				pixels [x] [y].setBlue(255);
    				pixels [x] [y].setGreen(255);
    			}
    			
    			if((int) distanceOfColorY > dist)
    			{
    				pixels [x] [y].setRed(0);
    				pixels [x] [y].setBlue(0);
    				pixels [x] [y].setGreen(0);
    			}
    			
    			else if((int) distanceOfColorX > dist)
    			{
    				
    			}
    			else
    			{
    				pixels [x] [y].setRed(255);
    				pixels [x] [y].setBlue(255);
    				pixels [x] [y].setGreen(255);
    			}
    			
    			
    			
    		}
    	}
    	//TODO
    }


	/** copy another picture's pixels into this picture, if a color is within dist of param Color */
	public void chromakey(Picture other, Color color, int dist)
	{
		
	    	for(int x = 0; x < pixels.length; x++)
	    	{
	    		for(int y = 0; y < pixels[x].length; y++)
	    		{
	    			if(pixels [x] [y].colorDistance(color) < dist)
	    			{
	    				pixels [x] [y].setColor(other.pixels [x] [y].getColor());
	    			}
	    		}
	    	}
	    	
	    	//TODO
	    

		//TODO
	}

	/** steganography encode (hide the message in msg in this picture) */
	public void encode(Picture msg)
	{
		for(int x = 0; x < pixels.length; x++)
    	{
    		for(int y = 0; y < pixels[x].length; y++)
    		{
    			if(pixels [x] [y].getRed() % 2 != 0)
    			{
    				pixels [x] [y].setRed(pixels [x] [y].getRed() - 1);
    			}
    		}
    	}
		for(int x = 0; x < pixels.length; x++)
    	{
    		for(int y = 0; y < pixels[x].length; y++)
    		{
    			if(msg. pixels [x] [y].colorDistance(Color.BLACK) < 50)
    			{
    				pixels [x] [y].setRed(pixels [x] [y].getRed() + 1);
    			}
    		}
    	}
		//TODO
	}

	/** steganography decode (return a new Picture containing the message hidden in this picture) */
	public Picture decode()
	{
		Picture newPic = new Picture(pixels.length, pixels [0].length);
		for(int x = 0; x < pixels.length; x++)
    	{
    		for(int y = 0; y < pixels[x].length; y++)
    		{
    			if(pixels [x] [y].getRed() % 2 == 1)
    			{
    				newPic.pixels [x] [y].setColor(Color.BLACK);
    			}
    		}
    	}
		
		//TODO

		return newPic; //REPLACE
	}

	/** perform a simple blur using the colors of neighboring pixels */
	public Picture simpleBlur()
	{
		Picture newPicture = new Picture(pixels.length, pixels [0].length);
		int totalRed = 0;
		int totalBlue = 0;
		int totalGreen = 0;
		int num = 0;
		for(int x = 0; x < pixels.length; x++)
		{
			for(int y = 0; y < pixels[x].length; y++)
			{
				totalRed += pixels[x] [y].getRed();
				totalGreen += pixels[x] [y].getGreen();
				totalBlue += pixels[x] [y].getBlue();
				num++;
				if(x > 0) //left
				{
					totalRed += pixels [x-1] [y].getRed();
					totalGreen += pixels[x-1] [y].getGreen();
					totalBlue += pixels[x-1] [y].getBlue();
					num++;
				}
				
				if(x < pixels[x].length-2) //right
				{
					totalRed += pixels [x+1] [y].getRed();
					totalGreen += pixels[x+1] [y].getGreen();
					totalBlue += pixels[x+1] [y].getBlue();
					num++;
				}
				
				if(y > 0) //up
				{
					totalRed += pixels [x] [y-1].getRed();
					totalGreen += pixels[x] [y-1].getGreen();
					totalBlue += pixels[x] [y-1].getBlue();
					num++;
				}
				
				if(y < pixels.length-1) //down
				{
					totalRed += pixels [x] [y+1].getRed();
					totalGreen += pixels[x] [y+1].getGreen();
					totalBlue += pixels[x] [y+1].getBlue();
					num++;
				}
				
				newPicture.pixels [x] [y].setRed(totalRed/num);
				newPicture.pixels [x] [y].setGreen(totalGreen/num);
				newPicture.pixels [x] [y].setBlue(totalBlue/num);

				
				totalRed = 0;
				totalGreen = 0;
				totalBlue = 0;
				num = 0;
				
				
			}
		}
		//TODO

		return newPicture; //REPLACE
	}

	/** perform a blur using the colors of pixels within radius of current pixel */
	public Picture blur(int radius)
	{
		Picture newPicture = new Picture(pixels.length, pixels [0].length);
		
		for(int x = 0; x < pixels.length; x++)
    	{
    		for(int y = 0; y < pixels[x].length; y++)
    		{
    			int totalRed = 0;
    			int totalBlue = 0;
    			int totalGreen = 0;
    			int total = 0;
    			
    			for(int row = x - radius; row <= x + radius; row++)
    			{
    				for(int col = y - radius; col <= y + radius; col++)
    				{
    					if(row >= 0 && col >= 0 && col < pixels [0].length && row < pixels.length)
    					{
    						
    						totalRed += pixels [row] [col].getRed();
    						totalGreen += pixels [row] [col].getGreen();
    						totalBlue += pixels [row] [col].getBlue();
    						total++;
    					}
    					
    					
    					
    				}
    			}
    			
   
    			if(total > 0)
    			{
    				newPicture.pixels [x] [y].setColor(totalRed/total, totalGreen/total, totalBlue/total);
    			}
    			else
    			{
    				newPicture.pixels [x] [y].setColor(0,0,0);
    			}
    		}
    	}
		
		//TODO

		return newPicture; //REPLACE
	}
	
	/**
	 * Simulate looking at an image through a pane of glass
	 * @param dist the "radius" of the neighboring pixels to use
	 * @return a new Picture with the glass filter applied
	 */
	public Picture glassFilter(int dist) 
	{
		Picture newPicture = new Picture(pixels.length, pixels [0].length);
		
		for(int x = 0; x < pixels.length; x++)
    	{
    		for(int y = 0; y < pixels[x].length; y++)
    		{
    			boolean randomExists = false;
    			int xPos = 0;
    			int yPos = 0;
    			while(randomExists == false)
    			{
    				xPos = x + (int) (Math.random() * ((2 * dist) - 1) - (dist - 1));
    				yPos = y + (int) (Math.random() * ((2 * dist) - 1) - (dist - 1));
    				
    				if(xPos >= 0 && xPos < pixels.length && yPos >= 0 && yPos < pixels [0].length)
    				{
    					randomExists = true;
    				}
    			}
    			
    			newPicture.pixels [x] [y].setColor(pixels [xPos] [yPos].getColor());
    			
    			
    			
   
    			
    		}
    	}
		
		//TODO
		
		return newPicture; 
	}
}
