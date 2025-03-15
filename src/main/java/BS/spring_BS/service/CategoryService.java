package BS.spring_BS.service;

import BS.spring_BS.dto.CategoryResponseDTO;
import BS.spring_BS.entity.Category;
import BS.spring_BS.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryResponseDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private CategoryResponseDTO convertToDTO(Category category) {
        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setCategoryId(Math.toIntExact(category.getCategoryId()));
        dto.setCategoryName(category.getCategoryName());
        return dto;
    }
}
