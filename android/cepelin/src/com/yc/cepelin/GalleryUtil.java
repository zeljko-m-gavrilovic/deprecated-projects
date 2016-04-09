package com.yc.cepelin;

public class GalleryUtil {

	public static String getLineDescription() {
	    return "[" + Thread.currentThread().getStackTrace()[3].getLineNumber() + "] ";
	}
}
