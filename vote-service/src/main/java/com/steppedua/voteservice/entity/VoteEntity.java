package com.steppedua.voteservice.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Accessors(chain = true)
@Table(name = "vote")
public class VoteEntity {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "user_id", unique = true, nullable = false)
    @Type(type="pg-uuid")
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "vote_value", nullable = false)
    private Vote voteValue;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        VoteEntity that = (VoteEntity) o;
        return getUserId() != null && Objects.equals(getUserId(), that.getUserId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
