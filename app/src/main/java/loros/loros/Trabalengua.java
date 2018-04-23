package loros.loros;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Nahuel on 07/03/2018.
 */

public class Trabalengua {


    public String title;
    public String description;
    public boolean active;

    public Trabalengua(String _title) {
        title = _title;
    }

    public  Trabalengua(String _title, String _description) {
        title = _title;
        description = _description;
    }

    public Trabalengua(String _title, String _description, boolean _active) {
        title = _title;
        description = _description;
        active = _active;
    }

    public String getTitle () {
        return title;
    }

    public String getDescription () {
        return description;
    }

    public boolean isActive() {return active;}

    public static ArrayList<Trabalengua> getTrabalenguasFromFile(String filename, Context context){
        final ArrayList<Trabalengua> trabalenguaList;
        String jsonString = loadJsonFromAsset("trabalenguas.json", context);
        trabalenguaList = new Gson().fromJson(jsonString, new TypeToken<ArrayList<Trabalengua>>(){}.getType());
        return trabalenguaList;
    }


    private static String loadJsonFromAsset(String filename, Context context) {
        String json = null;

        try {
            String path = Environment.getExternalStorageDirectory() + "/" + filename;
            File file = new File(path);
            FileInputStream is = new FileInputStream(file);
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
