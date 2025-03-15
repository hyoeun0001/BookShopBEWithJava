package BS.spring_BS.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(nullable = false)
    private String categoryName;

    @OneToMany(mappedBy = "category")
    private List<Book> books; // Category에 속한 모든 Book을 가져올 수 있음

    // 카테고리 생성 시 이름을 받는 생성자
    public Category(String categoryName) {
        this.categoryName = categoryName;
    }
}
