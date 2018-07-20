package kamon.demo.tracing.item.model;

import java.util.Optional;
import lombok.Value;

@Value(staticConstructor = "of")
public class DetailItem {

    Item item;
    Optional<Seller> seller;

}
