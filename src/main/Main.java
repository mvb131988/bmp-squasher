package main;

import java.io.IOException;

public class Main {

    //TODO: solve padding issue, when 3*width%4 != 0 round up width to make 3*new_width%4 == 0
    public static void main(String[] args) throws IOException {
        BmpFile c = new ImageReader().read();
        
        BmpFileTransformer t = new BmpFileTransformer();
        c = t.transform(c);
        
        new ImageInHtmlWriter().create(c);
        BmpFileWriter w = new BmpFileWriter();
        w.write(c);
    }
    
}