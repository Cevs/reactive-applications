package com.cevs.reactive.chat.repositories;

import com.cevs.reactive.chat.domain.Chat;
import org.bson.types.ObjectId;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ChatRepository extends ReactiveCrudRepository<Chat, ObjectId> {
}
