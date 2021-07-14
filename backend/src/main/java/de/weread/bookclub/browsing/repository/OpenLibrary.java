package de.weread.bookclub.browsing.repository;

import de.weread.bookclub.browsing.domain.Book;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
class OpenLibrary implements LibraryRepository {
    private static final String base_url = "http://openlibrary.org/search.json?q=";

    private final RestTemplate restTemplate = new RestTemplateBuilder().build();

    @Override
    public Set<Book> findBy(String query) {
        return queryApi(query)
                .map(this::mapToEntities)
                .orElse(Collections.emptySet());
    }

    private Optional<Response> queryApi(String query) {
        final Response response = restTemplate.getForObject(base_url + query, Response.class);
        return Optional.ofNullable(response);
    }

    private Set<Book> mapToEntities(Response response) {
        return response.docs.stream().map(this::mapToEntity).collect(Collectors.toSet());
    }

    private Book mapToEntity(Doc doc) {
        var book = new Book(doc.key,
                            doc.title,
                            Optional.ofNullable(doc.author_name).map(list -> list.get(0)).orElse(null),
                            findCoverURL(doc));

        ;

        return book;
    }

    private String findCoverURL(Doc doc) {
        if(doc.isbn == null) {
            return "";
        }

        for (String isbn : doc.isbn) {
            try {
                restTemplate.headForHeaders("https://covers.openlibrary.org/b/isbn/" + isbn + "-L.jpg?default=false");
                return "https://covers.openlibrary.org/b/isbn/" + isbn + "-L.jpg?default=false";
            } catch (Exception e) {
            }
        }

        return "";
    }
}

class Response {
    public int start;
    public int num_found;
    public List<Doc> docs;

    @Override
    public String toString() {
        return "Response{" +
                "start=" + start +
                ", num_found=" + num_found +
                ", docs=" + docs +
                '}';
    }
}

class Doc {
    public String key;
    public String title;
    public List<String> isbn;
    public List<String> author_name;

    @Override
    public String toString() {
        return "Doc{" +
                "key='" + key + '\'' +
                ", title='" + title + '\'' +
                ", author_name=" + author_name +
                ", isbn=" + isbn +
                '}';
    }
}