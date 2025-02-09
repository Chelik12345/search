package searchengine.model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import searchengine.services.IndexingService;

@RestController
    @RequestMapping("/indexing")
    public class IndexingController {

        @Autowired
        private IndexingService indexingService;

        @PostMapping("/start")
    public String startIndexing(@RequestParam String url, @RequestParam String name) {
        indexingService.startIndexing(url, name);
        return "Индексация для сайта " + name + " началась.";
    }

        public IndexingService getIndexingService() {
            return indexingService;
        }
    }
