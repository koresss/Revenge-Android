package com.example.orestis.myapplication;

import android.app.Activity;

import android.app.ProgressDialog;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.Gravity;

import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

public class generalTop10nomoi  extends Activity
{
    public final String[] nomoi = {"Αιτωλοακαρνανίας","Αργολίδας","Αρκαδίας","Άρτας","Αττικής","Αχαΐας","Βοιωτίας","Γρεβενών","Δράμας","Δωδεκανήσου","Εβοίας","Έβρου","Ευρυτανίας","Ζακύνθου","Ηλείας","Ημαθίας","Ηρακλείου","Θεσπρωτίας","Θεσσαλονίκης","Ιωαννίνων","Καβάλας","Καρδίτσας","Καστοριάς","Κέρκυρας","Κεφαλληνίας","Κιλκίς","Κοζάνης","Κορινθίας","Κυκλάδων","Λακωνίας","Λάρισας","Λασιθίου","Λέσβου","Λευκάδας","Μαγνησίας","Μεσσηνίας","Ξάνθης","Πέλλας","Πιερίας","Πρέβεζας","Ρεθύμνου","Ροδόπης","Σάμου","Σερρών","Τρικάλων","Φθιώτιδας","Φλώρινας","Φωκίδας","Χαλκιδικής","Χανίων","Χίου"};
    public static final String[] NOMOICAPS = {"ΑΙΤΩΛΟΑΚΑΡΝΑΝΙΑΣ", "ΑΡΓΟΛΙΔΑΣ", "ΑΡΚΑΔΙΑΣ", "ΑΡΤΑΣ", "ΑΤΤΙΚΗΣ", "ΑΧΑΙΑΣ", "ΒΟΙΩΤΙΑΣ", "ΓΡΕΒΕΝΩΝ", "ΔΡΑΜΑΣ", "ΔΩΔΕΚΑΝΗΣΟΥ", "ΕΒΟΙΑΣ", "ΕΒΡΟΥ", "ΕΥΡΥΤΑΝΙΑΣ", "ΖΑΚΥΝΘΟΥ", "ΗΛΕΙΑΣ", "ΗΜΑΘΙΑΣ", "ΗΡΑΚΛΕΙΟΥ", "ΘΕΣΠΡΩΤΙΑΣ", "ΘΕΣΣΑΛΟΝΙΚΗΣ", "ΙΩΑΝΝΙΝΩΝ", "ΚΑΒΑΛΑΣ", "ΚΑΡΔΙΤΣΑΣ", "ΚΑΣΤΟΡΙΑΣ", "ΚΕΡΚΥΡΑΣ", "ΚΕΦΑΛΛΗΝΙΑΣ", "ΚΙΛΚΙΣ", "ΚΟΖΑΝΗΣ", "ΚΟΡΙΝΘΙΑΣ", "ΚΥΚΛΑΔΩΝ", "ΛΑΚΩΝΙΑΣ", "ΛΑΡΙΣΑΣ", "ΛΑΣΙΘΙΟΥ", "ΛΕΣΒΟΥ", "ΛΕΥΚΑΔΑΣ", "ΜΑΓΝΗΣΙΑΣ", "ΜΕΣΣΗΝΙΑΣ", "ΞΑΝΘΗΣ", "ΠΕΛΛΑΣ", "ΠΙΕΡΙΑΣ", "ΠΡΕΒΕΖΑΣ", "ΡΕΘΥΜΝΟΥ", "ΡΟΔΟΠΗΣ", "ΣΑΜΟΥ", "ΣΕΡΡΩΝ", "ΤΡΙΚΑΛΩΝ", "ΦΘΙΩΤΙΔΑΣ", "ΦΛΩΡΙΝΑΣ", "ΦΩΚΙΔΑΣ", "ΧΑΛΚΙΔΙΚΗΣ", "ΧΑΝΙΩΝ", "ΧΙΟΥ"};
    TableLayout table_layout;
    Socket socket;
    ProgressDialog pd;
    PrintWriter printwriter;
    BufferedReader bufferedReader;
    String serverReply;
    String points[];
    String nomos[];
    String myNomos;
    String myNomosRank;
    boolean notInTop10 = false;
    ScrollView sv;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top10nomoi);
        table_layout = (TableLayout) findViewById(R.id.top10nomoitable);
        sv = (ScrollView)findViewById(R.id.sv);
        socket = SocketHandler.getSocket();
        pd = ProgressDialog.show(this, "", "Φόρτωση top νομών...");
        requestTop100 rt = new requestTop100();
        rt.execute();
        SharedPreferences prefs = getSharedPreferences("revengePrefs", MODE_PRIVATE);
        myNomos = prefs.getString("nomos", "No username found");//"No name defined" is the default value.
        for(int i =0;i<NOMOICAPS.length;i++){
            if(myNomos.equalsIgnoreCase(NOMOICAPS[i])){
                myNomos = nomoi[i];
                break;
            }
        }
    }

    private void BuildTable(int rows, int cols) {

        // outer for loop
        for (int i = -1; i < rows; i++) {

            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            if(i!=-1) {
                for (int j = 0; j < cols; j++) {
                    switch (j) {
                        case 0:
                            TextView tv0 = new TextView(this);
                            tv0.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                    TableRow.LayoutParams.WRAP_CONTENT));
                            tv0.setPadding(5, 5, 5, 5);
                            tv0.setTextSize(25);
                            if(i!=10) {
                                tv0.setText(Integer.valueOf(i + 1).toString());
                            }else{
                                tv0.setText(myNomosRank);
                            }
                            tv0.setGravity(Gravity.CENTER);
                            row.addView(tv0);
                            break;
                        case 1:
                            TextView tv = new TextView(this);
                            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                    TableRow.LayoutParams.WRAP_CONTENT));
                            tv.setPadding(5, 5, 5, 5);
                            tv.setTextSize(15);
                            if(i!=10) {
                                tv.setText(nomos[i]);
                            }else{
                                tv.setText(myNomos);
                            }
                            tv.setGravity(Gravity.CENTER);
                            row.addView(tv);
                            break;
                        case 2:
                            TextView tv2 = new TextView(this);
                            tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                    TableRow.LayoutParams.WRAP_CONTENT));
                            tv2.setPadding(5, 5, 5, 5);
                            tv2.setText(points[i]);
                            tv2.setTextSize(15);
                            tv2.setGravity(Gravity.CENTER);
                            row.addView(tv2);
                            break;
                    }
                }
            }else{
                for (int j = 0; j < cols; j++) {
                    switch (j) {
                        case 0:
                            TextView tv0 = new TextView(this);
                            tv0.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                    TableRow.LayoutParams.WRAP_CONTENT));
                            tv0.setPadding(5, 5, 5, 5);
                            tv0.setText("#");
                            tv0.setGravity(Gravity.CENTER);
                            row.addView(tv0);
                            break;
                        case 1:
                            TextView tv = new TextView(this);
                            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                    TableRow.LayoutParams.WRAP_CONTENT));
                            tv.setPadding(5, 5, 5, 5);
                            tv.setText("Νομός");
                            tv.setGravity(Gravity.CENTER);
                            row.addView(tv);
                            break;
                        case 2:
                            TextView tv2 = new TextView(this);
                            tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                    TableRow.LayoutParams.WRAP_CONTENT));
                            tv2.setPadding(5, 5, 5, 5);
                            tv2.setText("Πόντοι");
                            tv2.setGravity(Gravity.CENTER);
                            row.addView(tv2);
                            break;
                    }
                }
            }

            if(i!=-1) {
                if(nomos[i].equalsIgnoreCase(myNomos)){
                    row.setBackgroundResource(R.drawable.row_border_user);
                }else {
                    row.setBackgroundResource(R.drawable.row_border);
                }
            }
            row.setTag(i);
            table_layout.addView(row);

        }
    }



    private class requestTop100 extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                printwriter = new PrintWriter(socket.getOutputStream(), true);
                printwriter.println("request_generaltop10nomoi"); // write the message to output stream

                InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
                bufferedReader = new BufferedReader(inputStreamReader);
                do {
                    serverReply = bufferedReader.readLine();
                }while(serverReply==null || !serverReply.contains("generaltop10nomoi:"));

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            serverReply = serverReply.replace("generaltop10nomoi:","");
            String[] perioxesPoints = serverReply.split(Pattern.quote("-*-"));
            if(perioxesPoints.length==11){
                notInTop10=true;
                myNomosRank = perioxesPoints[10].split("666")[0];
            }

            nomos = new String[perioxesPoints.length];
            points = new String[perioxesPoints.length];

            for(int i = 0;i<perioxesPoints.length;i++){
                if(i!=10) {
                    nomos[i] = perioxesPoints[i].split(Pattern.quote("-$-"))[0];
                    points[i] = perioxesPoints[i].split(Pattern.quote("-$-"))[1];
                }else{
                    nomos[i]=myNomos;
                    points[i] = perioxesPoints[10].split("666")[1];
                }
            }

            table_layout.removeAllViews();
            BuildTable(perioxesPoints.length, 3);
            pd.dismiss();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        sv.setBackgroundResource(R.drawable.menu_op);
    }

    @Override
    protected void onStop() {
        super.onStop();
        sv.setBackgroundDrawable(null);
        sv.setBackgroundColor(Color.parseColor("#67d4ff"));
    }
}