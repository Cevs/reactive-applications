package com.cevs.reactivesocialapp.services;

import com.cevs.reactivesocialapp.domain.Image;
import com.cevs.reactivesocialapp.repositories.ImageRepository;
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
import java.util.UUID;

@Service
public class ImageService {

    private static String UPLOAD_ROOT = "upload-dir";

    //Base folder where images will be saved
    private final ResourceLoader resourceLoader;
    private final ImageRepository imageRepository;

    public ImageService(ResourceLoader resourceLoader, ImageRepository imageRepository) {
        this.resourceLoader = resourceLoader;
        this.imageRepository = imageRepository;
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

    /*
        Istovremeno spremi sliku u bazu i na server.
        Ukratko, ne čeka se da se prvo spremi datoteka u bazu pa da se tek onda krene spremati na server,
        vec se radnje odvijaju simultano
     */
    public Mono<Void> createImage(Flux<FilePart> files){
        return files
                .log("createImage-files")
                .flatMap(file ->{
                    Mono<Image> saveDatabaseImage = imageRepository.save(
                            new Image(
                                    UUID.randomUUID().toString(),
                                    file.filename()

                            )
                    ).log("createImage-save");

                    Mono<Void> copyFile = Mono.just(Paths.get(UPLOAD_ROOT,file.filename()).toFile())
                            .log("createImage-picktarget")
                            .map(destFile ->{
                                try {
                                    destFile.createNewFile();
                                    return destFile;
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            })
                            .log("createImage-flatMap")
                            .flatMap(file::transferTo)
                            .log("createImage-copy");
                    //To ensure both of these operations are completed, join them together
                    return Mono.when(saveDatabaseImage, copyFile).log("createImage-when");
                })
                .log("createImage-flatMap")
                .then()
                .log("createImage-then"); //Signal when all files have been processed
    }

    public Mono<Void> deleteImage(String filename){
        Mono<Void> deleteDatabaseImage = imageRepository
                .findByName(filename)
                .log("deleteImage-find")
                .flatMap(image -> imageRepository.delete(image))
                .log("deleteImage-record");

        Mono<Void> deleteFiles = Mono.fromRunnable(()->{
            try {
                Files.deleteIfExists(
                        Paths.get(UPLOAD_ROOT, filename)
                );
            } catch (IOException e) {

                throw new RuntimeException(e);
            }
        });

        // When using .when() and .then() is also known as PROMISE PATTERN
        return Mono.when(deleteDatabaseImage, deleteFiles)
                .log("deleteImage-when")
                .then()
                .log("deleteImage-done");
    }


    /*
        Preload test images
     */
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
