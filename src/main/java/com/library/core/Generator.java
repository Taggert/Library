package com.library.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.pojo.Book;
import com.library.pojo.Reader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

public class Generator implements Runnable {

    private static ObjectMapper mapper = new ObjectMapper();
    private static final String[] bookNames = new String[]{"Voina i mir", "Antoshka", "Tikhiy don"};
    private static final String[] authors = new String[]{"Tolstoy", "Belyanin", "Ivan Petrovich"};
    private static final String[] names = new String[]{"Egor", "Ivan", "John", "Diego", "Gennadiy"};
    private static final String[] lastnames = new String[]{"Ivanov", "Stoyanov", "Petrov", "Lopez", "Smith", "Shapiro"};
    private static final Random rnd = new Random();

   /* public static void generator(int booksAmount, int readersAmount) throws IOException, InterruptedException {
        Properties properties = new Properties();
        InputStream is = Generator.class.getResourceAsStream("/application.properties");
        properties.load(is);
        for (int i = 0; i < booksAmount; i++) {
            Book book = generateBooks();
            File file = new File(properties.getProperty("sourcePath")+book.getBookName()+"_"+i+".book");
            mapper.writeValue(file, book);
            Thread.sleep(2000);
        }
        for (int i = 0; i < readersAmount; i++) {
            Reader reader = generateReaders();
            File file = new File(properties.getProperty("sourcePath")+reader.getLastName()+"_"+i+".reader");
            mapper.writeValue(file, reader);
            Thread.sleep(2000);

        }
    }*/

    private static Book generateBooks() {

        Book book = new Book();
        book.setId(UUID.randomUUID().toString());
        book.setAutor(authors[rnd.nextInt(authors.length)]);
        book.setBookName(bookNames[rnd.nextInt(bookNames.length)]);
        book.setIssueYear(1000 + rnd.nextInt(1000));

        return book;
    }

    private static Reader generateReaders() {

        Reader reader = new Reader();
        reader.setId(UUID.randomUUID().toString());
        reader.setFirstName(names[rnd.nextInt(names.length)]);
        reader.setLastName(lastnames[rnd.nextInt(lastnames.length)]);
        reader.setBirthYear(1930 + rnd.nextInt(65));

        return reader;
    }


    @Override
    public void run() {
        Properties properties = new Properties();
        InputStream is = Generator.class.getResourceAsStream("/application.properties");
        try {
            properties.load(is);
        } catch (IOException e) {}
        int booksAmount = 1 + rnd.nextInt(20);
        int readersAmount = 1 + rnd.nextInt(20);
        for (int i = 0; i < booksAmount; i++) {
            Book book = generateBooks();
            synchronized (ApplicationLibrary.blockBook) {
                File file = new File(properties.getProperty("sourcePath") + book.getBookName() + "_" + i + ".book");
                System.out.println(file.getName() + " " + book);
                try {
                    mapper.writeValue(file, book);

                } catch (IOException e) {}
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {}

        }
        for (int j = 0; j < readersAmount; j++) {
            Reader reader = generateReaders();
            synchronized (ApplicationLibrary.blockReader) {
                File file = new File(properties.getProperty("sourcePath") + reader.getLastName() + "_" + j + ".reader");
                System.out.println(file.getName() + " " + reader);

                try {

                    mapper.writeValue(file, reader);

                } catch (IOException e) {
                }

            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }

        }
    }
}
