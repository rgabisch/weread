package de.weread.bookclub.browsing.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class Book implements Serializable {
    @Id
    @Column(name = "book_id")
    private String id;

    private String title;

    private String author;

    private String cover;

    public Book(String id, String title, String author, String cover) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.cover = cover;
    }

    public Book(String id) {
        this.id = id;
    }

    public Book() {
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public Book setId(String id) {
        this.id = id;
        return this;
    }

    public Book setTitle(String title) {
        this.title = title;
        return this;
    }

    public Book setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getCover() {
        return cover;
    }

    public Book setCover(String cover) {
        this.cover = cover;
        return this;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", cover='" + cover + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
