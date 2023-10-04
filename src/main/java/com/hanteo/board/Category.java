package com.hanteo.board;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.hanteo.board.CategoryType.ANONYMOUS;
import static com.hanteo.board.CategoryType.ROOT;

public class Category {

    private final Long id;
    private final Map<Long, Category> childrenId;
    private final CategoryType type;
    private final String title;

    private Category(Long id, String title) {
        this.id = id;
        this.childrenId = new HashMap<>();
        this.type = ROOT;
        this.title = title;
    }

    private Category(Long id, CategoryType type, String title) {
        this.id = id;
        this.childrenId = new HashMap<>();
        this.type = type;
        this.title = title;
    }

    private Category(Long id, CategoryType type) {
        this.id = id;
        this.childrenId = null;
        this.type = type;

        if (type == ANONYMOUS) this.title = "anonymous board";
        else throw new IllegalArgumentException("invalid type");
    }

    public static Category createRootCategory(Long id, String title) {
        return new Category(id, title);
    }

    public static Category createCategoryWithTitle(Long id, CategoryType type, String title) {
        if (type == ANONYMOUS) {
            throw new IllegalArgumentException("Cannot provide custom title for : " + type);
        }

        return new Category(id, type, title);
    }

    public static Category createAnonymousCategory(Long id) {
        return new Category(id, ANONYMOUS);
    }

    public Long getId() {
        return id;
    }

    public Map<Long, Category> getChildrenId() {
        return childrenId;
    }

    public CategoryType getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public void addChild(Category child) {
        this.childrenId.put(child.getId(), child);
    }

    public void removeChild(Category child) {
        this.childrenId.remove(child.getId());
    }

    public void removeChildren() {
        if (this.childrenId != null) this.childrenId.clear();
    }

    public CategoryDto toDto() {
        return new CategoryDto(
            this.id,
            this.type,
            this.title,
            Optional.ofNullable(this.getChildrenId())
                    .map(children -> children.values().stream()
                            .filter(Objects::nonNull)
                            .map(Category::toDto)
                            .sorted(Comparator
                                        .comparing((CategoryDto c) -> c.type().getOrder())
                                        .thenComparingLong(CategoryDto::id))
                            .toList()
            ).orElse(null)
        );
    }
}
