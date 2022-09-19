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
import org.jooq.Record9;
import org.jooq.Row9;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class NotificationRecord extends UpdatableRecordImpl<NotificationRecord> implements Record9<UUID, UUID, UUID, NotificationType, String, String, Boolean, OffsetDateTime, OffsetDateTime> {

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
     * Setter for <code>public.notification.linkid</code>.
     */
    public void setLinkid(UUID value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.notification.linkid</code>.
     */
    public UUID getLinkid() {
        return (UUID) get(2);
    }

    /**
     * Setter for <code>public.notification.type</code>.
     */
    public void setType(NotificationType value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.notification.type</code>.
     */
    public NotificationType getType() {
        return (NotificationType) get(3);
    }

    /**
     * Setter for <code>public.notification.title</code>.
     */
    public void setTitle(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.notification.title</code>.
     */
    public String getTitle() {
        return (String) get(4);
    }

    /**
     * Setter for <code>public.notification.description</code>.
     */
    public void setDescription(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.notification.description</code>.
     */
    public String getDescription() {
        return (String) get(5);
    }

    /**
     * Setter for <code>public.notification.read_status</code>.
     */
    public void setReadStatus(Boolean value) {
        set(6, value);
    }

    /**
     * Getter for <code>public.notification.read_status</code>.
     */
    public Boolean getReadStatus() {
        return (Boolean) get(6);
    }

    /**
     * Setter for <code>public.notification.created_at</code>.
     */
    public void setCreatedAt(OffsetDateTime value) {
        set(7, value);
    }

    /**
     * Getter for <code>public.notification.created_at</code>.
     */
    public OffsetDateTime getCreatedAt() {
        return (OffsetDateTime) get(7);
    }

    /**
     * Setter for <code>public.notification.updated_at</code>.
     */
    public void setUpdatedAt(OffsetDateTime value) {
        set(8, value);
    }

    /**
     * Getter for <code>public.notification.updated_at</code>.
     */
    public OffsetDateTime getUpdatedAt() {
        return (OffsetDateTime) get(8);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<UUID> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record9 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row9<UUID, UUID, UUID, NotificationType, String, String, Boolean, OffsetDateTime, OffsetDateTime> fieldsRow() {
        return (Row9) super.fieldsRow();
    }

    @Override
    public Row9<UUID, UUID, UUID, NotificationType, String, String, Boolean, OffsetDateTime, OffsetDateTime> valuesRow() {
        return (Row9) super.valuesRow();
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
    public Field<UUID> field3() {
        return Notification.NOTIFICATION.LINKID;
    }

    @Override
    public Field<NotificationType> field4() {
        return Notification.NOTIFICATION.TYPE;
    }

    @Override
    public Field<String> field5() {
        return Notification.NOTIFICATION.TITLE;
    }

    @Override
    public Field<String> field6() {
        return Notification.NOTIFICATION.DESCRIPTION;
    }

    @Override
    public Field<Boolean> field7() {
        return Notification.NOTIFICATION.READ_STATUS;
    }

    @Override
    public Field<OffsetDateTime> field8() {
        return Notification.NOTIFICATION.CREATED_AT;
    }

    @Override
    public Field<OffsetDateTime> field9() {
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
    public UUID component3() {
        return getLinkid();
    }

    @Override
    public NotificationType component4() {
        return getType();
    }

    @Override
    public String component5() {
        return getTitle();
    }

    @Override
    public String component6() {
        return getDescription();
    }

    @Override
    public Boolean component7() {
        return getReadStatus();
    }

    @Override
    public OffsetDateTime component8() {
        return getCreatedAt();
    }

    @Override
    public OffsetDateTime component9() {
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
    public UUID value3() {
        return getLinkid();
    }

    @Override
    public NotificationType value4() {
        return getType();
    }

    @Override
    public String value5() {
        return getTitle();
    }

    @Override
    public String value6() {
        return getDescription();
    }

    @Override
    public Boolean value7() {
        return getReadStatus();
    }

    @Override
    public OffsetDateTime value8() {
        return getCreatedAt();
    }

    @Override
    public OffsetDateTime value9() {
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
    public NotificationRecord value3(UUID value) {
        setLinkid(value);
        return this;
    }

    @Override
    public NotificationRecord value4(NotificationType value) {
        setType(value);
        return this;
    }

    @Override
    public NotificationRecord value5(String value) {
        setTitle(value);
        return this;
    }

    @Override
    public NotificationRecord value6(String value) {
        setDescription(value);
        return this;
    }

    @Override
    public NotificationRecord value7(Boolean value) {
        setReadStatus(value);
        return this;
    }

    @Override
    public NotificationRecord value8(OffsetDateTime value) {
        setCreatedAt(value);
        return this;
    }

    @Override
    public NotificationRecord value9(OffsetDateTime value) {
        setUpdatedAt(value);
        return this;
    }

    @Override
    public NotificationRecord values(UUID value1, UUID value2, UUID value3, NotificationType value4, String value5, String value6, Boolean value7, OffsetDateTime value8, OffsetDateTime value9) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
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
    public NotificationRecord(UUID id, UUID profileid, UUID linkid, NotificationType type, String title, String description, Boolean readStatus, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        super(Notification.NOTIFICATION);

        setId(id);
        setProfileid(profileid);
        setLinkid(linkid);
        setType(type);
        setTitle(title);
        setDescription(description);
        setReadStatus(readStatus);
        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
    }
}
