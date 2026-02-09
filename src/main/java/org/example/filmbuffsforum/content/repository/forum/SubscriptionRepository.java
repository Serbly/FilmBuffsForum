package org.example.filmbuffsforum.content.repository.forum;

import org.example.filmbuffsforum.auth.model.User;
import org.example.filmbuffsforum.content.model.forum.Subscription;
import org.example.filmbuffsforum.content.model.forum.SubscriptionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, SubscriptionId> {

    boolean existsBySubscriberAndTargetUser(User subscriber, User targetUser);

    void deleteBySubscriberAndTargetUser(User subscriber, User targetUser);

    long countByTargetUser(User targetUser);

    List<Subscription> findBySubscriber(User subscriber);
}
