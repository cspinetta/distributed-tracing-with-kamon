package kamon.demo.tracing.item.repositories;

import java.util.Optional;
import kamon.demo.tracing.item.model.Seller;

public interface SellerRepository {

    Optional<Seller> findById(Long id);

}
