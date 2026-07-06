package com.msa4meerkatgram.domain.post.entities;

import com.msa4meerkatgram.domain.user.entities.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "posts")
@SQLDelete(sql = "UPDATE posts SET deleted_at = NOW() where id = ?")
@SQLRestriction("deleted_at IS NULL")
@Getter
@Setter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED")
    private long id;

    @Column(name = "content", nullable = false, length = 200)
    private String content;

    @Column(name = "image", nullable = false, length = 100)
    private String image;

    @CreatedDate // 생성 시, 자동으로 현재시간 입력해줌
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate // 수정 시, 자동으로 시간을 업데이트 해줌
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at", nullable = true) // 빈값 허용함 nullable = true 로 인해.
    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",
            insertable = true, updatable = false, nullable = false
        //,foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT) // 물리적 FK 생성하고 싶지 않을때 사용
    )
    private User user;

}

// @ManyToOne // Many -> 현재 엔티티가 몇개냐(바로위의 엔티티들), one -> 연결할 대상 엔티티(user 엔티티)
// 로딩설정 :  EGAR, LAZY 를 설정 할 수 있음.
// EAGER 은, 즉시로딩, LAZY 는 지연로딩
// "연관된 데이터를 데이터베이스에서 언제 조회할 것인가?"를 결정하는 로딩 옵션
// "지금 당장 한 번에 다 가져올래(EAGER), 아니면 나중에 진짜 필요할 때 가져올래(LAZY)?"의 차이



// * insertable = false,
// INSERT 할때, user 객체에 어떤 값을 넣더라도, INSERT문에 'user_id' 컬럼을 포함하지 않겠다

// * updatable = false
// UPDATE 할때, user 객체에 어떤 값을 넣더라도, UPDATE 문에, 'user_id' 컬럼을 포함하지 않겠다.

