package br.com.budismo.nikkyojers.ui.hbs;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import br.com.budismo.nikkyojers.R;

public class HbsActivity extends AppCompatActivity implements SearchAsyncTask.OnSearch{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    SearchAsyncTask mSearchAsyncTask;
    MenuItem mMenuSearchItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hbs);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hbs, menu);

        // Associate searchable configuration with the SearchView
        final SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(HbsActivity.this, "Query:" + query, Toast.LENGTH_SHORT).show();
                mSearchAsyncTask = new SearchAsyncTask(HbsActivity.this, HbsActivity.this);
                mSearchAsyncTask.execute(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mMenuSearchItem = menu.findItem(R.id.search);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
/*        if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSearchAsyncTask != null) {
            mSearchAsyncTask.cancel(true);
            mSearchAsyncTask = null;
        }
    }

    @Override
    public void onSearched(final SearchResult result) {
        mViewPager.setCurrentItem(result.getSectionNumber());
        PlaceholderFragment fragment = (PlaceholderFragment) mSectionsPagerAdapter.getRegisteredFragment(result.getSectionNumber());
        fragment.setTextSearched(result);
        mMenuSearchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                PlaceholderFragment fragment = (PlaceholderFragment) mSectionsPagerAdapter.getRegisteredFragment(result.getSectionNumber());
                fragment.clearTextSearched();
                return true;
            }
        });
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        private TextView mTextView;
        private NestedScrollView mScrollView;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_hbs, container, false);
            mScrollView = rootView.findViewById(R.id.section_container);
            mTextView = rootView.findViewById(R.id.section_label);
            setTextFromResource();
            return rootView;
        }

        public void setTextSearched(SearchResult result) {
            int lineNumber = mTextView.getLayout().getLineForOffset(result.getIndexOfQuery());
            setTextFromHtml(mTextView, result.getfullTextHighlighted());
            mScrollView.scrollTo(0, mTextView.getLayout().getLineTop(lineNumber));
        }

        public void clearTextSearched() {
            setTextFromResource();
        }

        private void setTextFromHtml(TextView textView, String string) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                textView.setText(Html.fromHtml(string, Html.FROM_HTML_MODE_COMPACT));
            } else {
                textView.setText(Html.fromHtml(string));
            }
        }

        private void setTextFromResource() {
            String contentString = "";
            int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);

            switch (sectionNumber) {
                case 1:
                    contentString = getString(R.string.section_1_content);
                    break;
                case 2:
                    contentString = getString(R.string.section_2_content);
                    break;
                case 3:
                    contentString = getString(R.string.section_3_1_content);
                    break;
            }

            setTextFromHtml(mTextView, contentString);
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }
    }
}
