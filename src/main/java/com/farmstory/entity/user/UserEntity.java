package com.farmstory.entity.user;

import com.farmstory.responsedto.user.GetUsersRespDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_idx")
    private Long userIdx;

    @Column(name = "user_id", nullable = false, unique = true, length = 40)
    private String userId;

    @Column(name = "user_pwd", nullable = false, length = 255)
    private String userPwd;

    @Column(name = "user_name", nullable = false, length = 50)
    private String userName;

    @Column(name = "user_nick", nullable = false, unique = true, length = 40)
    private String userNick;

    @Column(name = "user_email", nullable = false, unique = true, length = 40)
    private String userEmail;

    @Column(name = "user_hp", nullable = false, length = 13)
    private String userHp;

    @Column(name = "user_create_at")
    @CreationTimestamp
    private Date userCreateAt;

    @Column(name = "user_role", nullable = true, length = 10)
    private String userRole ;

    @Builder
    public UserEntity(long userIdx,String userId, String userPwd, String userName, String userNick, String userEmail, String userHp) {
        this.userIdx = userIdx;
        this.userId = userId;
        this.userPwd = userPwd;
        this.userName = userName;
        this.userNick = userNick;
        this.userEmail = userEmail;
        this.userHp = userHp;
        this.userRole = null;
        this.userCreateAt = null;
    }


    public GetUsersRespDto toDto() {
        return GetUsersRespDto.builder()
                .userIdx(userIdx)
                .userId(userId)
                .userName(userName)
                .userNick(userNick)
                .userEmail(userEmail)
                .userHp(userHp)
                .userCreateAt(userCreateAt)
                .userRole(userRole)
                .build();
    }


    @PrePersist
    protected void onCreate() {
        if (userCreateAt == null) {
            userCreateAt = new Date();
        }
        if(userRole == null){
            this.userRole = "user";
        }
    }

}
