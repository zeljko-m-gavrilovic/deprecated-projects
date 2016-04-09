package com.yc.cepelin;

public class GalleryConstants {

	public static final boolean debug = true;
	
	public static final int HANDLER_MESSAGE_UPDATE_IMAGE = 0;
	public static final int HANDLER_MESSAGE_TAKE_IMAGE = 1;
	public static final int HANDLER_MESSAGE_IMAGE_NOT_SENT = 2;
	public static final int HANDLER_MESSAGE_IMAGE_SENT = 3;
	public static final int HANDLER_MESSAGE_HIDE_BUTTONS = 4;
	public static final int HANDLER_MESSAGE_SHOW_BUTTONS = 5;
	public static final int HANDLER_MESSAGE_NO_IMAGES = 6;

	public static final int ADD_PHOTO_FOR_ITEM_RESULT_STATUS = 0; // returned if photo is successfully uploaded
	public static final boolean ADD_PHOTO_FOR_ITEM_CHECK_STATUS = false; // set true on production
	
	// ako je true onda korisnik u slučaju neuspešnog slanja slike na server
	// ima priliku da pokuša ponovo novim klikom na dugme Pošalji
	public static final boolean RESEND_ENABLED = true;
	
	// ako su donje vrednosti obe false krajnja veličina slike je maksimalna podržana na uređaju
	// najverovatnije će biti bačen izuzetak zbog nedostatka memorije pri rotiranju ili dekodiranju bitmape
	public static final boolean RESIZE_ENABLED_METHOD_1 = false; // default is true
	public static final boolean RESIZE_ENABLED_METHOD_2 = true; // default is false
	
	public static final boolean SAVE_ONCE_ENABLED = false; // if true user can only once save the same picture
}
