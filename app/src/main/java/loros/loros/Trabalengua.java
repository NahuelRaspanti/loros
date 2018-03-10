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

    public static ArrayList<Trabalengua> getTrabalenguasFromFile(String filename, Context context){
        final ArrayList<Trabalengua> trabalenguaList = new ArrayList<>();

        try {
            // Load data
            String jsonString = loadJsonFromAsset("trabalenguas.json", context);
            JSONObject json = new JSONObject(jsonString);
            JSONArray trabalenguas = json.getJSONArray("trabalenguas");

            // Get Trabalenguas objects from data
            for(int i = 0; i < trabalenguas.length(); i++){
                Trabalengua trabalengua = new Trabalengua();

                trabalengua.title = trabalenguas.getJSONObject(i).getString("title");
                trabalengua.description = trabalenguas.getJSONObject(i).getString("description");
                trabalengua.label = trabalenguas.getJSONObject(i).getString("label");

                trabalenguaList.add(trabalengua);
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
