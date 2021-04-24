function searchTodo(){
	var formData = $('form').serialize();
    $.ajax({
    	type: "POST",
        url: "/search",
        data: formData
        }).done(function(data, textStatus, jqXHR) {
            if(!data){
                return;
            }

            // 画面のtableタグの全てのtrタグを削除
            $('#todoList').find("tr:gt(0)").remove();

            let i = 0;
            for(i = 0; i < data.length; i++){
                let trTag = $("<tr />");
                trTag.append($("<td></td>").text(decodeURI(data[i].id)));
                trTag.append($("<td></td>").text(decodeURI(data[i].title)));
                trTag.append($("<td></td>").text(decodeURI(data[i].content)));
                trTag.append($("<td></td>").text(decodeURI(data[i].limittimeText)));
                $('#todoList').append(trTag);
            }

        }).fail(function(jqXHR, textStatus, errorThrown ) {
            alert("データが取得できませんでした");
    });
}