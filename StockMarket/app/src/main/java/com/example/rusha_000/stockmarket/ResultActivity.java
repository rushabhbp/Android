package com.example.rusha_000.stockmarket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.util.ArrayList;
import java.util.List;


public class ResultActivity extends AppCompatActivity {
    String tString;
    ShareDialog shareDialog;
    CallbackManager callbackManager;
    public  static MenuItem fbitem ;

    public static ArrayAdapter<Favourite> ada ;
    static int flag =1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ada = new MyListAdapter();

        FacebookSdk.sdkInitialize(this);

        /*favcheck(fbitem);*/
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                if (result.getPostId() != null) {
                    Log.d("DEBUG", "SUCESS: " + result.getPostId());
                    Toast.makeText(getApplicationContext(), "Posted successfully",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Post cancelled",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancel() {

                Toast.makeText(getApplicationContext(), "Post cancelled",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {

                Toast.makeText(getApplicationContext(), "Error while posting",
                        Toast.LENGTH_SHORT).show();
            }
        });




        Intent intent = getIntent();
        tString = intent.getStringExtra("data");
        /*getSupportActionBar().setHomeButtonEnabled(true);*/
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("CURRENT"));
        tabLayout.addTab(tabLayout.newTab().setText("HISTORICAL"));
        tabLayout.addTab(tabLayout.newTab().setText("NEWS"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void favcheck(MenuItem item) {

        SharedPreferences sharepref = getSharedPreferences("stockdata", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharepref.edit();
        String pushString = "", popString = "";
        StringBuilder sb = new StringBuilder();

        /*String[] sep = new String[10];*/
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
        if (check != null) {
            String[] sp = check.split(",");
            for (int i = 0; i < sp.length; i++) {
                try {
                    if (sp[i].equalsIgnoreCase(MainActivity.jsonObject.getString("Symbol"))) {
                        item.setIcon(android.R.drawable.btn_star_big_on);
                        item.setChecked(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ress_bar, menu);
        fbitem = menu.getItem(0);
        favcheck(menu.getItem(0));
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        favcheck(menu.getItem(0));
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        switch (id){
            case android.R.id.home:
                /*NavUtils.navigateUpFromSameTask(this);
                MainActivity.back = 1;*/
                this.finish();
                return true;

            case R.id.face:try
            {
                String fb = MainActivity.jsonObject.getString("Symbol").toString();
               /* URI uri = Uri.parse("http://chart.finance.yahoo.com/t?s=aapl&lang=en-US&width=400&height=300");*/
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setContentTitle("Current Stock Price of "+MainActivity.jsonObject.getString("Name").toString()+","+MainActivity.jsonObject.getString("LastPrice").toString())
                            .setContentDescription(
                                    "Stock Information of "+MainActivity.jsonObject.getString("Name").toString())


                            .setImageUrl(Uri.parse("http://chart.finance.yahoo.com/t?s="+fb+"&lang=en-US&width=200&height=150"))
                            .build();

                    shareDialog.show(linkContent);
                }
                break;
            }
            catch (JSONException e) {
                e.printStackTrace();
                break;
            }
            case R.id.add_fav:

                if(!item.isChecked())
                {

                    SharedPreferences sharepref =getSharedPreferences("stockdata",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharepref.edit();
                    String pushString="",popString="";
                    StringBuilder sb = new StringBuilder();

                    String [] sep = new String[10];
                    String i = "1";
                    item.setChecked(true);
                    item.setIcon(android.R.drawable.btn_star_big_on);
                    flag=0;
                    try {
                        String check = sharepref.getString("55",null);
                        if(check!=null)
                        { if(check.startsWith(","))
                        {
                            check =     check.substring(1,check.length());
                        }}
                        if(check != null && check.equalsIgnoreCase("\"")!=true)
                        {
                            String[] sp = check.split(",");
                            for (int j = 0; j < sp.length; j++)
                            {
                                sb.append(sp[j]).append(",");
                            }
                            sb.append(MainActivity.jsonObject.getString("Symbol")).append(",");
                            editor.putString("55",sb.toString());
                            editor.commit();
                            //populate list view
                            populatefavlistview();
                            populatefavdetails();
                            MainActivity.ada.notifyDataSetChanged();

                            popString = sharepref.getString("55","");
                            String[] playlists = popString.split(",");
                        }
                        else
                        {


                            sb.append(MainActivity.jsonObject.getString("Symbol")).append(",");
                            editor.putString("55",sb.toString());
                            editor.commit();
                            populatefavlistview();
                            populatefavdetails();
                            MainActivity.ada.notifyDataSetChanged();

                            //populate list view
                            popString = sharepref.getString("55","");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    editor.apply();

                }
                else
                {
                    item.setChecked(false);
                    item.setIcon(android.R.drawable.btn_star_big_off);
                    int cc=MainActivity.lf.getCount();
                    SharedPreferences sharepref =getSharedPreferences("stockdata",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharepref.edit();
                    String pushString="",popString="";
                    StringBuilder sb = new StringBuilder();
                    for(int count=0;count<cc;count++)
                    {
                      Favourite obj =  (Favourite)MainActivity.lf.getItemAtPosition(count);
                        try {
                            if(obj.getSymbol().equalsIgnoreCase(MainActivity.jsonObject.getString("Symbol")))
                            {

                              MainActivity.ada.remove(obj);
                                MainActivity.ada.notifyDataSetChanged();
                                flag=1;
                                String check = sharepref.getString("55",null);
                                if(check != null && check.equalsIgnoreCase(",")!=true)
                                {
                                    String[] sp = check.split(",");
                                    for (int j = 0; j < sp.length; j++)
                                    {

                                        if(sp[j].equalsIgnoreCase(MainActivity.jsonObject.getString("Symbol")))
                                            continue;
                                        sb.append(sp[j]).append(",");
                                    }
                                    editor.putString("55",sb.toString());
                                    editor.commit();
                                    //populate list view

                                    popString = sharepref.getString("55","");
                                    break;
                                }



                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    flag=1;



                }

        }


        return super.onOptionsItemSelected(item);
    }
    private void populatefavdetails() {
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
        MainActivity.favdetails.add(new Favourite(favstring[0],favstring[1],favstring[5],favstring[8],favstring[2]));




    }

    private void populatefavlistview () {




        MainActivity.lf.setAdapter(MainActivity.ada);

        int z = MainActivity.lf.getCount();
        int s = MainActivity.lf.getChildCount();

    }

    class MyListAdapter extends ArrayAdapter<Favourite> {
        public MyListAdapter() {
            super(getBaseContext(),R.layout.favourites,R.id.favsymbol,MainActivity.favdetails);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {   LayoutInflater mInflater;
            Context context=getContext();
            mInflater = LayoutInflater.from(context);
            View iView = convertView;
            if(iView == null)
            {   iView = mInflater.inflate(R.layout.favourites, null);
                /*itemView.mI.inflate(R.layout.details_layout,parent,false);*/
            }

            Favourite currentCar = MainActivity.favdetails.get(position);

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




}


