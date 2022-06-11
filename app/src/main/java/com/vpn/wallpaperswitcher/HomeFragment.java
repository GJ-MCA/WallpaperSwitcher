package com.vpn.wallpaperswitcher;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.vpn.wallpaperswitcher.APIHelper.WebURL;
import com.vpn.wallpaperswitcher.Adapter.WallPaperAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {
    SearchView svSearch;
    RecyclerView rvWallpapers;
    FloatingActionButton fabPrev, fabNext;

    ArrayList<String> wallpaperList;
    String url;
    String category;
    ProgressDialog dialog;
    int page;
    boolean curated = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        svSearch = view.findViewById(R.id.svSearch);
        svSearch.clearFocus();
        rvWallpapers = view.findViewById(R.id.rvWallpapers);
        fabPrev = view.findViewById(R.id.fabPrev);
        fabNext = view.findViewById(R.id.fabNext);

        dialog = new ProgressDialog(getContext());
        dialog.setTitle("Loading...");
        dialog.setMessage("Please Wait!");
        dialog.setCancelable(false);

        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchCategory();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        fabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWallpapers(url);
            }
        });

        fabPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (page > 1) {
                    String[] temp = url.split("/");
                    for (int i = 0; i < temp.length; i++) {
                        Log.d("Temp split", temp[i]);
                    }

                    if (temp[4].equals("curated")) {
                        url = WebURL.base_url + "curated/?per_page=40&page=" + (page-1);
                        getWallpapers(url);
                    } else {
                        temp = url.split("=");
                        for (int i = 0; i < temp.length; i++) {
                            Log.d("Temp split", temp[i]);
                        }

                        url = WebURL.base_url + "search/?query=" + temp[temp.length-1] + "&per_page=40&page=" + (page-1);
                        getWallpapers(url);
                    }

                }
            }
        });

        //set recycler view layout
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        rvWallpapers.setLayoutManager(manager);

        //for random wallpapers
        url = WebURL.base_url + "curated/?per_page=40&page=1";
        getWallpapers(url);

        return view;
    }

    private void searchCategory() {

        category = svSearch.getQuery().toString();
        if (category.equals("")) {
            FancyToast.makeText(getContext(), "Please enter Category", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
        } else {
            //for category wallpaper
            url = WebURL.base_url + "search/?query=" + category + "&per_page=40&page=1";
            getWallpapers(url);
        }

    }

    private void getWallpapers(String tempUrl) {

        dialog.show();
        wallpaperList = new ArrayList<String>();
        wallpaperList.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, tempUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                parseJSONWallpapers(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                dialog.dismiss();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", WebURL.authorization_key);
                return headers;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    private void parseJSONWallpapers(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            page = jsonObject.optInt("page");
            Log.d("PAGE : ", String.valueOf(page));
            url = jsonObject.optString("next_page");
            Log.d("URL : ", String.valueOf(url));
            JSONArray photoArray = jsonObject.getJSONArray("photos");
            int count = photoArray.length();
            if (count > 0) {
                //curated = true;
                for (int i = 0; i < photoArray.length(); i++) {
                    JSONObject photoObj = photoArray.getJSONObject(i);
                    String imgUrl = photoObj.getJSONObject("src").getString("portrait");
                    wallpaperList.add(imgUrl);
                }
                Log.d("Wallpaper count", String.valueOf(wallpaperList.size()));
                WallPaperAdapter adapter = new WallPaperAdapter(getContext(), wallpaperList);
                rvWallpapers.setAdapter(adapter);
                dialog.dismiss();
                svSearch.clearFocus();

            } else {
                FancyToast.makeText(getContext(), "No Wallpapers Found", FancyToast.DEFAULT, FancyToast.DEFAULT, false).show();
                dialog.dismiss();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
