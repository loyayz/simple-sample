package com.loyayz.sample.test;

import com.loyayz.sample.AutoIdModel;
import com.loyayz.sample.LongIdModel;
import com.loyayz.sample.StringIdModel;
import org.junit.jupiter.api.Assertions;
import org.springframework.dao.DuplicateKeyException;

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
        testSave();
        testInsert();
        testUpdate();
        testDelete();
    }

    public static void testSave() {
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

    public static void testInsert() {
        // 未设置id
        LongIdModel longIdModel = new LongIdModel();
        Assertions.assertNull(longIdModel.getId());
        Assertions.assertTrue(longIdModel.insert());
        Assertions.assertNotNull(longIdModel.getId());
        Assertions.assertThrows(DuplicateKeyException.class, longIdModel::insert);

        StringIdModel stringIdModel = new StringIdModel();
        Assertions.assertNull(stringIdModel.id());
        Assertions.assertTrue(stringIdModel.insert());
        Assertions.assertEquals(32, stringIdModel.id().length());
        Assertions.assertThrows(DuplicateKeyException.class, stringIdModel::insert);


        // 有设置id
        longIdModel = new LongIdModel(5L);
        Assertions.assertTrue(longIdModel.insert());
        Assertions.assertEquals(5L, longIdModel.getId());

        stringIdModel = new StringIdModel("abc");
        Assertions.assertTrue(stringIdModel.insert());
        Assertions.assertEquals("abc", stringIdModel.id());


        // 自增id
        AutoIdModel autoIdModel = AutoIdModel.createNewModel();
        Assertions.assertNull(autoIdModel.getId());
        Assertions.assertTrue(autoIdModel.insert());
        Assertions.assertNotNull(autoIdModel.getId());


        // 批量新增
        String batchName = UUID.randomUUID().toString();
        List<LongIdModel> models = Stream.iterate(0, i -> ++i)
                .limit(ThreadLocalRandom.current().nextInt(100))
                .map(i -> new LongIdModel().setName(batchName))
                .collect(Collectors.toList());
        Assertions.assertEquals(0, new LongIdModel().setName(batchName).listByCondition().size());
        Assertions.assertTrue(new LongIdModel().batchInsert(models));
        Assertions.assertEquals(models.size(), new LongIdModel().setName(batchName).listByCondition().size());
        long id = Long.MIN_VALUE;
        for (LongIdModel model : models) {
            Assertions.assertTrue(id < model.getId());
            id = model.getId();
        }
    }

    public static void testUpdate() {
        // 只修改不为 null 的字段
        long id = 1L;
        Assertions.assertEquals(10, new LongIdModel().findById(id).getAge());
        Assertions.assertEquals("张三", new LongIdModel().findById(id).getName());
        // 只修改 age
        Assertions.assertTrue(new LongIdModel(id).setAge(5).updateById());
        // 判断 age 被修改
        Assertions.assertEquals(5, new LongIdModel().findById(id).getAge());
        // 判断 name 未被修改
        Assertions.assertEquals("张三", new LongIdModel().findById(id).getName());

        id = 2L;
        Assertions.assertEquals(20, new LongIdModel().findById(id).getAge());
        Assertions.assertEquals("李四", new LongIdModel().findById(id).getName());
        // 只修改 name
        Assertions.assertTrue(new LongIdModel(id).setName("").updateById());
        // 判断 age 未被修改
        Assertions.assertEquals(20, new LongIdModel().findById(id).getAge());
        // 判断 name 被修改
        Assertions.assertEquals("", new LongIdModel().findById(id).getName());


        // 修改所有字段
        id = 3L;
        Assertions.assertEquals(28, new LongIdModel().findById(id).getAge());
        Assertions.assertEquals("王五", new LongIdModel().findById(id).getName());
        // 修改 age
        Assertions.assertTrue(new LongIdModel(id).setAge(1).updateByIdWithNull());
        LongIdModel model = new LongIdModel().findById(id);
        // 判断 age 被修改
        Assertions.assertEquals(1, model.getAge());
        // 判断其他字段被修改为 null
        Assertions.assertNull(model.getName());
        Assertions.assertNull(model.getEmail());
        Assertions.assertNull(model.getRoleId());


        // id不存在
        id = 10L;
        Assertions.assertNull(new LongIdModel().findById(id));
        Assertions.assertFalse(new LongIdModel(id).setAge(5).updateById());
        Assertions.assertFalse(new LongIdModel(id).setAge(5).updateByIdWithNull());
    }

    public static void testDelete() {
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
    }

}
