package com.loyayz.sample.test;

import com.loyayz.sample.LongIdModel;
import com.loyayz.sample.StringIdModel;
import com.loyayz.simple.Sorter;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public final class BasicQueryTest {

    public static void test() {
        findById();
        listByIds();
        listByCondition();
        testCountAndExist();
    }

    /**
     * 根据id查询
     */
    public static void findById() {
        LongIdModel longIdModel = new LongIdModel().findById(2L);
        Assertions.assertEquals(2L, longIdModel.getId());
        Assertions.assertEquals("李四", longIdModel.getName());
        Assertions.assertEquals(20, longIdModel.getAge());
        Assertions.assertEquals("test2@loyayz.com", longIdModel.getEmail());
        Assertions.assertEquals(2L, longIdModel.getRoleId());

        StringIdModel stringIdModel = new StringIdModel().findById("CCC");
        Assertions.assertEquals("王五", stringIdModel.name());
        Assertions.assertNotNull(stringIdModel.gmtCreate());
        Assertions.assertNotNull(stringIdModel.gmtModified());

        Assertions.assertNull(new LongIdModel().findById(null));
        Assertions.assertNull(new LongIdModel().findById(10L));
        Assertions.assertNull(new StringIdModel().findById(null));
        Assertions.assertNull(new StringIdModel().findById(""));
    }

    /**
     * 根据ids查询
     */
    public static void listByIds() {
        Assertions.assertArrayEquals(
                new LongIdModel().listByIds(Arrays.asList(1L, 2L, 3L, 4L)).toArray(),
                new Object[]{
                        new LongIdModel().findById(1L),
                        new LongIdModel().findById(2L),
                        new LongIdModel().findById(3L),
                        new LongIdModel().findById(4L)
                }
        );
        Assertions.assertArrayEquals(
                new LongIdModel().listByIds(Arrays.asList(2L, 4L)).toArray(),
                new Object[]{
                        new LongIdModel().findById(2L),
                        new LongIdModel().findById(4L),
                }
        );
        Assertions.assertArrayEquals(
                new StringIdModel().listByIds(Arrays.asList("BBB","CCC")).toArray(),
                new Object[]{
                        new StringIdModel().findById("BBB"),
                        new StringIdModel().findById("CCC")
                }
        );
        Assertions.assertTrue(new LongIdModel().listByIds(null).isEmpty());
        Assertions.assertTrue(new LongIdModel().listByIds(new ArrayList<>()).isEmpty());
        Assertions.assertTrue(new StringIdModel().listByIds(null).isEmpty());
        Assertions.assertTrue(new StringIdModel().listByIds(new ArrayList<>()).isEmpty());
    }

    /**
     * 条件查询
     */
    public static void listByCondition() {
        // 无条件查询（查全部）
        Assertions.assertAll(
                () -> Assertions.assertArrayEquals(
                        new LongIdModel().listByCondition().toArray(),
                        new LongIdModel().listByIds(Arrays.asList(1, 2, 3, 4)).toArray()
                ),
                // id 倒序
                () -> Assertions.assertArrayEquals(
                        new LongIdModel().listByCondition(Sorter.desc(LongIdModel::getId)).toArray(),
                        new LongIdModel().listByCondition(Sorter.desc(LongIdModel::setId)).toArray()
                ),
                () -> Assertions.assertArrayEquals(
                        new LongIdModel().listByCondition(Sorter.desc(LongIdModel::getId)).toArray(),
                        new Object[]{
                                new LongIdModel().findById(4L),
                                new LongIdModel().findById(3L),
                                new LongIdModel().findById(2L),
                                new LongIdModel().findById(1L)
                        }
                ),
                // roleId 倒序
                () -> Assertions.assertArrayEquals(
                        new LongIdModel().listByCondition(Sorter.desc(LongIdModel::getRoleId)).toArray(),
                        new Object[]{
                                new LongIdModel().findById(4L),
                                new LongIdModel().findById(2L),
                                new LongIdModel().findById(3L),
                                new LongIdModel().findById(1L)
                        }
                ),
                // roleId 倒序 + id 倒序
                () -> Assertions.assertArrayEquals(
                        new LongIdModel().listByCondition(Sorter.desc(LongIdModel::getRoleId), Sorter.desc(LongIdModel::getId)).toArray(),
                        new Object[]{
                                new LongIdModel().findById(4L),
                                new LongIdModel().findById(3L),
                                new LongIdModel().findById(2L),
                                new LongIdModel().findById(1L)
                        }
                )
        );
        // 查无数据
        Assertions.assertTrue(new LongIdModel(10L).listByCondition().isEmpty());
        // 单条件查询
        LongIdModel queryModel = new LongIdModel().setRoleId(2L);
        Assertions.assertAll(
                () -> Assertions.assertArrayEquals(
                        queryModel.listByCondition().toArray(),
                        new LongIdModel().listByIds(Arrays.asList(2, 3)).toArray()
                ),
                // id 倒序
                () -> Assertions.assertArrayEquals(
                        queryModel.listByCondition(Sorter.desc(LongIdModel::getId)).toArray(),
                        new Object[]{
                                new LongIdModel().findById(3L),
                                new LongIdModel().findById(2L),
                        }
                ),
                // age 升序
                () -> Assertions.assertArrayEquals(
                        queryModel.listByCondition(Sorter.asc(LongIdModel::getAge)).toArray(),
                        new LongIdModel().listByIds(Arrays.asList(2, 3)).toArray()
                ),
                // age 倒序
                () -> Assertions.assertArrayEquals(
                        queryModel.listByCondition(Sorter.desc(LongIdModel::getAge)).toArray(),
                        new Object[]{
                                new LongIdModel().findById(3L),
                                new LongIdModel().findById(2L),
                        }
                )
        );
        // 多条件查询
        queryModel.setName("李四");
        List<LongIdModel> models = queryModel.listByCondition();
        Assertions.assertEquals(1, models.size());
        Assertions.assertEquals("李四", models.get(0).getName());
    }

    public static void testCountAndExist() {
        // 无条件查询统计
        Assertions.assertAll(
                () -> Assertions.assertEquals(4, new LongIdModel().countByCondition()),
                () -> Assertions.assertTrue(new LongIdModel().existByCondition())
        );
        // 单条件查询统计
        Assertions.assertAll(
                () -> Assertions.assertEquals(1, new LongIdModel(1L).countByCondition()),
                () -> Assertions.assertEquals(1, new LongIdModel().setName("李四").countByCondition()),
                () -> Assertions.assertEquals(2, new LongIdModel().setRoleId(2L).countByCondition()),
                () -> Assertions.assertTrue(new LongIdModel(1L).existByCondition()),
                () -> Assertions.assertTrue(new LongIdModel().setName("李四").existByCondition()),
                () -> Assertions.assertTrue(new LongIdModel().setRoleId(2L).existByCondition())
        );
        // 多条件查询统计
        Assertions.assertAll(
                () -> Assertions.assertEquals(1, new LongIdModel(1L).setName("张三").countByCondition()),
                () -> Assertions.assertEquals(0, new LongIdModel(1L).setName("李四").countByCondition()),
                () -> Assertions.assertTrue(new LongIdModel(1L).setName("张三").existByCondition()),
                () -> Assertions.assertFalse(new LongIdModel(1L).setName("李四").existByCondition())
        );
    }

}
