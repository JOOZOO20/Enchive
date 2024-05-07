package net.skhu.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "userid", nullable = false, columnDefinition = "TEXT")
    private String userid;

    @Column(name = "pw", nullable = false, columnDefinition = "TEXT")
    private String pw;

    @Column(name = "username", nullable = false,columnDefinition = "TEXT")
    private String username;

    @Column(name = "nickname", nullable = false, columnDefinition = "TEXT")
    private String nickname;


}
