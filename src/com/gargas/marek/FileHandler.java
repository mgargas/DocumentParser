package com.gargas.marek;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class FileHandler {

    final String dateRegex="^\\d{4}-\\d{2}-\\d{2}$";
    public List<String> getFormatedText(String path) throws IOException
    {
        List<String> text = readRawLinesFromFile(path);
        text=deleteLinesWithRegex(text,dateRegex);
        text=deleteLinesWithRegex(text,"^©.*$");
        text=deleteLinesWithRegex(text,"^.{1}$");
        text=combineDividedLines(text);
        return text;
    }
    public List<String> readRawLinesFromFile(String path) throws IOException {
        File file = new File(path);
        Scanner scanner = new Scanner(file);
        List<String> linesList = new ArrayList<>();
        while (scanner.hasNextLine()) {
            linesList.add(scanner.nextLine());
        }
        return linesList;
    }

    public List<String> deleteLinesWithRegex(List<String>text,String regex)
    {
        return text.stream().filter(line -> !line.matches(regex)).collect(Collectors.toList());
    }


    public List<String> combineDividedLines(List<String>text)
    {
        for(int i=0;i<text.size();i++){
            if(text.get(i).matches("^.*[-]$"))
            {
                String endOfWord = text.get(i+1).split(" ",2)[0];
                text.set(i,text.get(i).replaceFirst("[-]$",endOfWord));
                if(text.get(i+1).split(" ").length==1) // when the next line consists only of the ending of the word
                {
                    text.remove(i+1);
                }
                else
                {
                    text.set(i + 1, text.get(i + 1).replaceFirst("^\\S+\\s", ""));
                }
            }
        }
        return text;
    }

}
