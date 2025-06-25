package com.demo.couponHub.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private Timestamp createdDate;

    @CreatedBy
    @Column(name = "created_by_id", updatable = false)
    private String createdById;

    @LastModifiedDate
    @Column(name = "updated_date", insertable = false)
    private Timestamp updatedDate;

    @LastModifiedBy
    @Column(name = "updated_by_id", insertable = false)
    private String updatedById;
}
