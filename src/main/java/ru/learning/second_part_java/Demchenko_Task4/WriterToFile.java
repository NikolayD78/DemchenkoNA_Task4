package ru.learning.second_part_java.Demchenko_Task4;

import java.io.FileWriter;
import java.io.IOException;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

@Component
public class WriterToFile implements ConveyerDataWriter {
    public WriterToFile( @Value("${spring.application.pathoutput}") String path) {
        this.pathoutput = path;
    }

    //public WriterToFile() {}

    @Value("${spring.application.pathoutput}")
    private String pathoutput;

    @Override
    public void setPathOutput(String s)
    {this.pathoutput=s;}

    @Override
    public void write(String str) {
        FileWriter fw;
        try {
            fw = new FileWriter(pathoutput,true);
            fw.write(str+"\n");
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
