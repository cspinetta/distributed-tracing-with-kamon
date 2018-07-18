package kamon.demo.tracing.item.services;

import java.util.Optional;
import kamon.annotation.api.SpanCustomizer;
import kamon.demo.tracing.item.model.DetailItem;
import kamon.demo.tracing.item.model.Item;
import kamon.demo.tracing.item.model.SearchFilter;
import kamon.demo.tracing.item.repositories.ItemRepository;
import kamon.demo.tracing.item.repositories.SellerRepository;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    SellerRepository sellerRepository;

    @SpanCustomizer(operationName = "item.search")
    public Page<Item> search(SearchFilter searchFilter, Integer page, Integer size) {
        if (searchFilter.getKeyWord().isPresent())
            return itemRepository.findByFilter(searchFilter.getKeyWord().orElse(""),
                PageRequest.of(page, size));
        else
            return itemRepository.findAll(PageRequest.of(page, size));
    }

    public Optional<Item> findById(Long id) {
        return itemRepository.findById(id);
    }

    public Optional<DetailItem> details(Long id) {
        return itemRepository
            .findById(id)
            .flatMap((item) -> {
                val seller = sellerRepository.findById(item.getId());
                return seller.map((s) -> DetailItem.of(item, s));
            });
    }
}
