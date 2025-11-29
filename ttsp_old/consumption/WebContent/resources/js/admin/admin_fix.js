q$ = jQuery;
q$(document).ready(function() {
	q$('body').addClass('skin-blue');
//	 q$(document).ajaxStart(function() { Pace.restart(); });
});
function redirectPage(url){
	setTimeout(() => window.location.replace(url), 1000);
}
function showMessage(title,messages,icon,timer){
		setTimeout(function(){
			swaldesigntimer(title, messages,icon,timer)
		}, 100);
}
function showPageNow(el) {
	var href=q$(el).attr('href');
	if(href!='#'){
		window.location.href = href;
	}else{
		 q$(el).parent().addClass('active');
		 q$('.treeview').removeClass('active');
		 q$('.singleNodeNav').removeClass('active');
	}
}
window.onload = function()
{
   var arrayElement=q$('.treeview-menu');
   var urlCurrent=window.location.pathname;
   for(i=0;i<arrayElement.length;i++){
	 var href= arrayElement[i].previousElementSibling.getAttribute("href");
	 if(urlCurrent==href){
		 arrayElement[i].parentNode.classList.add("active");		 
		 return;
	 }
   }
   var singleNodeNavArr=q$('.singleNodeNav');
   for(i=0;i<singleNodeNavArr.length;i++){
	  var href=singleNodeNavArr[i].querySelector('a').getAttribute("href");
	  if(href==urlCurrent){
		  singleNodeNavArr[i].classList.add("active");
		  break;
	  }
   }
   var menuOpen=q$('.treeview-menu>li>a');
   for(i=0;i<menuOpen.length;i++){
	 var href1=menuOpen[i].getAttribute("href");
	 if(href1==urlCurrent){
	    console.log(menuOpen[i].parentNode)
		 menuOpen[i].parentNode.classList.add("active");
	    menuOpen[i].closest(".treeview").classList.add("active");
	    break;
	 }
   }
   q$(".main-sidebar").height();
   q$(".content-wrapper").height(q$(".main-sidebar").height());
};
function callService(url, data) {
	q$.ajax({
		url : "#{request.contextPath}/api/" + url,
		type : "post", // chọn phương thức gửi là post
		dataType : "text", // dữ liệu trả về dạng text
		data : JSON.stringify(data),
		success : function(result) {
			// Sau khi gửi và kết quả trả về thành công thì gán nội dung trả về
			// đó vào thẻ div có id = re
			console.log("result:" + result);
			if (result == "0") {
				console.log("Them thanhcong")
			}
		}
	});
}
