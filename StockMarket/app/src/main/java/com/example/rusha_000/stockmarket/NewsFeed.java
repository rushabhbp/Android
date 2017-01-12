package com.example.rusha_000.stockmarket;

import java.util.Date;

/**
 * Created by rusha_000 on 02-05-2016.
 */
public class NewsFeed {
    private String title;
    private String desc;
    private String link;
    private String publisher;
    private String d;

    public NewsFeed(String title, String desc, String link, String publisher, String d)
    {
        super();
        this.title=title;
        this.desc=desc;
        this.link=link;
        this.publisher=publisher;
        this.d=d;

    }
    public String getDate() {
        return d;
    }

    public String getPublisher() {

        return publisher;
    }

    public String getDesc() {

        return desc;
    }

    public String getLink() {

        return link;
    }

    public String getTitle() {

        return title;
    }



}
