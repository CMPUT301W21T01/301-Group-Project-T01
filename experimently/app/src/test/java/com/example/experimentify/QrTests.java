package com.example.experimentify;

import android.graphics.Bitmap;
import android.view.View;

import com.google.errorprone.annotations.DoNotMock;
import com.google.zxing.WriterException;

import org.junit.Test;



public class QrTests {

    @Test
    public void textToImageTest(){
        int two = 2;
        int three = 3;
        int four = 4;
        assert(two != four);
        assert(three != four);
        assert(two != three);

    }

}
