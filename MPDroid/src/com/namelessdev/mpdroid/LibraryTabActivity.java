package com.namelessdev.mpdroid;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ArrayAdapter;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.namelessdev.mpdroid.fragments.AlbumsFragment;
import com.namelessdev.mpdroid.fragments.ArtistsFragment;
import com.namelessdev.mpdroid.fragments.FSFragment;
import com.namelessdev.mpdroid.fragments.SongSearchMessageFragment;

public class LibraryTabActivity extends SherlockFragmentActivity implements OnNavigationListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
     * sections. We use a {@link android.support.v4.app.FragmentPagerAdapter} derivative, which will
     * keep every loaded fragment in memory. If this becomes too memory intensive, it may be best
     * to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;	

    @TargetApi(11)
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        
        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(true);
        
        ArrayAdapter<CharSequence> actionBarAdapter = new ArrayAdapter<CharSequence>(this, R.layout.sherlock_spinner_item);
        actionBarAdapter.add(getString(R.string.artists));
        actionBarAdapter.add(getString(R.string.albums));
        actionBarAdapter.add(getString(R.string.songs));
        actionBarAdapter.add(getString(R.string.files));
        
        if(Build.VERSION.SDK_INT >= 14) {
        	//Bug on ICS with sherlock's layout
        	actionBarAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        } else {
        	actionBarAdapter.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
        }
        actionBar.setListNavigationCallbacks(actionBarAdapter, this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding tab.
        // We can also use ActionBar.Tab#select() to do this if we have a reference to the
        // Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });
    }

	@Override
	public void onStart() {
		super.onStart();
		MPDApplication app = (MPDApplication) getApplicationContext();
		app.setActivity(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		
		MPDApplication app = (MPDApplication) getApplicationContext();
		app.unsetActivity(this);
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		mViewPager.setCurrentItem(itemPosition);
		return true;
	}
	
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = null;
            switch (i) {
            	case 0: fragment = new ArtistsFragment(); break;
            	case 1: fragment = new AlbumsFragment(); break;
            	case 2: fragment = new SongSearchMessageFragment(); break;
            	case 3: fragment = new FSFragment(); break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: return getString(R.string.artists);
                case 1: return getString(R.string.albums);
                case 2: return getString(R.string.songs);
                case 3: return getString(R.string.files);
            }
            return null;
        }
    }
}
