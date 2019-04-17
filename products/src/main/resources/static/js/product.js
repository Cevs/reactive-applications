$(document).ready(function(){
    $("#navLinkProductAbout").on("click", function () {
        $("#navLinkProductUserReviews").removeClass("active");
        $(this).addClass("active");
        $(".product-review").hide();
        $(".product-about").show();
    });

    $("#navLinkProductUserReviews").on("click", function () {
        $("#navLinkProductAbout").removeClass("active");
        $(this).addClass("active");
        $(".product-review").show();
        $(".product-about").hide();
    });


    var source = new EventSource("http://localhost:8080/advertisement/");
    source.onmessage = function (evt) {
        var dataObj  = evt.data;
        var tupleObject = JSON.parse(dataObj);
        var imageNamePrimary = tupleObject.t1.imageName;
        var imageNameSecondary = tupleObject.t2.imageName;

        $("#advertisementImagePrimary").attr('src',  "/advertisement/" + imageNamePrimary + "/raw");
        $("#advertisementImageSecondary").attr('src',  "/advertisement/" + imageNameSecondary + "/raw");
    }


    var socket = new WebSocket(
        'ws://localhost:8200/topic/reviews.new'
    );
    socket.onopen = function (ev) {
        console.log('--- Connected to chat service ---');
        console.log(event);
    }

    // Receive new comments from CHAT SERVICE
    // Update comment UI
    socket.onmessage = function(event){
        jsonDataObject = event.data;
        reviewData = JSON.parse(jsonDataObject);
        userId = reviewData.userId;
        date = reviewData.date;
        comment = reviewData.comment;
        $.get( "http://localhost:8080/user/"+userId, function( userData ) {

            var $div =$(
                "<div id=\"userInfo-\" class=\"card mb-3\">"+
                    "<div class=\"row no-gutters p-1\">"+
                        "<div class='col-md-2  align-self-center'>" +
                            "<img src=\"/products/" + userData.imageName + "/raw\"  class=\'card-img mx-auto p-1\ style=\'height:80%;\'>" +
                            "<p style=\'font-size:12px; margin:0\'>"+userData.username+"</p>"+
                            "<p class=\'card-text\'><small class=\'text-muted\' style=\'font-size:12px;\'>"+date+"</small></p>"+
                        "</div>"+
                        "<div class=\"col-md-10  align-self-center\">" +
                            "<div class=\"card-body pt-0 pb-0\">" +
                                "<p class=\'card-text\'  style=\'font-size:14px;\'>"+reviewData.comment+"</p>"+
                            "</div>"+
                        "</div>"+
                    "</div>"+
                "</div>"
            );

            $div.effect("highlight", {}, 2000);
            $("#userReviewSection").append($div);
        });
    }

    $(".button-review").on("click", function(){
        button = $(".button-review");
        review = $("#reviewText").val();
        today = new Date();
        dd = String(today.getDate()).padStart(2,'0');
        mm = String(today.getMonth()).padStart(2,'0');
        yyyy = String(today.getFullYear());
        date = yyyy+"-"+mm+"-"+dd;

        xhr = new XMLHttpRequest();
        xhr.open('POST', '/reviews', true);
        console.log(button.attr("id"));
        formData = new FormData();
        formData.append('userId', 11);
        formData.append('comment', review);
        formData.append('productId', button.attr("id"));
        formData.append('date', date);
        xhr.send(formData);
        $("#reviewText").val("");
    });


});