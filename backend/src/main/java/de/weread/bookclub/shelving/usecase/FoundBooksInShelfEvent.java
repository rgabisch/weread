package de.weread.bookclub.shelving.usecase;

class FoundBooksInShelfEvent {
    public String id;

    public String title;

    public String author;

    public String cover;

    public int amountOfClubs;

    public boolean isClubMember;

    public long clubId;

    FoundBooksInShelfEvent(String id, String title, String author, String cover, int amountOfClubs, boolean isClubMember, long clubId) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.cover = cover;
        this.amountOfClubs = amountOfClubs;
        this.isClubMember = isClubMember;
        this.clubId = clubId;
    }
}
