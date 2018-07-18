package kamon.demo.tracing.item.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Value;

@Value(staticConstructor = "of")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Seller {

    private Long id;

    private String firstName;
    private String lastName;
    private LocalDate birthday;
    private SellerCategory category;
    private LocalDateTime registrationDate;
    private BigDecimal grade;

}
