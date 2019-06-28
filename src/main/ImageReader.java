package main;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class ImageReader {

    public BmpFile read() {
        BmpFile bmpFile = null;
        
        Path path =  Paths.get("C:\\endava\\temp\\bmp_img.bmp");
        
        try(InputStream input = Files.newInputStream(path, StandardOpenOption.READ)) {
            
            byte[] header0 = new byte[54];
            input.read(header0, 0, 54);
            BmpFileHeaderRaw rawHeader = new BmpFileHeaderRaw(header0);
            BmpFileHeader header = transform(rawHeader);
            
            BmpFile.Pixel[][] pCanvas = new BmpFile.Pixel[183][];
            for(int z = 0; z<183; z++) {
                //read single row
                byte[] row  = new byte[276*3];
                input.read(row, 0, 276*3);
                
                BmpFile.Pixel[] pRow = new BmpFile.Pixel[276];
                //transform
                for(int i=0, j=0; i<276*3; i=i+3, j++) {
                    pRow[j] = new BmpFile.Pixel(row[i+2] & 0xFF, row[i+1] & 0xFF, row[i] & 0xFF);
                }
                
                pCanvas[z] = pRow;
            }
            
            bmpFile = new BmpFile(header, pCanvas);
            
            reverse(pCanvas);
                    
        } catch(IOException e) {
            System.out.println(e);
        }
        
        return bmpFile;
    }
    
    private BmpFileHeader transform(BmpFileHeaderRaw rawHeader) {
        BmpFileHeader header = new BmpFileHeader();
        header.signature = littleEndian2Bytes(rawHeader.signature);
        header.fileSize = littleEndian4Bytes(rawHeader.fileSize);
        header.reserved1 = littleEndian2Bytes(rawHeader.reserved1);
        header.reserved2 = littleEndian2Bytes(rawHeader.reserved2);
        header.imageDataOffset = littleEndian4Bytes(rawHeader.imageDataOffset);
        header.bitmapInfoHeaderSize = littleEndian4Bytes(rawHeader.bitmapInfoHeaderSize);
        header.imageWidth = littleEndian4Bytes(rawHeader.imageWidth);
        header.imageHeight = littleEndian4Bytes(rawHeader.imageHeight);
        header.numberOfPlanes = littleEndian2Bytes(rawHeader.numberOfPlanes);
        header.numberOfBitsPerPixel = littleEndian2Bytes(rawHeader.numberOfBitsPerPixel);
        header.compressionType = littleEndian4Bytes(rawHeader.compressionType);
        header.sizeOfImageData = littleEndian4Bytes(rawHeader.sizeOfImageData);
        header.horizontalResolution = littleEndian4Bytes(rawHeader.horizontalResolution);
        header.verticalResolution = littleEndian4Bytes(rawHeader.verticalResolution);
        header.numberOfColors = littleEndian4Bytes(rawHeader.numberOfColors);
        header.numberOfImportantColors = littleEndian4Bytes(rawHeader.numberOfImportantColors);
        return header;
    }
    
    private long littleEndian4Bytes(int[] bytes4) {
        long l =  (long) bytes4[0] + 
                 ((long) bytes4[1] << 8) + 
                 ((long) bytes4[2] << 16) + 
                 ((long) bytes4[3] << 24);
        return l;
    }
    
    private int littleEndian2Bytes(int[] bytes2) {
        int i =  bytes2[0] + 
                (bytes2[1] << 8); 
        return i;
    }
    
    private void reverse(BmpFile.Pixel[][] pCanvas) {
        for(int i=0; i<pCanvas.length/2; i++) {
            BmpFile.Pixel[] tmp = pCanvas[i];
            pCanvas[i] = pCanvas[pCanvas.length-1-i];
            pCanvas[pCanvas.length-1-i] = tmp;
        }
    }
    
}
