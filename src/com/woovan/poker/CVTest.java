package com.woovan.poker;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class CVTest {
	
	public static final String INPUT_IMAGE = "D://aaa.png";
	
	public static final String OUTPUT_IMAGE = "D://bbb.bmp";

	public static void main(String[] args) {
		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		Mat mat = Imgcodecs.imread(INPUT_IMAGE);
		Mat dst = new Mat();
		Imgproc.blur(mat, dst, mat.size(), new Point(11,11));
		
		ImageViewer viewer = new ImageViewer(dst, "bbb");
//		viewer.imshow();
		Imgcodecs.imwrite("OUTPUT_IMAGE", mat);
	}
}
