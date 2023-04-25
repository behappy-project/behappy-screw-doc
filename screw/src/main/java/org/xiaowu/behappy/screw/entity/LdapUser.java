package org.xiaowu.behappy.screw.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.DnAttribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;

/**
 * todo LdapUser未用到
 * @author xiaowu
 */
@Data
@ToString
@Entry(base = "ou=people,dc=extension,dc=net", objectClasses = "inetOrgPerson")
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

    @Attribute(name="employeeNumber")
    private String employeeNumber;

    @Attribute(name="mobile")
    private String mobile;

    @Attribute(name="postalAddress")
    private String postalAddress;

    @Attribute(name="description")
    private String description;

    @Attribute(name="objectClass")
    private String objectClass;

    @Attribute(name="userPassword")
    private String userPassword;

    @Attribute(name="businessCategory")
    private String businessCategory;

    @Attribute(name="givenName")
    private String givenName;

    @Attribute(name="displayName")
    private String displayName;

    @Attribute(name="departmentNumber")
    private String departmentNumber;

}
