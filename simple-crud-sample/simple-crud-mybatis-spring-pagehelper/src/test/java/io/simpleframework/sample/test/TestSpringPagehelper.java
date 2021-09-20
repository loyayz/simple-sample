package io.simpleframework.sample.test;

import io.simpleframework.sample.Application;
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
public class TestSpringPagehelper {

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

}
