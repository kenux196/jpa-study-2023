package study.kenux.jpa.repository.dto;

import lombok.Data;

@Data
public class ItemSearchCond {

    private String itemName;
    private Integer price;
    private String storeName;
}
