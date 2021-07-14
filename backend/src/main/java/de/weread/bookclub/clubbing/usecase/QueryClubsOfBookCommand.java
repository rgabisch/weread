package de.weread.bookclub.clubbing.usecase;

public class QueryClubsOfBookCommand {
    public long readerId;
    public String isbn;

    @Override
    public String toString() {
        return "QueryClubsOfBookCommand{" +
                "readerId=" + readerId +
                ", isbn='" + isbn + '\'' +
                '}';
    }
}
