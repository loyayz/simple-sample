package io.simpleframework.sample.test;

import io.simpleframework.crud.core.ConditionType;
import io.simpleframework.crud.core.Conditions;
import io.simpleframework.sample.model.AutoIdModel;
import io.simpleframework.sample.model.LongIdModel;
import io.simpleframework.sample.model.StringIdModel;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public final class BasicCommandTest {

    public static void test() {
        execThanRollback(null);
        execThanRollback(BasicCommandTest::testSave);
        execThanRollback(BasicCommandTest::testInsert);
        execThanRollback(BasicCommandTest::testUpdate);
        execThanRollback(BasicCommandTest::testDelete);
        BasicDomainTest.test();
    }

    public static void execThanRollback(Func func) {
        try {
            if (func != null) {
                func.exec();
            }
        } finally {
            AutoIdModel.rollback();
            LongIdModel.rollback();
            StringIdModel.rollback();
        }
    }

    private static void testSave() {
        Supplier<Integer> queryTotal = () -> new LongIdModel().listByCondition().size();
        int total = queryTotal.get();
        // 无 id 保存 = 新增
        Assertions.assertTrue(new LongIdModel().save());
        Assertions.assertEquals(total + 1, queryTotal.get());
        total = queryTotal.get();

        // 有 id 保存 = 修改
        Assertions.assertTrue(new LongIdModel().setId(1L).setEmail("").save());
        Assertions.assertEquals(total, queryTotal.get());
        Assertions.assertFalse(new LongIdModel().setId(1000L).setEmail("").save());
    }

    private static void testInsert() {
        // 未设置id
        LongIdModel longIdModel = new LongIdModel();
        Assertions.assertNull(longIdModel.getId());
        Assertions.assertTrue(longIdModel.insert());
        Assertions.assertNotNull(longIdModel.getId());
        try {
            longIdModel.insert();
            Assertions.fail();
        } catch (Exception ignored) {
        }

        StringIdModel stringIdModel = new StringIdModel();
        Assertions.assertNull(stringIdModel.getId());
        Assertions.assertTrue(stringIdModel.insert());
        Assertions.assertEquals(32, stringIdModel.getId().length());
        try {
            stringIdModel.insert();
            Assertions.fail();
        } catch (Exception ignored) {
        }

        // 有设置id
        longIdModel = new LongIdModel(5L);
        Assertions.assertTrue(longIdModel.insert());
        Assertions.assertEquals(5L, longIdModel.getId());

        stringIdModel = new StringIdModel("abc");
        Assertions.assertTrue(stringIdModel.insert());
        Assertions.assertEquals("abc", stringIdModel.getId());


        // 自增id
        AutoIdModel autoIdModel = AutoIdModel.createNewModel();
        Assertions.assertNull(autoIdModel.getId());
        Assertions.assertTrue(autoIdModel.insert());
        Assertions.assertNotNull(autoIdModel.getId());


        // 批量新增
        String batchName = UUID.randomUUID().toString();
        List<LongIdModel> models = Stream.iterate(0, i -> ++i)
                .limit(ThreadLocalRandom.current().nextInt(100))
                .map(i -> new LongIdModel().setUserName(batchName))
                .collect(Collectors.toList());
        Assertions.assertEquals(0, new LongIdModel().setUserName(batchName).listByCondition().size());
        Assertions.assertTrue(new LongIdModel().batchInsert(models));
        Assertions.assertEquals(models.size(), new LongIdModel().setUserName(batchName).listByCondition().size());
        long id = Long.MIN_VALUE;
        for (LongIdModel model : models) {
            Assertions.assertTrue(id < model.getId());
            id = model.getId();
        }
    }

    private static void testUpdate() {
        // 只修改不为 null 的字段
        long id = 1L;
        Assertions.assertEquals(10, new LongIdModel().findById(id).getAge());
        Assertions.assertEquals("张三", new LongIdModel().findById(id).getUserName());
        // 只修改 age
        Assertions.assertTrue(new LongIdModel(id).setAge(5).updateById());
        // 判断 age 被修改
        Assertions.assertEquals(5, new LongIdModel().findById(id).getAge());
        // 判断 name 未被修改
        Assertions.assertEquals("张三", new LongIdModel().findById(id).getUserName());

        id = 2L;
        Assertions.assertEquals(20, new LongIdModel().findById(id).getAge());
        Assertions.assertEquals("李四", new LongIdModel().findById(id).getUserName());
        // 只修改 name
        Assertions.assertTrue(new LongIdModel(id).setUserName("").updateById());
        // 判断 age 未被修改
        Assertions.assertEquals(20, new LongIdModel().findById(id).getAge());
        // 判断 name 被修改
        Assertions.assertEquals("", new LongIdModel().findById(id).getUserName());


        // 修改所有字段
        id = 3L;
        Assertions.assertEquals(28, new LongIdModel().findById(id).getAge());
        Assertions.assertEquals("王五", new LongIdModel().findById(id).getUserName());
        // 修改 age
        Assertions.assertTrue(new LongIdModel(id).setAge(1).updateByIdWithNull());
        LongIdModel model = new LongIdModel().findById(id);
        // 判断 age 被修改
        Assertions.assertEquals(1, model.getAge());
        // 判断其他字段被修改为 null
        Assertions.assertNull(model.getUserName());
        Assertions.assertNull(model.getEmail());
        Assertions.assertNull(model.getRoleId());


        // id不存在
        id = 10L;
        Assertions.assertNull(new LongIdModel().findById(id));
        Assertions.assertFalse(new LongIdModel(id).setAge(5).updateById());
        Assertions.assertFalse(new LongIdModel(id).setAge(5).updateByIdWithNull());


        // 根据条件修改
        id = 4L;
        model = new LongIdModel().findById(id);
        Assertions.assertEquals(21, model.getAge());
        Assertions.assertEquals("赵六", model.getUserName());
        model.setId(5L);
        model.setAge(22);
        model.setUserName("赵六长大了");
        model.setEmail(null);
        model.setRoleId(null);
        Conditions conditions = Conditions.of().addCondition(LongIdModel::getUserName, "赵六");
        Assertions.assertEquals(1, model.updateByConditions(conditions));
        LongIdModel updatedModel = new LongIdModel().findById(id);
        // 判断 age 和 name 被修改了
        Assertions.assertEquals(22, updatedModel.getAge());
        Assertions.assertEquals("赵六长大了", updatedModel.getUserName());
        // 判断其他字段未被修改
        Assertions.assertEquals(id, updatedModel.getId());
        Assertions.assertNotNull(updatedModel.getEmail());
        Assertions.assertNotNull(updatedModel.getRoleId());
    }

    private static void testDelete() {
        long id = 1L;
        LongIdModel model = new LongIdModel();
        Assertions.assertNotNull(model.findById(1L));
        Assertions.assertTrue(model.deleteById(id));
        Assertions.assertNull(model.findById(1L));
        Assertions.assertFalse(model.deleteById(id));
        Assertions.assertFalse(model.deleteById(null));


        List<Long> modelIds = Arrays.asList(2L, 3L);
        Assertions.assertEquals(modelIds.size(), new LongIdModel().listByIds(modelIds).size());
        Assertions.assertTrue(model.deleteByIds(modelIds));
        Assertions.assertTrue(model.listByIds(modelIds).isEmpty());
        Assertions.assertFalse(model.deleteByIds(null));
        Assertions.assertFalse(model.deleteByIds(new ArrayList<>()));
        Assertions.assertNotNull(model.findById(4L));


        // 根据条件删除
        Conditions conditions = Conditions.of().addCondition(LongIdModel::getId, ConditionType.not_equal, 4L);
        Assertions.assertEquals(0, model.deleteByConditions(conditions));
        Assertions.assertNotNull(model.findById(4L));
        conditions = Conditions.of().addCondition(LongIdModel::getId, ConditionType.equal, 4L);
        Assertions.assertEquals(1, model.deleteByConditions(conditions));
        Assertions.assertNull(model.findById(4L));
    }

    public interface Func {
        void exec();
    }

}
