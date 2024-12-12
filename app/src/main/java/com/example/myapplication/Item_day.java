package com.example.myapplication;


public class Item_day {
    String dayyear;
    String daymonth;
    String daynum;
    String dayname;
    boolean checked;

    public Item_day(String dayyear, String daymonth, String daynum, String dayname, boolean checked) {
        this.dayyear=dayyear;
        this.daymonth=daymonth;
        this.daynum = daynum;
        this.dayname = dayname;
        this.checked=checked;
    }


    public String getDaymonth() {
        return daymonth;
    }

    public String getDayyear() {
        return dayyear;
    }

    public void setDaymonth(String daymonth) {
        this.daymonth = daymonth;
    }

    public void setDayyear(String dayyear) {
        this.dayyear = dayyear;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean getchecked() {
        return checked;
    }

    public String getDaynum() {
        return daynum;
    }

    public void setDaynum(String day) {
        this.daynum = day;
    }

    public void setDayname(String dayname) {
        this.dayname = dayname;
    }

    public String getDayname() {
        return dayname;
    }
}
