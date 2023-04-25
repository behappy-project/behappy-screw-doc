package org.xiaowu.behappy.screw;

import cn.hutool.crypto.SecureUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.ContainerCriteria;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.stereotype.Service;
import org.xiaowu.behappy.screw.entity.LdapUser;
import org.xiaowu.behappy.screw.entity.User;
import org.xiaowu.behappy.screw.repository.LdapUserRepository;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import java.util.Iterator;
import java.util.List;


/**
 *
 * @author xiaowu
 */
@Service
public class TestLdapService {

    @Autowired
    private LdapUserRepository ldapUserRepository;

    @Autowired
    private LdapTemplate ldapTemplate;

    public void findAll() {
        ldapUserRepository.findAll().forEach(System.out::println);
    }

    public void findUserByUid(String uid) {
        ContainerCriteria query = LdapQueryBuilder.query()
                .where("uid")
                .is(uid);
        LdapUser ldapUser = ldapTemplate.findOne(query, LdapUser.class);
        System.out.println(ldapUser);
    }

    public void authenticationTest() {
        String uid = "xiaowu";
        LdapUser authenticate = ldapTemplate.authenticate(
                LdapQueryBuilder.query().where("uid").is(uid),
                "123456",
                (dirContext, ldapEntryIdentification) ->
                        ldapTemplate.findOne(LdapQueryBuilder.query().where("uid").is(uid), LdapUser.class));
        System.out.println(authenticate);
    }


    @SneakyThrows
    public void findUser() {
        DirContextAdapter obj = (DirContextAdapter) ldapTemplate.lookup("uid=xiaowu,ou=people");
        NamingEnumeration<? extends Attribute> namingEnumeration = obj.getAttributes().getAll();
        Iterator<? extends Attribute> iterator = namingEnumeration.asIterator();
        while (iterator.hasNext()) {
            Attribute attribute = iterator.next();
            System.out.println(attribute.getID() + " - " + attribute.get());
        }
    }

    public void findUserByQuery(){
        ContainerCriteria query = LdapQueryBuilder.query()
                .where("uid")
                .is("xiaowu")
                .and(LdapQueryBuilder.query()
                        .where("userPassword")
                        .is("123456"));
        List<User> list = ldapTemplate.search(query,
                (AttributesMapper<User>) attrs -> {
                    User ldapUser = new User();
                    String userPassword = new String((byte[]) attrs.get("userPassword").get());
                    ldapUser.setPassword(SecureUtil.md5(userPassword));
                    return ldapUser;
                });
        System.out.println(list);
    }
}
