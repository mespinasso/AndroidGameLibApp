package br.com.mespinasso.gamelib.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.facebook.login.LoginManager;

import br.com.mespinasso.gamelib.R;
import br.com.mespinasso.gamelib.fragments.AboutFragment;
import br.com.mespinasso.gamelib.fragments.GameCatalogFragment;
import br.com.mespinasso.gamelib.fragments.GameLibraryFragment;
import br.com.mespinasso.gamelib.fragments.StoreFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setFragment(new GameCatalogFragment(), R.string.catalog);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_catalog) {
            setFragment(new GameCatalogFragment(), R.string.catalog);
        } else if (id == R.id.nav_library) {
            setFragment(new GameLibraryFragment(), R.string.library);
        } else if (id == R.id.nav_store) {
            setFragment(new StoreFragment(), R.string.stores);
        } else if (id == R.id.nav_about) {
            setFragment(new AboutFragment(), R.string.about);
        } else if (id == R.id.nav_quit) {
            exitApp();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void setFragment(Fragment fragment, @StringRes int title) {
        setTitle(title);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.rl_content_main, fragment, fragment.getTag()).commit();
    }

    private void exitApp() {
        LoginManager.getInstance().logOut();

        SharedPreferences sp = getSharedPreferences(LoginActivity.KEY_APP_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(LoginActivity.KEY_LOGIN, "");
        editor.apply();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

        this.finish();
    }
}
