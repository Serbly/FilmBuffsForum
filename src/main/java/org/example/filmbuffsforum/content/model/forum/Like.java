package org.example.filmbuffsforum.content.model.forum;

import jakarta.persistence.*;
import lombok.Data;
import org.example.filmbuffsforum.auth.model.User;

@Entity
@Table(name = "likes")
@IdClass(LikeId.class)
@Data
public class Like {

    @Id
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
