package br.com.mespinasso.gamelib.adapters;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.com.mespinasso.gamelib.R;
import br.com.mespinasso.gamelib.activities.StoreMapActivity;
import br.com.mespinasso.gamelib.helper.Extras;
import br.com.mespinasso.gamelib.models.Store;

/**
 * Created by MatheusEspinasso on 14/09/17.
 */

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder> {

    private List<Store> listStores;

    public StoreAdapter(List<Store> listStores, Context context) {
        this.listStores = listStores;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_store, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Store store = listStores.get(position);

        holder.store = store;
        holder.tvStoreName.setText(store.getName());
        holder.tvStoreAddress.setText(store.getAddress());

        holder.pbStore.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() { return listStores.size(); }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        Store store;

        TextView tvStoreName;
        TextView tvStoreAddress;
        ProgressBar pbStore;
        ViewGroup container;

        public ViewHolder(View itemView) {
            super(itemView);

            tvStoreName = (TextView) itemView.findViewById(R.id.tv_store_name);
            tvStoreAddress = (TextView) itemView.findViewById(R.id.tv_store_address);
            pbStore = (ProgressBar) itemView.findViewById(R.id.pb_store);
            container = (ViewGroup) itemView.findViewById(R.id.cl_store);

            container.setOnClickListener(this);
            container.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            openMapActivity(v.getContext());
        }

        @Override
        public boolean onLongClick(View v) {
            final PopupMenu popup = new PopupMenu(v.getContext(), v);
            final Context popupMenuContext = v.getContext();

            popup.getMenuInflater().inflate(R.menu.store_context, popup.getMenu());

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.action_open_map:
                            openMapActivity(popupMenuContext);
                            break;
                        case R.id.action_make_call:
                            makeCall(popupMenuContext);
                            break;
                    }

                    return true;
                }
            });

            popup.show();
            return true;
        }

        private void openMapActivity(Context context) {
            Store store = listStores.get(this.getAdapterPosition());

            Intent intent = new Intent(context, StoreMapActivity.class);
            intent.putExtra(Extras.EXTRA_STORE_OBJECT, store);

            context.startActivity(intent);
        }

        private void makeCall(Context context) {
            Uri telURI = Uri.parse("tel:" + listStores.get(this.getAdapterPosition()).getPhoneValue());
            Intent intent = new Intent(Intent.ACTION_CALL, telURI);

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Permission to make calls not conceded", Toast.LENGTH_LONG).show();

                return;
            }

            context.startActivity(intent);
        }
    }
}
