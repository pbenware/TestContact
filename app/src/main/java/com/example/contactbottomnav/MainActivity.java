package com.example.contactbottomnav;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ListActivity {
    private TextView _mTextMessage;

    BottomNavigationView _navView;

    private GetCategoriesTask _getCategoriesTask = null;
    private GetContactsTask _getContactsTask = null;
    private ContactCategory[] _categoryList;
    private ArrayList<Contact> _workContactList = new ArrayList<Contact>();
    private ArrayList<Contact> _homeContactList = new ArrayList<Contact>();
    private ArrayList<Contact> _friendContactList = new ArrayList<Contact>();

    private ContactAdapter _adapter;
    private ListView _listV;
    private int _activeTabId;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            int selectedItemId = item.getItemId();

            // store the active tab id
            _activeTabId = selectedItemId;
            SettingsStore settings = new SettingsStore(MainActivity.this);
            settings.setCurrentFocusedTab(_activeTabId);

            // now show it
            switch (selectedItemId) {
                case R.id.navigation_work:
                    updateContacts(_workContactList);
                    _mTextMessage.setText(R.string.title_work);
                    return true;
                case R.id.navigation_home:
                    updateContacts(_homeContactList);
                    _mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_friends:
                    updateContacts(_friendContactList);
                    _mTextMessage.setText(R.string.title_friends);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _navView = findViewById(R.id.nav_view);
//        _mTextMessage = findViewById(R.id.message);
        _mTextMessage = findViewById(android.R.id.empty);
        _navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // initial active tab defaults to work
        _activeTabId = R.id.navigation_work;

        // see if we saved an active tab from previous run
        SettingsStore settings = new SettingsStore(this);
        int lastTabId = settings.getCurrentFocusedTab();
        if (lastTabId != 0) {
            _activeTabId = lastTabId;
        }

        _navView.setSelectedItemId(_activeTabId);

        _adapter = new ContactAdapter(this,R.layout.contact_row, _workContactList);
        setListAdapter(_adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // get the categories
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                _getCategoriesTask = new GetCategoriesTask(MainActivity.this, onGetCategoriesComplete);
                _getCategoriesTask.execute();
            }
        }, 300);
    }

    // onGetCategoriesComplete
    public final Runnable onGetCategoriesComplete = new Runnable() {
        @Override
        public void run() {
            // make sure we have a valid result
            if (null != _getCategoriesTask) {
                if (0 == _getCategoriesTask.getResult()) {

                    // get the categories
                    _categoryList = _getCategoriesTask.getCategories();

                    //
                    // update the UI
                    //

                    // get the menu
                    Menu navMenu = _navView.getMenu();

                    // loop through and replace the text/icon with "downloaded" values
                    for (int i = 0; i < _categoryList.length; i++) {
                        try {
                            String categoryId = _categoryList[i].getCategoryId();
                            for (int j = 0; j < navMenu.size(); j++) {
                                //TODO: come back to this
                            }

                            String foo = _categoryList[i].getCategoryId() + ":" + _categoryList[i].getName();
                            Log.d("MyApp", foo);
                        } catch (Exception e) {

                        }
                    }

                    // now request the contacts for each category
                    ArrayList<String> categoryIds = new ArrayList<String>();
                    for (int i = 0; i < _categoryList.length; i++) {
                        categoryIds.add(_categoryList[i].getCategoryId());
                    }

                    _getContactsTask = new GetContactsTask(MainActivity.this, onGetContactsComplete, categoryIds);
                    _getContactsTask.execute();
                }
            }
        }
    };

    // onGetContactsComplete
    public final Runnable onGetContactsComplete = new Runnable() {
        @Override
        public void run() {
            // make sure we have a valid result
            if (null != _getContactsTask) {
                if (0 == _getContactsTask.getResult()) {
                    // retrieve the contacts
                    for (ContactCategory category : _categoryList)
                    {
                        // get the list based on category
                        ArrayList<Contact> list = _getContactsTask.getContactList(category.getCategoryId());

                        // save to right list
                        if (category.getName().equalsIgnoreCase("Work") ) {
                            _workContactList = list;
                        } else if (category.getName().equalsIgnoreCase("Home")) {
                            _homeContactList = list;
                        } else if (category.getName().equalsIgnoreCase("Friends")) {
                            _friendContactList = list;
                        } else {
                            // error condition
                        }
                    }

                    // Now update the UI with the contact information
                    switch (_activeTabId) {
                        case R.id.navigation_friends:
                            updateContacts(_friendContactList);
                            break;
                        case R.id.navigation_home:
                            updateContacts(_homeContactList);
                            break;
                        case R.id.navigation_work:
                            // fall through
                        default:
                            // default to work
                            updateContacts(_workContactList);
                    }
                }
            }
        }
    };

    protected void updateContacts(ArrayList<Contact> contacts)
    {
        if (null != _adapter) {
            _adapter.setData(contacts);
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);

        onViewContactDetails((Contact) l.getItemAtPosition(position));
    }

    protected void onViewContactDetails(Contact contact)
    {
        try {
            // launch an activity to view the contact details
            Intent intent = new Intent(MainActivity.this, ContactDetailActivity.class);
            intent.putExtra("ContactId", contact.getContactId());
            intent.putExtra("ContactName", contact.getFirstName() + " " + contact.getLastName());
            intent.putExtra("CompanyName", contact.getCompany());
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}