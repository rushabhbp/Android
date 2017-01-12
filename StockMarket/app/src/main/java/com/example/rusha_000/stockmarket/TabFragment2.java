package com.example.rusha_000.stockmarket;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.json.JSONException;

public class TabFragment2 extends Fragment {
    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v= inflater.inflate(R.layout.tab_fragment_2, container, false);
        WebView webview = (WebView) v.findViewById(R.id.web);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient(){

            public void onReceviederror(WebView web,int errorcode,String desc,String failurl)
            {
                Toast.makeText(getContext(),"no"+desc,Toast.LENGTH_SHORT).show();
            }

        });


        try {
            webview.loadUrl("http://www.kakarnishant.com/HW8/chart.php?symbol="+MainActivity.jsonObject.getString("Symbol"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return v;
    }

}
