package org.xiaowu.behappy.screw.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.DnAttribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.lang.model.element.Name;

@Data
@ToString
@Entry(base = "ou=people,dc=haohaozhu,dc=com", objectClasses = "inetOrgPerson")
public class LdapUser {

    @Id
    private Name id;

    @DnAttribute(value = "uid")
    private String uid;

    @Attribute(name = "cn")
    private String cn;

    @Attribute(name = "sn")
    private String sn;

    @Attribute(name="mail")
    private String mail;

    @Attribute(name = "homedirectory")
    private String homeDirectory;

    @Attribute(name = "gidnumber")
    private String gidNumber;

    @Attribute(name = "uidnumber")
    private String uidNumber;

}
