package com.gdj.studyoptimize;

import android.graphics.Bitmap;

public class Test {

	public static native String compressBitmap(int w, int h, int quality, byte[] fileNameBytes,
            boolean optimize);
}
