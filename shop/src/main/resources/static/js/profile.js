$(document).ready(function () {

    $("#upload").change(function () {
        readURL(this);
    });

    $('#btnSelectImage').on('click', function () {
        $('#upload').trigger('click');

    });

    /*$("#formUpdate").submit(function (event) {
        event.preventDefault();
        formData = new FormData(this);
        endPoint = window.location;
        $.ajax({
            url: endPoint,
            type: 'POST',
            data: formData,
            contentType: false,
            processData: false,
            success: function (response) {
                if(response == "true"){
                    $("#msgSuccess").removeClass("d-none");
                    $("#msgFail").addClass("d-none");

                }else{
                    $("#msgFail").removeClass("d-none");
                    $("#msgSuccess").addClass("d-none");
                }
            }
        });
    });*/
});


function readURL(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
            $('.view-img img').attr('src', e.target.result);
        }

        reader.readAsDataURL(input.files[0]);
    }
}
