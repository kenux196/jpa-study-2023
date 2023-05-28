package study.kenux.jpa.repository.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
