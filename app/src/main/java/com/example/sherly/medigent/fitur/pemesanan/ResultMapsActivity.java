package com.example.sherly.medigent.fitur.pemesanan;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sherly.medigent.R;
import com.example.sherly.medigent.model.agent.AgentModel;
import com.example.sherly.medigent.model.agent.DataAgentModel;
import com.example.sherly.medigent.model.alamat.AddressModel;
import com.example.sherly.medigent.model.alamat.PostAddressModel;
import com.example.sherly.medigent.model.histori.DetailHistoryModel;
import com.example.sherly.medigent.service.ApiService;
import com.example.sherly.medigent.service.PlaceAutocompleteAdapter;
import com.example.sherly.medigent.service.PlaceInfo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultMapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener {

    private static final String TAG = "ResultsMapsActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOC_PERMISSION_CODE = 1234;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));

    private Boolean mLocPermissionGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    //private AutoCompleteTextView etSearch;
    private ImageView ivGps;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    private PlaceInfo mPlace;
    private Marker mMarker;
    private String judul, alamat, id_user, token, alamat_lengkap;
    private Double lat, lng;

    LinearLayout layoutBottomSheet;
    BottomSheetBehavior sheetBehavior;
    TextView tvBottomSheet;
    ImageView ivPanah1, ivPanah2;
    DaftarAgenAdapter agenAdapter;
    RecyclerView rvDaftarAgen;
    ArrayList<DataAgentModel> agentModels;
    String id_order;
    Double agentLat, agentLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_result);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Hasil Pencarian");

        //etSearch = (AutoCompleteTextView) findViewById(R.id.etSearch);
        ivGps = (ImageView) findViewById(R.id.ivGps);

        final SharedPreferences pref = getSharedPreferences("medigent", MODE_PRIVATE);
        id_user = pref.getString("id_user", "null");
        token = pref.getString("token", "null");

        Intent intent = getIntent();
        id_order = intent.getStringExtra("id_order");

        layoutBottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        tvBottomSheet = (TextView) findViewById(R.id.tvBottomSheet);
        ivPanah1 = (ImageView) findViewById(R.id.ivPanah1);
        ivPanah2 = (ImageView) findViewById(R.id.ivPanah2);
        rvDaftarAgen = (RecyclerView) findViewById(R.id.rvDaftarAgen);
        ivPanah1.setBackgroundResource(R.drawable.ic_up);
        ivPanah2.setBackgroundResource(R.drawable.ic_up);

        //Toast.makeText(ResultMapsActivity.this, ""+id_order+", "+token, Toast.LENGTH_LONG).show();
        ApiService.service_get.getHistoryByOO2("Bearer "+token, id_order).enqueue(new Callback<DetailHistoryModel>() {
            @Override
            public void onResponse(Call<DetailHistoryModel> call, Response<DetailHistoryModel> response) {
                if(response.isSuccessful()){
                    agentLat = response.body().getLat();
                    agentLng = response.body().getLng();
                    alamat_lengkap = response.body().getAlamat_lengkap();
                    //Toast.makeText(ResultMapsActivity.this, ""+agentLat+", "+agentLng, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(ResultMapsActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DetailHistoryModel> call, Throwable t) {
                Toast.makeText(ResultMapsActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        ApiService.service_get.getAgents("Bearer "+token).enqueue(new Callback<AgentModel>() {
            @Override
            public void onResponse(Call<AgentModel> call, Response<AgentModel> response) {
                if(response.isSuccessful()) {

                    agenAdapter = new DaftarAgenAdapter(ResultMapsActivity.this, response.body().getAgents());
                    rvDaftarAgen.setLayoutManager(new LinearLayoutManager(ResultMapsActivity.this));
                    //rvDaftarHistori.setFocusable(false);
                    //rvDaftarHistori.setNestedScrollingEnabled(false);
                    rvDaftarAgen.setAdapter(agenAdapter);
                    agenAdapter.notifyDataSetChanged();

                    agentModels = response.body().getAgents();
                    createMarker(agentModels, response.body().getCount());
                }
                else {
                    Toast.makeText(ResultMapsActivity.this, "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AgentModel> call, Throwable t) {
                Toast.makeText(ResultMapsActivity.this, "Error : "+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        tvBottomSheet.setText("Tutup Daftar");
                        ivPanah1.setBackgroundResource(R.drawable.ic_down);
                        ivPanah2.setBackgroundResource(R.drawable.ic_down);
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        tvBottomSheet.setText("Lihat Daftar");
                        ivPanah1.setBackgroundResource(R.drawable.ic_up);
                        ivPanah2.setBackgroundResource(R.drawable.ic_up);
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        getLocationPermission();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void createMarker(ArrayList<DataAgentModel> dataAgent, int count) {
        mMap.clear();

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(agentLat, agentLng))
                .title("Lokasi saya")
                .snippet(alamat_lengkap)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        ArrayList<LatLng> latLngs = new ArrayList<>();
        Double jarak;

        Double dekat[] = new Double[count];

        for(int i=0; i<count; i++) {

            jarak = distance(agentLat, agentLng, dataAgent.get(i).getLat(), dataAgent.get(i).getLng());
            dekat[i] = jarak;

            mMap.addMarker(new MarkerOptions().position(new LatLng(dataAgent.get(i).getLat(), dataAgent.get(i).getLng())).title(dataAgent.get(i).getNama_lengkap()).snippet(dataAgent.get(i).getAlamat_lengkap()));
        }

        for(int i=0; i<dekat.length; i++) {
            for(int j=0; j<dekat.length; j++) {
                if(dekat[i] < dekat[j]) {
                    Double tampung = dekat[i];
                    dekat[i] = dekat[j];
                    dekat[j] = tampung;

                }
            }
        }

        for(int i=0; i<dekat.length; i++) {
            Toast.makeText(ResultMapsActivity.this, ""+dekat[i], Toast.LENGTH_SHORT).show();
        }
    }

    public static double distance(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371; //meters
        //double earthRadius = 3958.75;
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        //double dist = earthRadius * c *1000;
        //dist = Math.pow(dist,2);
        double dist = earthRadius * c;
        //double meterConversion = 1609;
        return dist ;
    }

    private void init(){

//        mGoogleApiClient = new GoogleApiClient
//                .Builder(this)
//                .addApi(Places.GEO_DATA_API)
//                .addApi(Places.PLACE_DETECTION_API)
//                .enableAutoManage(this, this)
//                .build();
//
//        etSearch.setOnItemClickListener(mAutocompleteClickListener);
//
//        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient ,LAT_LNG_BOUNDS, null);
//
//        etSearch.setAdapter(mPlaceAutocompleteAdapter);
//
//        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
//                if(actionId == EditorInfo.IME_ACTION_SEARCH
//                        || actionId == EditorInfo.IME_ACTION_DONE
//                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
//                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){
//
//                    //execute our method for searching
//                    geoLocate();
//                }
//                return false;
//            }
//        });

        ivGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDeviceLocation();
            }
        });
        hideSoftKeyboard();
    }

//    private void geoLocate(){
//        String searchString = etSearch.getText().toString();
//
//        Geocoder geocoder = new Geocoder(ResultMapsActivity.this);
//        List<Address> list = new ArrayList<>();
//        try{
//            list = geocoder.getFromLocationName(searchString, 1);
//        }catch (IOException e){
//           // Log.e(TAG, "geoLocate: IOException: " + e.getMessage() );
//            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//
//        if(list.size() > 0){
//            Address address = list.get(0);
//
//            //Log.d(TAG, "geoLocate: found a location: " + address.toString());
//            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();
//            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), 15, address.getAddressLine(0));
//        }
//    }

    private void initMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);
    }

    private void getDeviceLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocPermissionGranted) {
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            Location currentLocation = (Location) task.getResult();
                            //move camera here
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 15, "My Location");
                        } else {
                            Toast.makeText(ResultMapsActivity.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Toast.makeText(ResultMapsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void moveCamera(LatLng latLng, float zoom, PlaceInfo placeInfo){
        //Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        mMap.clear();

        mMap.setOnMarkerClickListener(this);

        if(placeInfo != null){
            try{
                judul = placeInfo.getName();
                alamat = placeInfo.getAddress();
                lat = placeInfo.getLatlng().latitude;
                lng = placeInfo.getLatlng().longitude;

                String snippet = "Alamat: " + placeInfo.getAddress() + "\n";

                MarkerOptions options = new MarkerOptions()
                        .position(latLng)
                        .title(placeInfo.getName())
                        .snippet(snippet);
                mMarker = mMap.addMarker(options);

            }catch (NullPointerException e){
                Log.e(TAG, "moveCamera: NullPointerException: " + e.getMessage() );
            }
        }else{
            mMap.addMarker(new MarkerOptions().position(latLng));
        }

        hideSoftKeyboard();
    }

    private void moveCamera(LatLng latLng, float zoom, String title) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if(!title.equals("My Location")) {
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);

            mMap.addMarker(options);
        }

        hideSoftKeyboard();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(ResultMapsActivity.this, "Maps is ready", Toast.LENGTH_SHORT).show();
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        if (mLocPermissionGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

            init();
        }
    }

    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocPermissionGranted = true;
                initMap();
            }
            else {
                ActivityCompat.requestPermissions(this, permissions, LOC_PERMISSION_CODE);
            }
        }
        else {
            ActivityCompat.requestPermissions(this, permissions, LOC_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocPermissionGranted = false;
        switch (requestCode) {
            case LOC_PERMISSION_CODE:{
                if(grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocPermissionGranted = false;
                            return;
                        }
                    }
                    mLocPermissionGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /*
        --------------------------- google places API autocomplete suggestions -----------------
     */

//    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//            hideSoftKeyboard();
//
//            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(i);
//            final String placeId = item.getPlaceId();
//
//            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
//                    .getPlaceById(mGoogleApiClient, placeId);
//            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
//        }
//    };
//
//    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
//        @Override
//        public void onResult(@NonNull PlaceBuffer places) {
//            if(!places.getStatus().isSuccess()){
//                Log.d(TAG, "onResult: Place query did not complete successfully: " + places.getStatus().toString());
//                places.release();
//                return;
//            }
//            final Place place = places.get(0);
//
//            try{
//                //Address loc = new Address(place.getLocale());
//
//                //Toast.makeText(MapsActivity.this, ""+loc.getAddressLine(0)+", "+loc.getSubAdminArea()+", "+loc.getAdminArea()+", "+loc.getCountryName()+", "+loc.getPostalCode(), Toast.LENGTH_LONG).show();
//                mPlace = new PlaceInfo();
//                mPlace.setName(place.getName().toString());
//                Log.d(TAG, "onResult: name: " + place.getName());
//                mPlace.setAddress(place.getAddress().toString());
//                Log.d(TAG, "onResult: address: " + place.getAddress());
//                Toast.makeText(ResultMapsActivity.this, ""+place.getAddress(), Toast.LENGTH_SHORT).show();
////                mPlace.setAttributions(place.getAttributions().toString());
////                Log.d(TAG, "onResult: attributions: " + place.getAttributions());
//                mPlace.setId(place.getId());
//                Log.d(TAG, "onResult: id:" + place.getId());
//                mPlace.setLatlng(place.getLatLng());
//                Log.d(TAG, "onResult: latlng: " + place.getLatLng());
//                mPlace.setRating(place.getRating());
//                Log.d(TAG, "onResult: rating: " + place.getRating());
//                mPlace.setPhoneNumber(place.getPhoneNumber().toString());
//                Log.d(TAG, "onResult: phone number: " + place.getPhoneNumber());
//                mPlace.setWebsiteUri(place.getWebsiteUri());
//                Log.d(TAG, "onResult: website uri: " + place.getWebsiteUri());
//
//                Log.d(TAG, "onResult: place: " + mPlace.toString());
//            }catch (NullPointerException e){
//                Log.e(TAG, "onResult: NullPointerException: " + e.getMessage() );
//            }
//
//            moveCamera(new LatLng(place.getViewport().getCenter().latitude,
//                    place.getViewport().getCenter().longitude), 15, mPlace);
//
//            places.release();
//        }
//    };
//
    @Override
    public boolean onMarkerClick(Marker marker) {
//        Toast.makeText(this, "Bisaaa", Toast.LENGTH_SHORT).show();
        final AlertDialog.Builder builder = new AlertDialog.Builder(ResultMapsActivity.this);

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alertdialog_maps, null);
        final EditText etTambahan = (EditText) dialogView.findViewById(R.id.etEdit);
        final TextView tvAlamat = (TextView) dialogView.findViewById(R.id.tvAlamat);

        tvAlamat.setText(alamat);

        builder.setCancelable(false);
        builder.setView(dialogView);
        builder.setMessage("Apakah Anda ingin menambahkan alamat ini sebagai alamat Anda ?");

        builder.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, int i) {
                final String tambahan = etTambahan.getText().toString();
                //Toast.makeText(MapsActivity.this, ""+tambahan, Toast.LENGTH_SHORT).show();

                AddressModel address = new AddressModel(judul, alamat, tambahan, lat, lng, id_user );

                ApiService.service_post.postAddress("Bearer "+token, address).enqueue(new Callback<PostAddressModel>() {
                    @Override
                    public void onResponse(Call<PostAddressModel> call, Response<PostAddressModel> response) {
                        //Toast.makeText(MapsActivity.this, "Alamat berhasil ditambahkan", Toast.LENGTH_LONG).show();
                        if(response.isSuccessful()) {

                            Toast.makeText(ResultMapsActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else {
                            Toast.makeText(ResultMapsActivity.this, "Error 1", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PostAddressModel> call, Throwable t) {
                        Toast.makeText(ResultMapsActivity.this, "Error 2: "+t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return false;
    }
}
