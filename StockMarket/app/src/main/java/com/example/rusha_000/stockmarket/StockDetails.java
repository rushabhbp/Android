package com.example.rusha_000.stockmarket;

/**
 * Created by rusha_000 on 02-05-2016.
 */
public class StockDetails {
    private String title;
    private String text;
    private int iconID;

    public StockDetails(String title,String text,int iconID)
    {
        super();
        this.title=title;
        this.text=text;
        this.iconID=iconID;

    }

    public int getIconID() {
        return iconID;
    }

    public String getText() {

        return text;
    }

    public String getTitle() {

        return title;
    }
}
