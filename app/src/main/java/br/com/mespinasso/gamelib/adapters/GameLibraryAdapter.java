package br.com.mespinasso.gamelib.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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
import br.com.mespinasso.gamelib.activities.GameLibraryDetail;
import br.com.mespinasso.gamelib.dao.LibraryGameDAO;
import br.com.mespinasso.gamelib.helper.Extras;
import br.com.mespinasso.gamelib.models.LibraryGame;

/**
 * Created by MatheusEspinasso on 12/09/17.
 */

public class GameLibraryAdapter extends RecyclerView.Adapter<GameLibraryAdapter.ViewHolder> {

    private List<LibraryGame> listLibraryGames;

    public GameLibraryAdapter(List<LibraryGame> listLibraryGames, Context context) {
        this.listLibraryGames = listLibraryGames;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_game_library, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LibraryGame libraryGame = listLibraryGames.get(position);

        holder.libraryGame = libraryGame;
        holder.tvLibraryTitle.setText(libraryGame.getGame().getTitle());
        holder.tvLibraryPlatforms.setText(libraryGame.getGame().getPlatform());

        Picasso.with(holder.itemView.getContext())
                .load(listLibraryGames.get(position).getGame().getCoverURL())
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_placeholder)
                .into(holder.ivLibraryCover);

        holder.pbLibrary.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() { return listLibraryGames.size(); }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        LibraryGame libraryGame;

        TextView tvLibraryTitle;
        TextView tvLibraryPlatforms;
        ImageView ivLibraryCover;
        ProgressBar pbLibrary;
        ViewGroup container;

        public ViewHolder(View itemView) {
            super(itemView);

            tvLibraryTitle = (TextView) itemView.findViewById(R.id.tv_library_title);
            tvLibraryPlatforms = (TextView) itemView.findViewById(R.id.tv_library_platforms);
            ivLibraryCover = (ImageView) itemView.findViewById(R.id.iv_library_cover);
            pbLibrary = (ProgressBar) itemView.findViewById(R.id.pb_library);
            container = (ViewGroup) itemView.findViewById(R.id.cl_library);

            container.setOnClickListener(this);
            container.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            LibraryGame game = listLibraryGames.get(this.getAdapterPosition());

            Intent intent = new Intent(v.getContext(), GameLibraryDetail.class);
            intent.putExtra(Extras.EXTRA_LIBRARY_GAME_OBJECT, game);

            v.getContext().startActivity(intent);
        }

        @Override
        public boolean onLongClick(final View v) {
            PopupMenu popup = new PopupMenu(v.getContext(), v);
            popup.getMenuInflater().inflate(R.menu.game_library_context, popup.getMenu());

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.action_add_library:

                        final Dialog dialog = new Dialog(v.getContext());
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
                                    LibraryGameDAO dao = new LibraryGameDAO(v.getContext());
                                    dao.update(libraryGame);

                                    dialog.dismiss();
                                    notifyDataSetChanged();

                                    Toast.makeText(v.getContext(), R.string.saved_exclamation, Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Toast.makeText(v.getContext(), R.string.error_update, Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                        dialog.show();

                        break;
                    case R.id.action_del_library:

                        AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                        alert.setTitle(R.string.warning);
                        alert.setMessage(R.string.confirm_delete_game);
                        alert.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    LibraryGameDAO dao = new LibraryGameDAO(v.getContext());
                                    dao.delete(libraryGame);
                                    listLibraryGames.remove(libraryGame);

                                    notifyDataSetChanged();
                                } catch (Exception e) {
                                    Toast.makeText(v.getContext(), R.string.error_update, Toast.LENGTH_LONG).show();
                                } finally {
                                    dialog.dismiss();
                                }
                            }
                        });

                        alert.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        alert.show();

                        break;
                }

                return true;
                }
            });

            popup.show();
            return false;
        }
    }
}
