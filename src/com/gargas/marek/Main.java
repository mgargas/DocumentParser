package com.gargas.marek;

import org.apache.commons.cli.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException
    {
        final List<String> constitutionHigherRegexList = Arrays.asList("^Rozdział\\s\\w+$","^[A-ZĄĆŹÓŻĘŁŃŚ,]{2,}(\\s+[A-ZĄĆŹÓŻĘŁŃŚ,]+)*$","^\\S+\\.\\s\\d+[a-z]*\\..*$");
        final List<String> uokikHigherRegexList = Arrays.asList("^DZIAŁ\\sX{0,3}(IX|IV|V?I{0,3})[A-Z]$","^Rozdział\\s\\w+$","^\\S+\\.\\s\\d+[a-z]*\\..*$");
        final List<String> lowerRegexList = Arrays.asList("^\\S+\\.\\s\\d+[a-z]*\\..*$","^\\d+\\.\\s+.+$","^\\d+[a-z]*[)]\\s+.+$","^[a-z][)].*$");



        Options options = new Options();

        Option f = new Option("f", "file", true, "Ścieżka do pliku");
        f.setRequired(true);
        options.addOption(f);

        Option w = new Option("w", "whole", false, "Cały dokument");
        w.setRequired(false);
        options.addOption(w);

        Option a = new Option("a", "article", true, "Artykuł lub specyficzny element artykułu, np. 'Art. 2.,2.,2),a)'");
        a.setRequired(false);
        options.addOption(a);

        Option e = new Option("e", "element", true, "Element wyższy niż artykuł, np. 'DZIAŁ IIIA' lub 'Rozdział 1'");
        e.setRequired(false);
        options.addOption(e);

        Option ar = new Option("ar", "articles range", true, "Zakres artykułów, np. 'Art. 2.-Art. 5.'");
        ar.setRequired(false);
        options.addOption(ar);

        Option m = new Option("m", "mode", true, "Tryb programu: `s`(spis treści) lub `t`(treść)");
        m.setRequired(true);
        options.addOption(m);


        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException ex) {
            System.out.println(ex.getMessage());
            formatter.printHelp("\nZalecamy użycie następującej kolejności opcji: " +
                    "\n(ścieżka do pliku) (element do wyświetlenia) (Tryb działania)", options);
            System.exit(0);
            return;
        }


        FileHandler fileHandler=new FileHandler();
        List<String> text = new ArrayList<>();
        try {
            text = fileHandler.getFormatedText(cmd.getOptionValue("f"));
        } catch (IOException e1) {
            System.out.println("Plik o ścieżce '" + cmd.getOptionValue("f") + "' nie istnieje.");
        }

        DocumentParser documentParserHigher;
        if(text.stream().anyMatch(line->line.matches(uokikHigherRegexList.get(0)))) {
            documentParserHigher = new DocumentParser(uokikHigherRegexList);
        }
        else{
            documentParserHigher = new DocumentParser(constitutionHigherRegexList);
        }
        Element bill = documentParserHigher.parseBill(text);
        text = documentParserHigher.deleteHigherRegexes(text,uokikHigherRegexList,lowerRegexList.get(0));
        DocumentParser documentParserLower = new DocumentParser(lowerRegexList);
        Element billInArticles = documentParserLower.parseBill(text);

        Printer printer = new Printer(bill,billInArticles);

        try {
            if (cmd.getOptionValue("m").equals("s")) {
                if (cmd.hasOption("w")) {
                    printer.wholeTable();
                }
                if (cmd.hasOption("a")) {
                    System.out.println("Nie można wyświetlić spisu treściu dla artykułu.");
                }
                if(cmd.hasOption("ar")){
                    System.out.println("Nie można wyświetlić spisu treściu dla zakresu artykułu.");
                }
                if(cmd.hasOption("e")) {
                    printer.elementTable(cmd.getOptionValue("e"));
                }
            }
            else if (cmd.getOptionValue("m").equals("t")) {
                if (cmd.hasOption("w")) {
                    printer.wholeContent();
                }
                if (cmd.hasOption("a")) {
                    printer.articleContent(cmd.getOptionValue("a").split(","));
                }
                if(cmd.hasOption("ar")){
                    printer.articleRangeContent(cmd.getOptionValue("ar").split("-"));
                }
                if(cmd.hasOption("e")) {
                    printer.elementContent(cmd.getOptionValue("e"));
                }
            }
            else {
                System.out.println("Podano niewłaściwy argument dla opcji '-m'");
            }
        }catch (IllegalArgumentException e2)
        {
            System.out.println(e2.getMessage());
            System.out.println("Użyj opcji '-help', by uzyskać więcej informacji o opcjach programu, jak np. wyświetlenie spisu treści dokumentu.");
        }






    }
}
