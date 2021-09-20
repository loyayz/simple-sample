package io.simpleframework.sample;

import io.simpleframework.sample.mapper.DemoMapper;
import io.simpleframework.sample.model.Demo;
import io.simpleframework.sample.test.BasicCommandTest;
import io.simpleframework.sample.test.BasicPageTest;
import io.simpleframework.sample.test.BasicQueryTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TestSpringMybatisplus {
    @Autowired
    private DemoMapper demoMapper;

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
        BasicPageTest.test();
    }

    @Test
    public void testMybatisPlusInsert() {
        Supplier<Integer> queryTotal = () -> this.demoMapper.selectList(null).size();
        int total = queryTotal.get();

        this.demoMapper.insert(new Demo());
        Assertions.assertEquals(total + 1, queryTotal.get());
    }

}
