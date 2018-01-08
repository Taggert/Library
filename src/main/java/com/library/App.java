package com.library;

import com.library.annotations.Value;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;

//*********************************************************************//
//Это просто кусуок классной работы он не относится к домашнему заданию//
//*********************************************************************//



public class App {
    public static void main(String[] args) throws IOException, IllegalAccessException, InstantiationException {
        //System.out.println( UUID.randomUUID() );
        Properties properties = new Properties();
        InputStream is = App.class.getResourceAsStream("/application.properties");
        properties.load(is);
       // System.out.println(properties.getProperty("filepath"));

       /* Reflections reflections = new Reflections("com.library", new FieldAnnotationsScanner());
        Set<Field> fieldsAnnotatedWith = reflections.getFieldsAnnotatedWith(Value.class);
        for (Field f : fieldsAnnotatedWith) {
            System.out.println(f.getName());
        }*/

        MyFileReader myFileReader = MyFileReader.class.newInstance();
        Field[] declaredFields = MyFileReader.class.getDeclaredFields();
        for(Field field: declaredFields){
            if(field.isAnnotationPresent(Value.class)){
                String annotationValue = field.getAnnotation(Value.class).value();
                field.setAccessible(true);
                field.set(myFileReader, properties.getProperty(annotationValue));
            }
        }
        myFileReader.printFilePath();

    }
}

class MyFileReader {
    @Value(value = "filepath")
    String filepath;

    public void printFilePath() {
        System.out.println(filepath);
    }
}
