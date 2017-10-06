package be.ap.edu.mobiledevelopmentexamen;

import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
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
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MapActivity extends AppCompatActivity {

    private TextView searchField;
    private Button searchButton;
    private MapView mapView;
    private String urlSearch = "http://nominatim.openstreetmap.org/search?q=";
    private LocationManager locationManager;
    private LocationListener locationListener;
    private RequestQueue mRequestQueue;
    private MapSQLiteHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mapView = (MapView)findViewById(R.id.mapview);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(18);

        mapView.getController().setCenter(new GeoPoint(51.215559, 4.410595));
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
                //Intent i = new Intent(this, DetailActivity.class);
                //i.putExtra("latitude", (Serializable) lat);
                //i.putExtra("longitude", (Serializable) lon);
                //startActivity(i);
        }
        return super.dispatchTouchEvent(ev);
    }
}
