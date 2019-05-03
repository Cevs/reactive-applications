package com.cevs.reactive.shop.repositories;

import com.cevs.reactive.shop.domain.UserOrder;
import org.bson.types.ObjectId;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserOrderRepository extends ReactiveCrudRepository<UserOrder, ObjectId> {
}
