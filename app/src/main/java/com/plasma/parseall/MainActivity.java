package com.plasma.parseall;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.plasma.parseall.db.DBUtil;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String records = "",error="";
    Button getData;
    TextView candidatedata, errorView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getData = (Button) findViewById(R.id.btngetdata);
        candidatedata = (TextView) findViewById(R.id.candidatedata);
        errorView = (TextView) findViewById(R.id.errorview);

        getData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncTasks() {
                    @Override
                    public void onPreExecute() {

                        if (isConnected(MainActivity.this)){
                            showCustomDialog();
                        }

                        // before execution
                        Connection connection = null;
                        try {
                            Class.forName("com.mysql.cj.jdbc.Driver");
                            connection = DriverManager.getConnection("jdbc:mysql://dpomserver.mysql.database.azure.com:3306/deepbluecomp", "dpomserver@dpomserver", "Yashw@123");
                            Statement statement = connection.createStatement();
                            ResultSet resultSet = statement.executeQuery("SELECT * FROM modelfinal");
                            while(resultSet.next()) {
                                records += resultSet.getString("Name") + " " + resultSet.getString("Phone_Number") + "\n";
                            }
                        }
                        catch(Exception e) {
                            error = e.toString();
                        }
                        finally {
                            if (connection!=null)
                                try {
                                    connection.close();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                        }
                    }

                    @Override
                    public void doInBackground() {
                        // background task here

                    }

                    @Override
                    public void onPostExecute() {
                        if (isConnected(MainActivity.this)){
                            showCustomDialog();
                        }
                        // Ui task here
                        candidatedata.setText(records);
                        if(error != "")
                            errorView.setText(error);
                    }
                }.execute();
            }
        });
    }

    private void showCustomDialog() {
        AlertDialog.Builder builder =new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Please Connect to internet")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent((Settings.ACTION_WIFI_SETTINGS)));
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    /*class Task extends AsyncTask<Void, Void, Void> {
        String records = "",error="";

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://dpomserver.mysql.database.azure.com:3306/deepbluecomp", "dpomserver@dpomserver", "Yashw@123");
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM modelfinal");
                while(resultSet.next()) {
                    records += resultSet.getString("Name") + " " + resultSet.getString("Phone_Number") + "\n";
                }
            }
            catch(Exception e) {
                error = e.toString();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            candidatedata.setText(records);
            if(error != "")
                errorView.setText(error);
            super.onPostExecute(aVoid);

        }
    }*/

    private boolean isConnected(MainActivity mainActivity){
        ConnectivityManager connectivityManager = (ConnectivityManager) mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifiConn != null && wifiConn.isConnected() || mobileConn !=null && mobileConn.isConnected()){
            return true;
        }
        else
            return false;
    }

}