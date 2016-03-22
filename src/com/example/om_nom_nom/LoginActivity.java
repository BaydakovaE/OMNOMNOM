package com.example.om_nom_nom;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private EditText username, password;
	private Button login;
	
	private Context context = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		context = getApplicationContext();		
		
		username = (EditText)findViewById(R.id.usernameEdit);
		password = (EditText)findViewById(R.id.passwordEdit);
		login = (Button)findViewById(R.id.loginButton);
		
		login.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				class Task extends AsyncTask<String, Integer, String> {
					@Override
					protected void onPreExecute() {
						login.setEnabled(false);
	                }
					@Override
					protected String doInBackground(String... params) {
						try {
							Connection conn = new Connection(context);
							HttpURLConnection urlConnection = loginRequest();
							conn.setConnection(urlConnection);
							InputStream in = new BufferedInputStream(
									urlConnection.getInputStream());
							BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					        if (reader.readLine().equals("ok")) {
					        	Connection.isLogged = true;
					        } else {
					        	Connection.isLogged = false;
					        }
					        reader.close();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return null;
					}
					@Override
					protected void onPostExecute(String s) {
						if (!Connection.isLogged) {
							login.setEnabled(true);
							Toast.makeText(context, "Incorrect login or password", Toast.LENGTH_LONG).show();
						} else {
							Toast.makeText(context, "You are successfully logged in", Toast.LENGTH_LONG).show();
			                finish();
						}
                    }
				}
				Task task = new Task();
				task.execute();
			}
		});
	}

	protected HttpURLConnection loginRequest() throws Exception {
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("login", username.getText().toString());
		hashmap.put("password", password.getText().toString());
		HttpURLConnection conn = Post.performPostCall(Connection.mainUrl + "/?r=authentication/login", hashmap);
		return conn;
	}
}
