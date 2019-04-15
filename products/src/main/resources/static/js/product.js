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
});