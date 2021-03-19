package com.example.experimentify;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;


public class qrCodeGen extends ExperimentActivity {
    static int whitePixel = 0xFFFFFFFF;
    static int blackPixel = 0xFF000000;
    // THIS METHOD WAS OBTAINED FROM STACKOVERFLOW. THIS IS HOW THE CREATION OF A QR WORKS BY USING THE ZXING LIBRARY THAT WE HAVE IMPLEMENTED
    // IN OUR PROJECT. TO GET IT TO WORK WITH OUR COUDE WE DID MODIFY ITS USE HOWEVER THE CREDIT IS DESERVERED TO THE AUTOR
    // AUTHOR: https://stackoverflow.com/users/4758255/ישו-אוהב-אותך
    // SOURCE: STACKOVERFLOW
    // LINK: https://stackoverflow.com/questions/46065310/how-to-create-a-qr-code-generator-for-android-using-fragments

    /**
     * This method takes the UID from the experiment and then uses the Zxing library to convert the string into a bitmap which will be
     * represented as a QR code allowing the user to scan it and open the rest of the experiments in their app
     * @param input
     * @param width
     * @param height
     * @throws null pointer exception
     */
    public static Bitmap textToImage(String input, int width, int height) throws WriterException, NullPointerException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(input, BarcodeFormat.DATA_MATRIX.QR_CODE,
                    width, height, null);
        } catch (IllegalArgumentException Illegalargumentexception) {
            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();
        int bitMatrixHeight = bitMatrix.getHeight();
        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];


        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;
            for (int x = 0; x < bitMatrixWidth; x++) {
                pixels[offset + x] = bitMatrix.get(x, y) ? blackPixel : whitePixel;
            }
        }
        Bitmap qrbitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        qrbitmap.setPixels(pixels, 0, width, 0, 0, bitMatrixWidth, bitMatrixHeight);

        return qrbitmap;
    }
    //END OF CODE USED
}
