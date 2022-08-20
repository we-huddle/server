/*
 * This file is generated by jOOQ.
 */
package com.wehuddle.db.tables;


import com.wehuddle.db.Keys;
import com.wehuddle.db.Public;
import com.wehuddle.db.enums.UserRole;
import com.wehuddle.db.tables.records.ProfileRecord;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.JSONB;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row13;
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
public class Profile extends TableImpl<ProfileRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.profile</code>
     */
    public static final Profile PROFILE = new Profile();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ProfileRecord> getRecordType() {
        return ProfileRecord.class;
    }

    /**
     * The column <code>public.profile.id</code>.
     */
    public final TableField<ProfileRecord, UUID> ID = createField(DSL.name("id"), SQLDataType.UUID.nullable(false).defaultValue(DSL.field("uuid_generate_v4()", SQLDataType.UUID)), this, "");

    /**
     * The column <code>public.profile.name</code>.
     */
    public final TableField<ProfileRecord, String> NAME = createField(DSL.name("name"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>public.profile.github_username</code>.
     */
    public final TableField<ProfileRecord, String> GITHUB_USERNAME = createField(DSL.name("github_username"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>public.profile.email</code>.
     */
    public final TableField<ProfileRecord, String> EMAIL = createField(DSL.name("email"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>public.profile.photo</code>.
     */
    public final TableField<ProfileRecord, String> PHOTO = createField(DSL.name("photo"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>public.profile.github_unique_id</code>.
     */
    public final TableField<ProfileRecord, String> GITHUB_UNIQUE_ID = createField(DSL.name("github_unique_id"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>public.profile.access_token</code>.
     */
    public final TableField<ProfileRecord, String> ACCESS_TOKEN = createField(DSL.name("access_token"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>public.profile.role</code>.
     */
    public final TableField<ProfileRecord, UserRole> ROLE = createField(DSL.name("role"), SQLDataType.VARCHAR.nullable(false).asEnumDataType(com.wehuddle.db.enums.UserRole.class), this, "");

    /**
     * The column <code>public.profile.created_at</code>.
     */
    public final TableField<ProfileRecord, OffsetDateTime> CREATED_AT = createField(DSL.name("created_at"), SQLDataType.TIMESTAMPWITHTIMEZONE(6).nullable(false).defaultValue(DSL.field("now()", SQLDataType.TIMESTAMPWITHTIMEZONE)), this, "");

    /**
     * The column <code>public.profile.updated_at</code>.
     */
    public final TableField<ProfileRecord, OffsetDateTime> UPDATED_AT = createField(DSL.name("updated_at"), SQLDataType.TIMESTAMPWITHTIMEZONE(6).nullable(false).defaultValue(DSL.field("now()", SQLDataType.TIMESTAMPWITHTIMEZONE)), this, "");

    /**
     * The column <code>public.profile.bio</code>.
     */
    public final TableField<ProfileRecord, String> BIO = createField(DSL.name("bio"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>public.profile.city</code>.
     */
    public final TableField<ProfileRecord, String> CITY = createField(DSL.name("city"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>public.profile.links</code>.
     */
    public final TableField<ProfileRecord, JSONB> LINKS = createField(DSL.name("links"), SQLDataType.JSONB, this, "");

    private Profile(Name alias, Table<ProfileRecord> aliased) {
        this(alias, aliased, null);
    }

    private Profile(Name alias, Table<ProfileRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.profile</code> table reference
     */
    public Profile(String alias) {
        this(DSL.name(alias), PROFILE);
    }

    /**
     * Create an aliased <code>public.profile</code> table reference
     */
    public Profile(Name alias) {
        this(alias, PROFILE);
    }

    /**
     * Create a <code>public.profile</code> table reference
     */
    public Profile() {
        this(DSL.name("profile"), null);
    }

    public <O extends Record> Profile(Table<O> child, ForeignKey<O, ProfileRecord> key) {
        super(child, key, PROFILE);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public UniqueKey<ProfileRecord> getPrimaryKey() {
        return Keys.PROFILE_PKEY;
    }

    @Override
    public List<UniqueKey<ProfileRecord>> getKeys() {
        return Arrays.<UniqueKey<ProfileRecord>>asList(Keys.PROFILE_PKEY);
    }

    @Override
    public Profile as(String alias) {
        return new Profile(DSL.name(alias), this);
    }

    @Override
    public Profile as(Name alias) {
        return new Profile(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Profile rename(String name) {
        return new Profile(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Profile rename(Name name) {
        return new Profile(name, null);
    }

    // -------------------------------------------------------------------------
    // Row13 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row13<UUID, String, String, String, String, String, String, UserRole, OffsetDateTime, OffsetDateTime, String, String, JSONB> fieldsRow() {
        return (Row13) super.fieldsRow();
    }
}
