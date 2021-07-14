package de.weread.bookclub.shelving.domain;


import de.weread.bookclub.browsing.domain.Book;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static javax.persistence.FetchType.EAGER;

@Entity
public class Shelf implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "shelf_id")
    private Long id;

    @ManyToMany(fetch = EAGER)
    @JoinColumn(name = "book_id")
    @JoinTable(
            name = "Shelf_Book",
            joinColumns = {@JoinColumn(name = "shelf_id")},
            inverseJoinColumns = {@JoinColumn(name = "book_id")}
    )
    private Set<Book> books;

    public Shelf() {
        this.books = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public Shelf setId(long id) {
        this.id = id;
        return this;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public Shelf setBooks(Set<Book> books) {
        this.books = books;
        return this;
    }

    public void addBook(Book book) {
        books.add(book);
    }

    @Override
    public String toString() {
        return "Shelf{" +
                "id=" + id +
                ", books=" + books +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shelf shelf = (Shelf) o;
        return id == shelf.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
