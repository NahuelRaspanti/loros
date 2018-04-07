package loros.loros;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Nahuel on 07/03/2018.
 */

public class Trabalengua {


    public String title;
    public String description;
    public String label;

    public Trabalengua(String _title) {
        title = _title;
    }

    public  Trabalengua(String _title, String _description, String _label) {
        title = _title;
        description = _description;
        label = _label;
    }

    public String getTitle () {
        return title;
    }

    public String getDescription () {
        return description;
    }

    public static ArrayList<Trabalengua> getTrabalenguasFromFile(String filename, Context context){
        final ArrayList<Trabalengua> trabalenguaList = new ArrayList<>();

        try {
            // Load data
            String jsonString = loadJsonFromAsset("trabalenguas.json", context);
            JSONObject json = new JSONObject(jsonString);
            JSONArray trabalenguas = json.getJSONArray("trabalenguas");

            // Get Trabalenguas objects from data
            for(int i = 0; i < trabalenguas.length(); i++){
                String _title = trabalenguas.getJSONObject(i).getString("title");
                String _desc = trabalenguas.getJSONObject(i).getString("description");
                String _label = trabalenguas.getJSONObject(i).getString("label");

                trabalenguaList.add(new Trabalengua(_title, _desc, _label));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return trabalenguaList;
    }

    private static String loadJsonFromAsset(String filename, Context context) {
        String json = null;

        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        }
        catch (java.io.IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return json;
    }


}
