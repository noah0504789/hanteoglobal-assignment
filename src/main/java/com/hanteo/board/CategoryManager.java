package com.hanteo.board;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class CategoryManager {

    private final Map<Long, Category> categoriesById;
    private final Map<String, Category> categoriesByTitle;
    private final ObjectMapper mapper;
    private final Category root;

    public CategoryManager(ObjectMapper mapper) {
        this.categoriesById = new HashMap<>();
        this.categoriesByTitle = new HashMap<>();
        this.mapper = mapper;
        this.root = Category.createRootCategory(0L, "ROOT");
    }

    public ObjectMapper getMapper() {
        return mapper;
    }

    public Category getRoot() {
        return this.root;
    }

    public void insertRoot(Category root) {
        this.root.addChild(root);
        categoriesById.put(root.getId(), root);
        categoriesByTitle.put(root.getTitle(), root);
    }

    public void insert(Category parent, Category child) {
        parent.addChild(child);
        categoriesById.put(child.getId(), child);
        categoriesByTitle.put(child.getTitle(), child);
    }

    public void remove(Category parent, Category child) {
        parent.removeChild(child);
        removeRecursive(child);
    }

    public Category searchByIdOrTitle(String id) {
        return canParseLong(id) ? searchById(Long.parseLong(id)) : searchByTitle(id);
    }

    public String serializeToJson(CategoryDto dto) throws JsonProcessingException {
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto);
    }

    private void removeRecursive(Category category) {
        if (category == null) {
            return;
        }

        category.removeChildren();
        categoriesById.remove(category.getId());
        categoriesByTitle.remove(category.getTitle());

        if (category.getChildrenId() == null) {
            return;
        }

        for (Category child : category.getChildrenId().values()) {
            removeRecursive(child);
        }
    }

    private Category searchById(Long id) {
        return categoriesById.getOrDefault(id, null);
    }

    private Category searchByTitle(String title) {
        return categoriesByTitle.getOrDefault(title, null);
    }

    private boolean canParseLong(String id) {
        try {
            Long.parseLong(id);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }
}
