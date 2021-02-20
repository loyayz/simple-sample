package com.loyayz.sample.test;

import com.loyayz.sample.Application;
import com.loyayz.sample.LongIdModel;
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
public class TestWithoutJpa {
    @Autowired
    private BaseMapper<LongIdModel> modelMapper;

    @Test
    public void testMapper() {
        Assertions.assertTrue(modelMapper instanceof MybatisBaseMapper);
        Assertions.assertEquals(modelMapper, new LongIdModel().mapper());
        Assertions.assertEquals(modelMapper, ModelHelper.mapper(LongIdModel.class));
        ModelInfo modelInfo = ModelHelper.modelInfo(LongIdModel.class);
        Assertions.assertEquals(modelInfo.idField().property(), "id");
        Assertions.assertEquals(modelInfo.idField().column(), "id");
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
