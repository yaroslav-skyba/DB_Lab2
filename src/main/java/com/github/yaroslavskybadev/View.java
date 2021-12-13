package com.github.yaroslavskybadev;

import com.github.yaroslavskybadev.dao.impl.AuthorDao;
import com.github.yaroslavskybadev.dao.impl.BookDao;
import com.github.yaroslavskybadev.dao.impl.ReaderDao;
import com.github.yaroslavskybadev.dao.impl.SubscriptionDao;
import com.github.yaroslavskybadev.dto.Author;
import com.github.yaroslavskybadev.dto.Book;
import com.github.yaroslavskybadev.dto.Reader;
import com.github.yaroslavskybadev.dto.Subscription;

import java.sql.Date;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.function.Consumer;

public class View {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final AuthorDao AUTHOR_DAO = new AuthorDao();
    private static final BookDao BOOK_DAO = new BookDao();
    private static final ReaderDao READER_DAO = new ReaderDao();
    private static final SubscriptionDao SUBSCRIPTION_DAO = new SubscriptionDao();

    public void runMenu() {
        while (true) {
            System.out.println();
            System.out.println("Press c - to create an entity");
            System.out.println("Press ri - to read an entity by id");
            System.out.println("Press ra - to read all entities");
            System.out.println("Press u - to update an entity");
            System.out.println("Press d - to delete an entity");
            System.out.println("Press q - to quit the program");
            System.out.println();

            System.out.print("Choose an action: ");

            switch (SCANNER.next()) {
                case "c":
                    createEntity();
                    break;
                case "ri":
                    readEntityById();
                    break;
                case "ra":
                    readAllEntities();
                    break;
                case "u":
                    updateEntity();
                    break;
                case "d":
                    deleteEntity();
                    break;
                case "q":
                    System.exit(0);
                default:
                    System.out.println("An incorrect action. Try again.");
            }
        }
    }

    private void createEntity() {
        while (true) {
            System.out.println();
            System.out.println("Press a - to create an author");
            System.out.println("Press b - to create a book");
            System.out.println("Press r - to create a reader");
            System.out.println("Press s - to create a subscription");
            System.out.println("Press q - to quit the program");
            System.out.println();

            System.out.print("Choose an entity: ");

            switch (SCANNER.next()) {
                case "a":
                    final Author author = new Author();
                    setId(author::setId);
                    setAuthor(author);

                    try {
                        AUTHOR_DAO.create(author);
                    } catch (IllegalStateException exception) {
                        System.out.println("User with id=" + author.getId() + " already exists");
                        continue;
                    }

                    break;
                case "b":
                    final Book book = new Book();

                    System.out.println();
                    System.out.print("Id: ");

                    book.setId(SCANNER.nextLong());
                    setBook(book);

                    BOOK_DAO.create(book);

                    break;
                case "r":
                    final Reader reader = new Reader();

                    System.out.println();
                    System.out.print("Id: ");

                    reader.setId(SCANNER.nextLong());
                    setReader(reader);

                    READER_DAO.create(reader);

                    break;
                case "s":
                    final Subscription subscription = new Subscription();

                    System.out.println();
                    System.out.print("Id: ");

                    subscription.setId(SCANNER.nextLong());
                    setSubscription(subscription);

                    SUBSCRIPTION_DAO.create(subscription);

                    break;
                case "q":
                    System.exit(0);
                default:
                    System.out.println("Incorrect entity type. Try again.");
                    continue;
            }

            System.out.println("An entity has been successfully created");

            break;
        }
    }

    private void readEntityById() {
        while (true) {
            System.out.println();
            System.out.println("Press a - to read an author");
            System.out.println("Press b - to read a book");
            System.out.println("Press r - to read a reader");
            System.out.println("Press s - to read a subscription");
            System.out.println("Press q - to quit the program");
            System.out.println();

            System.out.print("Choose an entity: ");

            switch (SCANNER.next()) {
                case "a":
                    System.out.println();
                    System.out.print("Id: ");
                    printAuthor(AUTHOR_DAO.findById(SCANNER.nextLong()));

                    break;
                case "b":
                    System.out.println();
                    System.out.print("Id: ");
                    printBook(BOOK_DAO.findById(SCANNER.nextLong()));

                    break;
                case "r":
                    System.out.println();
                    System.out.print("Id: ");
                    printReader(READER_DAO.findById(SCANNER.nextLong()));

                    break;
                case "s":
                    System.out.println();
                    System.out.print("Id: ");
                    printSubscription(SUBSCRIPTION_DAO.findById(SCANNER.nextLong()));

                    break;
                case "q":
                    System.exit(0);
                default:
                    System.out.println("Incorrect entity type. Try again.");
                    continue;
            }

            break;
        }
    }

    private void readAllEntities() {
        while (true) {
            System.out.println();
            System.out.println("Press a - to read authors");
            System.out.println("Press b - to read books");
            System.out.println("Press r - to read readers");
            System.out.println("Press s - to read subscriptions");
            System.out.println("Press q - to quit the program");
            System.out.println();

            System.out.print("Choose an entity: ");

            switch (SCANNER.next()) {
                case "a":
                    for (Author author : AUTHOR_DAO.findAll()) {
                        System.out.println("Id: " + author.getId());
                        printAuthor(author);
                        System.out.println();
                    }

                    break;
                case "b":
                    for (Book book : BOOK_DAO.findAll()) {
                        System.out.println("Id: " + book.getId());
                        printBook(book);
                        System.out.println();
                    }

                    break;
                case "r":
                    for (Reader reader : READER_DAO.findAll()) {
                        System.out.println("Id: " + reader.getId());
                        printReader(reader);
                        System.out.println();
                    }

                    break;
                case "s":
                    for (Subscription subscription : SUBSCRIPTION_DAO.findAll()) {
                        System.out.println("Id: " + subscription.getId());
                        printSubscription(subscription);
                        System.out.println();
                    }

                    break;
                case "q":
                    System.exit(0);
                default:
                    System.out.println("Incorrect entity type. Try again.");
                    continue;
            }

            break;
        }
    }

