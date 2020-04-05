package com.example.inevent;

import java.util.Date;

public class Group {

    String gName;
    String gDate;
    String gAdmin;
    String gSafe;
    Integer gCount;
    String gPlace;
    Integer gAffectedCount;

    public Integer getgAffectedCount() {
        return gAffectedCount;
    }

    public void setgAffectedCount(Integer gAffectedCount) {
        this.gAffectedCount = gAffectedCount;
    }

    public Group(){

    }

    public Group(String gName, String gDate, String gAdmin, String gSafe, Integer gCount, String gPlace, Integer gAffectedCount) {
        this.gName = gName;
        this.gDate = gDate;
        this.gAdmin = gAdmin;
        this.gSafe = gSafe;
        this.gCount = gCount;
        this.gPlace = gPlace;
        this.gAffectedCount = gAffectedCount;
    }

    public String getgName() {
        return gName;
    }

    public void setgName(String gName) {
        this.gName = gName;
    }

    public String getgDate() {
        return gDate;
    }

    public void setgDate(String gDate) {
        this.gDate = gDate;
    }

    public String getgAdmin() {
        return gAdmin;
    }

    public void setgAdmin(String gAdmin) {
        this.gAdmin = gAdmin;
    }

    public String getgSafe() {
        return gSafe;
    }

    public void setgSafe(String gSafe) {
        this.gSafe = gSafe;
    }

    public Integer getgCount() {
        return gCount;
    }

    public void setgCount(Integer gCount) {
        this.gCount = gCount;
    }

    public String getgPlace() {
        return gPlace;
    }

    public void setgPlace(String gPlace) {
        this.gPlace = gPlace;
    }
}
