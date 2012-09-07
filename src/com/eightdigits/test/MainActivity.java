package com.eightdigits.test;

import java.util.Arrays;

import com.eightdigits.sdk.EightDigitsClient;
import com.eightdigits.sdk.exceptions.EightDigitsApiException;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
    public String[] items = new String[20];
    EightDigitsClient eightDigitsClient;
    String               hitCode;
    ListView mainListView;
    Button newActivityButton;
    ArrayAdapter<String> listAdapter;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        createEightDigitsClient();
        fillListView();
        attachListViewEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    protected void onDestroy() {
      super.onDestroy();
      this.eightDigitsClient.endScreen(this.hitCode);
    }
    
    private void init() {
      mainListView = (ListView) findViewById(R.id.listView1);
      newActivityButton = (Button) findViewById(R.id.newActivityButton);
    }
    
    /**
     * Attaches onItemClick event to list
     */
    private void attachListViewEvents() {
      mainListView.setOnItemClickListener(new ListViewOnItemClickListener(this));
      newActivityButton.setOnClickListener(new newActivityButtonOnClickListener(this));
    }
    
    /**
     * Adds data to list view
     */
    private void fillListView() {
      
      for(int i = 0; i < items.length; i++) {
        items[i] = "Item " + i;
      }
      
      listAdapter = new ArrayAdapter<String>(this, R.layout.row_item, Arrays.asList(items));
      mainListView.setAdapter(listAdapter);
      
    }
    
    /**
     * Creates and assigns an instance of 8digits client
     */
    private void createEightDigitsClient() {
      this.eightDigitsClient = EightDigitsClient.createInstance(this,
          "http://demo1.8digits.com", "DJjAd2sj03");
      
      try {
        this.eightDigitsClient.authWithUsername("verisun", "hebelek");
        this.hitCode = this.eightDigitsClient.newVisit("Yeni Ziyaret", "/home");
      } catch (EightDigitsApiException e) {
        Log.e("API Error", e.getMessage());
      }
    }
    
    /**
     * 
     * @author gurkanoluc
     *
     */
    private static class newActivityButtonOnClickListener implements OnClickListener {
      
      private MainActivity activity;
      
      public newActivityButtonOnClickListener(MainActivity activity) {
        this.activity = activity;
      }
      
      @Override
      public void onClick(View v) {
        this.activity.eightDigitsClient.newEvent("newactivitybuttonclicked", "buttonvalue", this.activity.hitCode);
        Intent secondActivityIntent = new Intent(this.activity, SecondActivity.class);
        this.activity.startActivity(secondActivityIntent);
      }
    }
    
    /**
     * List item on click handler
     * 
     * @author gurkanoluc
     *
     */
    private static class ListViewOnItemClickListener implements OnItemClickListener {
      
      private MainActivity activity;
      
      public ListViewOnItemClickListener(MainActivity activity) {
        this.activity = activity;
      }
      
      @Override
      public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
          long arg3) {
        Toast.makeText(this.activity.getApplicationContext(), this.activity.items[arg2], Toast.LENGTH_LONG).show();
        this.activity.eightDigitsClient.newEvent("New Click On List", this.activity.items[arg2], this.activity.hitCode);
      }
    }
}
