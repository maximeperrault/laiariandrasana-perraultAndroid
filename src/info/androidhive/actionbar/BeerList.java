package info.androidhive.actionbar;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import org.json.JSONArray;
import info.androidhive.actionbar.model.SpinnerNavItem;
import info.androidhive.info.actionbar.adapter.TitleNavigationAdapter;

import java.util.ArrayList;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
public class BeerList  extends Activity {

    private Context context;
    private static String url1 = "http://binouze.fabrigli.fr/bieres.json";


    ListView lv;

    JSONArray JArray = null;
    ArrayList<HashMap<String, String>> jlist = new ArrayList<HashMap<String, String>>();
    JSONParser Json = new JSONParser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_found);

        lv = (ListView) findViewById(R.id.listView);
        HashMap<String, String> element;

        ListAdapter adapter = new SimpleAdapter(this,jlist,android.R.layout.simple_list_item_2,new String[] {"text1", "text2"},new int[] {android.R.id.text1, android.R.id.text2 });

        lv.setAdapter(adapter);

        new DLTask(BeerList.this).execute(url1);

            }


    private class DLTask extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog;
        private Context context;
        private Activity activity;

        public DLTask(Activity activity) {
            this.activity = activity;
            context = activity;
            dialog = new ProgressDialog(context);
        }

        @Override
        protected String doInBackground(String...urls){
            try{
                getJSON(urls[0],2);
                return downloadUrl(urls[0]);
            } catch (IOException e){
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        protected void onPreExecute() {
            this.dialog.setMessage("Loading Database");
            this.dialog.show();
        }

        @Override
        protected void onPostExecute(String result){
            if (dialog.isShowing()) {
                dialog.dismiss();
            }


            Json.Input(result);

            JSONArray jsonArray = Json.GetBeerArray();


            for(int i = 0; i<jsonArray.length(); i++)
                try {
                    JSONObject c = jsonArray.getJSONObject(i);
                    HashMap<String, String> element = new HashMap<String, String>();
                    element.put("text1", c.getString("name"));
                    element.put("text2", c.getString("category"));
                    jlist.add(element);
                }
                catch (JSONException e) {
                    Toast.makeText(getApplication().getApplicationContext(),"Bug",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            ListAdapter adapter = new SimpleAdapter(context,jlist,android.R.layout.simple_list_item_2,new String[] {"text1", "text2"},new int[] {android.R.id.text1, android.R.id.text2 });

            lv.setAdapter(adapter);
        }
    }

    private String downloadUrl(String my_url) throws  IOException{
        InputStream is = null;

        try{
            URL url = new URL(my_url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int response = conn.getResponseCode();
            is = conn.getInputStream();

            String contentAsString = readIt(is, 50);
            return contentAsString;
        } finally {
            if(is != null){
                is.close();
            }
        }
    }

    public void getJSON(String nameurl,int i) throws IOException {

        InputStream is = null;
        JSONArray Array = null;
        StringBuilder builder = new StringBuilder();
        String line;

        try{
            URL url = new URL(nameurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int response = conn.getResponseCode();
            is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            try{
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Array = new JSONArray( builder.toString());
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }
        } finally {
            if(is != null){
                is.close();
            }
        }

        if(i==1)
            Json.InputElements(Array);
        if(i==2)
            Json.InputList(Array);
    }

    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

}
