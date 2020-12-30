package lt.userblog.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
@NamedQueries({
        @NamedQuery( name = "Blog.findAll", query = "select b from Blog as b")
})
@Table(name = "BLOG")
@Getter
@Setter
public class Blog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Size(max = 50)
    @Column(name = "TITLE")
    private String title;

    @Size(max = 500)
    @Column(name = "TEXT")
    private String text;

    @Version
    @Column(name = "OPT_LOCK_VERSION")
    private Integer version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Blog  blog = (Blog) o;
        return Objects.equals(id, blog.id) &&
                Objects.equals(user, blog.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text);
    }
}