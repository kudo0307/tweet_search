$(function() {
  $(".favorite_btn").click(function() {
    $.ajax({
      url: $(this).parent("form").attr("action"),
      type: "POST",
      data: {
        tweetId: $(this).data("id"),
        _csrf: $("*[name=_csrf]").val()
      }
    })
    .done(function(data) {
        if(data.status =="register"){
            $('#tweet_id_'+data.id).find("div").removeClass("non_display");
            $('#tweet_id_'+data.id).find(".non_heart").addClass("non_display");
        }else if(data.status =="release"){
            $('#tweet_id_'+data.id).find("div").removeClass("non_display");
            $('#tweet_id_'+data.id).find(".heart").addClass("non_display");
        }
    })
    .fail(function() {
      alert("お気に入り情報の更新に失敗しました。");
    })
  });
});