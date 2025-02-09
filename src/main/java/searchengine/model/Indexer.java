package searchengine.model;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.index.IndexWriterConfig;
import org.springframework.stereotype.Service;

@Service
public class Indexer {
    private RAMDirectory index;

    public Indexer() {
        index = new RAMDirectory();
    }


    public void indexPage(Site site, String url, String content) {
        try (IndexWriter writer = new IndexWriter(index, new IndexWriterConfig(new StandardAnalyzer()))) {
            Document doc = new Document();

            // Для URL: Сохраняем значение URL с помощью StoredField (для извлечения)
            doc.add(new StoredField("url", url));  // Сохраняем URL, но не индексируем его для поиска

            // Для контента: Индексируем текст с помощью TextField
            doc.add(new TextField("content", content, org.apache.lucene.document.Field.Store.YES));  // Индексируем и сохраняем текст контента

            // Добавляем документ в индекс
            writer.addDocument(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
