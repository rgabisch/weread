package de.weread.bookclub.reader.domain;


import de.weread.bookclub.clubbing.domain.Club;
import de.weread.bookclub.shelving.domain.Shelf;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;

@Entity
public class Reader implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "reader_id")
    private Long id;

    @OneToOne(cascade = ALL)
    @JoinColumn(name = "shelf_id")
    private Shelf shelf;

    @ManyToMany(fetch = EAGER)
    @JoinColumn(name = "club_id")
    @JoinTable(
            name = "club_readers",
            joinColumns = {@JoinColumn(name = "reader_id")},
            inverseJoinColumns = {@JoinColumn(name = "club_id")}
    )
    private Set<Club> clubs;

    private String name;

    @Transient
    private boolean isNew = true;

    public Reader(long id, Shelf shelf) {
        this.id = id;
        this.shelf = shelf;
    }

    public Reader() {
        this.shelf = new Shelf();
    }

    public Long getId() {
        return id;
    }

    public boolean isNew() {
        return false;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Shelf getShelf() {
        if (shelf == null) {
            return new Shelf();
        }
        return shelf;
    }

    public void setShelf(Shelf shelf) {
        this.shelf = shelf;
    }

    public Set<Club> getClubs() {
        return clubs;
    }

    public Reader setClubs(Set<Club> clubs) {
        this.clubs = clubs;
        return this;
    }

    public String getName() {
        return name;
    }

    public Reader setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return "Reader{" +
                "id=" + id +
                ", shelf=" + shelf +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reader reader = (Reader) o;
        return id == reader.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
