package com.example.contactbottomnav;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GetContactsTask extends AsyncTask<String, Boolean, Boolean>
{

    public final int ERR_EXCEPTION = -2;

    private Handler _handler = new Handler();

    private int _errorCode = -1;        // value of 0 indicates success
    private String _errorString = null;
    private Runnable _runnable = null;
    private Context _context = null;

    private String category;
    private Map<String, Object> _contactMapLists = new HashMap<String, Object>();
    private ArrayList<String> _categories;

    // default constructor
    public GetContactsTask()
    {
    }

    public GetContactsTask(Context context, Runnable runnable, ArrayList<String> categories)
    {
        _context = context;
        _runnable = runnable;
        _categories = categories;
    }

    @Override
    protected Boolean doInBackground(String... params)
    {
        // simulate the network connection here

        for (String category : _categories) {
            // but actually read the json file from assets
            String jsonStr = null;
            try {
                InputStream is = _context.getAssets().open(category + ".json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                jsonStr = new String(buffer, "UTF-8");

                // convert json string to classes
                Contact[] contactList = new Gson().fromJson(jsonStr, Contact[].class);

                // store this list in the map
                _contactMapLists.put(category, contactList);

            } catch (IOException e) {
                e.printStackTrace();
                _errorCode = ERR_EXCEPTION;
                _errorString = e.toString();
            } catch (Exception e) {
                e.printStackTrace();
                _errorCode = ERR_EXCEPTION;
                _errorString = e.toString();
            }
        }

        return true;
    }

    @Override
    protected void onPreExecute()
    {

    }

    @Override
    protected void onPostExecute(Boolean result)
    {
        // success
        _errorCode = 0;
        _errorString = null;

        // notify the runnable
        _handler.post(_runnable);
    }

    public String getCategory() {
        return category;
    }

    public ArrayList<Contact> getContactList(String category)
    {
        Contact[] tmpList = (Contact[]) _contactMapLists.get(category);
        ArrayList<Contact> tmpArray = new ArrayList<Contact>(Arrays.asList(tmpList));
        return tmpArray;
    }

    public int getResult() {
        return _errorCode;
    }

}
