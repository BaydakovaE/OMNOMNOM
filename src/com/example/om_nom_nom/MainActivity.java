package com.example.om_nom_nom;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.GestureDetector;

public class MainActivity extends Activity {

	Menu menu;
	Fragment fwelc;
	Fragment fcat;
	FragmentTransaction frt;
	private boolean switcher = true;

	class IsGuestAsync extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				return LoginHelper.isGuest(getApplicationContext());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}
	}

	class LogoutAsync extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			Connection conn = new Connection(getApplicationContext());
			HttpURLConnection urlConnection = null;
			BufferedReader reader = null;
			try {
				urlConnection = conn.getConnection(Connection.mainUrl
						+ "/?r=authentication/logout");
				InputStream in = new BufferedInputStream(
						urlConnection.getInputStream());
				reader = new BufferedReader(new InputStreamReader(in));
				reader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				urlConnection.disconnect();
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return true;
		}
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putBoolean("state", switcher);
		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		LinearLayout ll = (LinearLayout) this.findViewById(R.id.ll);
		ll.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
		    public void onSwipeTop() {
		        Toast.makeText(MainActivity.this, "top", Toast.LENGTH_SHORT).show();
		    }
		    public void onSwipeRight() {
		        Toast.makeText(MainActivity.this, "right", Toast.LENGTH_SHORT).show();
		    }
		    public void onSwipeLeft() {
		    	frt = getFragmentManager().beginTransaction();
				frt.replace(R.id.frgmCont, fcat);
				frt.commit();
		    }
		    public void onSwipeBottom() {
		        Toast.makeText(MainActivity.this, "bottom", Toast.LENGTH_SHORT).show();
		    }

		});

		fwelc = new fWelcome();
		fcat = new fCatalog();

		if (savedInstanceState != null) {
			switcher = savedInstanceState.getBoolean("state");
		} else {
			frt = getFragmentManager().beginTransaction();
			frt.replace(R.id.frgmCont, fwelc);
			frt.commit();
		}
	}

	/*@Override
	public void onClick(View v) {
		frt = getFragmentManager().beginTransaction();
		if (switcher) {
			frt.replace(R.id.frgmCont, fcat);
		} else {
			frt.replace(R.id.frgmCont, fwelc);
		}
		switcher = !switcher;
		frt.commit();
	}*/

	
	
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.main, menu);
		this.menu = menu;
		IsGuestAsync task = new IsGuestAsync();
		task.execute();
		try {
			Connection.isLogged = !task.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		updateMenuTitles();
		return true;
	}

	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		this.menu = menu;
		updateMenuTitles();
		return true;
	};

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		case R.id.loginItem:
			if (item.getTitle() == "Log in") {
				Intent intent = new Intent(MainActivity.this,
						LoginActivity.class);
				startActivity(intent);
			} else {
				LogoutAsync task = new LogoutAsync();
				task.execute();
				Connection.isLogged = false;
			}
		}
		return super.onOptionsItemSelected(item);
	}

	private void updateMenuTitles() {
		MenuItem menuItem = menu.findItem(R.id.loginItem);
		if (!Connection.isLogged) {
			menuItem.setTitle("Log in");
		} else {
			menuItem.setTitle("Log out");
		}
	}
}
