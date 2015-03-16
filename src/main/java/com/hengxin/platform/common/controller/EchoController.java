package com.hengxin.platform.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class EchoController {

	@RequestMapping("echo")
	@ResponseBody
	public String echo() {
		return String.valueOf(System.currentTimeMillis());
	}

}
