package co.uk.android.lldc.custom;

import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

public class PageChangeListener implements OnPageChangeListener {

	@Override
	public void onPageScrollStateChanged(int state) {
		if (state == ViewPager.SCROLL_STATE_IDLE) {
			
		}
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
	}
}