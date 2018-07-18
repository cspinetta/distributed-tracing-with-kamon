package kamon.demo.tracing.item.model;

import java.util.Optional;
import lombok.Value;

@Value(staticConstructor = "of")
public class SearchFilter {

    private Optional<String> keyWord;

}
