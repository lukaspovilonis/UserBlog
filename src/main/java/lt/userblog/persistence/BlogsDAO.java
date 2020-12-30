package lt.userblog.persistence;

import lt.userblog.entities.Blog;
import lt.userblog.entities.User;
import lt.userblog.rest.contracts.BlogDto;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class BlogsDAO {

    @Inject
    private EntityManager em;

    public void persist(Blog blog){
        this.em.persist(blog);
    }

    public Blog findOne(Integer id){
        return em.find(Blog.class, id);
    }

    public Blog update(Blog blog){
        return em.merge(blog);
    }

    public void removeOne(Blog blog){
        em.remove(blog);
    }

    public List<BlogDto> getUserBlogs(User user){
        return  em.createQuery("select b from Blog b where b.user = ?1", Blog.class)
                .setParameter(1, user)
                .getResultList().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public List<BlogDto> getAllBlogs(){
        return  em.createQuery("select b from Blog b", Blog.class)
                .getResultList().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private BlogDto convertToDto(Blog blog) {
        BlogDto blogDto = new BlogDto();
        blogDto.setId(blog.getId());
        blogDto.setTitle(blog.getTitle());
        blogDto.setText(blog.getText());
        return blogDto;
    }
}
