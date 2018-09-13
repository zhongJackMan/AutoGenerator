
function test() {
    return "test";
};

export function getUrlPrefix() {
    return $('#contextPath').attr('href');
}

export function getFormData(formId) {
    var id = '#' + formId;
    var data = $(id).serializeArray();
    var jsonData = {};
    $.each(data,function(i,item){

        jsonData[item.name] = item.value;

    });
    return jsonData;
}

export function ajaxQuery(url, param, async) {
    var result;
    $.ajax({
        type: 'post',
        url: url,
        data: param,
        dataType: 'json',
        async:async,
        success: function(data) {
            console.log(data);
            if(data.code < 0) {
                alert(data.msg);
                window.location.reload();
                return;
            }
            result = data;
        }
    });
    return result;
}
