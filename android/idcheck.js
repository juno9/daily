function checkDuplicate() {
    const idInput = document.getElementById("idInput");
    const memberId = idInput.value;
  
    const xhr = new XMLHttpRequest();
    xhr.open("POST", "idcheckprac.php");
    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xhr.onload = function() {
      const result = JSON.parse(xhr.responseText);
      if (result.isDuplicate) {
        alert("중복된 아이디입니다.");
      } else {
        alert("사용 가능한 아이디입니다.");
        idInput.disabled = true; // 입력창 비활성화
      }
    };
    xhr.send(JSON.stringify({ memberId }));
  }