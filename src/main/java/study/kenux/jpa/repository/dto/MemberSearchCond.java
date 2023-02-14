package study.kenux.jpa.repository.dto;

import lombok.Data;

@Data
public class MemberSearchCond {

    private String name;
    private Integer age;
    private String teamName;
}