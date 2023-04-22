package org.xiaowu.behappy.screw.repository;

import org.springframework.data.ldap.repository.LdapRepository;
import org.springframework.stereotype.Repository;
import org.xiaowu.behappy.screw.entity.LdapUser;

@Repository
public interface LdapUserRepository extends LdapRepository<LdapUser> {
}
