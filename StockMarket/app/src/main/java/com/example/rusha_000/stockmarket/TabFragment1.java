package com.example.rusha_000.stockmarket;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class TabFragment1 extends Fragment {
    View v;
    ShareDialog shareDialog;
    CheckBox cb;
    private List<StockDetails> details = new ArrayList<StockDetails>();
    public List<Favourite> favdetails = new ArrayList<Favourite>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v=inflater.inflate(R.layout.tab_fragment_1, container, false);
        FacebookSdk.sdkInitialize(v.getContext());
        CallbackManager callbackManager;

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        populateStockDetails();
        populateListView();
        try {
            getActivity().setTitle(MainActivity.jsonObject.getString("Name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String [] myData = {"blue","black","red","white"};
        String [] Data = new String[2];
        try {
            Data[0]="Symbol\n \n"+MainActivity.jsonObject.getString("Symbol").toString();
            Data[1]=MainActivity.jsonObject.getString("Name").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),R.layout.items,Data);

        ListView list = (ListView)v.findViewById(R.id.listViewMain);
        list.setAdapter(adapter);*/

      /* TextView tv = (TextView) v.findViewById(R.id.textView);
        try {
            tv.setText(MainActivity.jsonObject.getString("Name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
*/


      /*  return inflater.inflate(R.layout.tab_fragment_1, container, false);*/

        Button fb= (Button)v.findViewById(R.id.fbook);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse("https://developers.facebook.com"))
                        .build();
                ShareButton shareButton = (ShareButton)findViewById(R.id.fb_share_button);
                shareButton.setShareContent(content);*/

            try{
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
            }
             catch (JSONException e) {
                e.printStackTrace();
            }
            }
        });

        cb = (CheckBox) v.findViewById(R.id.checkBoxStar);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getContext();
                SharedPreferences sharepref =context.getSharedPreferences("stockdata",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharepref.edit();
                String pushString="",popString="";
                StringBuilder sb = new StringBuilder();

                String [] sep = new String[10];
               String i = "1";
                try {
                    pushString = MainActivity.jsonObject.getString("Symbol")+" "+MainActivity.jsonObject.getString("Title");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(cb.isChecked())
                {
                    try {
                        String check = sharepref.getString("3","");
                        if(check != null)
                        {
                            String[] sp = check.split(",");
                            for (int j = 0; j < sp.length; j++)
                            {
                                sb.append(sp[j]).append(",");
                            }
                            sb.append(MainActivity.jsonObject.getString("Symbol")).append(",");
                            editor.putString("3",sb.toString());
                            editor.commit();
                            //populate list view
                            populatefavlistview();
                            populatefavdetails();
                            popString = sharepref.getString("3","");
                            String[] playlists = popString.split(",");
                        }
                        else
                        {
                            sb.append(MainActivity.jsonObject.getString("Symbol")).append(",");
                            editor.putString("3",sb.toString());
                            editor.commit();
                            populatefavlistview();
                            populatefavdetails();
                            //populate list view
                            popString = sharepref.getString("3","");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    editor.apply();

                   ;
                   /* sep=popString.split(" ");*/
                    Log.d("waaaaaaaaaaaaa",popString);

                   /* t1.setText(sep[1]);*/

                }

                else{
                    Toast.makeText(getContext(),"hello",Toast.LENGTH_SHORT).show();


                    for(int x =0; x< MainActivity.lf.getCount();x++)
                    {
                        View view = MainActivity.lf.getChildAt(x);
                        MainActivity.lf.removeView(view);

                    }
                }
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
                favdetails.add(new Favourite(favstring[0],favstring[1],favstring[2],favstring[8],favstring[5]));




            }

            private void populatefavlistview () {
                ArrayAdapter<Favourite> ada = new MyListAdapter();




                MainActivity.lf.setAdapter(ada);

                int z = MainActivity.lf.getCount();
                int s = MainActivity.lf.getChildCount();

            }

                 class MyListAdapter extends ArrayAdapter<Favourite> {
                public MyListAdapter() {
                    super(getActivity(),R.layout.favourites,R.id.favsymbol,favdetails);
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
                    makechange.setText(currentCar.getChange());

                    // Year:





                    return  iView;
                }
            }


        });

        int zz = MainActivity.lf.getCount();
        int ss = MainActivity.lf.getChildCount();




        return  v;


    }


    private void populateStockDetails() {
        String [] temp= new String[15];
        try {
             temp[0] = MainActivity.jsonObject.getString("Symbol").toString();
             temp[1] = MainActivity.jsonObject.getString("Name").toString();
             temp[2] = MainActivity.jsonObject.getString("LastPrice").toString();

             temp[11] = MainActivity.jsonObject.getString("Change").toString();
             temp[3] = MainActivity.jsonObject.getString("ChangePercent").toString();
            String s1 = String.format("%.2f", Float.parseFloat(MainActivity.jsonObject.getString("ChangePercent")));
            String s = String.format("%.2f", Float.parseFloat(MainActivity.jsonObject.getString("Change")));

            temp[12] =  s+"("+s1.toString()+"%)";

             temp[4] = MainActivity.jsonObject.getString("TimeStamp").toString();
             temp[5] = MainActivity.jsonObject.getString("ChangeYTD").toString();
             temp[6] = MainActivity.jsonObject.getString("MarketCap").toString();
             temp[7] = MainActivity.jsonObject.getString("High").toString();
             temp[8] = MainActivity.jsonObject.getString("Low").toString();
             temp[9] = MainActivity.jsonObject.getString("Volume").toString();
             temp[10] = MainActivity.jsonObject.getString("Open").toString();
             temp[13] = MainActivity.jsonObject.getString("ChangePercentYTD").toString();
             String c1=String.format("%.2f", Float.parseFloat(MainActivity.jsonObject.getString("ChangeYTD")));
                    String c2=String.format("%.2f", Float.parseFloat(MainActivity.jsonObject.getString("ChangePercentYTD")));
             temp[14] = c1+"("+c2.toString()+"%)";

        } catch (JSONException e) {
            e.printStackTrace();
        }
        details.add(new StockDetails("NAME",temp[1],0));
        details.add(new StockDetails("SYMBOL",temp[0],0));
        details.add(new StockDetails("LASTPRICE",temp[2],0));

        try {
            if(Float.parseFloat(MainActivity.jsonObject.getString("Change"))>=0)
            {
                details.add(new StockDetails("CHANGE",temp[12],R.mipmap.ic_up_new));
            }
            else
            {
                details.add(new StockDetails("CHANGE",temp[12],R.mipmap.ic_down_new));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        details.add(new StockDetails("TimeStamp",temp[4],0));

        try {
            Float check =Float.parseFloat(MainActivity.jsonObject.getString("MarketCap"));

            if(check>=1000000000)
            {

                String ts = String.format("%.2f",( Float.parseFloat(MainActivity.jsonObject.getString("MarketCap")))/1000000000);

                String fmat = ts+" Billion";
                details.add(new StockDetails("MARKETCAP", fmat, 0));
            }
            else if(Float.parseFloat(MainActivity.jsonObject.getString("MarketCap"))>=1000000)
            {
                String ts = String.format("%.2f",( Float.parseFloat(MainActivity.jsonObject.getString("MarketCap")))/1000000);
                String fmat = ts+" Million";
                details.add(new StockDetails("MARKETCAP", fmat, 0));
            }
            else
            {
                details.add(new StockDetails("MARKETCAP", temp[6], 0));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*String vol = temp[9]+*/
        details.add(new StockDetails("Volume",temp[9],0));

        try {
            if(Float.parseFloat(MainActivity.jsonObject.getString("ChangePercentYTD"))>=0)
            {
                details.add(new StockDetails("CHANGEYTD",temp[14],R.mipmap.ic_up_new));
            }
            else
            {
                details.add(new StockDetails("CHANGEYTD",temp[14],R.mipmap.ic_down_new));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        /*details.add(new StockDetails("ChangeYTD",temp[5],R.drawable.ic_name));*/
        details.add(new StockDetails("HIGH",temp[7],0));
        details.add(new StockDetails("LOW",temp[8],0));
        details.add(new StockDetails("OPEN",temp[10],0));
       /* details.add(new StockDetails("Change",temp[11],R.drawable.ic_name));*/
    }

    private void populateListView() {
        ArrayAdapter<StockDetails> adapter = new MyListAdapter();
        ListView list = (ListView)v.findViewById(R.id.listViewMain);
        TextView textView = new TextView(getContext());
        textView.setHeight(80);
        textView.setTextSize(30);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setText("Stock Details");
        list.addHeaderView(textView);
        Bitmap bit = null;
        String ur = null;
        ImageView imageViewYahoo = new ImageView(getContext());

        try {
             ur="http://chart.finance.yahoo.com/t?s="+MainActivity.jsonObject.getString("Symbol")+"&lang=en-US&width=400&height=300";

             }
         catch (JSONException e) {
            e.printStackTrace();
        }


        class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
            ImageView bmImage;
            public DownloadImageTask(ImageView bmImage) {
                this.bmImage = bmImage;
            }

            protected Bitmap doInBackground(String... urls) {
                String urldisplay = urls[0];
                Bitmap mIcon11 = null;
                try {
                    InputStream in = new java.net.URL(urldisplay).openStream();
                    mIcon11 = BitmapFactory.decodeStream(in);
                } catch (Exception e) {

                    e.printStackTrace();
                }
                return mIcon11;
            }

            protected void onPostExecute(Bitmap result) {
                bmImage.setImageBitmap(result);
            }
        }
        new  DownloadImageTask(imageViewYahoo) .execute(ur);
       /* imageViewYahoo.setImageBitmap(bit);*/
        TextView t = new TextView(getContext());
        t.setText("Today's Stock Activity");
        t.setTextSize(20);
        list.addFooterView(t);
        list.addFooterView(imageViewYahoo);
        list.setAdapter(adapter);

    }


    private class MyListAdapter extends ArrayAdapter<StockDetails> {
        public MyListAdapter() {
            super(getActivity(),R.layout.details_layout,R.id.textView4,details);
        }
        @Override
        public View getView(int position,View convertView,ViewGroup parent)
        {   LayoutInflater mInflater;
            Context context=getContext();
            mInflater = LayoutInflater.from(context);
            View itemView = convertView;
            if(itemView == null)
            {   itemView = mInflater.inflate(R.layout.details_layout, null);
                /*itemView.mI.inflate(R.layout.details_layout,parent,false);*/
            }

            StockDetails currentCar = details.get(position);

            // Fill the view
            ImageView imageView = (ImageView)itemView.findViewById(R.id.imageView1);
            imageView.setImageResource(currentCar.getIconID());

            // Make:
            TextView makeText = (TextView) itemView.findViewById(R.id.textView4);
            makeText.setText(currentCar.getTitle());

            // Year:
            TextView yearText = (TextView) itemView.findViewById(R.id.textView5);
            yearText.setText("" + currentCar.getText());




            return  itemView;
        }
    }


}

