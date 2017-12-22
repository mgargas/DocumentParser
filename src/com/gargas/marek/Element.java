package com.gargas.marek;


import java.util.ArrayList;
import java.util.List;

public class Element {
    private String name;
    private String title;
    private List<Element> childrenList = new ArrayList<>();
    private List<String> data = new ArrayList<>();


    Element(String name, String title, List<String> data,List<Element> childrenList)
    {
        this.name = name;
        this.title = title;
        this.data = data;
        this.childrenList = childrenList;
    }


    public void printContent()
    {
        if(name!=null) System.out.println(name);
        if(title!=null) System.out.println(title);
        for(String line : this.data) System.out.println(line);
        if(childrenList!=null) {
            for (Element child : childrenList) child.printContent();
        }
    }
    public void printID()
    {
        if(name.matches("^DZIAŁ\\sX{0,3}(IX|IV|V?I{0,3})[A-Z]$") || name.matches("^Rozdział\\s\\w+$") || name.matches("^[A-ZĄĆŹÓŻĘŁŃŚ,]{2,}(\\s+[A-ZĄĆŹÓŻĘŁŃŚ,]+)*$"))
        {
            if(title==null) {
                System.out.println(name);
            }
            else System.out.println(name + " " + title);
        }
        if(childrenList!=null) {
            for (Element child : childrenList) child.printID();
        }
    }

    void setChildrenList(List<Element> childrenList)
    {
        this.childrenList = childrenList;
    }

    void setData(List<String> data)
    {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public List<String> getData() {
        return data;
    }

    public String getTitle() {
        return title;
    }

    public List<Element> getChildrenList()
    {
        return this.childrenList;
    }
}
