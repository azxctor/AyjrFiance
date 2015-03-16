require(['jquery',
         'global',
         'select2'], 
function($, global){
	var enlist = [
	     		{text:"内资企业",children:[
	            {id:190,text:"其他企业"},
	     		{text:"私营企业",children:[
	     			{id:174,text:"私营股份有限公司"},
	     			{id:173,text:"私营有限责任公司"},
	     			{id:172,text:"私营合伙企业"},
	     			{id:171,text:"私营独资企业"}
	     			
	     		]},
	     		{id:160,text:"股份有限公司"},
	     		{text:"有限责任公司",children:[
	     		     {id:159,text:"其他有限责任公司"},                     	
	     		     {id:151,text:"国有独资公司"}                	
	     		]},
	     		{text:"联营企业",children:[
	     		     {id:149,text:"其他联营企业"},
	     		     {id:143,text:"国有与集体联营企业"},
	     		     {id:142,text:"集体联营企业"},
	     		     {id:141,text:"国有联营企业"},
	     		]},
	     		{id:130,text:"股份合作企业"},
	     		{id:120,text:"集体企业"},
	     		{id:110,text:"国有企业"}
	     	]},
	     	{text:"港、澳、台商投资企业",children:[
	     	    {id:290,text:"其他港、澳、台商投资企业"},   
	     	    {id:240,text:"港、澳、台商投资股份有限公司"},   
	     	    {id:230,text:"港、澳、台商独资经营企业"},
	     	    {id:220,text:"合作经营企业（港或澳、台资）"},
	     	    {id:210,text:"合资经营企业（港或澳、台资）"}, 
	     	]},
	     	{text:"外商投资企业",children:[
	     	    {id:330,text:"外资企业"},
	     	    {id:320,text:"中外合作经营企业"},
	     	    {id:310,text:"中外合资经营企业"}
	     	]},
	     	{id:400,text:"个人"},
	     	{id:500,text:"国有"},
	     	{id:600,text:"集体"},
	     	{id:700,text:"有限责任公司"},
	     	{id:800,text:"股份有限公司"},
	     	{id:900,text:"外商独资"},
	     	{id:1000,text:"中外合资"},
	     	{id:9999,text:"其他"},
	     	];
		$("#org-type").select2({
			width:"100%",
			data:enlist,
			formatNoMatches: function () { return "未找到匹配项"; }
		});
	
});
