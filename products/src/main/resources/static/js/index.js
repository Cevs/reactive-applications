$(document).ready(function(){
    $("#addNewProduct").on("click", function () {
        $("#modalInsertProduct").modal({backdrop: 'static', keyboard: false}, "show");
    });

    $("#bntSaveProduct").on("click", function () {
        console.log("clicked");
    });

    $("#formAddNewProduct").on("submit", function () {
        event.preventDefault();
        saveNewProduct(this);
    })

    $("#uploadProductImage").change(function () {
       readURL(this);
    });

    $('#btnSelectImageProduct').on('click', function () {
        $('#uploadProductImage').trigger('click');
    });

    $("#btnCancel").on('click', function () {
        $(".modal-body input").val("");
        $('.view-img-product img').attr('src', "/images/placeholder.jpg");
    });

});

function readURL(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
            $('.view-img-product img').attr('src', e.target.result);
        }

        reader.readAsDataURL(input.files[0]);
    }
}

function saveNewProduct(event){
    endPoint = window.location + "products/new"
    formData = new FormData(event);
    console.log(formData);

    $.ajax({
        url: endPoint,
        type: 'POST',
        data: formData,
        contentType: false,
        processData: false,
        success: function (response) {
            //console.log(response);
        }
    });

    $("#modalInsertProduct").modal("hide");
    $(".modal-body input").val("");
    $('.view-img-product img').attr('src', "/images/placeholder.jpg");
}