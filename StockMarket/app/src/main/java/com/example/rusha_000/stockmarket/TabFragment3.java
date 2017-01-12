package com.example.rusha_000.stockmarket;


        import android.content.Context;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Color;
        import android.graphics.Typeface;
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
        import android.widget.AutoCompleteTextView;
        import android.widget.ImageView;
        import android.widget.ListView;
        import android.widget.TextView;
        import org.json.JSONObject;
        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.io.UnsupportedEncodingException;
        import java.net.HttpURLConnection;
        import java.net.MalformedURLException;
        import java.net.URL;
        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Date;
        import java.util.List;
        import java.util.Locale;

public class TabFragment3 extends Fragment {
    View v;
    static  InputStream is = null;
    static JSONObject one=null,two=null,three=null,four=null;
    private List<NewsFeed> newsdetails = new ArrayList<NewsFeed>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.tab_fragment_3, container, false);
        AutoCompleteTextView autoFrag3 = (AutoCompleteTextView)v.findViewById(R.id.autoComplete);
        /*String text = autoFrag3.getText().toString();*/

        /*if(text.length()>2)
        {
            String appendUrl ="http://webstock-env.us-west-2.elasticbeanstalk.com/?sym=aapl";
            new newsTask().execute(appendUrl);
        }*/
        String appendUrl = null;
        try {
            appendUrl = "http://webstock-env.us-west-2.elasticbeanstalk.com/?sym="+ MainActivity.jsonObject.getString("Symbol");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new newsTask().execute(appendUrl);

        return v;
    }



    public class newsTask extends AsyncTask<String,String,String> {

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
                is = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));
                String line="";

                while((line=reader.readLine())!=null)
                {
                    buffer.append(line);

                }

                String finalJson=buffer.toString();
                String hope = finalJson.substring(finalJson.indexOf("{"), finalJson.lastIndexOf("}") + 1) ;
               /* String hope = finalJson.substring(finalJson.indexOf("{"), finalJson.length() - 1) ;*/
                String hopjson =hope.replaceAll("\\\\", "");

                JSONObject newsObj =  new JSONObject(hopjson);
                return newsObj.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }/*catch (JSONException e){
                e.printStackTrace();
            }*/ catch (JSONException e) {
                e.printStackTrace();
            }
           /* finally {
                if(connection!=null){
                    connection.disconnect();
                }

                try {
                    if(reader!=null)
                    {reader.close();}
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }*/



            return "hi";
        }

        @Override
        protected void onPostExecute(String result) {
            if(result==null)return;
            super.onPostExecute(result);
            String tempresult = result;
            JSONObject newsObj =null,tempobj=null,t=null;

           /* JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(stringToParse);*/
            JSONArray jsonArray = null,temparrray=null;

            List<String> allNames = new ArrayList<String>();
            try {
                 newsObj = new JSONObject(result);
                tempobj=newsObj.getJSONObject("d");
                jsonArray = tempobj.getJSONArray("results");


                JSONArray cast = tempobj.getJSONArray("results");
                one = cast.getJSONObject(0);
                two = cast.getJSONObject(1);
                three = cast.getJSONObject(2);
                four = cast.getJSONObject(3);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            populateNewsdetails();
            populateListView();

            /*TextView fragtext = (TextView)v.findViewById(R.id.textViewn);
                fragtext.setText(allNames.toString());
*/





           /* tdata.setText(strArr[0]);*/
        }
    }

    private void populateNewsdetails() {
        String []onetemp = new String[20] ;
        String tempdate="";
        SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy, hh:mm:ss a", Locale.US);
        try {

            onetemp[0]= one.getString("Title").toString();
            onetemp[1]= one.getString("Description").toString();
            onetemp[2]= one.getString("Url").toString();
            onetemp[3]= "Publisher : "+one.getString("Source").toString();

            tempdate=one.getString("Date").toString();
            Date date = format.parse(tempdate);
             onetemp[4]= "Date : "+ sdf.format(date);

            onetemp[5]= two.getString("Title").toString();
            onetemp[6]= two.getString("Description").toString();
            onetemp[7]= two.getString("Url").toString();
            onetemp[8]= "Publisher : "+two.getString("Source").toString();

            tempdate=two.getString("Date").toString();
            date = format.parse(tempdate);
            onetemp[9]= "Date : "+ sdf.format(date);

            onetemp[10]= three.getString("Title").toString();
            onetemp[11]= three.getString("Description").toString();
            onetemp[12]= three.getString("Url").toString();
            onetemp[13]= "Publisher : "+three.getString("Source").toString();

            tempdate=three.getString("Date").toString();
            date = format.parse(tempdate);
            onetemp[14]= "Date : "+ sdf.format(date);

            onetemp[15]= four.getString("Title").toString();
            onetemp[16]= four.getString("Description").toString();
            onetemp[17]= four.getString("Url").toString();
            onetemp[18]= "Publisher : "+four.getString("Source").toString();

            tempdate=four.getString("Date").toString();
            date = format.parse(tempdate);
            onetemp[19]= "Date : "+ sdf.format(date);


            newsdetails.add(new NewsFeed(onetemp[0].toString(),onetemp[1],onetemp[2],onetemp[3],onetemp[4]));
            newsdetails.add(new NewsFeed(onetemp[5].toString(),onetemp[6],onetemp[7],onetemp[8],onetemp[9]));
            newsdetails.add(new NewsFeed(onetemp[10].toString(),onetemp[11],onetemp[12],onetemp[13],onetemp[14]));
            newsdetails.add(new NewsFeed(onetemp[15].toString(),onetemp[16],onetemp[17],onetemp[18],onetemp[19]));
         /*   TextView tv =(TextView)v.findViewById(R.id.title);
            tv.setClickable(true);
            tv.setMovementMethod(LinkMovementMethod.getInstance());
            String text = "<a href='http://www.google.com'> Google </a>";
            tv.setText(Html.fromHtml(text));
            */
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void populateListView() {
        ArrayAdapter<NewsFeed> ada = new MyListAdapter();
        ListView l = (ListView)v.findViewById(R.id.listViewNews);
       /* TextView textView = new TextView(getContext());
        textView.setHeight(60);
        textView.setTextSize(25);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setText("News Feed");
        l.addHeaderView(textView);*/
        l.setAdapter(ada);

    }


    private class MyListAdapter extends ArrayAdapter<NewsFeed> {
        public MyListAdapter() {
            super(getActivity(),R.layout.news,R.id.title,newsdetails);
        }
        @Override
        public View getView(int position,View convertView,ViewGroup parent)
        {   LayoutInflater mInflater;
            Context context=getContext();
            mInflater = LayoutInflater.from(context);
            View iView = convertView;
            if(iView == null)
            {   iView = mInflater.inflate(R.layout.news, null);
                /*itemView.mI.inflate(R.layout.details_layout,parent,false);*/
            }

            NewsFeed currentCar = newsdetails.get(position);

            // Fill the view
                       // Make:
            TextView maketitle = (TextView) iView.findViewById(R.id.title);
            maketitle.setText(Html.fromHtml("<a href="+currentCar.getLink()+">"+currentCar.getTitle()));
            maketitle.setMovementMethod(LinkMovementMethod.getInstance());
            maketitle.setLinkTextColor(Color.BLACK);
            /*maketitle.setText(currentCar.getTitle());*/

            TextView makedesc = (TextView) iView.findViewById(R.id.desc);
            makedesc.setText(currentCar.getDesc());
            TextView makepublisher = (TextView) iView.findViewById(R.id.publisher);
            makepublisher.setText(currentCar.getPublisher());
            TextView makedate = (TextView) iView.findViewById(R.id.date);
            makedate.setText(currentCar.getDate());

            // Year:





            return  iView;
        }
    }


}
