import java.awt.*;

public class PictureTester 
{
    public static void main(String[] args) 
    {
    	//construct a Picture object from a jpg image on disk
        Picture beach = new Picture("waterlilies.jpg"); 
        
        beach.view(); //calling the view() method displays the picture with the PictureViewer GUI
        //beach.zeroBlue();
      //beach.view();
        //beach.keepOnlyBlue();
      //beach.view();
       // beach.negate();
      //beach.view();
        //beach.solarize(127);
      //beach.view();
        //beach.grayscale();
      //beach.view();
        //beach.tint(1.25, .75, 1);
      //beach.view();
        //beach.posterize(63);
      //beach.view();
        //beach.mirrorVertical();
      //beach.view();
        //beach.mirrorRightToLeft();
      //beach.view();
        //beach.mirrorHorizontal();
      //beach.view();
        //beach.verticalFlip();
      //beach.view();
        //beach.fixRoof();
      //beach.view();
        //beach.edgeDetection(25);
        //beach.view();
        //testChromakey();
        //testSteganography();
        //beach.simpleBlur().view();
        //beach.blur(5).view();
       beach.glassFilter(5).view();
        
       
        
        

        
        
        
       
        
        
        
        
       
       
       
        
        
        //PUT YOUR TESTS HERE
        
        
    }
    
    
    
    
    /** this method is static, you don't need to call it on an object (just "testChromekey()") */
	public static void testChromakey()
	{
		Picture one = new Picture("blue-mark.jpg");
		Picture two = new Picture("moon-surface.jpg");
		
		one.view(); //show original mustache guy picture
		two.view(); //show the untouched moon's surface pic
		
		one.chromakey(two, new Color(10, 40, 75), 60); //replace this color if within 60
		
		one.view();
	}
	
    /** this method is static, you don't need to call it on an object (just "testSteganography()") */
	public static void testSteganography()
	{
		Picture msg   = new Picture("msg.jpg");
		Picture beach = new Picture("beach.jpg");
		
		beach.encode(msg); //hide message in beach picture
		beach.view();      //beach w/ hidden message inside, shouldn't look different
		
		beach.decode().view(); //see the hidden message in the beach picture
	}
}
