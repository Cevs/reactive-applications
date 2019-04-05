package com.cevs.reactivesocialapp.comments;

import com.cevs.reactivesocialapp.images.CommentReaderRepository;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CommentService {

    private CommentWriterRepository commentRepository;

    public CommentService(CommentWriterRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue,
            exchange = @Exchange(value = "learning-reactive"),
            key = "comments.new"
    ))
    public void save(Comment newComment){
        commentRepository
                .save(newComment)
                .log("commentService-save")
                .subscribe();
    }

    @Bean
    Jackson2JsonMessageConverter jackson2JsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }
}
