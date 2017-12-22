package com.gargas.marek;

public class Printer {
    private Element bill;
    private Element billInArticles;
    Printer(Element bill,Element billInArticles)
    {
        this.bill = bill;
        this.billInArticles = billInArticles;
    }


    public void wholeContent()
    {
        bill.printContent();
    }
    public void wholeTable()
    {
        for(Element child : bill.getChildrenList()) child.printID();
    }
    public void articleRangeContent(String[] articles) throws IllegalArgumentException
    {
        if(articles.length==2)
        {
            Element article1 = findInChildrenList(articles[0],billInArticles);
            Element article2 = findInChildrenList(articles[1],billInArticles);

            if(!properArticleOrder(article1,article2))
            {
                throw new IllegalArgumentException("Podano artykuły w niewłaściwej kolejności");
            }
            for(int i = articleIndex(article1); i<=articleIndex(article2);i++)
            {
                billInArticles.getChildrenList().get(i).printContent();
            }
        }
        else throw new IllegalArgumentException("Podano niewłaściwą liczbę artykułów. Podaj nazwy 2 artykułów oddzielone znakiem '-'");
    }

    public void articleContent(String[] description) throws IllegalArgumentException
    {
        if(description.length==0) throw new IllegalArgumentException("Podany opis jest niewłaściwy lub za krótki");
        String name = description[0];
        Element element = findInChildrenList(name,billInArticles);
        for(int i=1;i<description.length;i++)
        {
            name=description[i];
            element= findInChildrenList(name,element);
        }
        element.printContent();
    }

    public Element findInChildrenList(String name, Element element)  throws IllegalArgumentException
    {
        if(element.getChildrenList()!=null){
            for (Element child : element.getChildrenList())
            {
                if(child.getName().equals(name))
                    return child;
            }
        }
        throw new IllegalArgumentException("Nie ma elementu o podanej nazwie: '" + name +"'");
    }
    public Element findInElement(String name, Element element)
    {
        if(element.getName()!=null) {
            if (element.getName().equals(name)) return element;
        }
        if(element.getChildrenList()!=null){
            for (Element child : element.getChildrenList())
            {
                if(findInElement(name,child)!=null)
                    return findInElement(name,child);
            }
        }
        return null;
    }
    public void elementTable(String name) throws IllegalArgumentException
    {
        Element element = findInElement(name,bill);
        if(element==null) throw new IllegalArgumentException("Nie ma elementu o podanej nazwie: '" + name + "'");
        element.printID();
    }
    public void elementContent(String name) throws IllegalArgumentException
    {
        Element element = findInElement(name,bill);
        if(element==null) throw new IllegalArgumentException("Nie ma elementu o podanej nazwie: '" + name + "'");
        element.printContent();
    }
    public boolean properArticleOrder(Element article1, Element article2)
    {
        return articleIndex(article1) < articleIndex(article2);
    }
    public int articleIndex(Element article)
    {
        return billInArticles.getChildrenList().indexOf(article);
    }

}
