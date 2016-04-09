package rs.os.checklist.adapter;

import rs.os.checklist.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

/**
 * Used behind the GalleryActivity
 * @author zgavrilovic
 *
 */
public class ImageAdapter extends BaseAdapter {
    int mGalleryItemBackground;
    private Context mContext;
    private Integer[] mImageIds;
    
    public ImageAdapter(Context c, Integer[] mImageIds) {
        mContext = c;
        this.mImageIds = mImageIds;
        TypedArray a = mContext.obtainStyledAttributes(R.styleable.Gallery);
        mGalleryItemBackground = a.getResourceId(
                R.styleable.Gallery_android_galleryItemBackground, 0);
        a.recycle();
    }

    public int getCount() {
        return mImageIds.length;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
    	ImageView i = (ImageView) convertView;
    	if(convertView == null) {
    		i = new ImageView(mContext);
        }
    	i.setImageResource(mImageIds[position]);
        i.setLayoutParams(new Gallery.LayoutParams(100, 100));
        i.setScaleType(ImageView.ScaleType.FIT_XY);
        i.setBackgroundResource(mGalleryItemBackground);
        return i;
    }
    
    @Override
	public int getViewTypeCount() {
		return 1;
	}
}
