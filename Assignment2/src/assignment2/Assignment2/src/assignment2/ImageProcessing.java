

package assignment2;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ImageProcessing extends ImageProcessingGUI
{
    public final BufferedImage input = InputImagePanel.getBufferedImage();    
    
    public ImageProcessing()
    {
        //Empty
    }
      
    public static BufferedImage applyBoxBlur(BufferedImage input, int radius){
        final int ARRAY_DIM = 2*radius+1;
        final float[][] boxkernel = new float[ARRAY_DIM][ARRAY_DIM];
        float value = 1.0f / (ARRAY_DIM*ARRAY_DIM);
        for (int i = 0; i < ARRAY_DIM; i++) {
            for (int j = 0; j < ARRAY_DIM; j++) {
                boxkernel[i][j] = value;
            }
        }
        return applyConvolutionFilter(input, boxkernel);
        
    }
      
    public static BufferedImage applyGreyScale(BufferedImage input,BufferedImage output){
                 
       output = new BufferedImage(input.getWidth(),
                                                 input.getHeight(),
                                                 input.getType());
        
        for (int j=0; j<input.getHeight(); j++)
        {
            for (int i=0; i<input.getWidth(); i++)
            {
                // Read color from input pixel
                Color color = new Color(input.getRGB(i, j));
                
                // Average R G B color channels
                int r = color.getRed();
                int g = color.getGreen();
                int b = color.getBlue();
                int avg = (r + g + b) / 3;
                
                // Write the grey scales color to output pixel
                output.setRGB(i, j, new Color(avg, avg, avg).getRGB());
            }
        }                 
        return output; 
    }
    
    public static BufferedImage applyEdgeDetection(BufferedImage input)
    {
        final float[][] edgeKernel = new float[3][3];
        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 3; j++)
            {
                if(i==1 && j==1)
                {
                    edgeKernel[i][j] = 8;
                }
                else{ edgeKernel[i][j] = -1;}
            }
        }
        
        return applyConvolutionFilter(input,edgeKernel);
    }
    
    public static BufferedImage gammaCorrection(BufferedImage original, double gamma) {
 
    int alpha, red, green, blue;
    int newPixel;
 
    double gamma_new = 1 / gamma;
 
    BufferedImage gamma_cor = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
 
    for(int i=0; i<original.getWidth(); i++) {
        for(int j=0; j<original.getHeight(); j++) {
 
            // Get pixels by R, G, B
            alpha = new Color(original.getRGB(i, j)).getAlpha();
            red = new Color(original.getRGB(i, j)).getRed();
            green = new Color(original.getRGB(i, j)).getGreen();
            blue = new Color(original.getRGB(i, j)).getBlue();
 
            red = (int) (255 * (Math.pow((double) red / (double) 255, gamma_new)));
            green = (int) (255 * (Math.pow((double) green / (double) 255, gamma_new)));
            blue = (int) (255 * (Math.pow((double) blue / (double) 255, gamma_new)));
 
            // Return back to original format
            newPixel = colorToRGB(alpha, red, green, blue);
 
            // Write pixels into image
            gamma_cor.setRGB(i, j, newPixel);
 
        }
 
    } 
    return gamma_cor;        
}
    
     private static int colorToRGB(int alpha, int red, int green, int blue) {
 
        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += red; newPixel = newPixel << 8;
        newPixel += green; newPixel = newPixel << 8;
        newPixel += blue;
 
        return newPixel;
 
    }
    
   public static BufferedImage applyGaussianBlur(BufferedImage input)
    {
        final float gaussianKernel[][] = new float[][]
        {
            {0.00000067f, 0.00002292f, 0.00019117f, 0.00038771f, 0.00019117f, 0.00002292f, 0.00000067f},
            {0.00002292f, 0.00078634f, 0.00655965f, 0.01330373f, 0.00655965f, 0.00078633f, 0.00002292f},
            {0.00019117f, 0.00655965f, 0.05472157f, 0.11098164f, 0.05472157f, 0.00655965f, 0.00019117f},
            {0.00038771f, 0.01330373f, 0.11098164f, 0.22508352f, 0.11098164f, 0.01330373f, 0.00038771f},
            {0.00019117f, 0.00655965f, 0.05472157f, 0.11098164f, 0.05472157f, 0.00655965f, 0.00019117f},
            {0.00002292f, 0.00078634f, 0.00655965f, 0.01330373f, 0.00655965f, 0.00078633f, 0.00002292f},
            {0.00000067f, 0.00002292f, 0.00019117f, 0.00038771f, 0.00019117f, 0.00002292f, 0.00000067f}
        };

        return applyConvolutionFilter(input, gaussianKernel);
    }
   
   private static BufferedImage applyConvolutionFilter(BufferedImage input, float[][] kernel)
    {
        BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), input.getType());
        
        // Set output image from input image (img)
        for (int j=0; j<input.getHeight(); ++j)
        {
            for (int i=0; i<input.getWidth(); ++i)
            {
                // Calculate the gaussian blur
                Color pixelColor = applyKernel(input, kernel, i, j);
                output.setRGB(i, j, pixelColor.getRGB());                
            }
        }
        
        return output;
    }

   
    private static Color applyKernel(BufferedImage img, float[][] kernel, int row, int column)
    {
        float red = 0.0f;
        float green = 0.0f;
        float blue = 0.0f;

        int minIndexH = -(kernel.length / 2);
        int maxIndexH = minIndexH + kernel.length;
        int minIndexV = -(kernel[0].length / 2);
        int maxIndexV = minIndexV + kernel[0].length;
        
        for (int i = minIndexH; i < maxIndexH; ++i)
        {
            for (int j=minIndexV; j<maxIndexV; ++j)
            {
                int indexH = wrapHorizontalIndex(img, row + i);
                int indexV = wrapVerticalIndex(img, column + j);
                Color col = new Color(img.getRGB(indexH, indexV));

                red += col.getRed() * kernel[i-minIndexH][j-minIndexV];
                green += col.getGreen() * kernel[i-minIndexH][j-minIndexV];
                blue += col.getBlue() * kernel[i-minIndexH][j-minIndexV];
            }
        }

        red = Math.min(Math.max(red, 0.0f), 255.0f);
        green = Math.min(Math.max(green, 0.0f), 255.0f);
        blue = Math.min(Math.max(blue, 0.0f), 255.0f);
        
        return new Color((int) red, (int) green, (int) blue);
    }
    
    private static int wrapHorizontalIndex(BufferedImage input, int i)
    {
        // This takes care of negative and positive indices beyond the image resolution
        return (i + input.getWidth()) % input.getWidth();
    }

    private static int wrapVerticalIndex(BufferedImage input, int i)
    {
        // This takes care of negative and positive indices beyond the image resolution
        return (i + input.getHeight()) % input.getHeight();
    }
    
}
