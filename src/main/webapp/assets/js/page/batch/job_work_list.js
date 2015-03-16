require(['jquery',
         'global',
         'module/util',
         'module/datatables',
         'form',
         'select2',
         'requirejs/domready!'], 
function($, global, util, datatables, form){
	//变量
	var option={
			action:null,
			context:null,
			dataExtension:null,
	};
	//日期
	$('#work-date').datepicker({
		format: 'yyyy-mm-dd',
        weekStart: 1,
        autoclose: true,
        todayBtn: false,
        language: 'zh-CN'
    });
	
	$('#batch-task-list').dataTable( {
		  "aoColumnDefs": [{
             "sWidth": "30%",
             "aTargets": [6]
          }],
		  "bInfo": false,//页脚信息
		  "bPaginate": false, //开关，是否显示分页器  
		  "bFilter": false, //开关，是否启用客户端过滤器  
		  "bAutoWith": true,  
		  "bSort": false, //开关，是否启用各列具有按列排序的功能    
		  "bRetrieve": true,
		  "oLanguage": {
              "sProcessing": "正在加载中......",
              "sEmptyTable": "没有检索到数据",
          }
	  } );
	
	//日终任务执行
	$("#execute-btn-execution").on("click",function(){
		$("#execution-modal").modal('hide');
		option.action = global.context+"/web/batch/execute/taskgroup/jobWork";
		util.ajax_submit("#batch-task",option).done(function(response){
			var _response = eval(response);
			if(_response.code == "ACK"){
				$("#work-date").val('');
				$("#batch-task-search").click();
			}
		});
	});
	
	//日终任务创建
	$("#create-btn-execution").on("click",function(){
		$("#create-modal").modal('hide');
		option.action = global.context+"/web/batch/create/taskgroup/jobWork";
		util.ajax_submit("#batch-task",option).done(function(response){
			var _response = eval(response);
			if(_response.code == "ACK"){
				$("#batch-task-search").click();
			}
		});
	});
	
	//判断是否为当前系统工作日
	var checkNowDate = function(){
		var exec_able;
		var create_able;
		var nowDate = $("#now-date").text();
		var workDate = $("#work-date").val();
		if(nowDate != workDate){
			$("#end-day-task-execution").attr("disabled",true);
			$("#end-day-task-create").attr("disabled",true);
		}else{
			var exec_enbale = $("#exec-enable").text();
			var create_enbale = $("#create-enable").text();
			if(exec_enbale == "true"){
				exec_able = false;
			}
			else{
				exec_able = true;
			}
			if(create_enbale == "true"){
				create_able = false;
			}
			else{
				create_able = true;
			}
			$("#end-day-task-execution").attr("disabled",exec_able);
			$("#end-day-task-create").attr("disabled",create_able);
		}
	};
	$("#work-date").on("change",function(){
		checkNowDate();
	});
	checkNowDate();
});