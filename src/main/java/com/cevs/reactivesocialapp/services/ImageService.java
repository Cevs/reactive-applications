package com.cevs.reactivesocialapp.services;

import com.cevs.reactivesocialapp.Image;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class ImageService {

    private static String UPLOAD_ROOT = "upload-dir";

    //Base folder where images will be saved
    private final ResourceLoader resourceLoader;

    public ImageService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public Flux<Image> findAllImages() {
        try{
            return Flux.fromIterable(
                    //Directory stream is lazy iterable (which means that nothing is fetched until next() is called)
                    Files.newDirectoryStream(Paths.get(UPLOAD_ROOT))
            ).map(path-> new Image(path.hashCode()+"", path.getFileName().toString()));
        }catch (IOException ex){
            return Flux.empty();
        }
    }

    public Mono<Resource> findOneImage(String filename){
        //If we put Mono.just() instead of Mono.fromSupplier() the resource fetching would happen immediately
        //when the method is called.
        //By putting it inside a Java 8 Supplier, that won't happen until the lambda is invoked, and because
        //its wrapped by a Mono, invocation won't happen until the client subscribes
        return Mono.fromSupplier(()->
           resourceLoader.getResource("file:" + UPLOAD_ROOT + "/" + filename)
        );
    }

    public Mono<Void> createImage(Flux<FilePart> files){
        return files.flatMap(file -> file.transferTo(
                Paths.get(UPLOAD_ROOT, file.filename()).toFile()
        )).then();
    }

    public Mono<Void> deleteImage(String filename){
        return Mono.fromRunnable(()->{
            try{
                Files.deleteIfExists(Paths.get(UPLOAD_ROOT, filename));
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        });
    }


    /*
        Preload test images
     */
    @Bean
    CommandLineRunner setUp(){
        //Lambda automatically gets converted into a CommandLineRunner via Java 8 SAM ( Single abstraction method)
        return args -> {
            FileSystemUtils.deleteRecursively(new File(UPLOAD_ROOT));
            Files.createDirectory(Paths.get(UPLOAD_ROOT));

            FileCopyUtils.copy("Test file",
                    new FileWriter(UPLOAD_ROOT +
                            "Image 1"));
            FileCopyUtils.copy("Test file2",
                    new FileWriter(UPLOAD_ROOT +
                            "Image 2"));
            FileCopyUtils.copy("Test file3",
                    new FileWriter(UPLOAD_ROOT +
                            "Image 3"));
        };
    }
}
