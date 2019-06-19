package com.example.contactbottomnav;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.JsonReader;

import com.google.gson.Gson;

import org.json.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class GetCategoriesTask extends AsyncTask<String, Boolean, Boolean>
{
    public final int ERR_EXCEPTION = -2;

    private Handler _handler = new Handler();

    private int _errorCode = -1;        // value of 0 indicates success
    private String _errorString = null;
    private Runnable _runnable = null;
    private Context _context = null;

    private ContactCategory[] categories;

    // default constructor
    public GetCategoriesTask()
    {
    }

    public GetCategoriesTask(Context context, Runnable runnable)
    {
        _context = context;
        _runnable = runnable;
    }

    @Override
    protected Boolean doInBackground(String... params)
    {
        // simulate the network connection here

        // but actually read the json file from assets
        String jsonStr = null;
        try {
            InputStream is = _context.getAssets().open("categories.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonStr = new String(buffer, "UTF-8");

            categories = new Gson().fromJson(jsonStr, ContactCategory[].class);

        } catch (IOException e) {
            e.printStackTrace();
            _errorCode = ERR_EXCEPTION;
            _errorString = e.toString();
        } catch (Exception e) {
            e.printStackTrace();
            _errorCode = ERR_EXCEPTION;
            _errorString = e.toString();
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

    // hard coded data for the categories
    public ContactCategory[] getCategories()
    {
        return categories;
    }

    public int getResult() {
        return _errorCode;
    }
}
