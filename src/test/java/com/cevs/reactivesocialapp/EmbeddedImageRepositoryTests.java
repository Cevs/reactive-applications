package com.cevs.reactivesocialapp;

import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

@DataMongoTest
public class EmbeddedImageRepositoryTests extends  ImageRepositoryTest{}
