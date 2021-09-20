package io.simpleframework.sample.test;

import io.simpleframework.crud.BaseModelMapper;
import io.simpleframework.crud.ModelField;
import io.simpleframework.crud.ModelInfo;
import io.simpleframework.crud.Models;
import io.simpleframework.crud.mapper.mybatis.MybatisModelMapper;
import io.simpleframework.sample.Application;
import io.simpleframework.sample.DemoModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@SpringBootTest(classes = Application.class)
@Transactional
@Rollback
public class TestSpringJpa {
    @Autowired
    private BaseModelMapper<DemoModel> modelMapper;

    @Test
    public void testMapper() {
        Assertions.assertTrue(modelMapper instanceof MybatisModelMapper);
        Assertions.assertEquals(modelMapper, new DemoModel().mapper());
        Assertions.assertEquals(modelMapper, Models.mapper(DemoModel.class));
        ModelInfo<DemoModel> modelInfo = Models.info(DemoModel.class);
        Assertions.assertEquals(modelInfo.name(), "long_id_model");

        ModelField modelIdField = modelInfo.id();
        Assertions.assertTrue(modelIdField.insertable());
        Assertions.assertFalse(modelIdField.updatable());
        Assertions.assertEquals(modelIdField.fieldName(), "modelId");
        Assertions.assertEquals(modelIdField.column(), "id");
    }

    @Test
    public void testQuery() {
        BasicQueryTest.test();
    }

    @Test
    public void testCommand() {
        BasicCommandTest.test();
    }

}
