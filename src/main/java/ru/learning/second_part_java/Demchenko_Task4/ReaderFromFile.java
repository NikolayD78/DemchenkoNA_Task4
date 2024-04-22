package ru.learning.second_part_java.Demchenko_Task4;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
public class ReaderFromFile implements ConveyerDataReader {


    public ReaderFromFile( @Value("${spring.application.pathinput}") String path) {
        this.pathinput = path;
    }


     //public ReaderFromFile(){};

    public String getPath() {
        return pathinput;
    }

    public void setPath(String path) {
        this.pathinput = path;
    }


    @Value("${spring.application.pathinput}")
    private String pathinput;

    @Override
    public ArrayList<String> read() {
        System.out.println("inputpath = "+ getPath());
        ArrayList<String> StringsFromFile=new ArrayList<>(0);
        int numbOfStrings=0;

        try {

            File folder = new File(getPath());
            for (File file : folder.listFiles())
            {
                if (file.getName().substring(0,5).equals("input")==true) {
                    System.out.println(file.getName());
                    Scanner sc = new Scanner(file);

                    while (sc.hasNextLine()) {

                        StringsFromFile.add(sc.nextLine());

                    }
                }

            }
        }catch (FileNotFoundException e) {
                throw new RuntimeException(e);}

        return StringsFromFile;
    }
}

