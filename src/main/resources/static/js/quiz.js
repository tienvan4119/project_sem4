function HienThiThoiGianThi(time) {
  var countDownDate = new Date().getTime() + time * 1000 + 2;
// Update the count down every 1 second
  var x = setInterval(function () {
    var now = new Date().getTime();
    var distance = countDownDate - now;
    var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
    var seconds = Math.floor((distance % (1000 * 60)) / 1000);
    //Output the result in an element with id="demo"
    document.getElementById("count").innerHTML = minutes + "m " + seconds + "s ";
    // If the count down is over, write some text
    if (distance < 0) {
      clearInterval(x);
      //let CauHoi = LayCacGiaTriNULL();
      SubmitForm();
      window.location.replace('/section');
    }
  }, 1000);
}

function LayCacGiaTriNULL () {
  let CauHoiChuaDuocTraLoi = "";
  let Soluong = window.value;
  for (let i = 1; i<= Soluong; i++ ) {
    let count = 0;
    let baseQuestion = document.getElementById(i);
    let element = baseQuestion.children[2];
    for (let j = 0; j<element.children.length; j++ ) {
      if (element.children[j].firstElementChild.firstElementChild.checked) {
        count++;
      }
    }
    if (count === 0 )  {
      CauHoiChuaDuocTraLoi += `${i} `;
    }

  }
  if (CauHoiChuaDuocTraLoi !== "") CauHoiChuaDuocTraLoi += " is not answered";
  return CauHoiChuaDuocTraLoi;
}
function appearFunction () {
  event.preventDefault();
  Swal.fire({
    title: 'Do you want to finish the test?',
    text: "You won't be able to revert this!",
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#3085d6',
    cancelButtonColor: '#d33',
    confirmButtonText: 'OK'
  }).then((result) => {
    if (result.value) {
      let CauHoi = LayCacGiaTriNULL();
      // Các câu chưa được trả lời được lấy tại đây
      if (CauHoi !== ""){
        console.log("CO CAU HOI CHUA TRA LOI")
        Swal.fire({
          title: 'Some questions arent answered',
          text: CauHoi,
          icon: 'warning',
          showCancelButton: true,
          confirmButtonColor: '#3085d6',
          cancelButtonColor: '#d33',
          confirmButtonText: 'Click if u still wanna finished that test '
        }).then((result) =>  {
          if (result.value) {
            //SubmitForm();
            window.location.replace('/section');
          }
        })
      } else {
          console.log("TAT CA CAU HOI DA DUOC TRA LOI")
         // SubmitForm();
          window.location.replace('/section');
      }
    }
  });
}
function SubmitForm () {
  console.log($('#done').val());
  var getSectionId = $('#done').val();
  let timeRest = document.getElementById("count").innerHTML;
  let current_url = window.location.href
  // Thời gian được lấy ở TimmRest
  $.ajax({
    type: "POST",
    url: "/api/quiz/finishTest",
    dataType : 'json',
    data : {
      getSectionId : getSectionId,
      current_url : current_url
    },
    success: function () {

    },
  });
  window.location.replace('/section');
}
function createPagination(numberOfPage) {
  for (let i = 1; i<=numberOfPage; i++ )
  {
    if (i===1) {
      let node =  "<li class=\"page-item active\"><a class = \"button\" data-toggle=\"tab\" href=\"#"+i+"\">"+i+"</a></li>";
      $('#pagination').append(node);
      continue;
    }
    let node =  "<li class=\"page-item\"><a class = \"button\" data-toggle=\"tab\" href=\"#"+i+"\">"+i+"</a></li>";
    $('#pagination').append(node);
  }
let Next = "<li class=\"page-item\"><a id=\"next\" class=\"page-link\" href=\"#\">Next</a></li>";
  $('#pagination').append(Next);
}
//LayCacGiaTriNULL();