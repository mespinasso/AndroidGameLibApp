package br.com.mespinasso.gamelib.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import br.com.mespinasso.gamelib.R;
import br.com.mespinasso.gamelib.adapters.GameLibraryAdapter;
import br.com.mespinasso.gamelib.dao.LibraryGameDAO;
import br.com.mespinasso.gamelib.models.CatalogGame;
import br.com.mespinasso.gamelib.models.LibraryGame;
import br.com.mespinasso.gamelib.requests.GameCatalogRequest;

/**
 * A simple {@link Fragment} subclass.
 */
public class GameLibraryFragment extends Fragment {

    private LibraryGameDAO libraryGameDAO;

    private RecyclerView rvGameLibrary;
    private RecyclerView.Adapter adapter;

    private List<CatalogGame> listCatalogGames;
    private List<LibraryGame> listLibraryGames;

    public GameLibraryFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_library, container, false);

        rvGameLibrary = (RecyclerView) view.findViewById(R.id.rv_game_library);
        rvGameLibrary.setHasFixedSize(true);
        rvGameLibrary.setLayoutManager(new LinearLayoutManager(getContext()));

        libraryGameDAO = new LibraryGameDAO(getContext());
        listCatalogGames = new LinkedList<>();
        listLibraryGames = new LinkedList<>();

        adapter = new GameLibraryAdapter(listLibraryGames, getContext());
        rvGameLibrary.setAdapter(adapter);

        loadGameLibrary();

        return view;
    }

    private void loadGameLibrary() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.loading_game_library));
        progressDialog.show();

        listLibraryGames = libraryGameDAO.getAll();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonResponseObject = new JSONObject(response);
                    JSONArray jsonResponseArray = jsonResponseObject.getJSONArray("games");

                    for(int i = 0; i < jsonResponseArray.length(); i++) {
                        JSONObject jsonResponse = jsonResponseArray.getJSONObject(i);

                        Integer id = jsonResponse.getInt("id");
                        String title = jsonResponse.getString("title");
                        String developer = jsonResponse.getString("developer");
                        String publisher = jsonResponse.getString("publisher");
                        String platform = jsonResponse.getString("platform");
                        String genre = jsonResponse.getString("genre");
                        String cover = jsonResponse.getString("cover");
                        String banner = jsonResponse.getString("banner");
                        String description = jsonResponse.getString("description");

                        CatalogGame catalogGame = new CatalogGame(id, title, description, developer, publisher, platform, genre, cover, banner);

                        listCatalogGames.add(catalogGame);
                    }

                    for(LibraryGame libraryGame : listLibraryGames) {
                        if(listCatalogGames.contains(libraryGame.getGame()))
                            libraryGame.setGame(listCatalogGames.get(listCatalogGames.indexOf(libraryGame.getGame())));
                    }

                    adapter = new GameLibraryAdapter(listLibraryGames, getContext());
                    rvGameLibrary.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    progressDialog.dismiss();
                }
            }
        };

        GameCatalogRequest gameCatalogRequest = new GameCatalogRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(gameCatalogRequest);
    }

}
