package org.example.filmbuffsforum.content.model.forum;

import jakarta.persistence.*;
import lombok.Data;
import org.example.filmbuffsforum.auth.model.User;

@Entity
@Table(name = "subscriptions")
@IdClass(SubscriptionId.class)
@Data
public class Subscription {

    @Id
    @ManyToOne
    @JoinColumn(name = "subscriber_id")
    private User subscriber;

    @Id
    @ManyToOne
    @JoinColumn(name = "target_user_id")
    private User targetUser;
}
