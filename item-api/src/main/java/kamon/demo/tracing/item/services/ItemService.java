package kamon.demo.tracing.item.services;

import kamon.annotation.api.SpanCustomizer;
import kamon.demo.tracing.item.model.DetailItem;
import kamon.demo.tracing.item.model.Item;
import kamon.demo.tracing.item.model.QuerySimulator;
import kamon.demo.tracing.item.model.SearchFilter;
import kamon.demo.tracing.item.repositories.ItemRepository;
import kamon.demo.tracing.item.repositories.SellerRepository;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ItemService {

    private static Logger logger = LoggerFactory.getLogger(ItemService.class);

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    SellerRepository sellerRepository;

    @SpanCustomizer(operationName = "item.search")
    public Page<Item> search(SearchFilter searchFilter, Integer page, Integer size) {
        if (searchFilter.getKeyWord().isPresent())
            return itemRepository.findByFilter(searchFilter.getKeyWord().orElse(""), PageRequest.of(page, size));
        else
            return itemRepository.findAll(PageRequest.of(page, size));
    }

    public Optional<Item> findById(Long id) {
        return itemRepository.findById(id);
    }

    public Optional<DetailItem> details(Long id) {
        logger.info("getting item with id: " + id);
        QuerySimulator.runInParallelN(5);
//        QuerySimulator.runN(5);
        return itemRepository
            .findById(id)
            .map((item) -> {
                try {
                    val seller = sellerRepository.findById(item.getSellerId());
                    return DetailItem.of(item, seller);
                } catch (Throwable exc) {
                    logger.error("Error trying to retrieve seller: " + item.getSellerId(), exc);
                    return DetailItem.of(item, Optional.empty());
                }
            });
    }
}
