
package assignment2;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ImageProcessing extends ImageProcessingGUI
{
    public ImageProcessing(){     
            
    if(ConvertGreyScaleCheck.isSelected()) {
     final BufferedImage input = InputImagePanel.getBufferedImage();     
     BufferedImage output = new BufferedImage(input.getWidth(),
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
        
        OutputImagePanel.setImage(output);
     }
                      
}

    public static void main(String[] args) {


    }
    
}
