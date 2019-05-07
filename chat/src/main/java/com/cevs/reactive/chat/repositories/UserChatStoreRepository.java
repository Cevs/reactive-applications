package com.cevs.reactive.chat.repositories;

import com.cevs.reactive.chat.domain.Message;
import com.cevs.reactive.chat.domain.UserChatStore;
import org.bson.types.ObjectId;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserChatStoreRepository extends ReactiveCrudRepository<UserChatStore, String> {
}
