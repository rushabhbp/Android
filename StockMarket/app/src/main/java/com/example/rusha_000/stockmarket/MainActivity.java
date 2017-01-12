package com.example.rusha_000.stockmarket;

import android.content.Context;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;
import java.sql.BatchUpdateException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

public class MainActivity extends AppCompatActivity {


    private TextView tdata;
    AutoCompleteTextView auto;
    AlertDialog.Builder alertDialog;
    String [] appendUrl= new String[3];
    private static FragmentManager fragmentManager;
    public  static  JSONObject jsonObject= null;
    public  static  JSONObject  favjsonObject= null;
    public  static  JSONObject  tempfavjsonObject= null;
    String [] symarray = new String[10];
    public static List<Favourite> favdetails = new ArrayList<Favourite>();
    public static ListView lf ;
    public  static  JSONObject  [] finalJsonarray = null;
    public static  int back = 0;
   public static ArrayAdapter<Favourite> ada ;
    GestureDetector gestureDetector;
    TouchListener onTouchListener;
    int undo=0;


        int flag=0;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_home_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        lf=(ListView) findViewById(R.id.listViewfav);
        setTitle("Stock Market Viewer");
        fragmentManager = getSupportFragmentManager();//Get Fragment Manager
        ada = new MyListAdapter();

        frontFavView();



        gestureDetector = new GestureDetector(this, new GestureListener());
        onTouchListener = new TouchListener();
        lf.setOnTouchListener(onTouchListener);
        Button getQuote = (Button)findViewById(R.id.quote);

        context = getBaseContext();
        auto = (AutoCompleteTextView)findViewById(R.id.autoComplete);
        auto.setThreshold(3);
        
