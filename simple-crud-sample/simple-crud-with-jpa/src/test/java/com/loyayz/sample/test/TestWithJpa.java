package com.loyayz.sample.test;

import com.loyayz.sample.Application;
import com.loyayz.sample.DemoModel;
import com.loyayz.simple.BaseMapper;
import com.loyayz.simple.helper.ModelHelper;
import com.loyayz.simple.helper.ModelInfo;
import com.loyayz.simple.mybatis.MybatisBaseMapper;
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
public class TestWithJpa {
    @Autowired
    private BaseMapper<DemoModel> modelMapper;

    @Test
    public void testMapper() {
        Assertions.assertTrue(modelMapper instanceof MybatisBaseMapper);
        Assertions.assertEquals(modelMapper, new DemoModel().mapper());
        Assertions.assertEquals(modelMapper, ModelHelper.mapper(DemoModel.class));
        ModelInfo modelInfo = ModelHelper.modelInfo(DemoModel.class);
        Assertions.assertEquals(modelInfo.modelName(), "long_id_model");
        Assertions.assertEquals(modelInfo.idField().property(), "modelId");
        Assertions.assertEquals(modelInfo.idField().column(), "id");
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
