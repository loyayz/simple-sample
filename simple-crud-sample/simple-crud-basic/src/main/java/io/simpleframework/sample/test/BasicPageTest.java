package io.simpleframework.sample.test;

import io.simpleframework.crud.core.Page;
import io.simpleframework.sample.model.LongIdModel;
import org.junit.jupiter.api.Assertions;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public final class BasicPageTest {

    public static void test() {
        BasicCommandTest.execThanRollback(null);

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
