package com.neil.easycron.dao.repository;
import com.neil.easycron.constant.enums.RoleCode;
import com.neil.easycron.dao.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByNameAndPermissions(String name, String permissions);

    Role findByCode(RoleCode code);
}
