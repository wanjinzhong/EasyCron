package com.neil.easycron.dao.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "plugin",
       indexes = {@Index(columnList = "global_id", name = "plugin_I1")},
       uniqueConstraints = {@UniqueConstraint(columnNames = {"global_id", "artifact_id"})})
public class Plugin implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "global_id")
    private Long pluginId;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "group_id", length = 200, nullable = false)
    private String groupId;

    @Column(name = "artifact_id", length = 100, nullable = false)
    private String artifactId;

    @Column(name = "thumb_url", length = 1024)
    private String thumbUrl;

    @Column(name = "version", length = 20, nullable = false)
    private String version;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getPluginId() {
        return pluginId;
    }

    public void setPluginId(Long pluginId) {
        this.pluginId = pluginId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
