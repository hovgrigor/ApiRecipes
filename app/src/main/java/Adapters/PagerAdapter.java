package Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import Activities.Favourites;
import Activities.MainActivity;
import Activities.SearchArea;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private int mNoOfTabs;


    public SearchArea m_searchArea;
    public Favourites m_favourite;

    public PagerAdapter(FragmentManager fm, int NumberOfTabs)
    {
        super(fm);
        this.mNoOfTabs = NumberOfTabs;
    }



    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0:
                m_favourite = new Favourites();
                return  m_favourite;
            case 1:
                m_searchArea = new SearchArea();
                return m_searchArea;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}