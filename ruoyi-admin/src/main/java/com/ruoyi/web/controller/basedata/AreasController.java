package com.ruoyi.web.controller.basedata;

import com.ruoyi.basedata.domain.Areas;
import com.ruoyi.basedata.service.IAreasService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *  信息操作处理
 * 
 * @author ruoyi
 * @date 2019-05-11
 */
@Controller
@RequestMapping("/basedata/areas")
public class AreasController extends BaseController
{
    private String prefix = "financial/areas";
	
	@Autowired
	private IAreasService areasService;
	
	@GetMapping()
	public String areas()
	{
	    return prefix + "/areas";
	}
	
	/**
	 * 查询列表
	 */
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(Areas areas)
	{
		startPage();
        List<Areas> list = areasService.selectAreasList(areas);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出列表
	 */
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Areas areas)
    {
    	List<Areas> list = areasService.selectAreasList(areas);
        ExcelUtil<Areas> util = new ExcelUtil<Areas>(Areas.class);
        return util.exportExcel(list, "areas");
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
	@Log(title = "", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(Areas areas)
	{		
		return toAjax(areasService.insertAreas(areas));
	}

	/**
	 * 修改
	 */
	@GetMapping("/edit/{iD}")
	public String edit(@PathVariable("iD") String iD, ModelMap mmap)
	{
		Areas areas = areasService.selectAreasById(iD);
		mmap.put("areas", areas);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存
	 */
	@Log(title = "", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(Areas areas)
	{		
		return toAjax(areasService.updateAreas(areas));
	}
	
	/**
	 * 删除
	 */
	@Log(title = "", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(areasService.deleteAreasByIds(ids));
	}


	/**
	 * 根据地区名和level获取区域
	 */
	@PostMapping("/parentId")
	@ResponseBody
	public TableDataInfo findDistrictList(String province, String levelType)
	{
		startPage();
		List<Areas> list = areasService.findDistrictList(province, levelType);
		return getDataTable(list);
	}
}
