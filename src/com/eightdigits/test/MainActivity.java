package com.eightdigits.test;

import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.eightdigits.sdk.EightDigitsClient;

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
        System.out.println(android.os.Build.VERSION.SDK_INT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    protected void onDestroy() {
      super.onDestroy();
      this.eightDigitsClient.endScreen();
    }
    
    @Override
    protected void onRestart() {
      super.onRestart();
      this.eightDigitsClient.onRestart("Yeni ziyaret", "/home");
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
      this.eightDigitsClient = EightDigitsClient.createInstance(this, "http://192.168.1.89:8080/api/", "DJjAd2sj01");
      this.eightDigitsClient.auth("3b755d11ac246bf7e0d7cd4bde712145");
      this.eightDigitsClient.newVisit("Yeni Ziyaret", "/home");
      this.eightDigitsClient.setLocation("41.4", "29.5");
      this.eightDigitsClient.setVisitorAttribute("fullName", "Serkan Karababa");
      this.eightDigitsClient.setVisitorGSM("Turkcell");
      
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
        this.activity.eightDigitsClient.newEvent("newactivitybuttonclicked", "buttonvalue");
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
        this.activity.eightDigitsClient.newEvent("New Click On List", this.activity.items[arg2]);
      }
    }
}
