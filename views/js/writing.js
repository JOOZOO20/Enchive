// 업로드 버튼 트리거 함수
function triggerUpload(fileInputId) {
    document.getElementById(fileInputId).click();
}

// 이미지 미리보기 함수
function previewImage(event, previewId, textId) {
    var reader = new FileReader();
    reader.onload = function(){
        var output = document.getElementById(previewId);
        output.src = reader.result;
        output.style.display = 'block';

        // 텍스트 숨기기
        var textElement = document.getElementById(textId);
        if (textElement) {
            textElement.style.display = 'none';
        }
    };
    reader.readAsDataURL(event.target.files[0]);
}
