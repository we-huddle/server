/*
 * This file is generated by jOOQ.
 */
package com.wehuddle.db.tables.records;


import com.wehuddle.db.tables.Badge;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record8;
import org.jooq.Row8;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class BadgeRecord extends UpdatableRecordImpl<BadgeRecord> implements Record8<UUID, String, String, String, UUID[], UUID[], OffsetDateTime, OffsetDateTime> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.badge.id</code>.
     */
    public void setId(UUID value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.badge.id</code>.
     */
    public UUID getId() {
        return (UUID) get(0);
    }

    /**
     * Setter for <code>public.badge.title</code>.
     */
    public void setTitle(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.badge.title</code>.
     */
    public String getTitle() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.badge.description</code>.
     */
    public void setDescription(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.badge.description</code>.
     */
    public String getDescription() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.badge.photo</code>.
     */
    public void setPhoto(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.badge.photo</code>.
     */
    public String getPhoto() {
        return (String) get(3);
    }

    /**
     * Setter for <code>public.badge.dep_badges</code>.
     */
    public void setDepBadges(UUID[] value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.badge.dep_badges</code>.
     */
    public UUID[] getDepBadges() {
        return (UUID[]) get(4);
    }

    /**
     * Setter for <code>public.badge.dep_tasks</code>.
     */
    public void setDepTasks(UUID[] value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.badge.dep_tasks</code>.
     */
    public UUID[] getDepTasks() {
        return (UUID[]) get(5);
    }

    /**
     * Setter for <code>public.badge.created_at</code>.
     */
    public void setCreatedAt(OffsetDateTime value) {
        set(6, value);
    }

    /**
     * Getter for <code>public.badge.created_at</code>.
     */
    public OffsetDateTime getCreatedAt() {
        return (OffsetDateTime) get(6);
    }

    /**
     * Setter for <code>public.badge.updated_at</code>.
     */
    public void setUpdatedAt(OffsetDateTime value) {
        set(7, value);
    }

    /**
     * Getter for <code>public.badge.updated_at</code>.
     */
    public OffsetDateTime getUpdatedAt() {
        return (OffsetDateTime) get(7);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<UUID> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record8 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row8<UUID, String, String, String, UUID[], UUID[], OffsetDateTime, OffsetDateTime> fieldsRow() {
        return (Row8) super.fieldsRow();
    }

    @Override
    public Row8<UUID, String, String, String, UUID[], UUID[], OffsetDateTime, OffsetDateTime> valuesRow() {
        return (Row8) super.valuesRow();
    }

    @Override
    public Field<UUID> field1() {
        return Badge.BADGE.ID;
    }

    @Override
    public Field<String> field2() {
        return Badge.BADGE.TITLE;
    }

    @Override
    public Field<String> field3() {
        return Badge.BADGE.DESCRIPTION;
    }

    @Override
    public Field<String> field4() {
        return Badge.BADGE.PHOTO;
    }

    @Override
    public Field<UUID[]> field5() {
        return Badge.BADGE.DEP_BADGES;
    }

    @Override
    public Field<UUID[]> field6() {
        return Badge.BADGE.DEP_TASKS;
    }

    @Override
    public Field<OffsetDateTime> field7() {
        return Badge.BADGE.CREATED_AT;
    }

    @Override
    public Field<OffsetDateTime> field8() {
        return Badge.BADGE.UPDATED_AT;
    }

    @Override
    public UUID component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getTitle();
    }

    @Override
    public String component3() {
        return getDescription();
    }

    @Override
    public String component4() {
        return getPhoto();
    }

    @Override
    public UUID[] component5() {
        return getDepBadges();
    }

    @Override
    public UUID[] component6() {
        return getDepTasks();
    }

    @Override
    public OffsetDateTime component7() {
        return getCreatedAt();
    }

    @Override
    public OffsetDateTime component8() {
        return getUpdatedAt();
    }

    @Override
    public UUID value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getTitle();
    }

    @Override
    public String value3() {
        return getDescription();
    }

    @Override
    public String value4() {
        return getPhoto();
    }

    @Override
    public UUID[] value5() {
        return getDepBadges();
    }

    @Override
    public UUID[] value6() {
        return getDepTasks();
    }

    @Override
    public OffsetDateTime value7() {
        return getCreatedAt();
    }

    @Override
    public OffsetDateTime value8() {
        return getUpdatedAt();
    }

    @Override
    public BadgeRecord value1(UUID value) {
        setId(value);
        return this;
    }

    @Override
    public BadgeRecord value2(String value) {
        setTitle(value);
        return this;
    }

    @Override
    public BadgeRecord value3(String value) {
        setDescription(value);
        return this;
    }

    @Override
    public BadgeRecord value4(String value) {
        setPhoto(value);
        return this;
    }

    @Override
    public BadgeRecord value5(UUID[] value) {
        setDepBadges(value);
        return this;
    }

    @Override
    public BadgeRecord value6(UUID[] value) {
        setDepTasks(value);
        return this;
    }

    @Override
    public BadgeRecord value7(OffsetDateTime value) {
        setCreatedAt(value);
        return this;
    }

    @Override
    public BadgeRecord value8(OffsetDateTime value) {
        setUpdatedAt(value);
        return this;
    }

    @Override
    public BadgeRecord values(UUID value1, String value2, String value3, String value4, UUID[] value5, UUID[] value6, OffsetDateTime value7, OffsetDateTime value8) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached BadgeRecord
     */
    public BadgeRecord() {
        super(Badge.BADGE);
    }

    /**
     * Create a detached, initialised BadgeRecord
     */
    public BadgeRecord(UUID id, String title, String description, String photo, UUID[] depBadges, UUID[] depTasks, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        super(Badge.BADGE);

        setId(id);
        setTitle(title);
        setDescription(description);
        setPhoto(photo);
        setDepBadges(depBadges);
        setDepTasks(depTasks);
        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
    }
}