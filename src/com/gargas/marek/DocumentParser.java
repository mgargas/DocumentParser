package com.gargas.marek;

import java.util.ArrayList;
import java.util.List;

public class DocumentParser {
    private List<String> regexList = new ArrayList<>();
    DocumentParser(List<String> regexList)
    {
        this.regexList = regexList;
    }

    public Element parseBill(List<String> text)
    {
        List<String> data = getDataBeforeHighestRegex(text,regexList.get(0));
        List<Element> childrenList = getChildrenList(text,this.regexList.get(0));
        Element bill = new Element(null,null,data,childrenList);
        return bill;
    }

    public Element createElement(List<String> text,String regex)
    {
        String name = getName(text.get(0),regex);
        String title;
        if(hasTitle(regex)){
            title = getTitle(text.get(1));
        }
        else title=null;
        List<String> section = getSection(text,regex,name);//deletes name/split line with name,title
        List<String> data = getData(section,regex); //copy text till encounter lower name
        String firstRegex = findFirstRegex(section);
        List<Element> childrenList = getChildrenList(section,firstRegex);
        Element element = new Element(name,title,data,childrenList);
        return element;
    }

    public List<Element> getChildrenList(List<String> section,String firstRegex)
    {
        if(firstRegex==null) return null;
        List<Element> childrenList = new ArrayList<>();
        List<ArrayList<String>> listOfSections = getListOfSections(section,firstRegex);
        if(listOfSections.size()==0) return null;
        for(List<String> newSection:listOfSections)
        {
            Element child = createElement(newSection,getRegex(newSection.get(0)));
            childrenList.add(child);
        }
        return childrenList;
    }

    public List<ArrayList<String>> getListOfSections(List<String> text, String firstRegex)
    {
        List<ArrayList<String>> listOfSections = new ArrayList<ArrayList<String>>();
        List<String> higherRegexList = regexList.subList(0,regexList.indexOf(firstRegex)+1);
        for(int i=0;i<text.size();i++){
            if(ifMatchesAny(text.get(i),higherRegexList))
            {
                String regex = getRegex(text.get(i));
                ArrayList<String> section = new ArrayList<>();
                section.add(text.get(i));
                higherRegexList = regexList.subList(0,regexList.indexOf(regex)+1);
                for(int j=i+1;j< text.size() && !ifMatchesAny(text.get(j),higherRegexList);j++)
                {
                    section.add(text.get(j));
                    i=j-1;
                }
                listOfSections.add(section);
            }
        }
        return listOfSections;
    }

    public List<String> getDataBeforeHighestRegex(List<String> text, String regex) {
        List<String> data = new ArrayList<>();
        int i=0;
        while (!text.get(i).matches(regex))
        {
            data.add(text.get(i));
            i++;
        }
        return data;
    }

    public String findFirstRegex(List<String> text)
    {
        for(String line:text)
        {
            if(ifMatchesAny(line,regexList))
            {
                return getRegex(line);
            }
        }
        return null;
    }
    public String getName(String line,String regex)
    {
       switch (regex)
       {
           case "^DZIAŁ\\sX{0,3}(IX|IV|V?I{0,3})[A-Z]$":
               return line;
           case "^Rozdział\\s\\w+$":
               return line;
           case "^[A-ZĄĆŹÓŻĘŁŃŚ,]{2,}(\\s+[A-ZĄĆŹÓŻĘŁŃŚ,]+)*$":
               return line;
           case "^\\S+\\.\\s\\d+[a-z]*\\..*$":
               return line.split(" ")[0]+" "+line.split(" ")[1];
           case "^\\d+\\.\\s+.+$":
               return line.split(" ")[0];
           case "^\\d+[a-z]*[)]\\s+.+$":
               return line.split(" ")[0];
           case "^[a-z][)].*$":
               return line.split(" ")[0];
           default:
               return null;
       }
    }
    public String getTitle(String line)
    {
        return line;
    }
    public boolean hasTitle(String regex)
    {
        if(regex.equals("^DZIAŁ\\sX{0,3}(IX|IV|V?I{0,3})[A-Z]$") || regex.equals("^Rozdział\\s\\w+$")){
            return true;
        }
        return false;
    }
    public List<String> getSection(List<String> text,String regex,String name)
    {
        if(text.get(0).equals(name))
        {
            text.remove(0);
        }
        else
        {
            text.set(0,text.get(0).replace(name+" ",""));
        }
        if(regex.equals("^DZIAŁ\\sX{0,3}(IX|IV|V?I{0,3})[A-Z]$") || regex.equals("^Rozdział\\s\\w+$"))
        {
            text.remove(0);
        }
        return text;
    }

    public List<String> getData(List<String> section,String regex)
    {
        List<String> lowerRegexList = regexList.subList(regexList.indexOf(regex),regexList.size());
        List<String> data = new ArrayList<>();
        int i=0;
        while(i<section.size() && !ifMatchesAny(section.get(i),lowerRegexList))
        {
            data.add(section.get(i));
            i++;
        }
        return data;
    }



    public  String getRegex(String line)
    {
        for(String regex: this.regexList)
        {
            if(line.matches(regex)) return regex;
        }
        return null;
    }
    public List<String> deleteHigherRegexes(List<String>text,List<String> higherRegexList, String regex)
    {
        for(int i=0;i<text.size();i++)
        {
            if(ifMatchesAny(text.get(i),higherRegexList))
            {
                int j=i;
                while(!text.get(j).matches(regex))
                {
                    text.remove(j);
                }
                i=j;
            }
        }
        return text;
    }

    public Boolean ifMatchesAny(String line,List<String> regexList)
    {
        for(String regex: regexList)
        {
            if(line.matches(regex)) return true;
        }
        return false;
    }

}

