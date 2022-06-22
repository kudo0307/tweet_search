
/*
    削除ボタンクリック時の確認ダイアログ
    @param id アカウントid
 */
function deleteCheck(id) {

    if(confirm("id : "+id + "を削除しますか？")==false){
        return false
        }
}