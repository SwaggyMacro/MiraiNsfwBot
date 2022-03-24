package com.julym.util;

import net.mamoe.mirai.message.data.Image;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Iterator;
import java.util.Objects;

public class ImageUtil {

    public static String saveImage(Image image, String pluginPath, String ext) throws IOException {
        URL url = new URL(Image.queryUrl(image));
        final InputStream inputStream = url.openConnection().getInputStream();

        final File imageFile = new File(pluginPath + "images"+ File.separator + image.getImageId());

        if (!imageFile.isFile()) {
            if (!imageFile.createNewFile()){
                System.out.println("Create image file failed.");
            }
        }

        int packSize = 10240;
        byte[] bytes = new byte[packSize];
        int len;
        try (OutputStream outputStream = new FileOutputStream(imageFile)) {
            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
            }
        }
        inputStream.close();
        //String fileExt = getExtension(imageFile.getPath());
        //Get the truly ext of image file. (get it form message now)
        //String fileExt = Objects.requireNonNull(FileTypeJudge.getType(imageFile.getPath())).toString().toLowerCase();
        // convert image to jpeg
        if (!ext.equals("jpg")){
            if (ext.equals("jpeg")){
                return imageFile.getPath();
            }
            String[] fileName = imageFile.getPath().split("\\.");
            try {
                forJpg(imageFile.getPath(), fileName[0] + ".jpg", ext, "jpeg");
            }catch (IIOException e){
                png2jpg(imageFile.getPath(),fileName[0] + ".jpg");
            }
            return fileName[0] + ".jpg";
        }
        return imageFile.getPath();
    }

    private static void forJpg(String input,String output,String srcExt, String dstExt) throws IOException {

        FileImageInputStream fiis = new FileImageInputStream(new File(input));
        FileImageOutputStream fios = new FileImageOutputStream(new File(output));

        ImageReader jpegReader = null;
        Iterator<ImageReader> it1 = ImageIO.getImageReadersByFormatName(srcExt);
        if(it1.hasNext())
        {
            jpegReader = it1.next();
        }
        Objects.requireNonNull(jpegReader).setInput(fiis);

        ImageWriter bmpWriter = null;
        Iterator<ImageWriter> it2 = ImageIO.getImageWritersByFormatName(dstExt);
        if(it2.hasNext())
        {
            bmpWriter = it2.next();
        }
        Objects.requireNonNull(bmpWriter).setOutput(fios);
        BufferedImage br = jpegReader.read(0);
        bmpWriter.write(br);
        fiis.close();
        fios.close();
        if(!new File(input).delete()) System.out.println("Delete file failed");
    }

    private static void png2jpg(String path, String outpath){
        BufferedImage bufferedImage;
        try {
            // read image file
            bufferedImage = ImageIO.read(new File(path));
            // create a blank, RGB, same width and height, and a white
            // background
            BufferedImage newBufferedImage = new BufferedImage(
                    bufferedImage.getWidth(), bufferedImage.getHeight(),
                    BufferedImage.TYPE_INT_RGB);
            // TYPE_INT_RGB:create a rgb image, 24bit, convert 32bit to 24bit.
            newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0,
                    Color.WHITE, null);
            // write to jpeg file
            ImageIO.write(newBufferedImage, "jpg", new File(outpath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
