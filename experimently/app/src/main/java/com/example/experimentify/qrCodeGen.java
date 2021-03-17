package com.example.experimentify;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class qrCodeGen extends AppCompatActivity {

    public class MyQr {

        // Function to create the QR code
        public void createQR(String data, String path, String charset, Map hashMap, int height, int width) throws WriterException, IOException {

            BitMatrix matrix = new MultiFormatWriter().encode(new String(data.getBytes(charset), charset), BarcodeFormat.QR_CODE, width, height);

            MatrixToImageWriter.writeToFile(
                    matrix,
                    path.substring(path.lastIndexOf('.') + 1),
                    new File(path));
        }

        // Driver code
        public void main(String[] args) throws WriterException, IOException, NotFoundException {

            // The data that the QR code will contain
            String data = "www.geeksforgeeks.org";

            // The path where the image will get saved
            String path = "demo.png";

            // Encoding charset
            String charset = "UTF-8";

            Map<EncodeHintType, ErrorCorrectionLevel> hashMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();

            hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

            // Create the QR code and save
            // in the specified folder
            // as a jpg file
            createQR(data, path, charset, hashMap, 200, 200);
            System.out.println("QR Code Generated!!! ");
        }
    }
}
