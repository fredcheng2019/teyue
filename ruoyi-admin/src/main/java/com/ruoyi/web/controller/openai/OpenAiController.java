package com.ruoyi.web.controller.openai;

import com.ruoyi.common.core.controller.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * openai
 *
 * @author teyue
 */
@Controller
@RequestMapping("/openai")
public class OpenAiController  extends BaseController {

    private String prefix = "openai";

    @RequiresPermissions("openai:view")
    @GetMapping()
    public String openai()
    {
        return prefix + "/openai";
    }
}
