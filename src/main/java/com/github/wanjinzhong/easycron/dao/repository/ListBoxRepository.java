package com.github.wanjinzhong.easycron.dao.repository;

import java.util.List;

import com.github.wanjinzhong.easycron.constant.enums.ListCatalog;
import com.github.wanjinzhong.easycron.dao.entity.ListBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListBoxRepository extends JpaRepository<ListBox, Integer> {

    List<ListBox> findByCatalog(ListCatalog catalog);

    ListBox findByCatalogAndCode(ListCatalog catalog, String code);
}
