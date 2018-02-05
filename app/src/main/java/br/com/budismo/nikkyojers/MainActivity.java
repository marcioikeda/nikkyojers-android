package br.com.budismo.nikkyojers;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import br.com.budismo.nikkyojers.auth.FirebaseUIHelper;
import br.com.budismo.nikkyojers.ui.calendar.AddEventActivity;
import br.com.budismo.nikkyojers.ui.calendar.CalendarFragment;
import br.com.budismo.nikkyojers.ui.feed.AddPostActivity;
import br.com.budismo.nikkyojers.ui.feed.FeedFragment;
import br.com.budismo.nikkyojers.ui.hbs.HbsActivity;
import br.com.budismo.nikkyojers.util.Util;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FirebaseUIHelper.SignListener,
        FeedFragment.FeedListener, CalendarFragment.CalendarListener {

    private FirebaseUIHelper firebaseUIHelper;
    private CircleImageView mIvProfile;
    private TextView mTvUsername;
    private TextView mTvEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mIvProfile = navigationView.getHeaderView(0).findViewById(R.id.profile_image);
        mTvUsername = navigationView.getHeaderView(0).findViewById(R.id.tv_username);
        mTvEmail = navigationView.getHeaderView(0).findViewById(R.id.tv_email);
        Util.logKeyHash(this);
        firebaseUIHelper = new FirebaseUIHelper(this);

        if (savedInstanceState == null) {
            //Inflate Main and check drawer menu
            FeedFragment fragment = new FeedFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_main, fragment).commit();
            navigationView.setCheckedItem(R.id.nav_news);
        }

        //Handle backstack to check drawer menu
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Fragment current = getSupportFragmentManager().findFragmentById(R.id.content_main);
                if (current instanceof FeedFragment) {
                    navigationView.setCheckedItem(R.id.nav_news);
                } else if (current instanceof CalendarFragment) {
                    navigationView.setCheckedItem(R.id.nav_calendar);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseUIHelper.addAuthStateListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        firebaseUIHelper.removeAuthStateListener();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_news) {
            FeedFragment fragment = new FeedFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, fragment).addToBackStack(null).commit();
        } else if (id == R.id.nav_calendar) {
            CalendarFragment fragment = CalendarFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, fragment).addToBackStack(null).commit();
//        } else if (id == R.id.nav_achievements) {
        } else if (id == R.id.nav_hbs) {
            Intent intent = new Intent(this, HbsActivity.class);
            startActivity(intent);
//        } else if (id == R.id.nav_settings) {

//        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_logoff) {
            firebaseUIHelper.signOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Util.RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Sign-in succeeded, set up the UI
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public void onSignIn() {
        FirebaseUser user = firebaseUIHelper.getUser();
        if (user != null) {
            Toast.makeText(this, "User: " + user.getDisplayName() + " is signed in", Toast.LENGTH_LONG).show();
            Util.bindUserPictureIntoView(this, user.getPhotoUrl(), mIvProfile);
            mTvUsername.setText(user.getDisplayName());
            mTvEmail.setText(user.getEmail());
        }
    }

    @Override
    public void onSignOut() {
        Toast.makeText(this, "Signed out", Toast.LENGTH_LONG).show();
        Util.startFirebaseUIActivity(this);
    }

    @Override
    public void onFabAddPostClicked() {
        Intent intent = new Intent(MainActivity.this, AddPostActivity.class);
        startActivity(intent);
    }

    @Override
    public void onFabAddEventClicked() {
        Intent intent = new Intent(MainActivity.this, AddEventActivity.class);
        startActivity(intent);
    }
}
