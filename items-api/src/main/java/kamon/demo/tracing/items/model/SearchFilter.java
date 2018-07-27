package kamon.demo.tracing.items.model;

import java.util.Optional;
import lombok.Value;

@Value(staticConstructor = "of")
public class SearchFilter {

    private Optional<String> keyWord;

}
