package de.weread.bookclub.clubbing.domain;

import de.weread.bookclub.browsing.domain.Book;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.CascadeType.REFRESH;

@Entity
public class Club implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "club_id")
    private long id;

    @ManyToOne(cascade = REFRESH)
    @JoinColumn(name="book_id")
    private Book book;

    private String url;

    private String name;

    private int amountOfReaders;

    public long getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getUrl() {
        return url;
    }

    public Club setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getName() {
        return name;
    }

    public Club setName(String name) {
        this.name = name;
        return this;
    }

    public int getAmountOfReaders() {
        return amountOfReaders;
    }

    public Club setAmountOfReaders(int amountOfReaders) {
        this.amountOfReaders = amountOfReaders;
        return this;
    }

    @Override
    public String toString() {
        return "Club{" +
                "id=" + id +
                ", book=" + book +
                ", url='" + url + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

}
