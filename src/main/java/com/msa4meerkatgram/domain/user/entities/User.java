package com.msa4meerkatgram.domain.user.entities;

import com.msa4meerkatgram.global.security.constant.ProviderPolicy;
import com.msa4meerkatgram.global.security.constant.RolePolicy;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Types;
import java.time.LocalDateTime;

@Entity // 해당 클래스가 JPA 엔티티임을 선언함
@EntityListeners(AuditingEntityListener.class) // 엔티티의 이벤트리스너 지정
@Table(name = "users") // 테이블명 맵핑
@SQLDelete(sql = "UPDATE users SET deleted_at = NOW() where id = ?") // soft delete
@SQLRestriction("deleted_at IS NULL")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED")
    private long id;

    @Column(name = "email", unique = true, nullable = false, length = 100)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "nick", nullable = false, length = 20)
    private String nick;

    @Column(name = "provider", nullable = false, length = 10)
    @Enumerated(value = EnumType.STRING) // Enum 을 어떤 데이터 형식으로 저장할것인지, 설정하는 어노테이션
    @JdbcTypeCode(Types.VARCHAR) // 자바 엔티티 필드를 데이터베이스의 VARCHAR 로 강제매핑할때 사용
    private ProviderPolicy provider = ProviderPolicy.NONE;

    @Column(name = "role", nullable = false, length = 10)
    @Enumerated(value = EnumType.STRING)
    @JdbcTypeCode(Types.VARCHAR)
    private RolePolicy role = RolePolicy.NORMAL;

    @Column(name = "profile", nullable = false, length = 100)
    private String profile;

    @Column(name = "refresh_token", nullable = true, length = 255)
    private String refreshToken;

    @CreatedDate // 생성 시, 자동으로 현재시간 입력해줌
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate // 수정 시, 자동으로 시간을 업데이트 해줌
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at", nullable = true) // 빈값 허용함 nullable = true 로 인해.
    private LocalDateTime deletedAt;

}


// ORM 은, 데이터베이스 에서 설정하는 것들을, 자바 단에서 설정을 함.
// 어떤 쿼리를 ORM으로 설정했는지
//
//// jpa 가 원래 사용하던 delete 문 대신에, 내가 입력한 쿼리문을 실행해 라는뜻
//@SQLDelete(sql = "UPDATE users SET deleted_at = NOW() where id = ?")

//@GeneratedValue
//-> auto increment 와 같음
//-> pk 자동생성 전략을 설정하는 어노테이션

//@Column(name = "id", columnDefinition = "BIGINT UNSIGNED")
// -> name 은, db의 테이블의 컬럼, columnDefinition 컬럼에서 지정한 타입이 들어감

// 비밀번호는, 256 글자, 512 글자. 그 이유는 db에 저장될때, hash 화 되어서 난수가 엄청 많이 저장되기에.


// nullable = false 는, 기본값을 저장시킨다 라는뜻

// id 는, pk의 속성을 가지고 있음. 그래서 pk 와 unique 속성은 주어지지 않음

// Entity ~ SQLRestriction 까지는, JPA 어노테이션
//@SQLRestriction("deleted_at IS NULL") 엔티티의 조회시 항상 특정 조건을 추가하도록 지정


// private RolePolicy role = RolePolicy.NORMAL;  role을 디폴트값으로 RolePolicy.NORMAL 을 지정함


//@EntityListeners(AuditingEntityListener.class)
// 엔티티가 생성되거나 수정될 때 데이터(시간, 생성자 등)를 자동으로 기록(Auditing)해주는 아주 편리한 기능