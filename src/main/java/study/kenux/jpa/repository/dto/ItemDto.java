package study.kenux.jpa.repository.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;
import study.kenux.jpa.domain.Item;

@Data
@NoArgsConstructor
public class ItemDto {
    private String name;
    private Integer price;
    private String storeName;

    public ItemDto(String name, Integer price) {
        this.name = name;
        this.price = price;
    }

    @QueryProjection
    public ItemDto(String name, Integer price, String storeName) {
        this.name = name;
        this.price = price;
        this.storeName = storeName;
    }

    public static ItemDto from(Item item) {
        return new ItemDto(
                item.getName(),
                item.getPrice(),
                item.getStore() != null ? item.getStore().getName() : "no store info");
    }
}
