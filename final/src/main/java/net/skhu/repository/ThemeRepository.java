package net.skhu.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.skhu.entity.ThemeEntity;

@Repository
public interface ThemeRepository extends JpaRepository<ThemeEntity, Integer> {
    List<ThemeEntity> findByUserid(String userId);
}
