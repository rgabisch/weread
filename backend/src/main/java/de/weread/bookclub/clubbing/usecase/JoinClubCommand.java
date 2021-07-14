package de.weread.bookclub.clubbing.usecase;

public class JoinClubCommand {
    public long clubId;
    public long readerId;
    public String isbn;

    @Override
    public String toString() {
        return "JoinCommand{" +
                "clubId=" + clubId +
                ", readerId=" + readerId +
                ", isbn='" + isbn + '\'' +
                '}';
    }
}
