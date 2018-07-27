package kamon.demo.tracing.items.repositories;

import java.util.Optional;
import kamon.demo.tracing.items.model.Seller;

public interface SellersRepository {

    Optional<Seller> findById(Long id);

}