    private void updateEntity() {
        while (true) {
            System.out.println();
            System.out.println("Press a - to update an author");
            System.out.println("Press b - to update a book");
            System.out.println("Press r - to update a reader");
            System.out.println("Press s - to update a subscription");
            System.out.println("Press q - to quit the program");
            System.out.println();

            System.out.print("Choose an entity: ");

            switch (SCANNER.next()) {
                case "a":
                    System.out.println();
                    System.out.print("Id: ");

                    final Author author = AUTHOR_DAO.findById(SCANNER.nextLong());
                    setAuthor(author);

                    AUTHOR_DAO.update(author);

                    break;
                case "b":System.out.println();
                    System.out.print("Id: ");

                    final Book book = BOOK_DAO.findById(SCANNER.nextLong());
                    setBook(book);

                    BOOK_DAO.update(book);

                    break;
                case "r":System.out.println();
                    System.out.print("Id: ");

                    final Reader reader = READER_DAO.findById(SCANNER.nextLong());
                    setReader(reader);

                    READER_DAO.update(reader);

                    break;
                case "s":System.out.println();
                    System.out.print("Id: ");

                    final Subscription subscription = SUBSCRIPTION_DAO.findById(SCANNER.nextLong());
                    setSubscription(subscription);

                    SUBSCRIPTION_DAO.update(subscription);

                    break;
                case "q":
                    System.exit(0);
                default:
                    System.out.println("Incorrect entity type. Try again.");
                    continue;
            }

            System.out.println("An entity has been successfully created");

            break;
        }
    }

    private void deleteEntity() {
        while (true) {
            System.out.println();
            System.out.println("Press a - to delete an author");
            System.out.println("Press b - to delete a book");
            System.out.println("Press r - to delete a reader");
            System.out.println("Press s - to delete a subscription");
            System.out.println("Press q - to quit the program");
            System.out.println();

            System.out.print("Choose an entity: ");

            switch (SCANNER.next()) {
                case "a":
                    System.out.println();
                    System.out.print("Id: ");

                    AUTHOR_DAO.remove(AUTHOR_DAO.findById(SCANNER.nextLong()));

                    break;
                case "b":
                    System.out.println();
                    System.out.print("Id: ");

                    BOOK_DAO.remove(BOOK_DAO.findById(SCANNER.nextLong()));

                    break;
                case "r":
                    System.out.println();
                    System.out.print("Id: ");

                    READER_DAO.remove(READER_DAO.findById(SCANNER.nextLong()));

                    break;
                case "s":
                    System.out.println();
                    System.out.print("Id: ");

                    SUBSCRIPTION_DAO.remove(SUBSCRIPTION_DAO.findById(SCANNER.nextLong()));

                    break;
                case "q":
                    System.exit(0);
                default:
                    System.out.println("Incorrect entity type. Try again.");
                    continue;
            }

            break;
        }
    }

    private void printAuthor(Author author) {
        System.out.println("First name: " + author.getFirstName());
        System.out.println("Second name: " + author.getSecondName());
    }

    private void printBook(Book book) {
        System.out.println("Name: " + book.getName());
        System.out.println("Page count: " + book.getPageCount());
    }

    private void printReader(Reader reader) {
        System.out.println("First name: " + reader.getFirstName());
        System.out.println("Second name: " + reader.getSecondName());
    }

    private void printSubscription(Subscription subscription) {
        final Reader subscriptionReader = READER_DAO.findById(subscription.getReaderId());
        System.out.println("Reader first name: " + subscriptionReader.getFirstName());
        System.out.println("Reader second name: " + subscriptionReader.getSecondName());

        System.out.println("Registration date: " + subscription.getRegistrationDate());
        System.out.println("Expiration date: " + subscription.getExpirationDate());
    }

    private void setSubscription(Subscription subscription) {
        System.out.print("Reader id: ");
        subscription.setReaderId(SCANNER.nextLong());

        System.out.print("Registration date (yyyy-[m]m-[d]d): ");
        subscription.setRegistrationDate(Date.valueOf(SCANNER.next()));

        System.out.print("Expiration date (yyyy-[m]m-[d]d): ");
        subscription.setExpirationDate(Date.valueOf(SCANNER.next()));
    }

    private void setReader(Reader reader) {
        System.out.print("First name: ");
        reader.setFirstName(SCANNER.next());

        System.out.print("Second name: ");
        reader.setSecondName(SCANNER.next());
    }

    private void setBook(Book book) {
        System.out.print("Name: ");
        book.setName(SCANNER.next());

        System.out.print("Page count: ");
        book.setPageCount(SCANNER.nextInt());
    }

    private void setAuthor(Author author) {
        System.out.print("First name: ");
        author.setFirstName(SCANNER.next());

        System.out.print("Second name: ");
        author.setSecondName(SCANNER.next());
    }

    private void setId(Consumer<Long> idSetter) {
        while (true) {
            try {
                System.out.println();
                System.out.print("Id: ");
                idSetter.accept(SCANNER.nextLong());

                break;
            } catch (InputMismatchException exception) {
                System.out.println("This field should be an int");
                SCANNER.next();
            }
        }
    }
}
