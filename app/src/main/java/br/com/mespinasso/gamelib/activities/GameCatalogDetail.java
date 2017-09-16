package br.com.mespinasso.gamelib.activities;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import br.com.mespinasso.gamelib.R;
import br.com.mespinasso.gamelib.dao.LibraryGameDAO;
import br.com.mespinasso.gamelib.helper.Extras;
import br.com.mespinasso.gamelib.models.CatalogGame;
import br.com.mespinasso.gamelib.models.LibraryGame;

public class GameCatalogDetail extends AppCompatActivity {

    private CatalogGame game;

    private ImageView ivBanner;
    private TextView tvDescription;
    private TextView tvGenre;
    private TextView tvPlatforms;
    private TextView tvDeveloper;
    private TextView tvPublisher;

    private LibraryGameDAO libraryGameDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_catalog_detail);

        libraryGameDAO = new LibraryGameDAO(this);

        ivBanner = (ImageView) findViewById(R.id.iv_catalog_detail_banner);
        tvDescription = (TextView) findViewById(R.id.tv_catalog_detail_description);
        tvGenre = (TextView) findViewById(R.id.tv_catalog_detail_genre);
        tvPlatforms = (TextView) findViewById(R.id.tv_catalog_detail_platforms);
        tvDeveloper = (TextView) findViewById(R.id.tv_catalog_detail_developer);
        tvPublisher = (TextView) findViewById(R.id.tv_catalog_detail_publisher);

        Intent intent = getIntent();
        game = (CatalogGame) intent.getSerializableExtra(Extras.EXTRA_CATALOG_GAME_OBJECT);

        loadData();
    }

    private void loadData() {
        setTitle(game.getTitle());

        tvDescription.setText(game.getDescription());
        tvGenre.setText(game.getGenre());
        tvPlatforms.setText(game.getPlatform());
        tvDeveloper.setText(game.getDeveloper());
        tvPublisher.setText(game.getPublisher());

        Picasso.with(this)
                .load(game.getBannerURL())
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_placeholder)
                .into(ivBanner);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.game_catalog_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_add_library:
                addGameToLibrary();
                break;
            case R.id.action_share:

                Intent shareIntent = ShareCompat.IntentBuilder
                        .from(this)
                        .setType("text/plain")
                        .setText(getString(R.string.share_text) + " " + game.getTitle())
                        .getIntent();

                if(shareIntent.resolveActivity(getPackageManager()) != null)
                    startActivity(shareIntent);
                break;
        }

        return true;
    }

    private void addGameToLibrary() {
        if(!libraryGameDAO.checkLibraryGame(game.getId())) {
            final LibraryGame libraryGame = new LibraryGame(game, 0.0, "");

            final Dialog dialog = new Dialog(this);
            dialog.setTitle(libraryGame.getGame().getTitle());
            dialog.setContentView(R.layout.dialog_game);

            TextView tvDialogTitle = (TextView) dialog.findViewById(R.id.tv_dialog_title);
            final EditText etDialogNotes = (EditText) dialog.findViewById(R.id.et_dialog_notes);
            final RatingBar rbDialog = (RatingBar) dialog.findViewById(R.id.rb_dialog);
            Button btSave = (Button) dialog.findViewById(R.id.bt_dialog_save);

            tvDialogTitle.setText(libraryGame.getGame().getTitle());
            etDialogNotes.setText(libraryGame.getNotes());
            rbDialog.setRating(libraryGame.getRating().floatValue());

            btSave.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    libraryGame.setNotes(etDialogNotes.getText().toString());
                    libraryGame.setRating((double) rbDialog.getRating());

                    try {
                        libraryGameDAO.add(libraryGame);
                        dialog.dismiss();
                        Toast.makeText(v.getContext(), R.string.added_to_library, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(v.getContext(), R.string.error_add_to_library, Toast.LENGTH_LONG).show();
                    }
                }
            });

            dialog.show();
        } else {
            Toast.makeText(this, R.string.already_in_library, Toast.LENGTH_SHORT).show();
        }
    }
}
