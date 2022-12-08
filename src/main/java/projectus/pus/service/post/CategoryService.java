package projectus.pus.service.post;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import projectus.pus.dto.post.PostDto;
import projectus.pus.entity.post.Category;
import projectus.pus.entity.post.Post;
import projectus.pus.repository.post.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final TagService tagService;
    @Transactional
    public void addCategory(PostDto.Request requestDto, Post post){
        if(!CollectionUtils.isEmpty(requestDto.getCategory())) {
            for (PostDto.CategoryRequest categoryList : requestDto.getCategory()){
                Category category = Category
                        .builder()
                        .field(categoryList.getField())
                        .build();
                category.setPost(post);
                categoryRepository.save(category);
                tagService.addTag(categoryList.getTag(),category);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<PostDto.CategoryResponse> getCategoryList(Long postId) {
        return categoryRepository.findAllByPostId(postId)
                .stream()
                .map(c -> new PostDto.CategoryResponse(c.getField(), tagService.getTagList(c.getId())))
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateCategory(Post post, Long postId, PostDto.Request requestDto) {
        List<Category> dbCategoryList = categoryRepository.findAllByPostId(postId);
        if (!CollectionUtils.isEmpty(dbCategoryList)){
            for(Category dbCategory : dbCategoryList)
                categoryRepository.delete(dbCategory);
        }
        if(!CollectionUtils.isEmpty(requestDto.getCategory())) {
            for (PostDto.CategoryRequest categoryList : requestDto.getCategory()){
                Category category = Category
                        .builder()
                        .field(categoryList.getField())
                        .build();
                category.setPost(post);
                categoryRepository.save(category);
                tagService.addTag(categoryList.getTag(),category);
            }
        }
    }

    public List<Long> search(String category, List<String> tag) {
        List<Long> searchId;
        if (category==null||category.isBlank()){
            if(tag.isEmpty()){
                searchId = categoryRepository.findAllPost();
            }
            else {
                searchId = categoryRepository.findByTag(tag);
            }
        }
        else{
            if(tag.isEmpty()) {
                searchId = categoryRepository.findByCategory(category);
            }
            else {
                searchId = categoryRepository.findByCategoryAndTag(category,tag);
            }
        }
        return searchId;
    }
}