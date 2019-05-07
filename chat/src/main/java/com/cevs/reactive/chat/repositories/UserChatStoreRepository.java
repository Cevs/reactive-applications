package com.cevs.reactive.chat.repositories;

import com.cevs.reactive.chat.domain.UserChatStore;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserChatStoreRepository extends ReactiveCrudRepository<UserChatStore, String> {
}
