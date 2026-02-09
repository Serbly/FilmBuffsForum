package org.example.filmbuffsforum.content.service;

import lombok.RequiredArgsConstructor;
import org.example.filmbuffsforum.auth.model.User;
import org.example.filmbuffsforum.content.model.forum.Subscription;
import org.example.filmbuffsforum.content.repository.forum.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public boolean isSubscribed(User me, User target) {
        return subscriptionRepository.existsBySubscriberAndTargetUser(me, target);
    }

    public void follow(User me, User target) {
        if (me.getId().equals(target.getId())) return;

        if (!subscriptionRepository.existsBySubscriberAndTargetUser(me, target)) {
            Subscription s = new Subscription();
            s.setSubscriber(me);
            s.setTargetUser(target);
            subscriptionRepository.save(s);
        }
    }

    public void unfollow(User me, User target) {
        subscriptionRepository.deleteBySubscriberAndTargetUser(me, target);
    }

    public long countFollowers(User user) {
        return subscriptionRepository.countByTargetUser(user);
    }

    public List<User> getSubscriptions(User me) {
        return subscriptionRepository.findBySubscriber(me)
                .stream()
                .map(Subscription::getTargetUser)
                .toList();
    }
}
