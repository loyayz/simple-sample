package io.simpleframework.sample.test;

import io.simpleframework.crud.Models;
import io.simpleframework.crud.core.DatasourceType;
import io.simpleframework.crud.core.IdType;
import io.simpleframework.crud.info.dynamic.DynamicModelInfo;
import io.simpleframework.sample.Application;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static io.simpleframework.sample.Application.*;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@SpringBootTest(classes = Application.class)
@Transactional
@Rollback
public class TestDynamicModel {

    static final DynamicModelInfo longIdModel =
            new DynamicModelInfo(LONG_ID_MODEL_NAME, DatasourceType.Mybatis)
                    .setId("id")
                    .addField("name", String.class)
                    .addField("age", Integer.class)
                    .addField("email", String.class)
                    .addField("roleId", Long.class);
    static final DynamicModelInfo stringIdModel =
            new DynamicModelInfo(STRING_ID_MODEL_NAME, DatasourceType.Mybatis)
                    .setId("id", String.class, IdType.UUID32)
                    .addField("name", String.class)
                    .addField("gmtCreate", Date.class);
    static final DynamicModelInfo autoIdModel =
            new DynamicModelInfo(AUTO_ID_MODEL_NAME, DatasourceType.Mybatis)
                    .setId("id", String.class, IdType.AUTO_INCREMENT)
                    .addField("name", String.class);

    @BeforeEach
    public void before() {
        Models.register(longIdModel);
        Models.register(stringIdModel);
        Models.register(autoIdModel);
    }

    @Test
    public void testQuery() {
        BasicQueryTest.test();
    }

    @Test
    public void testDynamicQuery() {
        DynamicModelQueryTest.test();
    }

    @Test
    public void testCommand() {
        BasicCommandTest.test();
    }

    @Test
    public void testDynamicCommand() {
        DynamicModelCommandTest.test();
    }

    @Test
    public void testRegister() {
        Assertions.assertSame(longIdModel, Models.info(LONG_ID_MODEL_NAME));
        Assertions.assertSame(stringIdModel, Models.info(STRING_ID_MODEL_NAME));
    }

}
