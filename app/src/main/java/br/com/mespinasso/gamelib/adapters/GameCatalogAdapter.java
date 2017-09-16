package br.com.mespinasso.gamelib.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.mespinasso.gamelib.R;
import br.com.mespinasso.gamelib.activities.GameCatalogDetail;
import br.com.mespinasso.gamelib.dao.LibraryGameDAO;
import br.com.mespinasso.gamelib.helper.Extras;
import br.com.mespinasso.gamelib.models.CatalogGame;
import br.com.mespinasso.gamelib.models.LibraryGame;

/**
 * Created by MatheusEspinasso on 10/09/17.
 */

public class GameCatalogAdapter extends RecyclerView.Adapter<GameCatalogAdapter.ViewHolder> {

    private List<CatalogGame> listGames;

    public GameCatalogAdapter(List<CatalogGame> listGames, Context context) {
        this.listGames = listGames;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_game_catalog, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CatalogGame game = listGames.get(position);

        holder.tvCatalogTitle.setText(game.getTitle());
        holder.tvCatalogPlatforms.setText(game.getPlatform());

        Picasso.with(holder.itemView.getContext())
                .load(listGames.get(position).getCoverURL())
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_placeholder)
                .into(holder.ivCatalogCover);
        
        holder.pbCatalog.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() { return listGames.size(); }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView tvCatalogTitle;
        TextView tvCatalogPlatforms;
        ImageView ivCatalogCover;
        ProgressBar pbCatalog;
        ViewGroup container;

        public ViewHolder(View itemView) {
            super(itemView);

            tvCatalogTitle = (TextView) itemView.findViewById(R.id.tv_catalog_title);
            tvCatalogPlatforms = (TextView) itemView.findViewById(R.id.tv_catalog_platforms);
            ivCatalogCover = (ImageView) itemView.findViewById(R.id.iv_catalog_cover);
            pbCatalog = (ProgressBar) itemView.findViewById(R.id.pb_catalog);
            container = (ViewGroup) itemView.findViewById(R.id.cl_catalog);

            container.setOnClickListener(this);
            container.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            CatalogGame game = listGames.get(this.getAdapterPosition());

            Intent intent = new Intent(v.getContext(), GameCatalogDetail.class);
            intent.putExtra(Extras.EXTRA_CATALOG_GAME_OBJECT, game);

            v.getContext().startActivity(intent);

        }

        @Override
        public boolean onLongClick(View v) {
            final PopupMenu popup = new PopupMenu(v.getContext(), v);
            final Context popupMenuContext = v.getContext();

            popup.getMenuInflater().inflate(R.menu.game_catalog_context, popup.getMenu());

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.action_add_library:
                            final LibraryGameDAO libraryGameDAO = new LibraryGameDAO(popupMenuContext);
                            final CatalogGame catalogGame = listGames.get(getAdapterPosition());
                            final LibraryGame libraryGame = new LibraryGame(catalogGame, 0.0, "");

                            if(!libraryGameDAO.checkLibraryGame(libraryGame.getGame().getId())) {

                                final Dialog dialog = new Dialog(popupMenuContext);
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
                                Toast.makeText(popupMenuContext, R.string.already_in_library, Toast.LENGTH_SHORT).show();
                            }

                            break;
                    }

                    return true;
                }
            });

            popup.show();
            return true;
        }
    }
}
