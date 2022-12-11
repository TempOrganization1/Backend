package com.sparta.actualpractice.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberParty is a Querydsl query type for MemberParty
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberParty extends EntityPathBase<MemberParty> {

    private static final long serialVersionUID = -814755104L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberParty memberParty = new QMemberParty("memberParty");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMember member;

    public final QParty party;

    public QMemberParty(String variable) {
        this(MemberParty.class, forVariable(variable), INITS);
    }

    public QMemberParty(Path<? extends MemberParty> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberParty(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberParty(PathMetadata metadata, PathInits inits) {
        this(MemberParty.class, metadata, inits);
    }

    public QMemberParty(Class<? extends MemberParty> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
        this.party = inits.isInitialized("party") ? new QParty(forProperty("party"), inits.get("party")) : null;
    }

}

