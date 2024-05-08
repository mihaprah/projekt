package com.scm.scm.tenant.dao;

import com.scm.scm.tenant.vao.Tenant;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TenantRepository extends MongoRepository<Tenant, String> {
}
