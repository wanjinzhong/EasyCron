package com.neil.easycron.dao.repository;

import com.neil.easycron.constant.enums.ListCatalog;
import com.neil.easycron.dao.entity.ListBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListBoxRepository extends JpaRepository<ListBox, Integer> {
    ListBox findByCatalogAndCode(ListCatalog catalog, String code);
}