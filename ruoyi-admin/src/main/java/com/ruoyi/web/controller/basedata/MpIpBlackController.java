package com.ruoyi.web.controller.basedata;

import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.basedata.domain.MpIpBlack;
import com.ruoyi.basedata.service.IMpIpBlackService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 *  信息操作处理
 * 
 * @author ruoyi
 * @date 2019-05-15
 */
@Controller
@RequestMapping("/basedata/mpIpBlack")
public class MpIpBlackController extends BaseController
{
    private String prefix = "basedata/mpIpBlack";
	
	@Autowired
	private IMpIpBlackService mpIpBlackService;
	
	@RequiresPermissions("basedata:mpIpBlack:view")
	@GetMapping()
	public String mpIpBlack()
	{
	    return prefix + "/mpIpBlack";
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("basedata:mpIpBlack:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(MpIpBlack mpIpBlack)
	{
		startPage();
        List<MpIpBlack> list = mpIpBlackService.selectMpIpBlackList(mpIpBlack);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出列表
	 */
	@RequiresPermissions("basedata:mpIpBlack:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(MpIpBlack mpIpBlack)
    {
    	List<MpIpBlack> list = mpIpBlackService.selectMpIpBlackList(mpIpBlack);
        ExcelUtil<MpIpBlack> util = new ExcelUtil<MpIpBlack>(MpIpBlack.class);
        return util.exportExcel(list, "mpIpBlack");
    }
	
	/**
	 * 新增
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存
	 */
	@RequiresPermissions("basedata:mpIpBlack:add")
	@Log(title = "", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(MpIpBlack mpIpBlack)
	{		
		return toAjax(mpIpBlackService.insertMpIpBlack(mpIpBlack));
	}

	/**
	 * 修改
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Long id, ModelMap mmap)
	{
		MpIpBlack mpIpBlack = mpIpBlackService.selectMpIpBlackById(id);
		mmap.put("mpIpBlack", mpIpBlack);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存
	 */
	@RequiresPermissions("basedata:mpIpBlack:edit")
	@Log(title = "", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(MpIpBlack mpIpBlack)
	{		
		return toAjax(mpIpBlackService.updateMpIpBlack(mpIpBlack));
	}
	
	/**
	 * 删除
	 */
	@RequiresPermissions("basedata:mpIpBlack:remove")
	@Log(title = "", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(mpIpBlackService.deleteMpIpBlackByIds(ids));
	}

	/**
	 * 检测ip是否存在
	 */
	@RequestMapping("/checkIpUnique")
	@ResponseBody
	public String checkIpUnique(String ip,Long id){
		MpIpBlack mpIpBlack = new MpIpBlack();
		mpIpBlack.setIp(ip);
		List<MpIpBlack> mpIpBlacks = mpIpBlackService.selectMpIpBlackList(mpIpBlack);
		for(MpIpBlack m : mpIpBlacks){
			if(m.getIp().equals(ip) && m.getId().longValue() != id.longValue()){
				return "1";
			}
		}
		return "0";
	}
}
