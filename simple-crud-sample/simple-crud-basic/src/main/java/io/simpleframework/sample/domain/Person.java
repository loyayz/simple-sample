package io.simpleframework.sample.domain;

import io.simpleframework.crud.annotation.DomainEntity;
import io.simpleframework.crud.annotation.DomainValueObject;
import io.simpleframework.sample.model.User;
import io.simpleframework.sample.model.UserAccount;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Getter
public class Person {
    @DomainEntity
    private User user;
    @DomainValueObject(field = "userId")
    private List<UserAccount> accounts;

    public Person changeUserName(String name) {
        if (this.user == null) {
            this.user = new User();
        }
        this.user.setName(name);
        return this;
    }

    public Person addAccount(String accountName, String password) {
        if (this.accounts == null) {
            this.accounts = new ArrayList<>();
        }
        UserAccount account = new UserAccount();
        account.setAccountName(accountName);
        account.setAccountPassword(password);
        this.accounts.add(account);
        return this;
    }
}
