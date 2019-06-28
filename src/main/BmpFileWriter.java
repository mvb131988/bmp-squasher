package main;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BmpFileWriter {

    public void write(BmpFile bmpFile) throws IOException {
        Path path =  Paths.get("bmp_img_out.bmp");
        try(OutputStream os = Files.newOutputStream(path)) {
            os.write(convert(bmpFile.header));
            os.write(bmpFile.pixelsToBytes());
        }
        
    }
    
    private byte[] convert(BmpFileHeader h) {
        BmpFileHeaderRaw h0 = new BmpFileHeaderRaw(h);
        byte[] header = h0.toByteArray();
        return header;
    }
    
}
