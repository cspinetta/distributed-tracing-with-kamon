package kamon.demo.tracing.items.controllers;

import java.util.Optional;
import kamon.demo.tracing.items.model.DetailItem;
import kamon.demo.tracing.items.model.Item;
import kamon.demo.tracing.items.model.SearchFilter;
import kamon.demo.tracing.items.services.ItemsService;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/items")
public class ItemsController {
    private static final Logger logger = LoggerFactory.getLogger(ItemsController.class);

    @Autowired
    ItemsService itemsService;

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Item> byId(@PathVariable("id") Long id) {
        return itemsService
            .findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @RequestMapping(path = "/{id}/details", method = RequestMethod.GET)
    public ResponseEntity<DetailItem> details(@PathVariable("id") Long id) {
        return itemsService
            .details(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @RequestMapping(path = "/search", method = RequestMethod.GET)
    public Page<Item> search(
        @RequestParam(value = "key-word") Optional<String> keyWord,
        @RequestParam(value = "page", required = false, defaultValue = "${request.defaultPageValue}") Integer page,
        @RequestParam(value = "size", required = false, defaultValue = "${request.defaultSizeValue}") Integer size) {
        val filter = SearchFilter.of(keyWord);
        return itemsService.search(filter, page, size);
    }
}
