package com.eightdigits.test;

import com.eightdigits.sdk.EightDigitsClient;
import com.eightdigits.sdk.exceptions.EightDigitsApiException;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class SecondActivity extends Activity {

  EightDigitsClient eightDigitsClient;
  String            hitCode;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_second);
    this.eightDigitsClient = EightDigitsClient.getInstance();
    this.eightDigitsClient.newScreen("SecondAndroidActivity", "/second");
  }
  
  @Override
  protected void onRestart() {
    super.onRestart();
    this.eightDigitsClient.onRestart("SecondAndroidActivity", "/second");
  }
  

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.activity_second, menu);
    return true;
  }
}
