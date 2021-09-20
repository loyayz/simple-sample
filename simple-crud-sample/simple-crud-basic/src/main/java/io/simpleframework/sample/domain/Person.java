package io.simpleframework.sample.domain;

import io.simpleframework.crud.annotation.DomainEntity;
import io.simpleframework.crud.annotation.DomainValueObject;
import io.simpleframework.sample.model.User;
import io.simpleframework.sample.model.UserAccount;
import io.simpleframework.sample.model.UserExt;
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
    private UserExt ext;
    @DomainValueObject(field = "userId")
    private List<UserAccount> accounts;

    public Person changeUserName(String name) {
        if (this.user == null) {
            this.user = new User();
        }
        this.user.setName(name);
        return this;
    }

    public Person changeExt(String ext) {
        if (this.ext == null) {
            this.ext = new UserExt();
        }
        this.ext.setExt(ext);
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

    public Person removeAccount(String accountName) {
        if (this.accounts == null) {
            return this;
        }
        this.accounts.removeIf(a -> accountName.equals(a.getAccountName()));
        return this;
    }

}
