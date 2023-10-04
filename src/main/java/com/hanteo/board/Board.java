package com.hanteo.board;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static com.hanteo.board.CategoryType.GENERAL;
import static com.hanteo.board.CategoryType.NOTICE;
import static com.hanteo.board.CategoryType.SUB;

public class Board {
    public static void main(String[] args) throws JsonProcessingException {
        CategoryManager manager = new CategoryManager(new ObjectMapper());
        
        Category anonymous = CategoryType.getAnonymous();
        Category man = Category.createRootCategory(1L, "MAN");

        Category exo = Category.createCategoryWithTitle(10L, SUB, "EXO");
        Category noticeExo = Category.createCategoryWithTitle(100L, NOTICE, "[NOTICE] EXO");
        Category chen = Category.createCategoryWithTitle(101L, GENERAL, "CHEN");
        Category baekhyun = Category.createCategoryWithTitle(102L, GENERAL, "BAEKHYUN");
        Category xiumin = Category.createCategoryWithTitle(103L, GENERAL, "XIUMIN");

        Category bts = Category.createCategoryWithTitle(11L, SUB, "BTS");
        Category noticeBts = Category.createCategoryWithTitle(110L, NOTICE, "[NOTICE] BTS");
        Category v = Category.createCategoryWithTitle(111L, GENERAL, "V");

        Category woman = Category.createRootCategory(2L, "WOMAN");
        Category blackpink = Category.createCategoryWithTitle(20L, SUB, "BLACKPINK");
        Category noticeBlackpink = Category.createCategoryWithTitle(200L, NOTICE, "[NOTICE] BLACKPINK");
        Category rose = Category.createCategoryWithTitle(201L, GENERAL, "ROSE");

        manager.insertRoot(man);
        manager.insert(man, exo);
        manager.insert(exo, noticeExo);
        manager.insert(exo, chen);
        manager.insert(exo, baekhyun);
        manager.insert(exo, xiumin);

        manager.insert(man, bts);
        manager.insert(bts, noticeBts);
        manager.insert(bts, anonymous);
        manager.insert(bts, v);

        manager.insertRoot(woman);
        manager.insert(woman, blackpink);
        manager.insert(blackpink, noticeBlackpink);
        manager.insert(blackpink, anonymous);
        manager.insert(blackpink, rose);

        CategoryDto rDto = manager.getRoot().toDto();
        System.out.println(manager.serializeToJson(rDto));
    }
}
