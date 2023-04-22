package org.xiaowu.behappy.screw;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.stereotype.Service;
import org.xiaowu.behappy.screw.entity.LdapUser;
import org.xiaowu.behappy.screw.repository.LdapUserRepository;

import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

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

    public void findAll2() {
        LdapUser ldapUser = ldapTemplate.findOne(query().where("uid").is("ldapuser2"), LdapUser.class);
        System.out.println(ldapUser);
    }

    public void authenticationTest() {
        String uid = "ldapuser2";
        LdapUser authenticate = ldapTemplate.authenticate(
                query().where("uid").is(uid),
                "hadoop",
                (dirContext, ldapEntryIdentification) ->
                        ldapTemplate.findOne(query().where("uid").is(uid), LdapUser.class));
        System.out.println(authenticate);
    }


    public void findUser() {
        DirContextAdapter obj = (DirContextAdapter) ldapTemplate.lookup("uid=user01,ou=People");
        System.out.println(obj);
    }

    public void t2(){
        DirContextAdapter obj = (DirContextAdapter) ldapTemplate.lookup("uid=ben,ou=People");
        System.out.println(obj);
    }


    public void t3(){
        List<String> list = ldapTemplate.search(query().where("objectClass").is("person"), (AttributesMapper<String>) attrs -> (String) attrs.get("cn").get());
        System.out.println(list);//[faysona, user01, service]
    }
}
