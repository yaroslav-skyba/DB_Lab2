package com.github.yaroslavskybadev;

import com.github.yaroslavskybadev.dao.Dao;
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
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class View {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final AuthorDao AUTHOR_DAO = new AuthorDao();
    private static final BookDao BOOK_DAO = new BookDao();
    private static final ReaderDao READER_DAO = new ReaderDao();
    private static final SubscriptionDao SUBSCRIPTION_DAO = new SubscriptionDao();

    private static final String BOOK_NAMES_SEPARATOR = ";";
    private static final String KEY_TO_STOP_ADDING_ENTITIES = "s";

    public void runMenu() {
        while (true) {
            System.out.println();
            System.out.println("Press c - to create an entity");
            System.out.println("Press ri - to read an entity by id");
            System.out.println("Press ra - to read all entities");
            System.out.println("Press u - to update an entity");
            System.out.println("Press d - to delete an entity");
            System.out.println("Press g - to generate entities");
            System.out.println("Press s2 - to search with 2 tables");
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
                case "g":
                    generateEntities();
                    break;
                case "s2":
                    searchWithReaderAndSubscriptionEntities();
                    break;
                case "q":
                    System.exit(0);
                default:
                    System.out.println("ERROR. An incorrect action");
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
                    author.setId(getId());
                    setAuthorFields(author);

                    try {
                        AUTHOR_DAO.create(author);
                        AUTHOR_DAO.addBooks(author);
                    } catch (IllegalArgumentException exception) {
                        System.out.println("ERROR. An author with id=" + author.getId() + " already exists");
                        continue;
                    }

                    break;
                case "b":
                    final Book book = createBook();

                    try {
                        BOOK_DAO.create(book);
                    } catch (IllegalArgumentException exception) {
                        System.out.println("ERROR. A book with id=" + book.getId() + " already exists");
                        continue;
                    }

                    break;
                case "r":
                    final Reader reader = new Reader();
                    reader.setId(getId());
                    setReaderFields(reader);

                    try {
                        READER_DAO.create(reader);
                    } catch (IllegalArgumentException exception) {
                        System.out.println("ERROR. A reader with id=" + reader.getId() + " already exists");
                        continue;
                    }

                    break;
                case "s":
                    final Subscription subscription = new Subscription();
                    subscription.setId(getId());
                    setSubscriptionFields(subscription);

                    try {
                        SUBSCRIPTION_DAO.create(subscription);
                        SUBSCRIPTION_DAO.addBooks(subscription);
                    } catch (IllegalArgumentException exception) {
                        System.out.println("ERROR. A subscription with id=" + subscription.getId() + " already exists");
                        continue;
                    }

                    break;
                case "q":
                    System.exit(0);
                default:
                    System.out.println("ERROR. An incorrect entity type");
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
                    final long authorId = getId();

                    try {
                        printAuthor(AUTHOR_DAO.findById(authorId));
                    } catch (IllegalStateException exception) {
                        System.out.println("ERROR. An author with id=" + authorId + " does not exist");
                        continue;
                    }

                    break;
                case "b":
                    final long bookId = getId();

                    try {
                        printBook(BOOK_DAO.findById(bookId));
                    } catch (IllegalStateException exception) {
                        System.out.println("ERROR. A book with id=" + bookId + " does not exist");
                        continue;
                    }

                    break;
                case "r":
                    final long readerId = getId();

                    try {
                        printReader(READER_DAO.findById(readerId));
                    } catch (IllegalStateException exception) {
                        System.out.println("ERROR. A reader with id=" + readerId + " does not exist");
                        continue;
                    }

                    break;
                case "s":
                    final long subscriptionId = getId();

                    try {
                        printSubscription(SUBSCRIPTION_DAO.findById(subscriptionId));
                    } catch (IllegalStateException exception) {
                        System.out.println("ERROR. A subscription with id=" + subscriptionId + " does not exist");
                        continue;
                    }

                    break;
                case "q":
                    System.exit(0);
                default:
                    System.out.println("ERROR. An incorrect entity type");
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
                    System.out.println("ERROR. Incorrect entity type");
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
                    final long authorId = getId();

                    try {
                        final Author author = AUTHOR_DAO.findById(authorId);
                        setAuthorFields(author);

                        AUTHOR_DAO.update(author);
                        AUTHOR_DAO.addBooks(author);
                    } catch (IllegalStateException exception) {
                        System.out.println("ERROR. An author with id=" + authorId + " does not exist");
                        continue;
                    } catch (IllegalArgumentException exception) {
                        System.out.println("ERROR. An author already owns one of these books");
                        continue;
                    }

                    break;
                case "b":
                    final long bookId = getId();

                    try {
                        final Book book = BOOK_DAO.findById(bookId);
                        setBookFields(book);

                        BOOK_DAO.update(book);
                        BOOK_DAO.addAuthors(book);
                        BOOK_DAO.addSubscriptions(book);
                    } catch (IllegalStateException exception) {
                        System.out.println("ERROR. A book with id=" + bookId + " does not exist");
                        continue;
                    } catch (IllegalArgumentException exception) {
                        System.out.println("ERROR. A book is already owned by one of these authors");
                        continue;
                    }

                    break;
                case "r":
                    final long readerId = getId();
                    final Reader reader;

                    try {
                        reader = READER_DAO.findById(readerId);
                    } catch (IllegalStateException exception) {
                        System.out.println("ERROR. A reader with id=" + readerId + " does not exist");
                        continue;
                    }

                    setReaderFields(reader);
                    READER_DAO.update(reader);

                    break;
                case "s":
                    final long subscriptionId = getId();

                    try {
                        final Subscription subscription = SUBSCRIPTION_DAO.findById(subscriptionId);
                        setSubscriptionFields(subscription);

                        SUBSCRIPTION_DAO.update(subscription);
                        SUBSCRIPTION_DAO.addBooks(subscription);
                    } catch (IllegalStateException exception) {
                        System.out.println("ERROR. A subscription with id=" + subscriptionId + " does not exist");
                        continue;
                    } catch (IllegalArgumentException exception) {
                        System.out.println("ERROR. A subscription is already signed for one of these books");
                        continue;
                    }

                    break;
                case "q":
                    System.exit(0);
                default:
                    System.out.println("ERROR. An incorrect entity type");
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
                    final long authorId = getId();

                    try {
                        AUTHOR_DAO.remove(AUTHOR_DAO.findById(authorId));
                    } catch (IllegalStateException exception) {
                        System.out.println("ERROR. An author with id=" + authorId + " does not exist");
                        continue;
                    }

                    break;
                case "b":
                    final long bookId = getId();

                    try {
                        BOOK_DAO.remove(BOOK_DAO.findById(bookId));
                    } catch (IllegalStateException exception) {
                        System.out.println("ERROR. A book with id=" + bookId + " does not exist");
                        continue;
                    }

                    break;
                case "r":
                    final long readerId = getId();

                    try {
                        READER_DAO.remove(READER_DAO.findById(readerId));
                    } catch (IllegalStateException exception) {
                        System.out.println("ERROR. A reader with id=" + readerId + " does not exist");
                        continue;
                    }

                    break;
                case "s":
                    final long subscriptionId = getId();

                    try {
                        SUBSCRIPTION_DAO.remove(SUBSCRIPTION_DAO.findById(subscriptionId));
                    } catch (IllegalStateException exception) {
                        System.out.println("ERROR. A subscription with id=" + subscriptionId + " does not exist");
                        continue;
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

    private void generateEntities() {
        while (true) {
            System.out.println();
            System.out.println("Press a - to generate authors");
            System.out.println("Press b - to generate books");
            System.out.println("Press r - to generate readers");
            System.out.println("Press s - to generate subscriptions");
            System.out.println("Press q - to quit the program");
            System.out.println();

            System.out.print("Choose an entity: ");

            switch (SCANNER.next()) {
                case "a":
                    AUTHOR_DAO.generateEntities();
                    System.out.println("Authors were successfully generated");

                    break;
                case "b":
                    BOOK_DAO.generateEntities();
                    System.out.println("Books were successfully generated");

                    break;
                case "r":
                    READER_DAO.generateEntities();
                    System.out.println("Readers were successfully generated");

                    break;
                case "s":
                    SUBSCRIPTION_DAO.generateEntities();
                    System.out.println("Subscriptions were successfully generated");

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

    private void searchWithReaderAndSubscriptionEntities() {
        System.out.print("\nReader first name: ");
        final String firstName = SCANNER.next();

        System.out.print("Reader second name: ");
        final String secondName = SCANNER.next();

        System.out.print("Subscription registration date: ");
        final Date registrationDate = Date.valueOf(SCANNER.next());

        for (List<Map<String, Object>> recordList : Dao.searchWithReaderAndSubscriptionEntities(firstName, secondName, registrationDate)) {
            System.out.println("Reader id: " + recordList.get(0).get("reader_id"));
            System.out.println("Reader first name: " + recordList.get(1).get("first_name"));
            System.out.println("Reader second name: " + recordList.get(2).get("second_name"));
            System.out.println("Subscription id: " + recordList.get(3).get("id"));
            System.out.println("Subscription registration date: " + recordList.get(4).get("registration_date"));
            System.out.println("Subscription expiration date: " + recordList.get(5).get("expiration_date"));
            System.out.println();
        }
    }

    private void printAuthor(Author author) {
        System.out.println("First name: " + author.getFirstName());
        System.out.println("Second name: " + author.getSecondName());
        System.out.println("Books: ");
        author.getBookList().forEach(this::printBook);
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
        System.out.println("Books: ");
        subscription.getBookList().forEach(this::printBook);
    }

    private void setAuthorFields(Author author) {
        System.out.print("First name: ");
        author.setFirstName(SCANNER.next());

        System.out.print("Second name: ");
        author.setSecondName(SCANNER.next());

        do {
            System.out.println("\nAdd a book\n");

            final Book book = createBook();

            try {
                BOOK_DAO.create(book);
            } catch (IllegalArgumentException ignored) {
            }

            author.addBook(book);

            System.out.println("\nPress " + KEY_TO_STOP_ADDING_ENTITIES + " - to stop adding books");
            System.out.print("Continue: ");
        } while (!KEY_TO_STOP_ADDING_ENTITIES.equals(SCANNER.next()));
    }

    private void setBookFields(Book book) {
        System.out.print("Name: ");
        book.setName(SCANNER.next());

        while (true) {
            System.out.print("Page count: ");

            final int pageCount;
            final String errorMessage = "This field should be a positive int";

            try {
                pageCount = SCANNER.nextInt();
            } catch (InputMismatchException exception) {
                System.out.println(errorMessage);
                SCANNER.next();
                System.out.println();

                continue;
            }

            if (pageCount > 0) {
                book.setPageCount(pageCount);
                break;
            } else {
                System.out.println(errorMessage + "\n");
            }
        }
    }

    private void setReaderFields(Reader reader) {
        System.out.print("First name: ");
        reader.setFirstName(SCANNER.next());

        System.out.print("Second name: ");
        reader.setSecondName(SCANNER.next());
    }

    private void setSubscriptionFields(Subscription subscription) {
        subscription.setReaderId(getId("Reader "));

        while (true) {
            System.out.print("Registration date (yyyy-[m]m-[d]d): ");

            try {
                subscription.setRegistrationDate(Date.valueOf(SCANNER.next()));
                break;
            } catch (IllegalArgumentException exception) {
                System.out.println("This field should be a date\n");
            }
        }

        while (true) {
            System.out.print("Expiration date (yyyy-[m]m-[d]d): ");

            try {
                subscription.setExpirationDate(Date.valueOf(SCANNER.next()));
                break;
            } catch (IllegalArgumentException exception) {
                System.out.println("This field should be a date\n");
            }
        }

        do {
            System.out.println("\nAdd a book\n");

            final Book book = createBook();

            try {
                BOOK_DAO.create(book);
            } catch (IllegalArgumentException ignored) {
            }

            subscription.addBook(book);

            System.out.println("\nPress " + KEY_TO_STOP_ADDING_ENTITIES + " - to stop adding books");
            System.out.print("Continue: ");
        } while (!KEY_TO_STOP_ADDING_ENTITIES.equals(SCANNER.next()));
    }

    private long getId(String... optionalStrings) {
        while (true) {
            System.out.print(String.join(" ", optionalStrings) + "Id: ");

            final long id;
            final String errorMessage = "This field should be a positive int";

            try {
                id = SCANNER.nextLong();
            } catch (InputMismatchException exception) {
                System.out.println(errorMessage);
                SCANNER.next();
                System.out.println();

                continue;
            }

            if (id > 0) {
                return id;
            } else {
                System.out.println(errorMessage + "\n");
            }
        }
    }

    private Book createBook() {
        final Book book = new Book();
        book.setId(getId());
        setBookFields(book);

        return book;
    }
}
