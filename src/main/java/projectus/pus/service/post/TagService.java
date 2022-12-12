package projectus.pus.service.post;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import projectus.pus.entity.post.Category;
import projectus.pus.entity.post.Tag;
import projectus.pus.repository.post.TagRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    @Transactional
    public void addTag(List<String> tags, Category category) {
        if (!CollectionUtils.isEmpty(tags)) {
            List<Tag> tagList = Tag.of(tags);
            if (!tagList.isEmpty()){
                for(Tag tag: tagList){
                    tag.setCategory(category);
                    tagRepository.save(tag);
                }
            }
        }
    }

    public List<String> getTagList(Long categoryId) {
        return tagRepository.findAllByCategoryId(categoryId)
                .stream()
                .map(Tag::getName)
                .collect(Collectors.toList());
    }
}