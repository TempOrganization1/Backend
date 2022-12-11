package com.sparta.actualpractice.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -1517342810L;

    public static final QMember member = new QMember("member1");

    public final com.sparta.actualpractice.util.QTimeStamped _super = new com.sparta.actualpractice.util.QTimeStamped(this);

    public final ListPath<Admin, QAdmin> adminList = this.<Admin, QAdmin>createList("adminList", Admin.class, QAdmin.class, PathInits.DIRECT2);

    public final ListPath<Album, QAlbum> albumList = this.<Album, QAlbum>createList("albumList", Album.class, QAlbum.class, PathInits.DIRECT2);

    public final ListPath<Comment, QComment> commentList = this.<Comment, QComment>createList("commentList", Comment.class, QComment.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    public final NumberPath<Long> kakaoId = createNumber("kakaoId", Long.class);

    public final ListPath<MemberParty, QMemberParty> memberPartyList = this.<MemberParty, QMemberParty>createList("memberPartyList", MemberParty.class, QMemberParty.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public final ListPath<Notification, QNotification> notificationList = this.<Notification, QNotification>createList("notificationList", Notification.class, QNotification.class, PathInits.DIRECT2);

    public final StringPath password = createString("password");

    public final ListPath<Schedule, QSchedule> scheduleList = this.<Schedule, QSchedule>createList("scheduleList", Schedule.class, QSchedule.class, PathInits.DIRECT2);

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

