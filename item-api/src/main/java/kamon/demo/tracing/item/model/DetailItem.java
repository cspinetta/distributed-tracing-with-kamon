package kamon.demo.tracing.item.model;

import lombok.Value;

@Value(staticConstructor = "of")
public class DetailItem {

    Item item;
    Seller seller;

}
