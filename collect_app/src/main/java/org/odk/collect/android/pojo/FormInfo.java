package org.odk.collect.android.pojo;

/**
 * Created by kunalsingh on 12/03/17.
 */

public class FormInfo {

    private String name;
    private String subtext;
    private String date ;
    private String status;
    private Long id;

    public Long getId() {
        return id;
    }

    public FormInfo(String name, String subtext, String date , String status, Long id) {
        this.name = name;
        this.subtext = subtext;
        this.date = date;
        this.status = status;
        this.id = id;

    }

    public String getName() {
        return name;
    }

    public String getSubtext() {
        return subtext;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }
}
