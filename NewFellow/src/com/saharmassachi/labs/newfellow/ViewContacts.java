package com.saharmassachi.labs.newfellow;

import static com.saharmassachi.labs.newfellow.Constants.CID;
import static com.saharmassachi.labs.newfellow.Constants.FNAME;
import static com.saharmassachi.labs.newfellow.Constants.LNAME;

import java.util.HashMap;
//import static com.saharmassachi.labs.newfellow.Constants.NAME;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ViewContacts extends ListActivity {
	private DataHelper helper;
	private EditText filterText = null;
	private ArrayAdapter<String> adapter = null;
	private Intent nextPage;
	private long[] ids;
	Contact[] contacts;
	private HashMap<String, Long> map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		helper = new DataHelper(this);
		setContentView(R.layout.viewcontacts);

		filterText = (EditText) findViewById(R.id.search_line);
		filterText.addTextChangedListener(filterTextWatcher);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, getStringArrayList());
		
		nextPage = new Intent(this, ShowContact.class);

		setListAdapter(adapter);
		
		
		
		
		
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				final String name = (String) ((TextView) view).getText();
				final String fname = name.split(" ")[0];
				final String lname = name.split(" ")[1];
				nextPage.putExtra(FNAME, fname);
				nextPage.putExtra(LNAME, lname);
				nextPage.putExtra(CID, map.get(name));
				startActivity(nextPage);

			}});
		}

		private String[] getStringArrayList() {
			// get all attendees
			map = new HashMap<String, Long>();
			contacts = helper.getBasicContacts();
			String[] toReturn = new String[contacts.length];
			
			for (int i = 0; i < contacts.length; i++) {
				toReturn[i] = contacts[i].getName();
				map.put(toReturn[i], contacts[i].getCid());
			}

			
			return toReturn;
		}
		
		
		
		private TextWatcher filterTextWatcher = new TextWatcher() {

			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				adapter.getFilter().filter(s);
			}

		};

		@Override
		protected void onDestroy() {
			super.onDestroy();
			filterText.removeTextChangedListener(filterTextWatcher);
		}
	}

