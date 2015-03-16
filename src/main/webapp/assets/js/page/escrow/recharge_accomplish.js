

function recharge_colse(){
	//window.opener=null;  
	//window.open('','_self');  
	//window.close();  
	window.open('','_parent','');
	window.opener = window;
	window.close();
};
