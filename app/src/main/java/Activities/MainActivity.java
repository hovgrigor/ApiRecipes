package Activities;

import android.app.SearchManager;
import android.content.Context;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ArrayAdapter;

import com.example.rustam.apirecipes.R;

import java.util.Objects;

import Adapters.PagerAdapter;
import Ingridientslist.Ingridients;

public class MainActivity extends AppCompatActivity implements SearchArea.OnFragmentInteractionListener,Favourites.OnFragmentInteractionListener  {

    ViewPager m_viewPager;
    PagerAdapter m_pagerAdapter;
    int m_position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tabLayout = findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setText("Favourite"));
        tabLayout.addTab(tabLayout.newTab().setText("Search"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        m_viewPager = findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        m_viewPager.setAdapter(adapter);
        m_viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(m_viewPager));


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                m_position = tab.getPosition();
                //viewPager.setCurrentItem(tab.getPosition());
                //pagerAdapter = (PagerAdapter)viewPager.getAdapter();
                //Favourites favourites = Objects.requireNonNull(pagerAdapter).m_favourite;
                //favourites.refreshFavourites();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);

        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.menu_search_my).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);

        final SearchView.SearchAutoComplete searchAutoComplete = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setDropDownBackgroundResource(android.R.color.background_light);

        String dataArr[] = new Ingridients().getIngs();
        ArrayAdapter<String> newsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, dataArr);
        searchAutoComplete.setAdapter(newsAdapter);

        searchAutoComplete.setOnItemClickListener((adapterView, view, itemIndex, id) ->
        {
            String queryString=(String)adapterView.getItemAtPosition(itemIndex);
            searchAutoComplete.setText(queryString);
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            //Triggered when text is submitted in search bar
            public boolean onQueryTextSubmit(String s)
            {
                if(m_position == 1) {
                    //Clearing focus so it wont call twice
                    searchView.clearFocus();
                    m_pagerAdapter = (PagerAdapter) m_viewPager.getAdapter();
                    SearchArea searchArea = Objects.requireNonNull(m_pagerAdapter).m_searchArea;
                    searchArea.firstSearch(s);
                }
                return true;
            }
            @Override
            public boolean onQueryTextChange(String s)
            {
                return true;
            }
        });
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
