package rs.os.checklist;

import rs.os.checklist.adapter.ImageAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;

/**
 * Used for enabling used to select an image from application set of images
 * 
 * @author zgavrilovic
 * 
 */
public class GalleryActivity extends Activity {

	private Integer[] mImageIds = { R.drawable.checklist,
			R.drawable.categories_grey};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gallery);

		Gallery g = (Gallery) findViewById(R.id.gallery);
		g.setAdapter(new ImageAdapter(this, mImageIds));

		g.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				setResult(mImageIds[position]);
				finish();
			}
		});
	}
}
