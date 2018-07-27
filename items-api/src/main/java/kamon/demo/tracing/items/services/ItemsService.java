package kamon.demo.tracing.items.services;

import kamon.annotation.api.SpanCustomizer;
import kamon.demo.tracing.items.model.DetailItem;
import kamon.demo.tracing.items.model.Item;
import kamon.demo.tracing.items.model.QuerySimulator;
import kamon.demo.tracing.items.model.SearchFilter;
import kamon.demo.tracing.items.repositories.ItemsRepository;
import kamon.demo.tracing.items.repositories.SellersRepository;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ItemsService {

    private static Logger logger = LoggerFactory.getLogger(ItemsService.class);

    @Autowired
    ItemsRepository itemsRepository;

    @Autowired
    SellersRepository sellersRepository;

    @SpanCustomizer(operationName = "items.search")
    public Page<Item> search(SearchFilter searchFilter, Integer page, Integer size) {
        if (searchFilter.getKeyWord().isPresent())
            return itemsRepository
                .findByFilter(searchFilter.getKeyWord().orElse(""), PageRequest.of(page, size));
        else
            return itemsRepository.findAll(PageRequest.of(page, size));
    }

    public Optional<Item> findById(Long id) {
        return itemsRepository.findById(id);
    }

    public Optional<DetailItem> details(Long id) {
        logger.info("getting item with id: " + id);
        QuerySimulator.runInParallelN(5);
//        QuerySimulator.runN(5);
        return itemsRepository
            .findById(id)
            .map((item) -> {
                try {
                    val seller = sellersRepository.findById(item.getSellerId());
                    return DetailItem.of(item, seller);
                } catch (Throwable exc) {
                    logger.error("Error trying to retrieve seller: " + item.getSellerId(), exc);
                    return DetailItem.of(item, Optional.empty());
                }
            });
    }
}
