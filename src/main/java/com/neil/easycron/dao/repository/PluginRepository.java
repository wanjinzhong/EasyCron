package com.neil.easycron.dao.repository;
import com.neil.easycron.dao.entity.Plugin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PluginRepository extends JpaRepository<Plugin, Integer> {
}