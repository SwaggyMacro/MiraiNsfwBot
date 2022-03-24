package com.julym.util;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import ai.onnxruntime.OrtSession.Result;
import com.julym.config.NsfwConfig;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
public class AntiModel {
    private byte[] model;
    private String modelName;
    public AntiModel(String modelName) {
        try {
            this.modelName = modelName;
            model = Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(modelName)).readAllBytes();
            //model = new FileInputStream("M:/IdeaProjects/nsfw/src/main/resources/model.onnx").readAllBytes();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public String detector(BufferedImage content){
        BufferedImage scaled;
        String inputName;
        switch (this.modelName){
            case "model.inceptionv3.onnx":
                scaled = scaleImg(content, 299, 299);
                inputName = "input_1";
                break;
            case "model.mobilenet.onnx":
                scaled = scaleImg(content, 224, 224);
                inputName = "input_1";
                break;
            default:
                scaled = scaleImg(content, 256, 256);
                inputName = "inputs";
                break;
        }
        // BufferedImage scaled = scaleImg(content, 299, 299); inceptionv3
        float[][][][] inputArray = imageToMatrix(scaled);
        try {
            OrtEnvironment env = OrtEnvironment.getEnvironment();
            OrtSession session = env.createSession(model);
            OnnxTensor onnxTensor =  OnnxTensor.createTensor(OrtEnvironment.getEnvironment(), inputArray);
            Result result = session.run(
                    Map.of(inputName, onnxTensor)
            );
            session.close();
            onnxTensor.close();
            return pred(result, 1);

        } catch (OrtException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static BufferedImage scaleImg(BufferedImage image, int width, int height){
        Image scaledImg = image.getScaledInstance(width,height, Image.SCALE_FAST);
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        img.getGraphics().drawImage(scaledImg, 0, 0, null);
        return img;
    }

    // retType = 0 return JsonObj = Other return JsonArr
    public static String pred(Result tensorResult, int retType) {
        try {
            float[][] outputProbs = (float[][]) tensorResult.get(0).getValue();
            float[] probabilities = outputProbs[0];
            float maxVal = Float.NEGATIVE_INFINITY;
            String[] label = {"drawings", "hentai", "neutral", "porn", "sexy"};
            String result;
            for (int i = 0; i < probabilities.length; i++) {
                if (probabilities[i] > maxVal) {
                    maxVal = probabilities[i];

                }
                for(int j=0;j<probabilities.length-1-i;j++)
                {
                    if(probabilities[j]>probabilities[j+1])
                    {
                        float proTemp = probabilities[j];
                        probabilities[j]=probabilities[j+1];
                        probabilities[j+1]=proTemp;
                        String labelTemp = label[j];
                        label[j] = label[j+1];
                        label[j+1] = labelTemp;
                    }
                }
                // result.setValue(i, probabilities[i]);
            }
            if (retType == 0) {
                result = "{\"" + label[4] + "\":" + probabilities[4] +
                        ",\"" + label[3] + "\":" + probabilities[3] +
                        ", \"" + label[2] + "\":" + probabilities[2] +
                        ", \"" + label[1] + "\":" + probabilities[1] +
                        ", \"" + label[0] + "\":" + probabilities[0] + "}";
            }else{
                result = "[{\"className\":\"" + label[4] + "\", \"probability\":" + probabilities[4] +
                        "}, {\"className\":\"" + label[3] + "\", \"probability\":" + probabilities[3] +
                        "}, {\"className\":\"" + label[2] + "\", \"probability\":" + probabilities[2] +
                        "}, {\"className\":\"" + label[1] + "\", \"probability\":" + probabilities[1] +
                        "}, {\"className\":\"" + label[0] + "\", \"probability\":" + probabilities[0] + "}]";
            }
            tensorResult.close();
            return result;
        } catch (OrtException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static float[][][][] imageToMatrix(BufferedImage image){
        int width = image.getWidth();
        int height = image.getHeight();
        int[] pixels = new int[width * height];
        PixelGrabber pg = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        float[][][][] ret = new float[1][pg.getHeight()][pg.getWidth()][3];
        int pixel;
        int row = 0;
        int col = 0;
        while (row * width + col < pixels.length){
            pixel = row * width + col;
            ret[0][row][col][2] = (pixels[pixel] & 0x000000FF) / 255f; // blue
            ret[0][row][col][1] = (pixels[pixel]>> 8 & 0x000000FF) / 255f; // green
            ret[0][row][col][0] = (pixels[pixel]>> 16 & 0x000000FF) / 255f; // red
            col++;
            if (col == width - 1) {
                col = 0;
                row++;
            }
        }
        return ret;
/*
 another way
        for(int i=0;i<image.getHeight();i++)
        {
            for(int j=0;j<image.getWidth();j++)
            {
                ret[i][j][2]= (image.getRGB(i, j)>> 16) & 0x000000FF; // red
                ret[i][j][1]= (image.getRGB(i, j)>>8 ) & 0x000000FF; // green
                ret[i][j][0]= (image.getRGB(i, j)) & 0x000000FF; // blue
            }
        }
*/
    }
}