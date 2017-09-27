package com.elluminati.charge;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.elluminati.charge.component.DrawArcWithAngel;
import com.elluminati.charge.component.ResizeAnimation;
import com.elluminati.charge.models.datamodels.ChargersItem;
import com.elluminati.charge.models.datamodels.StationsItem;
import com.elluminati.charge.models.responsemodels.ParticularStationsResponse;
import com.elluminati.charge.models.responsemodels.StationsResponse;
import com.elluminati.charge.parser.ApiClient;
import com.elluminati.charge.parser.ApiInterface;
import com.elluminati.charge.utils.Const;
import com.elluminati.charge.utils.LocationHelper;
import com.elluminati.charge.utils.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements
        LocationHelper.OnLocationReceived, OnMapReadyCallback, View.OnClickListener {

    public static final String TAG = "MainActivity";
    private static boolean isMapTouched = false;
    private GoogleMap googleMap;
    private LocationHelper locationHelper;
    private MapView mapView;
    private CardView cvDetail, cvNext, cvOpenToolBox, cvCloseToolBox;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private RecyclerView recycleView;
    private TextView tvStationName, tvRate, tvLaval1, tvLaval2, tvLaval3, tvLavalDCFast,
            tvLavalSupper, tvActive, tvInactive, tvStopped, tvTime;
    private ImageView ivHevo, ivPlug, ivOpenToolBox, ivRefresh, ivLocation,
            ivFilter, ivFilterList, ivNavigated;
    private Bitmap selectedMakerBitmap;
    private Marker selectedMarker;
    private StationsItem stationsItem;
    private FrameLayout mapFrameLayout;

    private static void setMapTouched(boolean isTouched) {
        isMapTouched = isTouched;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolBar();
        initDrawer();
        findVewById();
        setViewLister();
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.getMapAsync(this);
        mapView.onCreate(savedInstanceState);
        locationHelper = new LocationHelper(this);
        locationHelper.setLocationReceivedLister(this);
        locationHelper.onStart();

    }

    private void findVewById() {
        cvDetail = (CardView) findViewById(R.id.llDetail);
        cvNext = (CardView) findViewById(R.id.llNext);
        tvStationName = (TextView) findViewById(R.id.tvStationName);
        tvRate = (TextView) findViewById(R.id.tvRate);
        tvLaval1 = (TextView) findViewById(R.id.tvLaval1);
        tvLaval2 = (TextView) findViewById(R.id.tvLaval2);
        tvLaval3 = (TextView) findViewById(R.id.tvLaval3);
        tvLavalDCFast = (TextView) findViewById(R.id.tvLavalDCFast);
        tvLavalSupper = (TextView) findViewById(R.id.tvLavalSupper);
        ivHevo = (ImageView) findViewById(R.id.ivHevo);
        ivPlug = (ImageView) findViewById(R.id.ivPlug);
        tvActive = (TextView) findViewById(R.id.tvActive);
        tvInactive = (TextView) findViewById(R.id.tvInactive);
        tvStopped = (TextView) findViewById(R.id.tvStopped);
        ivOpenToolBox = (ImageView) findViewById(R.id.ivOpenToolBox);
        ivRefresh = (ImageView) findViewById(R.id.ivRefresh);
        ivLocation = (ImageView) findViewById(R.id.ivLocation);
        ivFilter = (ImageView) findViewById(R.id.ivFilter);
        ivFilterList = (ImageView) findViewById(R.id.ivFilterList);
        cvOpenToolBox = (CardView) findViewById(R.id.cvOpenToolBox);
        cvCloseToolBox = (CardView) findViewById(R.id.cvCloseToolBox);
        ivNavigated = (ImageView) findViewById(R.id.ivNavigated);
        tvTime = (TextView) findViewById(R.id.tvTime);
        mapFrameLayout = (FrameLayout) findViewById(R.id.mapFrameLayout);
    }

    private void setViewLister() {
        cvNext.setOnClickListener(this);
        ivLocation.setOnClickListener(this);
        ivFilter.setOnClickListener(this);
        ivRefresh.setOnClickListener(this);
        ivFilterList.setOnClickListener(this);
        cvCloseToolBox.setOnClickListener(this);
        ivOpenToolBox.setOnClickListener(this);
        ivNavigated.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        locationHelper.onStop();
        super.onDestroy();
    }

    @Override
    public void onConnected(Bundle bundle) {
        checkPermissionLocation();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        setUpMap();


    }

    /**
     * This method is used to setUpMap option which help to load map as per option
     */
    private void setUpMap() {

        this.googleMap.getUiSettings().setMapToolbarEnabled(false);
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        this.googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            boolean doNotMoveCameraToCenterMarker = true;

            public boolean onMarkerClick(Marker marker) {
                return doNotMoveCameraToCenterMarker;
            }
        });
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                setSelectedMarker(null, null);
                stationsItem = (StationsItem) marker.getTag();
                selectedMakerBitmap = stationsItem.getBitmap();
                setSelectedMarker(stationsItem, marker);
                getDistanceMatrix();


                return true;
            }
        });

        this.googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                setSelectedMarker(null, null);
                slideUpDown(cvNext, cvDetail);

            }

        });

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission
                .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);


    }

    private void checkPermissionLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission
                .ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission
                            .ACCESS_FINE_LOCATION, android.Manifest.permission
                            .ACCESS_COARSE_LOCATION},
                    Const.PERMISSION_FOR_LOCATION);
        } else {
            //Do the stuff that requires permission...
            getStation(true, 10.0, 0.0, 20.0, locationHelper.getLastLocation().getLatitude(),
                    locationHelper.getLastLocation().getLongitude(), "",
                    "");


        }
    }

    private void setMarkerOnMap(List<StationsItem> stations) {
        if (googleMap != null && !stations.isEmpty()) {
            googleMap.clear();


            LatLngBounds.Builder bounds = new LatLngBounds.Builder();
            for (StationsItem station : stations) {
                DrawArcWithAngel drawArcWithAngel = new DrawArcWithAngel(AppCompatResources
                        .getDrawable(this, R
                                .drawable.pin), this);
                int active = 0, inactive = 0, stopped = 0, red = 0;
                for (ChargersItem chargersItem : station.getChargers()) {

                    switch (chargersItem.getStatus()) {
                        case Const.ACTIVE:
                            active++;
                            break;
                        case Const.INACTIVE:
                            inactive++;
                            break;
                        case Const.STOPPED:
                            stopped++;
                            break;
                        default:
                            // do with default
                            red++;
                            break;
                    }
                }
                int chargers = station.getChargers().size();
                if (chargers > 0) {
                    ArrayList<Integer> integers = new ArrayList<>();
                    ArrayList<Integer> integersColors = new ArrayList<>();
                    int activeParentage = 0, inactiveParentage = 0, stoppedParentage = 0,
                            redParentage = 0;
                    activeParentage = active * 100 / chargers;
                    inactiveParentage = inactive * 100 / chargers;
                    stoppedParentage = stopped * 100 / chargers;
                    redParentage = (red / chargers) * 100;

                    Log.i("activeParentage", activeParentage + "");
                    Log.i("inactiveParentage", inactiveParentage + "");
                    Log.i("stoppedParentage", stoppedParentage + "");
                    Log.i("redParentage", redParentage + "");
                    if (activeParentage > 0) {
                        integers.add(activeParentage);
                        integersColors.add(R.color.colorAccent);
                    }
                    if (inactiveParentage > 0) {
                        integers.add(inactiveParentage);
                        integersColors.add(R.color.colorPrimary);
                    }
                    if (stoppedParentage > 0) {
                        integers.add(stoppedParentage);
                        integersColors.add(R.color.colorGray);
                    }
                    if (redParentage > 0) {
                        integers.add(redParentage);
                        integersColors.add(R.color.colorRed);
                    }
                    int cap = 4;
                    int gap = 10;
                    int startAngle = 130;
                    int totalArc = 280 - gap * (integers.size() - 1);
                    for (int i = 0; i < integers.size(); i++) {
                        int endAngle = integers.get(i) * totalArc / 100;
                        Log.i("endAngle", endAngle + "");
                        Log.i("startAngle", startAngle + "");

                        drawArcWithAngel.addBitmap(startAngle, endAngle, integersColors.get(i),
                                false);
                        if (i == 0) {
                            drawArcWithAngel.addBitmap(startAngle - cap, cap, integersColors.get
                                    (i), true);
                        }
                        if (i == integers.size() - 1) {
                            drawArcWithAngel.addBitmap(startAngle + endAngle - cap, cap,
                                    integersColors.get
                                            (i), true);
                        }
                        startAngle = startAngle + endAngle + gap;

                    }

                    drawArcWithAngel.drawText(station.getChargers().size() + "", R.color
                            .colorBlack);
                }
                LatLng latLng = new LatLng(station.getCoordinates().getLatitude(), station
                        .getCoordinates().getLongitude());
                station.setBitmap(drawArcWithAngel.getBitmapOverly());
                googleMap.addMarker(new MarkerOptions().position(latLng)
                        .icon(BitmapDescriptorFactory
                                .fromBitmap(drawArcWithAngel.getBitmapOverly())))
                        .setTag(station);
                bounds.include(latLng);
            }

            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 0));

        }


    }

    public void slideUpDown(final View viewUp, final View viewDown) {
        if (viewUp.getVisibility() == View.GONE) {
            // Show the panel
            Animation bottomUp = AnimationUtils.loadAnimation(this,
                    R.anim.bottom_up);

            viewUp.startAnimation(bottomUp);
            viewUp.setVisibility(View.VISIBLE);
        }
        if (viewDown.getVisibility() == View.VISIBLE) {
            Animation bottomDown = AnimationUtils.loadAnimation(this,
                    R.anim.bottom_down);

            viewDown.startAnimation(bottomDown);
            viewDown.setVisibility(View.GONE);
        }

    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.appToolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initDrawer() {
        recycleView = (RecyclerView) findViewById(R.id.listViewDrawer);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        drawerLayout = (DrawerLayout) findViewById(R.id.DrawerLayout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                0, 0) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }
        };

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            switch (requestCode) {
                case Const.PERMISSION_FOR_LOCATION:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        //Do the stuff that requires permission...
                        getStation(true, 10.0, 0.0, 20.0, locationHelper.getLastLocation()
                                        .getLatitude(),
                                locationHelper.getLastLocation().getLongitude(), "",
                                "");
                    }
                    break;
                default:
                    //do with default
                    break;
            }
        }

    }

    public void moveCameraFirstMyLocation(boolean isAnimate) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission
                .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LatLng latLngOfMyLocation = null;
        if (locationHelper.getLastLocation() != null) {
            latLngOfMyLocation = new LatLng(locationHelper.getLastLocation().getLatitude(),
                    locationHelper.getLastLocation().getLongitude());
        }
        if (latLngOfMyLocation != null) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLngOfMyLocation).zoom(17).build();

            if (isAnimate) {
                googleMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));
            } else {
                googleMap.moveCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));
            }
            locationHelper.setOpenGpsDialog(false);
        }
    }

    private void getParticularStation(int id) {
        Log.i("STATION_ID", id + "");

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ParticularStationsResponse> stationResponseCall = apiInterface.getParticularStation
                (String.valueOf(id));
        stationResponseCall.enqueue(new Callback<ParticularStationsResponse>() {
            @Override
            public void onResponse(Call<ParticularStationsResponse> call,
                                   Response<ParticularStationsResponse> response) {

                if (response.isSuccessful()) {
                    Log.i("SIGNAL_STATION_RESPONSE", ApiClient.JSONResponse(response));
                    setMakerData(response.body().getStation());
                    Utils.hideProgressbar();

                } else {
                    Toast.makeText(MainActivity.this, "Station Response Failed", Toast
                            .LENGTH_SHORT).show();
                    Utils.hideProgressbar();
                }

            }

            @Override
            public void onFailure(Call<ParticularStationsResponse> call, Throwable t) {
                Log.d(TAG, t.getMessage());
                Utils.hideProgressbar();
            }
        });
    }

    private void getStation(boolean publish, Double limit, Double offset, Double distance, Double
            latitude, Double longitude, String id, String name) {
        Utils.showProgressDialog(this);
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put(Const.Params.PUBLISH, String.valueOf(publish));
        if (latitude != null && longitude != null) {
            stringMap.put(Const.Params.LATITUDE, String.valueOf(latitude));
            stringMap.put(Const.Params.LONGITUDE, String.valueOf(longitude));
        }
        if (limit != null) {
            stringMap.put(Const.Params.LIMIT, String.valueOf(limit));
        }
        if (offset != null) {
            stringMap.put(Const.Params.OFFSET, String.valueOf(offset));
        }
        if (distance != null) {
            stringMap.put(Const.Params.DISTANCE, String.valueOf(distance));
        }
        if (!TextUtils.isEmpty(id)) {
            stringMap.put(Const.Params.ID, id);
        }
        if (!TextUtils.isEmpty(name)) {
            stringMap.put(Const.Params.NAME, name);
        }
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<StationsResponse> stationResponseCall = apiInterface.getStations(stringMap);
        stationResponseCall.enqueue(new Callback<StationsResponse>() {
            @Override
            public void onResponse(Call<StationsResponse> call, Response<StationsResponse>
                    response) {
                Utils.hideProgressbar();
                if (response.isSuccessful()) {
                    setMarkerOnMap(response.body().getStations());
                } else {
                    Toast.makeText(MainActivity.this, "Station Response Failed", Toast
                            .LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<StationsResponse> call, Throwable t) {
                Log.d(TAG, t.getMessage());
                Utils.hideProgressbar();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llNext:

                break;
            case R.id.cvCloseToolBox:
                openToolBox();
                break;
            case R.id.ivOpenToolBox:
                closeToolBox();
                break;
            case R.id.ivLocation:
                moveCameraFirstMyLocation(false);
                break;
            case R.id.ivRefresh:
                getStation(true, 10.0, 0.0, 20.0, locationHelper.getLastLocation().getLatitude(),
                        locationHelper.getLastLocation().getLongitude(), "",
                        "");
                break;
            case R.id.ivFilter:

                break;
            case R.id.ivFilterList:

                break;
            case R.id.ivNavigated:
                goToGoogleMapApp();
                break;
            default:
                // do with default
                break;
        }
    }

    private void setMakerData(StationsItem stationsItem) {
        if (stationsItem != null) {

            int active = 0, inactive = 0, stopped = 0;
            for (ChargersItem chargersItem : stationsItem.getChargers()) {
                switch (chargersItem.getStatus()) {
                    case Const.ACTIVE:
                        active++;
                        break;
                    case Const.INACTIVE:
                        inactive++;
                        break;
                    case Const.STOPPED:
                        stopped++;
                        break;
                    default:
                        // do with default
                        break;
                }
                switch (chargersItem.getLevel()) {
                    case Const.LEVEL_1:
                        tvLaval1.setTextColor(ResourcesCompat.getColor(getResources(), R.color
                                .colorWhite, null));
                        tvLaval1.setBackground(ResourcesCompat.getDrawable(getResources(), R
                                .drawable.selector_round_shape_blue, null));
                        break;
                    case Const.LEVEL_2:
                        tvLaval2.setTextColor(ResourcesCompat.getColor(getResources(), R.color
                                .colorWhite, null));
                        tvLaval2.setBackground(ResourcesCompat.getDrawable(getResources(), R
                                .drawable.selector_round_shape_blue, null));
                        break;
                    case Const.LEVEL_3:
                        tvLaval3.setTextColor(ResourcesCompat.getColor(getResources(), R.color
                                .colorWhite, null));
                        tvLaval3.setBackground(ResourcesCompat.getDrawable(getResources(), R
                                .drawable.selector_round_shape_blue, null));
                        break;
                    case Const.LEVEL_SUPER:
                        tvLavalSupper.setTextColor(ResourcesCompat.getColor(getResources(), R.color
                                .colorWhite, null));
                        tvLavalSupper.setBackground(ResourcesCompat.getDrawable(getResources(), R
                                .drawable.selector_round_shape_blue, null));
                        break;
                    case Const.LEVEL_DC_FAST:
                        tvLavalDCFast.setTextColor(ResourcesCompat.getColor(getResources(), R.color
                                .colorWhite, null));
                        tvLavalDCFast.setBackground(ResourcesCompat.getDrawable(getResources(), R
                                .drawable.selector_round_shape_blue, null));
                        break;
                    default:
                        // do with default
                        break;
                }

                if (TextUtils.equals(Const.TYPE_HEVO, chargersItem.getType())) {
                    ivHevo.setImageResource(R.drawable.hevo_dark);
                } else {
                    ivPlug.setImageResource(R.drawable.plug_dark);
                }

            }
            tvInactive.setText(String.valueOf(inactive));
            tvActive.setText(String.valueOf(active));
            tvStopped.setText(String.valueOf(stopped));
            tvRate.setText(String.valueOf(stationsItem.getRating()));
            tvStationName.setText(stationsItem.getName());

            slideUpDown(cvDetail, cvNext);
        }

    }

    private void openToolBox() {
        ResizeAnimation resizeAnimation = new ResizeAnimation(cvOpenToolBox, getResources()
                .getDimensionPixelSize(R.dimen.size_box));
        resizeAnimation.setDuration(500);
        cvOpenToolBox.startAnimation(resizeAnimation);
        cvCloseToolBox.setVisibility(View.GONE);
    }

    private void closeToolBox() {
        ResizeAnimation resizeAnimation = new ResizeAnimation(cvOpenToolBox, 1);
        resizeAnimation.setDuration(500);
        cvOpenToolBox.startAnimation(resizeAnimation);
        cvCloseToolBox.setVisibility(View.VISIBLE);
    }

    private void setSelectedMarker(StationsItem stationsItem, Marker marker) {
        if (selectedMarker == null && stationsItem != null) {
            mapView.setForeground(ResourcesCompat.getDrawable(getResources(), R.drawable
                    .selector_gray, null));
            selectedMarker = marker;
            DrawArcWithAngel drawArcWithAngel = new DrawArcWithAngel(AppCompatResources
                    .getDrawable(this, R
                            .drawable.green_pin), this);
            drawArcWithAngel.drawText(String.valueOf(stationsItem.getChargers().size()), R.color
                    .colorWhite);
            selectedMarker.setIcon(BitmapDescriptorFactory.fromBitmap(drawArcWithAngel
                    .getBitmapOverly()));
        } else {
            mapView.setForeground(null);
            if (selectedMakerBitmap != null) {

                selectedMarker.setIcon(BitmapDescriptorFactory.fromBitmap(selectedMakerBitmap));
                selectedMarker = null;
                selectedMakerBitmap = null;
            }
        }

    }

    /**
     * this method is used to open Google Map app whit given LatLng
     */
    private void goToGoogleMapApp() {

        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + stationsItem.getCoordinates()
                .getLatitude()
                + "," + stationsItem.getCoordinates().getLongitude());
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            Toast.makeText(this, "googgle map not installed", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * this method called a webservice for get distance and time witch is provided by Google
     */
    private void getDistanceMatrix() {
        Utils.showProgressDialog(this);
        HashMap<String, String> hashMap = new HashMap<>();
        String origins = String.valueOf(locationHelper.getLastLocation().getLatitude()) +
                "," +
                locationHelper.getLastLocation().getLongitude();
        hashMap.put(Const.google.ORIGINS, origins);
        String destination = String.valueOf(stationsItem.getCoordinates().getLatitude()) +
                "," +
                stationsItem.getCoordinates().getLongitude();
        hashMap.put(Const.google.DESTINATIONS, destination);
        hashMap.put(Const.google.KEY, "AIzaSyCBoYCXuJtZw9tGMnQcpgDrQhbYdQOUe5A");


        ApiInterface apiInterface = new ApiClient().changeApiBaseUrl(Const.GOOGLE_API_URL)
                .create

                        (ApiInterface
                                .class);
        Call<ResponseBody> call = apiInterface.getGoogleDistanceMatrix(hashMap);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String distanceMatrix = response.body().string();
                    JSONObject jsonObject = new JSONObject(distanceMatrix);
                    if (jsonObject.getString(Const.google.STATUS).equals(Const.google.OK)) {
                        JSONObject rowsJson = jsonObject.getJSONArray(Const.google.ROWS)
                                .getJSONObject(0);
                        JSONObject elementsJson = rowsJson.getJSONArray(Const.google.ELEMENTS)
                                .getJSONObject(0);
                        if (elementsJson.getString(Const.google.STATUS).equals(Const.google.OK)) {

                            tvTime.setText(elementsJson.getJSONObject(Const.google.DURATION)
                                    .getString(Const.google.TEXT));
                        }
                    }
                } catch (JSONException | IOException e) {
                }
                getParticularStation(stationsItem.getId());

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }
}



