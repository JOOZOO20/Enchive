
package net.skhu.entity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "post")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "theme_id", referencedColumnName = "id", nullable = false)
    private ThemeEntity theme;

    @Column(nullable = false)
    private String title;

    private String content;

    private String post_image;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false) // user_id로 매핑
    private UserEntity user; // UserEntity와의 관계를 설정

}
