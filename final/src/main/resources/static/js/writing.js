// 파일 업로드 트리거 함수
function triggerUpload(id) {
    document.getElementById(id).click();
}

// 이미지 미리보기 함수
function previewImage(event, imgElementId, textElementId) {
    const file = event.target.files[0];
    const imgElement = document.getElementById(imgElementId);
    const textElement = document.getElementById(textElementId);

    if (!file) return;

    const validImageTypes = ['image/jpeg', 'image/png'];
    if (!validImageTypes.includes(file.type)) {
        alert("jpg형식 또는 png형식만 업로드가 가능해요!");
        event.target.value = ''; // 입력 필드를 초기화하여 잘못된 파일을 제거합니다.
        return;
    }

    const reader = new FileReader();
    reader.onload = function(e) {
        imgElement.src = e.target.result;
        imgElement.style.display = 'block';
        textElement.style.display = 'none';
    };
    reader.readAsDataURL(file);
}

function deletePost(postId) {
    if (confirm("정말로 이 글을 삭제하시겠습니까?")) {
        fetch(`/deletePost?id=${postId}`, {
            method: 'POST',
        })
            .then(response => {
                if (response.ok) {
                    window.location.href = '/bookmain';
                } else {
                    alert("삭제 중 오류가 발생했습니다.");
                }
            })
            .catch(error => {
                alert("삭제 중 오류가 발생했습니다.");
                console.error('Error:', error);
            });
    }
}
