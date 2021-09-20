package io.simpleframework.sample.test;

import io.simpleframework.crud.BaseModelMapper;
import io.simpleframework.crud.Models;
import io.simpleframework.crud.core.ConditionType;
import io.simpleframework.crud.core.Conditions;
import io.simpleframework.sample.model.LongIdModel;
import org.junit.jupiter.api.Assertions;
import org.springframework.dao.DuplicateKeyException;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.simpleframework.sample.Application.*;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public final class DynamicModelCommandTest {

    public static void test() {
        BasicCommandTest.execThanRollback(DynamicModelCommandTest::testInsert);
        BasicCommandTest.execThanRollback(DynamicModelCommandTest::testUpdate);
        BasicCommandTest.execThanRollback(DynamicModelCommandTest::testDelete);
    }

    public static BaseModelMapper<Map<String, Object>> longIdModelMapper() {
        return Models.mapper(LONG_ID_MODEL_NAME);
    }

    public static BaseModelMapper<Map<String, Object>> stringIdModelMapper() {
        return Models.mapper(STRING_ID_MODEL_NAME);
    }

    public static BaseModelMapper<Map<String, Object>> autoIdModelMapper() {
        return Models.mapper(AUTO_ID_MODEL_NAME);
    }

    public static void testInsert() {
        // 未设置id
        final Map<String, Object> longIdModel = new HashMap<>(8);
        Assertions.assertTrue(longIdModelMapper().insert(longIdModel));
        Assertions.assertNotNull(longIdModel.get("id"));
        Assertions.assertThrows(DuplicateKeyException.class, () -> longIdModelMapper().insert(longIdModel));

        final Map<String, Object> stringIdModel = new HashMap<>(8);
        Assertions.assertTrue(stringIdModelMapper().insert(stringIdModel));
        Assertions.assertEquals(32, stringIdModel.get("id").toString().length());
        Assertions.assertThrows(DuplicateKeyException.class, () -> stringIdModelMapper().insert(stringIdModel));


        // 有设置id
        longIdModel.clear();
        longIdModel.put("id", 5L);
        Assertions.assertTrue(longIdModelMapper().insert(longIdModel));
        Assertions.assertEquals(5L, longIdModel.get("id"));

        stringIdModel.clear();
        stringIdModel.put("id", "abc");
        Assertions.assertTrue(stringIdModelMapper().insert(stringIdModel));
        Assertions.assertEquals("abc", stringIdModel.get("id"));


        // 自增id
        final Map<String, Object> autoIdModel = new HashMap<>(8);
        autoIdModel.put("name", "");
        Assertions.assertTrue(autoIdModelMapper().insert(autoIdModel));
        Assertions.assertNotNull(autoIdModel.get("id"));


        // 批量新增
        String batchName = UUID.randomUUID().toString();
        List<Map<String, Object>> models = Stream.iterate(0, i -> ++i)
                .limit(ThreadLocalRandom.current().nextInt(100))
                .map(i -> {
                    Map<String, Object> model = new HashMap<>(8);
                    model.put("name", batchName);
                    return model;
                })
                .collect(Collectors.toList());
        Map<String, Object> param = new HashMap<>(8);
        param.put("name", batchName);
        Assertions.assertEquals(0, longIdModelMapper().listByCondition(param).size());
        Assertions.assertTrue(longIdModelMapper().batchInsert(models));
        Assertions.assertEquals(models.size(), longIdModelMapper().listByCondition(param).size());
        long id = Long.MIN_VALUE;
        for (Map<String, Object> model : models) {
            long currentId = Long.parseLong(model.get("id").toString());
            Assertions.assertTrue(id < currentId);
            id = currentId;
        }
    }

    public static void testUpdate() {
        // 只修改不为 null 的字段
        long id = 1L;
        Map<String, Object> model = longIdModelMapper().findById(id);
        Assertions.assertNotNull(model);
        Assertions.assertEquals(10, model.get("age"));
        Assertions.assertEquals("张三", model.get("name"));
        // 只修改 age
        model = new HashMap<>(8);
        model.put("id", id);
        model.put("age", 5);
        Assertions.assertTrue(longIdModelMapper().updateById(model));
        model = longIdModelMapper().findById(id);
        Assertions.assertNotNull(model);
        // 判断 age 被修改
        Assertions.assertEquals(5, model.get("age"));
        // 判断 name 未被修改
        Assertions.assertEquals("张三", model.get("name"));

        id = 2L;
        model = longIdModelMapper().findById(id);
        Assertions.assertNotNull(model);
        Assertions.assertEquals(20, model.get("age"));
        Assertions.assertEquals("李四", model.get("name"));
        // 只修改 name
        model = new HashMap<>(8);
        model.put("id", id);
        model.put("name", "");
        Assertions.assertTrue(longIdModelMapper().updateById(model));
        model = longIdModelMapper().findById(id);
        Assertions.assertNotNull(model);
        // 判断 age 未被修改
        Assertions.assertEquals(20, model.get("age"));
        // 判断 name 被修改
        Assertions.assertEquals("", model.get("name"));


        // 修改所有字段
        id = 3L;
        model = longIdModelMapper().findById(id);
        Assertions.assertNotNull(model);
        Assertions.assertEquals(28, model.get("age"));
        Assertions.assertEquals("王五", model.get("name"));
        // 修改 age
        model = new HashMap<>(8);
        model.put("id", id);
        model.put("age", 1);
        Assertions.assertTrue(longIdModelMapper().updateByIdWithNull(model));
        model = longIdModelMapper().findById(id);
        Assertions.assertNotNull(model);
        // 判断 age 被修改
        Assertions.assertEquals(1, model.get("age"));
        // 判断其他字段被修改为 null
        Assertions.assertNull(model.get("name"));
        Assertions.assertNull(model.get("email"));
        Assertions.assertNull(model.get("roleId"));


        // id不存在
        id = 10L;
        Assertions.assertNull(longIdModelMapper().findById(id));
        model = new HashMap<>(8);
        model.put("id", id);
        model.put("age", 5);
        Assertions.assertFalse(longIdModelMapper().updateById(model));
        Assertions.assertFalse(longIdModelMapper().updateByIdWithNull(model));


        // 根据条件修改
        id = 4L;
        model = longIdModelMapper().findById(id);
        Assertions.assertEquals(21, model.get("age"));
        Assertions.assertEquals("赵六", model.get("name"));
        model = new HashMap<>(12);
        model.put("id", 5L);
        model.put("age", 22);
        model.put("name", "赵六长大了");
        model.put("email", null);
        model.put("roleId", null);
        Conditions conditions = Conditions.of().addCondition("name", "赵六");
        Assertions.assertEquals(1, longIdModelMapper().updateByConditions(model, conditions));
        Map<String, Object> updatedModel = longIdModelMapper().findById(id);
        // 判断 age 和 name 被修改了
        Assertions.assertEquals(22, updatedModel.get("age"));
        Assertions.assertEquals("赵六长大了", updatedModel.get("name"));
        // 判断其他字段未被修改
        Assertions.assertEquals(id, updatedModel.get("id"));
        Assertions.assertNotNull(updatedModel.get("email"));
        Assertions.assertNotNull(updatedModel.get("roleId"));
    }

    public static void testDelete() {
        long id = 1L;
        Assertions.assertNotNull(longIdModelMapper().findById(1L));
        Assertions.assertTrue(longIdModelMapper().deleteById(id));
        Assertions.assertNull(longIdModelMapper().findById(1L));
        Assertions.assertFalse(longIdModelMapper().deleteById(id));
        Assertions.assertFalse(longIdModelMapper().deleteById(null));


        List<Long> modelIds = Arrays.asList(2L, 3L);
        Assertions.assertEquals(modelIds.size(), longIdModelMapper().listByIds(modelIds).size());
        Assertions.assertTrue(longIdModelMapper().deleteByIds(modelIds));
        Assertions.assertTrue(longIdModelMapper().listByIds(modelIds).isEmpty());
        Assertions.assertFalse(longIdModelMapper().deleteByIds(null));
        Assertions.assertFalse(longIdModelMapper().deleteByIds(new ArrayList<>()));
        Assertions.assertNotNull(longIdModelMapper().findById(4L));


        // 根据条件删除
        Conditions conditions = Conditions.of().addCondition(LongIdModel::getId, ConditionType.not_equal, 4L);
        Assertions.assertEquals(0, longIdModelMapper().deleteByConditions(conditions));
        Assertions.assertNotNull(longIdModelMapper().findById(4L));
        conditions = Conditions.of().addCondition(LongIdModel::getId, ConditionType.equal, 4L);
        Assertions.assertEquals(1, longIdModelMapper().deleteByConditions(conditions));
        Assertions.assertNull(longIdModelMapper().findById(4L));
    }

}
