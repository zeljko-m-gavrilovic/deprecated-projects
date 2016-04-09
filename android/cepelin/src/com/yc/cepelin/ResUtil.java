package com.yc.cepelin;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

public class ResUtil {
	/**
	 * Returns icon from the resource.
	 */
	public static Drawable getIcon(Resources resources, int iconId) {
		Drawable icon = resources.getDrawable(iconId);
		icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
		return icon;
	}

}
