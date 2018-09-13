
function test() {
    return "test";
};

export function getUrlPrefix() {
    return $('#contextPath').attr('href');
}

export function getFormData(formId) {
    var id = '#' + formId;
    var data = $(id).serialize();
    console.log(data);
    return data;
}

export function ajaxQuery(url, param) {
    var successResult;
    $.ajax({
        type: 'post',
        url: url,
        data: param,
        dataType: 'json',
        success: function(data) {
            console.log(data);
            if(data.code < 0) {
                alert(data.msg);
                window.location.reload();
                return;
            }
            successResult = data;
        }
    });
    console.log(successResult);
    return successResult;
}

//
// export {
//     test,
//     getUrlPrefix,
//     getFormData
// }