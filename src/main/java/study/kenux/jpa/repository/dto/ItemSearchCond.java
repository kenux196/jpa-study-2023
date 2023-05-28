package study.kenux.jpa.repository.dto;

import lombok.Data;

@Data
public class ItemSearchCond {
    private String itemName;
    private String storeName;
    private Integer minPrice;
    private Integer maxPrice;
}
