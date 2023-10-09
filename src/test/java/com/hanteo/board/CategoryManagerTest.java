package com.hanteo.board;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.hanteo.board.CategoryType.GENERAL;
import static com.hanteo.board.CategoryType.NOTICE;
import static com.hanteo.board.CategoryType.SUB;
import static org.junit.jupiter.api.Assertions.*;

class CategoryManagerTest {

    CategoryManager manager;
    Category man, woman, exo, noticeExo, chen, baekhyun, xiumin;

    @BeforeEach
    void init() {
        manager = new CategoryManager(new ObjectMapper());

        man = Category.createRootCategory(1L, "MAN");
        woman = Category.createRootCategory(2L, "WOMAN");

        exo = Category.createCategoryWithTitle(10L, SUB, "EXO");

        noticeExo = Category.createCategoryWithTitle(100L, NOTICE, "[NOTICE] EXO");
        chen = Category.createCategoryWithTitle(101L, GENERAL, "CHEN");
        baekhyun = Category.createCategoryWithTitle(102L, GENERAL, "BAEKHYUN");
        xiumin = Category.createCategoryWithTitle(103L, GENERAL, "XIUMIN");
    }

    @Test
    @DisplayName("insertRoot() : 게시판에 최상위 카테고리를 추가합니다.")
    void insertRoot() {
        manager.insertRoot(man);
        manager.insertRoot(woman);

        assertTrue(manager.getRoot().getChildrenId().containsValue(man));
        assertTrue(manager.getRoot().getChildrenId().containsValue(woman));
    }

    @Test
    @DisplayName("insert() : 카테고리 안에 하위 카테고리를 추가합니다.")
    void insert() {
        manager.insert(man, exo);
        manager.insert(exo, noticeExo);
        manager.insert(exo, chen);
        manager.insert(exo, baekhyun);
        manager.insert(exo, xiumin);

        assertTrue(man.getChildrenId().containsValue(exo));
        assertTrue(exo.getChildrenId().containsValue(noticeExo));
        assertTrue(exo.getChildrenId().containsValue(chen));
        assertTrue(exo.getChildrenId().containsValue(baekhyun));
        assertTrue(exo.getChildrenId().containsValue(xiumin));
    }

    @Test
    @DisplayName("remove() : 게시판 안에 있는 카테고리를 삭제합니다.")
    void remove() {
        manager.remove(exo, noticeExo);
        manager.remove(exo, chen);
        manager.remove(exo, baekhyun);
        manager.remove(exo, xiumin);

        assertFalse(exo.getChildrenId().containsValue(noticeExo));
        assertFalse(exo.getChildrenId().containsValue(chen));
        assertFalse(exo.getChildrenId().containsValue(baekhyun));
        assertFalse(exo.getChildrenId().containsValue(xiumin));
    }

    @Test
    @DisplayName("searchByIdOrTitle() : 게시판 안에 있는 카테고리를 id로 검색합니다.")
    void searchByIdOrTitle_id() {
        manager.insertRoot(man);

        Category manSearch = manager.searchByIdOrTitle("1");

        assertTrue(man.equals(manSearch));
    }

    @Test
    @DisplayName("searchByIdOrTitle() : 게시판 안에 있는 카테고리를 제목으로 검색합니다.")
    void searchByIdOrTitle_title() {
        manager.insertRoot(man);

        Category manSearch = manager.searchByIdOrTitle("MAN");

        assertTrue(man.equals(manSearch));
    }

    @Test
    @DisplayName("serializeToJson() : dto 객체를 JSON으로 직렬화합니다.")
    void serializeToJson() throws JsonProcessingException {
        manager.insertRoot(man);
        manager.insert(man, exo);
        manager.insert(exo, noticeExo);
        manager.insert(exo, chen);
        manager.insert(exo, baekhyun);
        manager.insert(exo, xiumin);

        String actualJson = manager.serializeToJson(man.toDto());
        ObjectMapper mapper = manager.getMapper();
        JsonNode manNode = mapper.readTree(actualJson);

        assertEquals(man.getId(), manNode.get("id").longValue());
        assertEquals(man.getType().toString(), manNode.get("type").asText());
        assertEquals(man.getTitle(), manNode.get("title").asText());

        JsonNode exoNode = manNode.path("subCategory");
        assertEquals(exoNode.get(0).get("title").asText(), "EXO");

        JsonNode subCategoryNode = exoNode.path("subCategory");
        for (JsonNode child : subCategoryNode) {
            Category source = null;
            String title = child.get("title").asText();

            if (title.equals("NOTICE")) source = noticeExo;
            else if (title.equals("CHEN")) source = chen;
            else if (title.equals("BAEKHYUN")) source = baekhyun;
            else if (title.equals("XIUMIN")) source = xiumin;

            assertEquals(source.getId(), child.get("id").longValue());
            assertEquals(source.getType().toString(), child.get("type").asText());
            assertEquals(source.getTitle(), title);
        }
    }
}
