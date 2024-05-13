package com.scm.scm.tenant.dao;

import com.scm.scm.tenant.vao.Tenant;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface TenantRepository extends MongoRepository<Tenant, String> {
    Tenant findByTenantUniqueName(String tenantUniqueName);
    List<Tenant> findByUsersContaining(String username);
}
