package be.ap.edu.mobiledevelopmentexamen;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity {

    private TextView searchField;
    private Button searchButton;
    private MapView mapView;
    private String urlSearch = "http://nominatim.openstreetmap.org/search?q=";
    private LocationManager locationManager;
    private LocationListener locationListener;
    private RequestQueue mRequestQueue;
    private MapSQLiteHelper helper;

    final ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mapView = (MapView)findViewById(R.id.mapview);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(18);

        mapView.getController().setCenter(new GeoPoint(51.1608194, 4.5103875));
        mRequestQueue = Volley.newRequestQueue(this);
        searchField = (TextView)findViewById(R.id.search_txtview);
        searchButton = (Button)findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchString = "";
                try {
                    searchString = URLEncoder.encode(searchField.getText().toString(), "UTF-8");
                }
                catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                JsonArrayRequest jr = new JsonArrayRequest(urlSearch + searchString + "&format=json", new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            //Log.d("edu.ap.maps", response.toString());
                            //hideSoftKeyBoard();
                            JSONObject obj = response.getJSONObject(0);
                            GeoPoint g = new GeoPoint(obj.getDouble("lat"), obj.getDouble("lon"));
                            mapView.getController().setCenter(g);
                        }
                        catch(JSONException ex) {
                            Log.e("be.ap.edu.mapsaver", ex.getMessage());
                        }
                    }
                },new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("be.ap.edu.mapsaver", error.getMessage());
                        //hideSoftKeyBoard();
                    }
                });

                mRequestQueue.add(jr);
            }
        });


        if(helper.getlat(1) != null) {
            GeoPoint g = new GeoPoint(Integer.parseInt(helper.getlat(1).toString()), Integer.parseInt(helper.getLon(1).toString()));
            addMarker(g);
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        int actionType = ev.getAction();
        switch(actionType){
            case MotionEvent.ACTION_UP:

                Projection proj = this.mapView.getProjection();
                GeoPoint loc = (GeoPoint) proj.fromPixels((int)ev.getX(), (int)ev.getY() - (searchField.getHeight() * 2));
                double lat = loc.getLatitude();
                double lon = loc.getLongitude();
                Intent i = new Intent(this, DetailActivity.class);
                i.putExtra("latitude", (Serializable) lat);
                i.putExtra("longitude", (Serializable) lon);
                startActivity(i);
        }
        return super.dispatchTouchEvent(ev);
    }

    private void addMarker(GeoPoint g) {
        OverlayItem myLocationOverlayItem = new OverlayItem("Here", "Current Position", g);
        Drawable myCurrentLocationMarker = ResourcesCompat.getDrawable(getResources(), R.drawable.marker, null);
        myLocationOverlayItem.setMarker(myCurrentLocationMarker);

        items.add(myLocationOverlayItem);
        DefaultResourceProxyImpl resourceProxy = new DefaultResourceProxyImpl(getApplicationContext());

        ItemizedIconOverlay<OverlayItem> currentLocationOverlay = new ItemizedIconOverlay<OverlayItem>(items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        return true;
                    }
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        return true;
                    }
                }, resourceProxy);
        this.mapView.getOverlays().add(currentLocationOverlay);
        this.mapView.invalidate();
    }
}
