/*
 * This file is generated by jOOQ.
 */
package com.wehuddle.db.tables.records;


import com.wehuddle.db.enums.NotificationType;
import com.wehuddle.db.tables.Notification;

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
public class NotificationRecord extends UpdatableRecordImpl<NotificationRecord> implements Record8<UUID, UUID, NotificationType, String, String, Boolean, OffsetDateTime, OffsetDateTime> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.notification.id</code>.
     */
    public void setId(UUID value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.notification.id</code>.
     */
    public UUID getId() {
        return (UUID) get(0);
    }

    /**
     * Setter for <code>public.notification.profileid</code>.
     */
    public void setProfileid(UUID value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.notification.profileid</code>.
     */
    public UUID getProfileid() {
        return (UUID) get(1);
    }

    /**
     * Setter for <code>public.notification.type</code>.
     */
    public void setType(NotificationType value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.notification.type</code>.
     */
    public NotificationType getType() {
        return (NotificationType) get(2);
    }

    /**
     * Setter for <code>public.notification.title</code>.
     */
    public void setTitle(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.notification.title</code>.
     */
    public String getTitle() {
        return (String) get(3);
    }

    /**
     * Setter for <code>public.notification.description</code>.
     */
    public void setDescription(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.notification.description</code>.
     */
    public String getDescription() {
        return (String) get(4);
    }

    /**
     * Setter for <code>public.notification.read_status</code>.
     */
    public void setReadStatus(Boolean value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.notification.read_status</code>.
     */
    public Boolean getReadStatus() {
        return (Boolean) get(5);
    }

    /**
     * Setter for <code>public.notification.created_at</code>.
     */
    public void setCreatedAt(OffsetDateTime value) {
        set(6, value);
    }

    /**
     * Getter for <code>public.notification.created_at</code>.
     */
    public OffsetDateTime getCreatedAt() {
        return (OffsetDateTime) get(6);
    }

    /**
     * Setter for <code>public.notification.updated_at</code>.
     */
    public void setUpdatedAt(OffsetDateTime value) {
        set(7, value);
    }

    /**
     * Getter for <code>public.notification.updated_at</code>.
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
    public Row8<UUID, UUID, NotificationType, String, String, Boolean, OffsetDateTime, OffsetDateTime> fieldsRow() {
        return (Row8) super.fieldsRow();
    }

    @Override
    public Row8<UUID, UUID, NotificationType, String, String, Boolean, OffsetDateTime, OffsetDateTime> valuesRow() {
        return (Row8) super.valuesRow();
    }

    @Override
    public Field<UUID> field1() {
        return Notification.NOTIFICATION.ID;
    }

    @Override
    public Field<UUID> field2() {
        return Notification.NOTIFICATION.PROFILEID;
    }

    @Override
    public Field<NotificationType> field3() {
        return Notification.NOTIFICATION.TYPE;
    }

    @Override
    public Field<String> field4() {
        return Notification.NOTIFICATION.TITLE;
    }

    @Override
    public Field<String> field5() {
        return Notification.NOTIFICATION.DESCRIPTION;
    }

    @Override
    public Field<Boolean> field6() {
        return Notification.NOTIFICATION.READ_STATUS;
    }

    @Override
    public Field<OffsetDateTime> field7() {
        return Notification.NOTIFICATION.CREATED_AT;
    }

    @Override
    public Field<OffsetDateTime> field8() {
        return Notification.NOTIFICATION.UPDATED_AT;
    }

    @Override
    public UUID component1() {
        return getId();
    }

    @Override
    public UUID component2() {
        return getProfileid();
    }

    @Override
    public NotificationType component3() {
        return getType();
    }

    @Override
    public String component4() {
        return getTitle();
    }

    @Override
    public String component5() {
        return getDescription();
    }

    @Override
    public Boolean component6() {
        return getReadStatus();
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
    public UUID value2() {
        return getProfileid();
    }

    @Override
    public NotificationType value3() {
        return getType();
    }

    @Override
    public String value4() {
        return getTitle();
    }

    @Override
    public String value5() {
        return getDescription();
    }

    @Override
    public Boolean value6() {
        return getReadStatus();
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
    public NotificationRecord value1(UUID value) {
        setId(value);
        return this;
    }

    @Override
    public NotificationRecord value2(UUID value) {
        setProfileid(value);
        return this;
    }

    @Override
    public NotificationRecord value3(NotificationType value) {
        setType(value);
        return this;
    }

    @Override
    public NotificationRecord value4(String value) {
        setTitle(value);
        return this;
    }

    @Override
    public NotificationRecord value5(String value) {
        setDescription(value);
        return this;
    }

    @Override
    public NotificationRecord value6(Boolean value) {
        setReadStatus(value);
        return this;
    }

    @Override
    public NotificationRecord value7(OffsetDateTime value) {
        setCreatedAt(value);
        return this;
    }

    @Override
    public NotificationRecord value8(OffsetDateTime value) {
        setUpdatedAt(value);
        return this;
    }

    @Override
    public NotificationRecord values(UUID value1, UUID value2, NotificationType value3, String value4, String value5, Boolean value6, OffsetDateTime value7, OffsetDateTime value8) {
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
     * Create a detached NotificationRecord
     */
    public NotificationRecord() {
        super(Notification.NOTIFICATION);
    }

    /**
     * Create a detached, initialised NotificationRecord
     */
    public NotificationRecord(UUID id, UUID profileid, NotificationType type, String title, String description, Boolean readStatus, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        super(Notification.NOTIFICATION);

        setId(id);
        setProfileid(profileid);
        setType(type);
        setTitle(title);
        setDescription(description);
        setReadStatus(readStatus);
        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
    }
}