        auto.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public  void   afterTextChanged(Editable s) {
                String text = auto.getText().toString();
                if(text=="")return ;
                if(text.length()>2)
                {
                    String appendUrl ="http://webstock-env.us-west-2.elasticbeanstalk.com/?input="+text;
                    new lookupTask().execute(appendUrl);
                }
              }
        });
        auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String)parent.getItemAtPosition(position);
                String[] separated = selection.split(" ");
                auto.setText(separated[0]);
                auto.setAdapter(null);
                auto.dismissDropDown();

            }
        });

        ImageButton imb = (ImageButton)findViewById(R.id.refresh);
        imb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=1;
                int cc=MainActivity.lf.getCount();
                if(cc==0 )
                    return;
                frontFavView();
            }
        });





        getQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(MainActivity.this);
                String text = auto.getText().toString();
                if(auto.getText().toString().equals(""))
                {
                    alertDialog.setMessage("Please enter a Stock Name/Symbol");
                    alertDialog.setCancelable(true);
                    alertDialog.setPositiveButton(
                            "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    alertDialog.show();
                    return;

                }
                auto.setAdapter(null);
                auto.dismissDropDown();
                auto.clearFocus();

                appendUrl[0] =   "http://webstock-env.us-west-2.elasticbeanstalk.com/?symbol="+text;

                new quoteTask().execute(appendUrl);

            }
        });

        Button clr = (Button)findViewById(R.id.clear);
        clr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auto.setText("");
            }
        });
        lf.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // selected item
                String selected = ((TextView) view.findViewById(R.id.favsymbol)).getText().toString();

               /* Toast toast = Toast.makeText(getApplicationContext(), selected, Toast.LENGTH_SHORT);
                toast.show();*/
                appendUrl[0] =   "http://webstock-env.us-west-2.elasticbeanstalk.com/?symbol="+selected;

                new quoteTask().execute(appendUrl);
            }
        });


    }

    private void frontFavView() {


        SharedPreferences sharepref = getSharedPreferences("stockdata", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharepref.edit();
        String pushString = "", popString = "";
        StringBuilder sb = new StringBuilder();
        editor.putString("55","");
        String[] sep = new String[10];
       /* String i = "1";
        try {
            pushString = MainActivity.jsonObject.getString("Symbol")+" "+MainActivity.jsonObject.getString("Title");
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        /*if(cb.isChecked())
        {
           */
        String check = sharepref.getString("55", null);
        int ccheck =0;
        if(check==null)
            return;
        ccheck = check.length();


        if(check.startsWith(","))
        {
            check =     check.substring(1,check.length());
            ccheck = check.length();
        }
        if (check != null && ccheck!=0 && check.equalsIgnoreCase(",")!=true &&check.equalsIgnoreCase("\"")!=true  )
        {
            String[] sp = check.split(",");
            new favprefTask().execute(sp);
        }
    }

    public class favprefTask extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String[] params) {
            String splitarray = params[0];

            HttpURLConnection connection = null;

            BufferedReader reader = null;

            try {
                symarray = new String[10];
                for (int count = 0; count < params.length; count++) {
                    StringBuffer buffer = new StringBuffer();
                    String appendUrl = "http://webstock-env.us-west-2.elasticbeanstalk.com/?symbol=" + params[count];
                    URL url = new URL(appendUrl);

                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();

                    InputStream stream = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    String line = "";

                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);

                    }
                    symarray[count] = (buffer.toString());
                    {
                        if (connection != null) {
                            connection.disconnect();
                        }

                        try {
                            if (reader != null) {
                                reader.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
                return symarray.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }/*catch (JSONException e){
                e.printStackTrace();
            }*/
            return "quote";
        }


        @Override
        protected void onPostExecute(String result) {
            if (result == null) return;
            super.onPostExecute(result);
            int len = symarray.length;
            int count = 0;
            while (symarray[count] != null) {

                try {
                    favjsonObject = new JSONObject(symarray[count]);
                    Log.d("hello", "hello");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (flag == 0) {
                    populatefavdetails();
                    populatefavlistview();
                    ada.notifyDataSetChanged();
                } else {
                    int cc=MainActivity.lf.getCount();
                    SharedPreferences sharepref = getSharedPreferences("stockdata", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharepref.edit();
                    String pushString = "", popString = "";
                    StringBuilder sb = new StringBuilder();
                    for (int cou = 0; cou < cc; cou++) {
                        Favourite obj = (Favourite) MainActivity.lf.getItemAtPosition(cou);

                        try {
                            if(obj.getSymbol().equalsIgnoreCase(favjsonObject.getString("Symbol")))
                            {
                               obj.setPrice(favjsonObject.getString("Symbol"));
                                //obj.setPrice("hi");
                               obj.setChange(favjsonObject.getString("Change"));
                               // obj.setChange("9");
                                /*populatefavdetails();
                                populatefavlistview();*/
                                ada.notifyDataSetChanged();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }
                count++;

            }

        }
    }

    private void populatefavdetails() {
        String[] favstring = new String[15];
        try {
            favstring[0] = MainActivity.favjsonObject.getString("Name").toString();
            favstring[1] = MainActivity.favjsonObject.getString("Symbol").toString();
            favstring[2] = "$ "+MainActivity.favjsonObject.getString("LastPrice").toString();
            favstring[3] = MainActivity.favjsonObject.getString("MarketCap").toString();
            favstring[4] = MainActivity.favjsonObject.getString("Change").toString();
            favstring[5] = String.format("%.2f", Float.parseFloat(MainActivity.favjsonObject.getString("ChangePercent")))+"%";

            favstring[6] = favstring[4]+"("+favstring[5]+"%)";

            Float check =Float.parseFloat(MainActivity.favjsonObject.getString("MarketCap"));

            if(check>=1000000000)
            {

                String ts = String.format("%.2f",( Float.parseFloat(MainActivity.favjsonObject.getString("MarketCap")))/1000000000);

                favstring[7] = ts+" Billion";

            }
            else if(Float.parseFloat(MainActivity.favjsonObject.getString("MarketCap"))>=1000000)
            {
                String ts = String.format("%.2f",( Float.parseFloat(MainActivity.favjsonObject.getString("MarketCap")))/1000000);
                favstring[7] = ts+" Million";

            }
            else
            {
                favstring[7]=favstring[3];
            }

            favstring[8]= "Market Cap : "+favstring[7];




        } catch (JSONException e) {
            e.printStackTrace();
        }
        favdetails.add(new Favourite(favstring[0],favstring[1],favstring[5],favstring[8],favstring[2]));




    }

    private void populatefavlistview () {

    /*MyListAdapter();*/



        MainActivity.lf.setAdapter(ada);

        int z = MainActivity.lf.getCount();
        int s = MainActivity.lf.getChildCount();

    }


    class MyListAdapter extends ArrayAdapter<Favourite> {
        public MyListAdapter() {
            super(getBaseContext(),R.layout.favourites,R.id.favsymbol,favdetails);
        }
        @Override
        public View getView(int position,View convertView,ViewGroup parent)
        {   LayoutInflater mInflater;
            Context context=getContext();
            mInflater = LayoutInflater.from(context);
            View iView = convertView;
            if(iView == null)
            {   iView = mInflater.inflate(R.layout.favourites, null);
                /*itemView.mI.inflate(R.layout.details_layout,parent,false);*/
            }

            Favourite currentCar = favdetails.get(position);

            // Fill the view
            // Make:

            /*maketitle.setText(currentCar.getTitle());*/

            TextView makename = (TextView) iView.findViewById(R.id.favname);
            makename.setText(currentCar.getCname());
            TextView makesym = (TextView) iView.findViewById(R.id.favsymbol);
            makesym.setText(currentCar.getSymbol());
            TextView makeprice = (TextView) iView.findViewById(R.id.favprice);
            makeprice.setText(currentCar.getPrice());
            TextView makecap = (TextView) iView.findViewById(R.id.favcap);
            makecap.setText(currentCar.getMcap());
            TextView makechange= (TextView) iView.findViewById(R.id.favchange);
            String per[] = currentCar.getChange().split("%");
            if(Float.parseFloat(per[0])>0)
            {
                makechange.setBackgroundColor(Color.GREEN);
            }
            else if (Float.parseFloat(per[0])<0)
            {
                makechange.setBackgroundColor(Color.RED);
            }
            makechange.setText(currentCar.getChange());

            // Year:





            return  iView;
        }
    }






  /*  private void populatefavdetails() {
        String[] favstring = new String[15];
        try {
            favstring[0] = MainActivity.jsonObject.getString("Name").toString();
            favstring[1] = MainActivity.jsonObject.getString("Symbol").toString();
            favstring[2] = "$ "+MainActivity.jsonObject.getString("LastPrice").toString();
            favstring[3] = MainActivity.jsonObject.getString("MarketCap").toString();
            favstring[4] = MainActivity.jsonObject.getString("Change").toString();
            favstring[5] = String.format("%.2f", Float.parseFloat(MainActivity.jsonObject.getString("ChangePercent")))+"%";

            favstring[6] = favstring[4]+"("+favstring[5]+"%)";

            Float check =Float.parseFloat(MainActivity.jsonObject.getString("MarketCap"));

            if(check>=1000000000)
            {

                String ts = String.format("%.2f",( Float.parseFloat(MainActivity.jsonObject.getString("MarketCap")))/1000000000);

                favstring[7] = ts+" Billion";

            }
            else if(Float.parseFloat(MainActivity.jsonObject.getString("MarketCap"))>=1000000)
            {
                String ts = String.format("%.2f",( Float.parseFloat(MainActivity.jsonObject.getString("MarketCap")))/1000000);
                favstring[7] = ts+" Million";

            }
            else
            {
                favstring[7]=favstring[3];
            }

            favstring[8]= "Market Cap : "+favstring[7];




        } catch (JSONException e) {
            e.printStackTrace();
        }
        favdetails.add(new Favourite(favstring[0],favstring[1],favstring[2],favstring[8],favstring[5]));


    }*/


    public class quoteTask extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String[] params) {
            HttpURLConnection connection = null;
            StringBuffer buffer = new StringBuffer();
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);

                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line);

                }
                String finalJson = buffer.toString();

                return finalJson;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }/*catch (JSONException e){
                e.printStackTrace();
            }*/ finally {
                if (connection != null) {
                    connection.disconnect();
                }

                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return "quote";
        }




        @Override
        protected void onPostExecute(String result) {
            if(result==null)return;
            super.onPostExecute(result);


            JSONArray jsonArray = null;
            try {
                jsonObject =new JSONObject(result);
                if(jsonObject.getString("Status").equalsIgnoreCase("SUCCESS") == false)
                {
                    alertDialog.setMessage("Please enter a valid Stock Name/Symbol");
                    alertDialog.setCancelable(true);
                    alertDialog.setPositiveButton(
                            "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    alertDialog.show();
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            /*if(jsonArray==null)return;
            String[] strArr = new String[jsonArray.length()];
            String[] autoString = new String[jsonArray.length()];*/


            /*tdata.setText(jsonObject.toString());*/
            TextView textView = (TextView)findViewById(R.id.textView);
            Intent intent = new Intent(MainActivity.this,ResultActivity.class);
            intent.putExtra("data",jsonObject.toString());



            startActivity(intent);
        }

    }


    public class lookupTask extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String []params) {
            HttpURLConnection connection=null;
            StringBuffer buffer = new StringBuffer();
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);

                connection = (HttpURLConnection)url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                String line="";

                while((line=reader.readLine())!=null)
                {
                    buffer.append(line);

                }
                String finalJson=buffer.toString();

                 return finalJson;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }/*catch (JSONException e){
                e.printStackTrace();
            }*/
            finally {
                if(connection!=null){
                    connection.disconnect();
                }

                try {
                    if(reader!=null)
                    {reader.close();}
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }



           return "hi";
        }

        @Override
        protected void onPostExecute(String result) {
            if(result==null)return;
            super.onPostExecute(result);
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(jsonArray==null)return;
            String[] strArr = new String[jsonArray.length()];
            String[] autoString = new String[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    strArr[i] = jsonArray.getString(i);
                    autoString = strArr[i].split("-");
                    strArr[i]=autoString[0]+"\n"+autoString[1].trim();
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                    android.R.layout.select_dialog_item, strArr);

            auto.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            if (!auto.isPopupShowing()) {
                auto.showDropDown();
            }

           /* tdata.setText(strArr[0]);*/
        }
    }
    protected class GestureListener extends GestureDetector.SimpleOnGestureListener
    {
        private static final int SWIPE_MIN_DISTANCE = 150;
        private static final int SWIPE_MAX_OFF_PATH = 100;
        private static final int SWIPE_THRESHOLD_VELOCITY = 100;

        private MotionEvent mLastOnDownEvent = null;


        @Override
        public boolean onDown(MotionEvent e)
        {
            mLastOnDownEvent = e;
            return super.onDown(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
        {
            if(e1 == null){
                e1 = mLastOnDownEvent;
            }
            if(e1==null || e2==null){
                return false;
            }
            int id = lf.pointToPosition((int) e1.getX(), (int) e1.getY());
            Favourite obj =  (Favourite) ada.getItem((id));
            float dX = e2.getX() - e1.getX();
            float dY = e1.getY() - e2.getY();

            if (Math.abs(dY) < SWIPE_MAX_OFF_PATH && Math.abs(velocityX) >= SWIPE_THRESHOLD_VELOCITY && Math.abs(dX) >= SWIPE_MIN_DISTANCE )
            {
                if (dX > 0) {

                } else {

                   /* alertDialog = new AlertDialog.Builder(MainActivity.this);
                        alertDialog.setMessage("ARE YOU SURE?");
                        alertDialog.setCancelable(true);
                        alertDialog.setPositiveButton(
                                "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        undo = 1;
                                        dialog.cancel();
                                    }
                                });
                        alertDialog.setNegativeButton(
                                "CANCEL",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        undo = 0;
                                        dialog.cancel();
                                    }
                                });
                        alertDialog.show();



                    if(undo == 1)*/
                    {
                    String Selected = obj.getSymbol();
                    ada.remove(obj);
                    ada.notifyDataSetChanged();
                    /*sharedPreference.removeFavorite(getApplicationContext(),id);*/
                    SharedPreferences sharepref = getSharedPreferences("stockdata", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharepref.edit();
                    String pushString = "", popString = "";
                    StringBuilder sb = new StringBuilder();
                    String check = sharepref.getString("55", null);

                    if (check != null && check.equalsIgnoreCase(",") != true) {
                        String[] sp = check.split(",");
                        for (int j = 0; j < sp.length; j++) {

                            if (sp[j].equalsIgnoreCase(Selected))
                                continue;
                            sb.append(sp[j]).append(",");
                        }
                        editor.putString("55", sb.toString());
                        editor.commit();
                        //populate list view

                        popString = sharepref.getString("55", "");

                    }
                }

                }
                return true;
            }
            return false;
        }
    }

    protected class TouchListener implements View.OnTouchListener
    {
        @Override
        public boolean onTouch(View v, MotionEvent e)
        {
            if (gestureDetector.onTouchEvent(e)){
                return true;
            }else{
                return false;
            }
        }
    }



}
