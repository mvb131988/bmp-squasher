package main;

public class BmpFileTransformer {

    public BmpFile transform(BmpFile c) {
        BmpFile.Pixel[][] pixels0 = c.pixels;
        long width0 = c.header.imageWidth;
        long height0 = c.header.imageHeight;
        
        int width = (int)width0/4;
        int height = (int)height0/4;
        BmpFile.Pixel[][] pixels = new BmpFile.Pixel[height][width];
        
        for(int i=0; i<height; i++) {
            for(int j=0; j<width; j++) {
                pixels[i][j] = pixels0[i*4][j*4];
            }
        }
        
        //TODO: clone this
        BmpFileHeader header  = c.header;
        header.imageWidth = width;
        header.imageHeight = height;
        //header size = 54 bytes
        header.fileSize = 3*height*width + 54;
        header.sizeOfImageData = 3*height*width;
        
        BmpFile res = new BmpFile(header, pixels);
        return res;
    }
    
}
