/*
 * This file is generated by jOOQ.
 */
package com.wehuddle.db.tables;


import com.wehuddle.db.Keys;
import com.wehuddle.db.Public;
import com.wehuddle.db.tables.records.SessionRecord;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row5;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Session extends TableImpl<SessionRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.session</code>
     */
    public static final Session SESSION = new Session();

    /**
     * The class holding records for this type
     */
    @Override
    @Nonnull
    public Class<SessionRecord> getRecordType() {
        return SessionRecord.class;
    }

    /**
     * The column <code>public.session.id</code>.
     */
    public final TableField<SessionRecord, UUID> ID = createField(DSL.name("id"), SQLDataType.UUID.nullable(false).defaultValue(DSL.field("uuid_generate_v4()", SQLDataType.UUID)), this, "");

    /**
     * The column <code>public.session.profile_id</code>.
     */
    public final TableField<SessionRecord, UUID> PROFILE_ID = createField(DSL.name("profile_id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>public.session.expires_at</code>.
     */
    public final TableField<SessionRecord, OffsetDateTime> EXPIRES_AT = createField(DSL.name("expires_at"), SQLDataType.TIMESTAMPWITHTIMEZONE(6).nullable(false), this, "");

    /**
     * The column <code>public.session.created_at</code>.
     */
    public final TableField<SessionRecord, OffsetDateTime> CREATED_AT = createField(DSL.name("created_at"), SQLDataType.TIMESTAMPWITHTIMEZONE(6).nullable(false).defaultValue(DSL.field("now()", SQLDataType.TIMESTAMPWITHTIMEZONE)), this, "");

    /**
     * The column <code>public.session.updated_at</code>.
     */
    public final TableField<SessionRecord, OffsetDateTime> UPDATED_AT = createField(DSL.name("updated_at"), SQLDataType.TIMESTAMPWITHTIMEZONE(6).nullable(false).defaultValue(DSL.field("now()", SQLDataType.TIMESTAMPWITHTIMEZONE)), this, "");

    private Session(Name alias, Table<SessionRecord> aliased) {
        this(alias, aliased, null);
    }

    private Session(Name alias, Table<SessionRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.session</code> table reference
     */
    public Session(String alias) {
        this(DSL.name(alias), SESSION);
    }

    /**
     * Create an aliased <code>public.session</code> table reference
     */
    public Session(Name alias) {
        this(alias, SESSION);
    }

    /**
     * Create a <code>public.session</code> table reference
     */
    public Session() {
        this(DSL.name("session"), null);
    }

    public <O extends Record> Session(Table<O> child, ForeignKey<O, SessionRecord> key) {
        super(child, key, SESSION);
    }

    @Override
    @Nonnull
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    @Nonnull
    public UniqueKey<SessionRecord> getPrimaryKey() {
        return Keys.SESSION_PKEY;
    }

    @Override
    @Nonnull
    public List<UniqueKey<SessionRecord>> getKeys() {
        return Arrays.<UniqueKey<SessionRecord>>asList(Keys.SESSION_PKEY);
    }

    @Override
    @Nonnull
    public List<ForeignKey<SessionRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<SessionRecord, ?>>asList(Keys.SESSION__SESSION_PROFILE_ID_FKEY);
    }

    public Profile profile() {
        return new Profile(this, Keys.SESSION__SESSION_PROFILE_ID_FKEY);
    }

    @Override
    @Nonnull
    public Session as(String alias) {
        return new Session(DSL.name(alias), this);
    }

    @Override
    @Nonnull
    public Session as(Name alias) {
        return new Session(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    @Nonnull
    public Session rename(String name) {
        return new Session(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    @Nonnull
    public Session rename(Name name) {
        return new Session(name, null);
    }

    // -------------------------------------------------------------------------
    // Row5 type methods
    // -------------------------------------------------------------------------

    @Override
    @Nonnull
    public Row5<UUID, UUID, OffsetDateTime, OffsetDateTime, OffsetDateTime> fieldsRow() {
        return (Row5) super.fieldsRow();
    }
}