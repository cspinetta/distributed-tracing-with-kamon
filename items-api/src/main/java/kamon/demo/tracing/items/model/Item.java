package kamon.demo.tracing.items.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Entity
@Value
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class Item {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @NonFinal
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private Category category;

    private Long sellerId;

    public static Item of(String title, Category category, Long sellerId) {
        return new Item(null, title, category, sellerId);
    }
}
