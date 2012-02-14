package com.saharmassachi.labs.newfellow;

import static com.saharmassachi.labs.newfellow.Constants.FNAME;
import static com.saharmassachi.labs.newfellow.Constants.LNAME;
import static com.saharmassachi.labs.newfellow.Constants.CID;
import static com.saharmassachi.labs.newfellow.Constants.RAWLOC;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.method.KeyListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class ShowContact extends Activity {
	
	private DBhelper helper;
	private  long id;
	private TextView[] tvs;
	private EditText[] ets;
	private KeyListener[] listeners;
	private InputFilter[][] filters;
	private TextView topName;
	private Spinner spinner;
	private EditText etaddress;
	
	private boolean editMode = false;
	private Button reset;
	private Button save;
	private int numRows = 4; //number of stanard textview :: edittext rows we have
	private String fname;
	private String lname;
	private String phone;
	private String email;
	private String twitter;
	private String base;
	private int oldlat;
	private int oldlong;
	private Contact toShow;
	private long cid;
	
	private ArrayList<String> addressList = new ArrayList<String>();
	private List<Address> allAddresses;
	private ArrayAdapter<String> adapter;
	private int whichAddress;
	private Geocoder geocoder;
	
	private final int MAXRESULTS = 5;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showcontact);
		helper = new DBhelper(this);
		Bundle extras = getIntent().getExtras();
		fname = extras.getString(FNAME);
		lname = extras.getString(LNAME);
		cid = extras.getLong(CID);
		geocoder = new Geocoder(this);;
		getValues();
		loadViews();
		setViews();
		
	}
	
	private void loadViews(){
		tvs = new TextView[numRows];
		ets = new EditText[numRows];
		//listeners = new KeyListener[numRows];
		//filters = new InputFilter[numRows][10];
		
		
		topName = (TextView) findViewById(R.id.topName);
		etaddress = (EditText) findViewById(R.id.etaddress);
		ets[0] = (EditText) findViewById(R.id.TextView1b);
		ets[1] = (EditText) findViewById(R.id.TextView2b);
		ets[2] = (EditText) findViewById(R.id.TextView3b);
		ets[3] = (EditText) findViewById(R.id.TextView4b);
		
		tvs[0] = (TextView) findViewById(R.id.TextView1a);
		tvs[1] = (TextView) findViewById(R.id.TextView2a);
		tvs[2] = (TextView) findViewById(R.id.TextView3a);
		tvs[3] = (TextView) findViewById(R.id.TextView4a);	
		
		/*for(int i = 0; i < numRows; i++){
			listeners[i] = ets[i].getKeyListener();
			filters[i] = ets[i].getFilters();
		}
		*/
		reset = (Button) findViewById(R.id.resetButton);
		save = (Button) findViewById(R.id.saveButton);
		spinner = (Spinner) findViewById(R.id.spincheck);
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView adapter, View v,
					int pos, long lng) {				
				whichAddress = pos;
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	
	}
	private void getValues(){
		//call the db to get the info on this person and preload the db with it
		//TODO
		toShow = helper.getContactInfo(cid);
		phone = toShow.getPhone();
		twitter = toShow.getTwitter();
		base = toShow.getBase();
		oldlat = toShow.getLat();
		oldlong = toShow.getLong();
	}
	
	private void setViews(){
		//TODO
		topName.setText(fname + " " + lname);
		tvs[0].setText("Name:");
		ets[0].setText(fname + " " + lname);
	
		tvs[1].setText("Phone:");
		if(phone != null && (phone.length() > 1)){
			ets[1].setText(phone);
		}
		else{
			ets[1].setText("");
		}
		
		tvs[2].setText("Email:");
		if(email != null && (email.length() > 1)){
			ets[2].setText(email);
		}
		else{
			ets[2].setText("");
		}
		
		tvs[3].setText("Twitter:");
		if(twitter != null && (twitter.length() > 0)){
			ets[3].setText(twitter);
		}
		else{
			ets[3].setText("");
		}
		
		if((base != null) && (base.length() > 0)){
			etaddress.setText(base);
		}
		
		
	}
	
	public void toggleEditMode(View v){
		CheckBox b = (CheckBox) v;
		if(b.isChecked()){
			editMode = true;
			//setEditables(editMode);
			//reset.setVisibility(View.VISIBLE);
		}
		else{
			editMode = false;
			//setEditables(editMode);
			//reset.setVisibility(View.INVISIBLE);
		}
	}
	
	/*private void setEditables(boolean m){
		if(m){
			for (int i=0; i<numRows; i++){
				//ets[i].setKeyListener(listeners[i]);
				ets[i].setFilters(filters[i]);
				
				/*ets[i].setFocusable(true);
				ets[i].setFocusableInTouchMode(true); // user touches widget on phone with touch screen
				ets[i].setClickable(true);*//*
			}
		}
		else{
			for (int i=0; i<numRows; i++){
				//ets[i].setKeyListener(null);
				ets[i].setFilters(new InputFilter[] {
					    new InputFilter() {
					        public CharSequence filter(CharSequence src, int start,
					                int end, Spanned dst, int dstart, int dend) {
					                return src.length() < 1 ? dst.subSequence(dstart, dend) : "";
					        }
					    }
					});
				/*ets[i].setFocusable(false);
				ets[i].setFocusableInTouchMode(false); // user touches widget on phone with touch screen
				ets[i].setClickable(false);*/
				 /*
			}
		}
		
	}*/
	
	public void getAddressInfo(View v) {
		Toast t = Toast.makeText(this,
				"Address not recognized, please try again", Toast.LENGTH_LONG);
		try {
			allAddresses = geocoder.getFromLocationName(etaddress.getText()
					.toString(), MAXRESULTS);
			if (!allAddresses.isEmpty()) {
				addressList.clear();
				for (Address a : allAddresses) {
					StringBuilder s = new StringBuilder();
					for (int i = 0; i < a.getMaxAddressLineIndex(); i++) {
						s.append(a.getAddressLine(i)).append("\n");
					}
					String sss = s.toString();
					addressList.add(s.toString());

				}

				adapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_spinner_item, addressList);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner.setAdapter(adapter);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		
	}
	
	public void Save(View v){
		//delete old value in the table
		//add this one instead.
		String[] newnames = ets[0].getText().toString().split(" ", 2);
		if(newnames[1] == null){
			Toast.makeText(this, "Please use a firstname and lastname", Toast.LENGTH_SHORT).show();
			return;
		}
		String newfname = newnames[0];
		String newlname = newnames[1];
		String newphone = ets[1].getText().toString();
		String newemail = ets[2].getText().toString();
		String newtwitter = ets[3].getText().toString();
		
		Address a = allAddresses.get(whichAddress);
		
		int j = a.getMaxAddressLineIndex();
		String toString = "";
		for (int i = 0; i <= j; i++) {
			toString += a.getAddressLine(i);
			toString += "\n";
		}
		String newbase = toString;
		
		//unless all the values are the same...
		if (!((newfname == fname) && (newlname == lname) && (newphone == phone) && (newtwitter == twitter) && (newemail == email) && (newbase == base))){
			//delete old value in the table
			//TODO 
			//add new value
			//helper.delContact(toShow);
			int newlat = (int) (a.getLatitude() * 1E6);
			int newlong =(int) ( a.getLongitude() * 1E6); 
			
			Contact c = new Contact(cid, newfname, newlname);
			c.setEmail(newemail);
			c.setPhone(newphone);
			c.setTwitter(newtwitter);
			c.setBase(newbase);
			c.setLat(newlat);
			c.setLong(newlong);
			//helper.addContact(c);
			
		}
		
	}
	
	public void Reset(View v){
		ets[0].setText(fname + " " + lname);
		if(phone != null)   {	ets[1].setText(phone);  }  else { ets[1].setText("");}
		if(email != null)	{	ets[2].setText(email);  }  else { ets[2].setText("");}
		if(twitter != null)	{	ets[3].setText(twitter);}  else { ets[3].setText("");}
		if(base != null)    {etaddress.setText(base);   }  else {etaddress.setText("");}
		adapter.clear();
	}
}
