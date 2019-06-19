package com.example.contactbottomnav;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.view.LayoutInflater;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactAdapter extends ArrayAdapter<Contact> {

    //
    private ArrayList<Contact> _items;
    private LayoutInflater _inflater;

    public ContactAdapter(Context context, int resource, ArrayList<Contact> items) {
        super(context, resource);

        _inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // save items for list
        _items = items;
    }

    // setData
    public void setData(ArrayList<Contact> items)
    {
        try
        {
            _items = items;

            // add items to adapter
            clear();
            for (int n = 0; n < items.size(); n++) {
                add(_items.get(n));
            }

            // signal the data set has changed
            notifyDataSetChanged();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v;

        if (null != convertView)
        {
            v = convertView;
        } else {
            v = _inflater.inflate(R.layout.contact_row, null);
        }

        try {
            // get contacts
            Contact contact = _items.get(position);

            // fix up the image

            // show the display name
            TextView tvName = (TextView) v.findViewById(R.id.contact_name);
            if (null != tvName) {
                tvName.setText(contact.getFirstName() + " " + contact.getLastName());
            }
            TextView tvCompany = (TextView) v.findViewById(R.id.contact_company);
            if (null != tvCompany) {
                tvCompany.setText(contact.getCompany());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return v;
    }
}
