package com.example.sherly.medigent.fitur.histori;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sherly.medigent.R;
import com.example.sherly.medigent.model.histori.HistoryModel;
import com.example.sherly.medigent.service.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class SuksesFragment extends Fragment {

    private RecyclerView rvDaftarHistori;
    private DaftarHistoriSuksesAdapter historiAdapter;


    public SuksesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sukses, container, false);

        final SharedPreferences pref = getActivity().getSharedPreferences("medigent", MODE_PRIVATE);
        final String id_user = pref.getString("id_user", "null");
        final String token = pref.getString("token", "null");

        rvDaftarHistori = (RecyclerView) view.findViewById(R.id.rvDaftarHistori);

        ApiService.service_get.getHistoryByUser23("Bearer "+token, id_user).enqueue(new Callback<HistoryModel>() {
            @Override
            public void onResponse(Call<HistoryModel> call, Response<HistoryModel> response) {
                if(response.isSuccessful()) {

                    historiAdapter = new DaftarHistoriSuksesAdapter(getActivity(), response.body().getDetailHistoryModels());
                    rvDaftarHistori.setLayoutManager(new LinearLayoutManager(getActivity()));
                    //rvDaftarHistori.setFocusable(false);
                    //rvDaftarHistori.setNestedScrollingEnabled(false);
                    rvDaftarHistori.setAdapter(historiAdapter);
                    historiAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(getActivity(), "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<HistoryModel> call, Throwable t) {

            }
        });
        return view;
    }

}
