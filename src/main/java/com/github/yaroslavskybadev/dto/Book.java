package com.github.yaroslavskybadev.dto;

import java.util.ArrayList;
import java.util.List;

public class Book {
    private Long id;
    private String name;
    private Integer pageCount;

    private final List<Author> authorList = new ArrayList<>();
    private final List<Subscription> subscriptionList = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public List<Author> getAuthorList() {
        return authorList;
    }

    public void addAuthor(Author author) {
        authorList.add(author);
    }

    public void removeAuthor(Author author) {
        authorList.remove(author);
    }

    public List<Subscription> getSubscriptionList() {
        return subscriptionList;
    }

    public void addSubscription(Subscription subscription) {
        subscriptionList.add(subscription);
    }

    public void removeSubscription(Subscription subscription) {
        subscriptionList.remove(subscription);
    }
}
