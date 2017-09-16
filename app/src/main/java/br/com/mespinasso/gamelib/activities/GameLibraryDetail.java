package br.com.mespinasso.gamelib.activities;

import android.content.Intent;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.com.mespinasso.gamelib.R;
import br.com.mespinasso.gamelib.helper.Extras;
import br.com.mespinasso.gamelib.models.CatalogGame;
import br.com.mespinasso.gamelib.models.LibraryGame;

public class GameLibraryDetail extends AppCompatActivity {

    private LibraryGame game;

    private ImageView ivBanner;
    private TextView tvDescription;
    private TextView tvGenre;
    private TextView tvPlatforms;
    private TextView tvDeveloper;
    private TextView tvPublisher;
    private TextView tvRating;
    private TextView tvNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_library_detail);

        ivBanner = (ImageView) findViewById(R.id.iv_catalog_detail_banner);
        tvDescription = (TextView) findViewById(R.id.tv_catalog_detail_description);
        tvGenre = (TextView) findViewById(R.id.tv_catalog_detail_genre);
        tvPlatforms = (TextView) findViewById(R.id.tv_catalog_detail_platforms);
        tvDeveloper = (TextView) findViewById(R.id.tv_catalog_detail_developer);
        tvPublisher = (TextView) findViewById(R.id.tv_catalog_detail_publisher);
        tvRating = (TextView) findViewById(R.id.tv_catalog_detail_rating);
        tvNotes = (TextView) findViewById(R.id.tv_catalog_detail_notes);

        Intent intent = getIntent();
        game = (LibraryGame) intent.getSerializableExtra(Extras.EXTRA_LIBRARY_GAME_OBJECT);

        loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.game_library_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_share:
                Intent shareIntent = ShareCompat.IntentBuilder
                        .from(this)
                        .setType("text/plain")
                        .setText(getString(R.string.share_text) + " " + game.getGame().getTitle())
                        .getIntent();

                if(shareIntent.resolveActivity(getPackageManager()) != null)
                    startActivity(shareIntent);
                break;
        }

        return true;
    }

    private void loadData() {
        setTitle(game.getGame().getTitle());

        tvDescription.setText(game.getGame().getDescription());
        tvGenre.setText(game.getGame().getGenre());
        tvPlatforms.setText(game.getGame().getPlatform());
        tvDeveloper.setText(game.getGame().getDeveloper());
        tvPublisher.setText(game.getGame().getPublisher());
        tvRating.setText(game.getRating().intValue() + "/5");
        tvNotes.setText(game.getNotes());

        Picasso.with(this)
                .load(game.getGame().getBannerURL())
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_placeholder)
                .into(ivBanner);
    }
}
