package net.skhu.service;

import java.util.List;
import net.skhu.entity.ThemeEntity;
import net.skhu.repository.ThemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ThemeService {

    @Autowired
    private ThemeRepository themeRepository;

    public List<ThemeEntity> findByUserId(String userId) {
        return themeRepository.findByUserid(userId);
    }

    public void saveTheme(ThemeEntity theme) {
        themeRepository.save(theme);
    }

    public ThemeEntity findThemeById(Integer themeId) {
        return themeRepository.findById(themeId).orElse(null);
    }

}
