package de.weread.bookclub.chatting;

import de.weread.bookclub.clubbing.domain.Club;
import de.weread.bookclub.reader.domain.Reader;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SenfCallRoomRepository implements RoomRepository {
    private static final String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36";
    private static final String url = "https://public.senfcall.de/";

    @Override
    public String openWith(String name) {
        final var page = tryToOpenPage(url);
        final var form = tryToSelectForm(page);
        tryToEnterNameOfRoom(form, name);
        tryToSubmitForm(page, form);

        return url + name;
    }

    @Override
    public Club join(Club club, Reader reader) {
        final var page = tryToOpenPage(club.getUrl());
        final var form = tryToSelectForm(page);
        tryToEnterNameOfReader(club, reader, form);
        tryToApplyRoomURL(club, page, form);

        return club;
    }

    private void tryToApplyRoomURL(Club club, Connection.Response page, FormElement form) {
        try {
            var response = form.submit()
                               .cookie("cookie_consented_a", "true")
                               .cookies(page.cookies())
                               .userAgent(userAgent)
                               .followRedirects(false)
                               .execute();
            club.setUrl(response.headers().getOrDefault("Location", null));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void tryToEnterNameOfReader(Club club, Reader reader, FormElement form) {
        Element loginField = form.select("#name").first();

        if (loginField == null) {
            openWith(club.getName());
            join(club, reader);
            return;
        }

        loginField.val(reader.getName());
    }


    private Connection.Response tryToOpenPage(String url) {
        try {
            return Jsoup.connect(url)
                        .method(Connection.Method.GET)
                        .cookie("cookie_consented_a", "true")
                        .userAgent(userAgent)
                        .execute();
        } catch (IOException e) {
        }
        return null;
    }

    private FormElement tryToSelectForm(Connection.Response page) {
        try {
            return (FormElement) page.parse()
                                     .select("form").first();
        } catch (IOException e) {
        }
        return null;
    }

    private void tryToEnterNameOfRoom(FormElement form, String name) {
        Element filed = form.select("#room").first();
        filed.val(name);
    }

    private Connection.Response tryToSubmitForm(Connection.Response page, FormElement form) {
        try {
            return form.submit()
                       .cookie("cookie_consented_a", "true")
                       .cookies(page.cookies())
                       .userAgent(userAgent)
                       .execute();
        } catch (IOException e) {
        }
        return null;
    }

}
