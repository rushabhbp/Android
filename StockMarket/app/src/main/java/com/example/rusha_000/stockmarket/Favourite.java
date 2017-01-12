package com.example.rusha_000.stockmarket;

/**
 * Created by rusha_000 on 03-05-2016.
 */
public class Favourite {
    private String cname;
    private String symbol;
    private String change;
    private String mcap;
    private String price;

    public Favourite(String cname, String symbol, String change, String mcap, String price) {
        this.cname = cname;
        this.symbol = symbol;
        this.change = change;
        this.mcap = mcap;
        this.price = price;

    }

    public void setChange(String change) {
        this.change = change;
    }

    public void setPrice(String price) {

        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public String getChange() {

        return change;
    }

    public String getMcap() {

        return mcap;
    }

    public String getSymbol() {

        return symbol;
    }


    public String getCname() {
        return cname;
    }
}
