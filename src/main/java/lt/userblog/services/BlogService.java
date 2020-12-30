package lt.userblog.services;

import lt.userblog.entities.Blog;
import lt.userblog.entities.User;
import lt.userblog.persistence.BlogsDAO;
import lt.userblog.rest.contracts.BlogDto;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class BlogService {

    @Inject
    private BlogsDAO blogsDAO;

    @Transactional
    public List<BlogDto> getUserBlogs(User user){
        return blogsDAO.getUserBlogs(user);
    }

    @Transactional
    public BlogDto saveBlog(User user, BlogDto blogDto){
        Blog blog = new Blog();
        blog.setUser(user);
        blog.setTitle(blogDto.getTitle());
        blog.setText(blogDto.getText());
        blogDto.setId(blogsDAO.update(blog).getId());
        return blogDto;
    }

    @Transactional
    public Blog updateBlog(User user, BlogDto blogDto){
        Blog blog = blogsDAO.findOne(blogDto.getId());
        //reikia patestuoti ar toks tikrinimas yra geras
        if(user.equals(blog.getUser())) {
            blog.setTitle(blogDto.getTitle());
            blog.setText(blogDto.getText());
            blogsDAO.update(blog);
        }
        return blogsDAO.update(blog);
    }

    @Transactional
    public boolean removeUserBlog(Integer id){
        try{
            blogsDAO.removeOne(blogsDAO.findOne(id));
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Transactional
    public List<BlogDto> getAllBLogs(){
        return blogsDAO.getAllBlogs();
    }

}
