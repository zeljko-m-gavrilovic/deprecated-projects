package com.yc.cepelin;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TabHost;

public class ItemTabActivity extends TabActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        
        setContentView(R.layout.item_tab_activity);

        // create intent for every tab
        Intent intentInfo = new Intent(this, InfoActivity.class);
        Intent intentComments = new Intent(this, CommentsActivity.class);
        Intent intentMap = new Intent(this, ShowPoiOnMapActivity.class);
        Intent intentGallery = new Intent(this, GalleryActivity.class);

        // take extra parameters from intent
        Intent intent = getIntent();
        String itemType = intent.getExtras().getString(MainTabActivity.ITEM_TYPE);
        Integer id = Integer.valueOf(intent.getExtras().getInt(MainTabActivity.ID));

        // put extra parameters in tab intents
        if ((itemType != null) && (id != null)) {
            putExtras(intentInfo, itemType, id);
            putExtras(intentComments, itemType, id);            
            putExtras(intentMap, itemType, id);
            /*
            intentMap.putExtra(ShowPoiOnMapActivity.PARAM_TITLE, "Zlatno burence");
            intentMap.putExtra(ShowPoiOnMapActivity.PARAM_SNIPPET, "Snipet!");
            
            intentMap.putExtra(ShowPoiOnMapActivity.PARAM_GEO_LATITUDE, 44.8025677);
            intentMap.putExtra(ShowPoiOnMapActivity.PARAM_GEO_LONGITUDE, 20.4673845);
            */
            putExtras(intentGallery, itemType, id);
        }

        // create tab control and link it with appropriate intents
        TabHost tabHost = getTabHost();
        tabHost.addTab(tabHost.newTabSpec("info_tab")
        			.setIndicator(getText(R.string.info_tab), getResources().getDrawable(R.drawable.info_icon))
        			.setContent(intentInfo));
        tabHost.addTab(tabHost.newTabSpec("comments_tab")
        			.setIndicator(getText(R.string.comments_tab), getResources().getDrawable(R.drawable.comment_icon))
        			.setContent(intentComments));
        tabHost.addTab(tabHost.newTabSpec("map_tab")
        			.setIndicator(getText(R.string.map_tab), getResources().getDrawable(R.drawable.map_icon))
        			.setContent(intentMap));
        tabHost.addTab(tabHost.newTabSpec("gallery_tab")
        			.setIndicator(getText(R.string.gallery_tab), getResources().getDrawable(R.drawable.gallery_icon))
        			.setContent(intentGallery));
        
        tabHost.setCurrentTab(0);        
    }

    private void putExtras(Intent intent, String itemType, Integer id) {
        intent.putExtra(MainTabActivity.ITEM_TYPE, itemType);
        intent.putExtra(MainTabActivity.ID, id);        
    }
}