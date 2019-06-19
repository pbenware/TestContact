package com.example.contactbottomnav;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class ContactDetailActivity extends Activity {

    private String _contactId = null;
    private String _contactName = null;
    private String _companyName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_details);

        // get the intent to grab inbound data
        Intent intent = getIntent();
        _contactId = intent.getStringExtra("ContactId");
        _contactName = intent.getStringExtra("ContactName");
        _companyName = intent.getStringExtra("CompanyName");

        TextView tvContactName = (TextView) findViewById(R.id.contact_name);
        tvContactName.setText(_contactName);

        TextView tvCompanyName = (TextView) findViewById(R.id.contact_company);
        tvCompanyName.setText(_companyName);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

}
