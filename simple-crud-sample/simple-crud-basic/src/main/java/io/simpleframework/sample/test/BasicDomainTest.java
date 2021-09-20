package io.simpleframework.sample.test;

import io.simpleframework.crud.Repos;
import io.simpleframework.sample.domain.Person;
import io.simpleframework.sample.model.User;
import io.simpleframework.sample.model.UserAccount;
import io.simpleframework.sample.model.UserExt;
import org.junit.jupiter.api.Assertions;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public final class BasicDomainTest {

    public static void test() {
        User.rollback();
        try {
            testPerson();
        } finally {
            User.rollback();
        }
    }

    public static void testPerson() {
        // 新增
        Person person = new Person()
                .changeUserName("loyayz")
                .addAccount("loyayz_account", "123456");
        String personId = Repos.save(person);

        // 查询
        person = Repos.findById(Person.class, personId);
        Assertions.assertNotNull(person);
        User user = person.getUser();
        UserExt userExt = person.getExt();
        List<UserAccount> accounts = person.getAccounts();
        Assertions.assertNull(userExt);
        Assertions.assertEquals("loyayz", user.getName());
        Assertions.assertEquals(1, accounts.size());
        Assertions.assertEquals(personId, accounts.get(0).getUserId());
        Assertions.assertEquals("loyayz_account", accounts.get(0).getAccountName());
        Assertions.assertEquals("123456", accounts.get(0).getAccountPassword());

        // 修改
        person.changeUserName("银子")
                .changeExt("ext_str")
                .addAccount("test", "123456");
        Repos.save(person);
        Assertions.assertNotNull(person);
        person = Repos.findById(Person.class, personId);
        user = person.getUser();
        userExt = person.getExt();
        accounts = person.getAccounts();
        Assertions.assertNotNull(userExt);
        Assertions.assertEquals("银子", user.getName());
        Assertions.assertEquals(2, accounts.size());

        person.removeAccount("test");
        Repos.save(person);
        person = Repos.findById(Person.class, personId);
        accounts = person.getAccounts();
        Assertions.assertEquals(1, accounts.size());

        person.removeAccount("loyayz_account");
        Repos.save(person);
        person = Repos.findById(Person.class, personId);
        accounts = person.getAccounts();
        Assertions.assertEquals(0, accounts.size());

        // 删除
        Repos.deleteById(Person.class, personId);
        person = Repos.findById(Person.class, personId);
        Assertions.assertNull(person);
    }

}
