package com.gargas.marek;

public enum ElementType {
    bill,
    heading,
    chapter,
    title,
    article,
    paragraph,
    point,
    letter;


    public String toRegex()
    {
        switch (this){
            case heading:
                return "^DZIAŁ\\sX{0,3}(IX|IV|V?I{0,3})[A-Z]$";
            case chapter:
                return "^Rozdział\\s\\w+$";
            case title:
                return "^[A-ZĄĆŹÓŻĘŁŃŚ,]{2,}(\\s+[A-ZĄĆŹÓŻĘŁŃŚ,]+)*$";
            case article:
                return "^\\S+\\.\\s\\d+[a-z]*\\..*$";
            case paragraph:
                return "^\\d+\\.\\s+.+$";
            case point:
                return "^\\d+[)]\\s+.+$";
            case letter:
                return "^[a-z][)].*$";
                default:
                    return null;
        }
    }


    public boolean hasTitle()
    {
        if(this.equals(ElementType.heading) || this.equals(ElementType.chapter)) return true;
        return false;
    }

    }
