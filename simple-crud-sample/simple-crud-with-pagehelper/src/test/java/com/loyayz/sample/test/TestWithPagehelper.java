package com.loyayz.sample.test;

import com.loyayz.sample.Application;
import com.loyayz.sample.LongIdModel;
import com.loyayz.simple.Page;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@SpringBootTest(classes = Application.class)
@Transactional
@Rollback
public class TestWithPagehelper {

    @Test
    public void testQuery() {
        BasicQueryTest.test();
    }

    @Test
    public void testCommand() {
        BasicCommandTest.test();
    }

    @Test
    public void testPage() {
        LongIdModel model = new LongIdModel().setRoleId(2L);
        int pageNum = 1, pageSize = 1;
        Page<LongIdModel> page = model.pageByCondition(pageNum, pageSize);
        Assertions.assertEquals(pageNum, page.getPageNum());
        Assertions.assertEquals(pageSize, page.getPageSize());
        Assertions.assertEquals(2, page.getTotal());
        Assertions.assertEquals(2, page.getPages());
        Assertions.assertEquals(0, page.getOffset());
        Assertions.assertEquals(1, page.getItems().size());

        pageNum = 2;
        pageSize = 1;
        page = model.pageByCondition(pageNum, pageSize);
        Assertions.assertEquals(pageNum, page.getPageNum());
        Assertions.assertEquals(pageSize, page.getPageSize());
        Assertions.assertEquals(2, page.getTotal());
        Assertions.assertEquals(2, page.getPages());
        Assertions.assertEquals(1, page.getOffset());
        Assertions.assertEquals(1, page.getItems().size());

        pageNum = 1;
        pageSize = 3;
        page = model.pageByCondition(pageNum, pageSize);
        Assertions.assertEquals(pageNum, page.getPageNum());
        Assertions.assertEquals(pageSize, page.getPageSize());
        Assertions.assertEquals(2, page.getTotal());
        Assertions.assertEquals(1, page.getPages());
        Assertions.assertEquals(0, page.getOffset());
        Assertions.assertEquals(2, page.getItems().size());
    }

}
