package com.saharmassachi.labs.newfellow;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;
import static com.saharmassachi.labs.newfellow.Constants.NAME;
import static com.saharmassachi.labs.newfellow.Constants.EMAIL;

public class ItemizedContactOverlay extends ItemizedOverlay<ContactOverlayItem> {
	DBhelper helper;
	Context mContext;
	private ArrayList<ContactOverlayItem> mOverlays;

	public ItemizedContactOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
		mOverlays = new ArrayList<ContactOverlayItem>();
		
	}

	public ItemizedContactOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		
		mContext = context;
		mOverlays = new ArrayList<ContactOverlayItem>();
		helper = new DBhelper(context);
		populate();
	}

	
	public void addToOverlay(ContactOverlayItem overlay) {
		mOverlays.add(overlay);
		populate();
	}

	/**
	 * method to empty out overlay
	 */
	protected void emptyOverlay() {
		mOverlays.clear();
		setLastFocusedIndex(-1);
		populate();
	}

	@Override
	protected ContactOverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}

	@Override
	protected boolean onTap(int index) {

		ContactOverlayItem item = (ContactOverlayItem) mOverlays.get(index);
		
		long cid = item.getContact();
	
		//Returns NAME PHONE EMAIL TWITTER FBID into cid 
		 
		String[] dbinfo = helper.getContactInfo(cid);
		
		if(dbinfo == null){
			dbinfo = new String[5];
		}
		final String[] cinfo = dbinfo;
		Dialog dialog = new Dialog(mContext);

		dialog.setContentView(R.layout.contactdialog);
		Button email = (Button) dialog.findViewById(R.id.emailbutton);
		Button sms = (Button) dialog.findViewById(R.id.smsbutton);
		Button phone = (Button) dialog.findViewById(R.id.phonebutton);
		if(null!= cinfo[1]){
			//if there is a phone number, then set the sms button to visible
			sms.setVisibility(0);
			phone.setVisibility(0);
		}
		if(cinfo[2] != null){
			//if there is an email address, then set the sms button to visible
			email.setVisibility(0);
		}
	
		dialog.setTitle(cinfo[0]); //title of the dialog is the name of the person
		dialog.setCanceledOnTouchOutside(true);
		if(cinfo[2] != null){
		email.setOnClickListener(
		          new OnClickListener() {

		      @Override
		      public void onClick(View v) {
		    	  final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		    	  emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{cinfo[2]});
		    	  emailIntent.setType("plain/text");  
		    	  mContext.startActivity(Intent.createChooser(emailIntent, "send mail"));
		    	  
		      }
		  });
		if(cinfo[1] != null){
			sms.setOnClickListener(new OnClickListener() {

			      @Override
			      public void onClick(View v) {
			    	  final Intent sendIntent = new Intent(android.content.Intent.ACTION_VIEW);
			    	  sendIntent.putExtra("sms_body", "Content of the SMS goes here...");
			    	  sendIntent.putExtra("address", cinfo[1]);
			    	  sendIntent.setType("vnd.android-dir/mms-sms"); 
			    	  mContext.startActivity(Intent.createChooser(sendIntent, "send sms"));
			    	  
			      }
			  });
			phone.setOnClickListener(new OnClickListener(){
				
				 @Override
			      public void onClick(View v) {
			    	  final Intent callIntent = new Intent(android.content.Intent.ACTION_CALL);
			    	  callIntent.setData(Uri.parse("tel:"+ cinfo[1])); 
			    	  mContext.startActivity(Intent.createChooser(callIntent, "make call"));  
			      }
			});
			
			
			
			
		};
        
        
        
        
        
		}
		
		
		dialog.show();
		return true;
	
/*		
		Dialog contactDlg = new Dialog(); 
			new Dialog(this, R.layout.contactdialog);
		
		contactDlg.setContentView(R.layout.contactdialog);

		contactDlg.show();
		return true;
*/
		
		/*
		OverlayItem item = mOverlays.get(index);
		AlertDialog.Builder dialogB = new AlertDialog.Builder(mContext); // dialog.setTitle("hello");
		dialogB.setTitle(item.getTitle());
		dialogB.setMessage(item.getSnippet());
		Dialog dialog = dialogB.create();
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
		return true;
		*/
	}

}
