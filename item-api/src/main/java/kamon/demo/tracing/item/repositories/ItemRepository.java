package kamon.demo.tracing.item.repositories;

import kamon.annotation.api.SpanCustomizer;
import kamon.demo.tracing.item.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface ItemRepository extends PagingAndSortingRepository<Item, Long> {

    @SpanCustomizer(operationName = "item.searchByFilter")
    @Query("from Item p where lower(p.title) like lower(concat('%', :keyword, '%'))")
    Page<Item> findByFilter(@Param("keyword") String keyWord, Pageable pageable);
}
