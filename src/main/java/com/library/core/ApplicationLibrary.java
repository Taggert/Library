package com.library.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.pojo.Book;
import com.library.pojo.Reader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ApplicationLibrary {

    private static List<Book> books = new ArrayList<>();
    private static List<Reader> readers = new ArrayList<>();
    public static final Object blockBook = new Object();
    public static final Object blockReader = new Object();


    public static void main(String[] args) throws IOException, InterruptedException {

        Properties properties = new Properties();
        InputStream is = ApplicationLibrary.class.getResourceAsStream("/application.properties");
        properties.load(is);


        File bookStorage = new File(properties.getProperty("booksPath"));
        File readersStorage = new File(properties.getProperty("readersPath"));
        Thread generator = new Thread(new Generator());
        generator.start();
        int stopper = 0;
        while (true) {
            if (stopper == 10) {
                return;
            }
            File sourse = new File(properties.getProperty("sourcePath"));
            File[] files = sourse.listFiles();

            if (files.length == 0) {
                Thread.sleep(1000);
                stopper++;
            }


            for (File file : files) {
                ObjectMapper objMapper = new ObjectMapper();

                if (file.getName().endsWith(".book")) {
                    synchronized (blockBook) {
                        Book book = objMapper.readValue(file, Book.class);

                        file.delete();

                        deserealizeStorage(bookStorage);
                        books.add(book);
                    }
                    System.out.println(books.toString());
                    serealizeStorage(bookStorage);
                    Thread.sleep(1000);
                }
                if (file.getName().endsWith(".reader")) {
                    synchronized (blockReader) {
                        Reader reader = objMapper.readValue(file, Reader.class);

                        file.delete();

                        deserealizeStorage(readersStorage);
                        readers.add(reader);
                    }
                    System.out.println(readers.toString());
                    serealizeStorage(readersStorage);
                    Thread.sleep(1000);
                }


            }

        }

    }

    private static void serealizeStorage(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        if (file.getName().startsWith("book")) {
            mapper.writeValue(file, books);
        } else if (file.getName().startsWith("reader")) {
            mapper.writeValue(file, readers);
        } else {
            System.err.println("Check file name of the storage!");
        }
    }

    private static void deserealizeStorage(File file) {
        ObjectMapper mapper = new ObjectMapper();
        if (file.exists() && file.getName().startsWith("book")) {
            try {
                books = mapper.readValue(file, new TypeReference<List<Book>>() {

                });
            } catch (IOException e) {
                System.err.println("Couldn't read book's storage");
            }
        } else if (file.exists() && file.getName().startsWith("reader")) {
            try {
                readers = mapper.readValue(file, new TypeReference<List<Reader>>() {

                });
            } catch (IOException e) {
                System.err.println("Couldn't read reader's storage");
            }
        } else {
            System.err.println("Check file name of the storage!");
        }
    }


}




