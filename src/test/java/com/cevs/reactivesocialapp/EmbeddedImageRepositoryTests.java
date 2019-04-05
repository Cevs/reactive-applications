package com.cevs.reactivesocialapp;

import com.cevs.reactivesocialapp.domain.Image;
import com.cevs.reactivesocialapp.repositories.ImageRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataMongoTest
public class EmbeddedImageRepositoryTests {

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    MongoOperations operations;

    @Before
    public void setUp(){
        operations.dropCollection(Image.class);
        operations.insert(new Image("1", "image1.jpg"));
        operations.insert(new Image("2", "image2.jpg"));
        operations.insert(new Image("3", "image3.jpg"));

        operations.findAll(Image.class).forEach(image -> {
            System.out.println(image.toString());
        });
    }

    @Test
    public void findAllShouldWork(){
        Flux<Image> images = imageRepository.findAll();
        StepVerifier.create(images)
                .recordWith(ArrayList::new)             //Fetch entire Flux and converts it into an ArrayList
                .expectNextCount(3)
                .consumeRecordedWith(results ->{
                    assertThat(results).hasSize(3);
                    assertThat(results)
                            .extracting(Image::getName)
                            .contains(
                                    "image1.jpg",
                                    "image2.jpg",
                                    "image3.jpg"
                            );
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void findByNameShouldWork(){
        Mono<Image> image = imageRepository.findByName("image1.jpg");

        StepVerifier.create(image)
                .expectNextMatches(result->{
                    assertThat(result.getId()).isEqualTo("1");
                    assertThat(result.getName()).isEqualTo("image1.jpg");
                    return true;
                });
    }
}
