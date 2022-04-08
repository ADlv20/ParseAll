package com.plasma.parseall;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
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
                        // Ui task here
                        candidatedata.setText(records);
                        if(error != "")
                            errorView.setText(error);
                    }
                }.execute();
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

}