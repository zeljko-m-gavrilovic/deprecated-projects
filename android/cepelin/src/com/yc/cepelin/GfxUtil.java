package com.yc.cepelin;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class GfxUtil {

	/**
	 * Draws text and rounded rectangle at given coordinates.
	 *
	 * @param canvas destination canvas
	 * @param x x coordinate of destination center
	 * @param y y coordinate of destination top
	 * @param padW width padding (single side)
	 * @param padH height padding (single side)
	 * @param r radius of rounded arc
	 * @param paint text paint
	 * @param rectPaint bacground rectangle paint.
	 */
	public static void drawTextInRect(Canvas canvas, float x, float y, float padW, float padH, float r, String text, Paint paint, Paint rectPaint) {
		Rect bounds = new Rect();
		paint.getTextBounds(text, 0, text.length(), bounds);
		float w2 = bounds.width() / 2.0f;
		float h = bounds.height();
		float w2p = w2 + padW;
		float h2 = h + padH + padH;
		RectF rectF = new RectF(x - w2p, y, x + w2p + 0.5f, y + h2 + 0.5f);
		canvas.drawRoundRect(rectF, r, r, rectPaint);
		canvas.drawText(text, x - w2, y + h + padH, paint);
	}
}
