package projectus.pus.entity;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Category {
    FRONTEND("FRONTEND"),
    BACKEND("BACKEND"),
    DESIGN("DESIGN"),
    PM("PM"),
    DATASCIENCE("DATASCIENCE");
    private final String category;
    Category(String category) {
        this.category = category;
    }
    public static Category of(String category) {
        return Arrays.stream(Category.values())
                .filter(type -> type.category.equals(category))
                .findFirst()
                .orElseThrow(()-> new RuntimeException()); //todo exception 추가하기
    }
}
