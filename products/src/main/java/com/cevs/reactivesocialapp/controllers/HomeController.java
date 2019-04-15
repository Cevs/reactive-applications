package com.cevs.reactivesocialapp.controllers;

import com.cevs.reactivesocialapp.dto.ProductDto;
import com.cevs.reactivesocialapp.products.CommentHelper;
import com.cevs.reactivesocialapp.products.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.thymeleaf.spring5.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.HashMap;

@Controller
public class HomeController {

    private static final String BASE_PATH = "/products";
    private static final String FILENAME = "{filename:.+}";

    private final ProductService productService;
    private final CommentHelper commentHelper;

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    public HomeController(ProductService productService, CommentHelper commentHelper) {
        this.productService = productService;
        this.commentHelper = commentHelper;
    }

    /*
        Generating the HTTP OK/HTTP BAD REQUEST response doesn't happen until map() is executed.
        This is chained to the image service fetching the file from disk.
        And none od that happens until the client subscribes.
        Subscribing is handled by framework when the request comes in.
     */
    //produces = MediaType.IMAGE_JPEG_VALUE => Tell browser to render image (set the Content-Type header)
    @GetMapping(value = BASE_PATH + "/" + FILENAME + "/raw", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public Mono<ResponseEntity<?>> oneRawImage(@PathVariable String filename){
        return productService.findOneProduct(filename)
                .map(resource -> {
                    try {
                        return ResponseEntity.ok().contentLength(resource.contentLength())
                                .body(new InputStreamResource(resource.getInputStream()));
                    }catch(IOException e){
                        return ResponseEntity.badRequest().body("Couldn't find " + filename + " => " + e.getMessage());
                    }
                });
    }

    @PostMapping(value = BASE_PATH + "/new")
    public Mono<String> insertProduct(ProductDto product){
        return productService.insertProduct(product).then(Mono.just("redirect:/"));
    }

    @DeleteMapping(value = BASE_PATH + "/" + FILENAME)
    public Mono<String> deleteFile(@PathVariable String filename){
        //use then() to wait until the delete is done before returning back a mono-wrapped redirect:/
        return productService.deleteProduct(filename).then(Mono.just("redirect:/"));
    }

    @GetMapping("/")
    public Mono<String> index(Model model) {

        IReactiveDataDriverContextVariable reactiveDataDrivenMode =
                new ReactiveDataDriverContextVariable(  productService.findAllProducts()
                        .map(product-> new HashMap<String, Object>(){{
                            log.info(product.toString());
                            put("id", product.getId());
                            put("name", product.getName());
                            put("description", product.getDescription());
                            put("imageName", product.getImageName());
                            put("category", product.getCategory());
                            put("price", product.getPrice());
                            put("comments", commentHelper.getComments(product.getId()));
                        }})
                        ,1);

        model.addAttribute("products", reactiveDataDrivenMode);
        return Mono.just("index");
    }
}
