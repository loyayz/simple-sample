package io.simpleframework.sample.test;

import io.simpleframework.crud.BaseModelMapper;
import io.simpleframework.crud.ModelField;
import io.simpleframework.crud.ModelInfo;
import io.simpleframework.crud.Models;
import io.simpleframework.crud.mapper.mybatis.MybatisModelMapper;
import io.simpleframework.sample.Application;
import io.simpleframework.sample.model.AutoIdModel;
import io.simpleframework.sample.model.LongIdModel;
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
public class TestSpring {
    @Autowired
    private BaseModelMapper<LongIdModel> modelMapper;

    @Test
    public void testMapper() {
        Assertions.assertTrue(modelMapper instanceof MybatisModelMapper);
        Assertions.assertEquals(modelMapper, new LongIdModel().mapper());
        Assertions.assertEquals(modelMapper, Models.mapper(LongIdModel.class));
        ModelInfo<LongIdModel> modelInfo = Models.info(LongIdModel.class);
        Assertions.assertEquals(modelInfo.name(), "long_id_model");

        ModelField modelIdField = modelInfo.id();
        Assertions.assertTrue(modelIdField.insertable());
        Assertions.assertFalse(modelIdField.updatable());
        Assertions.assertEquals(modelIdField.fieldName(), "id");
        Assertions.assertEquals(modelIdField.column(), "id");

        modelInfo = Models.info(AutoIdModel.class);
        modelIdField = modelInfo.id();
        Assertions.assertFalse(modelIdField.insertable());
        Assertions.assertFalse(modelIdField.updatable());
    }

    @Test
    public void testQuery() {
        BasicQueryTest.test();

        Assertions.assertThrows(IllegalArgumentException.class, () -> new LongIdModel().pageByCondition(1, 10));
    }

    @Test
    public void testCommand() {
        BasicCommandTest.test();
    }

}
