package io.simpleframework.sample.test;

import io.simpleframework.sample.Application;
import io.simpleframework.sample.two.TwodsModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@SpringBootTest(classes = Application.class)
@Transactional
@Rollback
public class TestSpringMultidatasource {

    @Test
    public void testQuery() {
        BasicQueryTest.test();
    }

    @Test
    public void testCommand() {
        BasicCommandTest.test();
    }

    @Test
    public void testMulti() {
        Supplier<Integer> queryTotal = () -> new TwodsModel().listByCondition().size();

        int total = queryTotal.get();
        Assertions.assertTrue(new TwodsModel().insert());
        Assertions.assertEquals(total + 1, queryTotal.get());
    }

}
