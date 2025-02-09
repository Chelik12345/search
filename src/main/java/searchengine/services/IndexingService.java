package searchengine.services;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import searchengine.model.Indexer;
import searchengine.model.Site;
import searchengine.model.Status;
import searchengine.repository.SiteRepository;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class IndexingService {

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private Indexer indexer;

    public void startIndexing(String url, String name) {
        if (url == null || url.isEmpty() || name == null || name.isEmpty()) {
            throw new IllegalArgumentException("URL и имя сайта не могут быть пустыми!");
        }

        // Создание нового объекта сайта
        Site site = new Site();
        site.setUrl(url);
        site.setName(name);
        site.setStatus(Status.INDEXING);
        site.setStatusTime(LocalDateTime.now());
        siteRepository.save(site);

        try {
            // Подключение к странице и получение HTML-контента
            Document document = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                    .referrer("http://www.google.com")
                    .get();

            if (document.body() == null) {
                throw new IOException("Содержимое страницы пустое.");
            }

            String content = document.body().text();

            // Индексация страницы через Indexer
            indexer.indexPage(site, url, content);

            // Успешное завершение индексации
            site.setStatus(Status.INDEXED);
        } catch (IOException e) {
            // Ошибка при индексации
            site.setStatus(Status.FAILED);
        }

        // Сохранение изменений статуса
        site.setStatusTime(LocalDateTime.now());
        siteRepository.save(site);
    }
}

