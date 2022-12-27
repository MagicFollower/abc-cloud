package com.abc.system.controller;

import com.abc.common.core.constant.CacheConstants;
import com.abc.common.core.web.controller.BaseController;
import com.abc.common.core.web.domain.AjaxResult;
import com.abc.common.core.web.page.TableDataInfo;
import com.abc.common.redis.service.RedisService;
import com.abc.common.security.annotation.InnerAuth;
import com.abc.common.security.annotation.RequiresPermissions;
import com.abc.system.api.domain.SysLogininfor;
import com.abc.system.service.ISysLogininforService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 系统访问记录
 *
 * @author abc
 */
@RestController
@RequestMapping("/logininfor")
public class SysLogininforController extends BaseController {
    @Autowired
    private ISysLogininforService logininforService;

    @Autowired
    private RedisService redisService;

    @RequiresPermissions("system:logininfor:list")
    @GetMapping("/list")
    public TableDataInfo list(SysLogininfor logininfor) {
        startPage();
        List<SysLogininfor> list = logininforService.selectLogininforList(logininfor);
        return getDataTable(list);
    }

//    // @Log(title = "登录日志", businessType = BusinessType.EXPORT)
//    @RequiresPermissions("system:logininfor:export")
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, SysLogininfor logininfor) {
//        List<SysLogininfor> list = logininforService.selectLogininforList(logininfor);
//        ExcelUtil<SysLogininfor> util = new ExcelUtil<SysLogininfor>(SysLogininfor.class);
//        util.exportExcel(response, list, "登录日志");
//    }

    @RequiresPermissions("system:logininfor:remove")
    // @Log(title = "登录日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{infoIds}")
    public AjaxResult remove(@PathVariable Long[] infoIds) {
        return toAjax(logininforService.deleteLogininforByIds(infoIds));
    }

    @RequiresPermissions("system:logininfor:remove")
    // @Log(title = "登录日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/clean")
    public AjaxResult clean() {
        logininforService.cleanLogininfor();
        return success();
    }

    @RequiresPermissions("system:logininfor:unlock")
    // @Log(title = "账户解锁", businessType = BusinessType.OTHER)
    @GetMapping("/unlock/{userName}")
    public AjaxResult unlock(@PathVariable("userName") String userName) {
        redisService.deleteObject(CacheConstants.PWD_ERR_CNT_KEY + userName);
        return success();
    }

    @InnerAuth
    @PostMapping
    public AjaxResult add(@RequestBody SysLogininfor logininfor) {
        return toAjax(logininforService.insertLogininfor(logininfor));
    }
}
