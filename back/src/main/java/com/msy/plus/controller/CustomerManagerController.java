package com.msy.plus.controller;

import com.msy.plus.core.jwt.JwtUtil;
import com.msy.plus.core.response.Result;
import com.msy.plus.core.response.ResultGenerator;
import com.msy.plus.dto.CustomerManagerList;
import com.msy.plus.entity.CustomerManager;
import com.msy.plus.service.CustomerManagerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
* @author MoShuYing
* @date 2021/05/20
*/
@PreAuthorize(
        "hasAuthority('ADMIN')"+
                "or hasAuthority('董事长')"+
                "or hasAuthority('主席')"+
                "or hasAuthority('高级主席')"+
                "or hasAuthority('副主席')"+
                "or hasAuthority('总裁')"+
                "or hasAuthority('会长')"+
                "or hasAuthority('高级总裁')"+
                "or hasAuthority('高级副总裁')"+
                "or hasAuthority('副总裁')"+
                "or hasAuthority('总经理')"+
                "or hasAuthority('副总经理')"+
                "or hasAuthority('总监')"+
                "or hasAuthority('经理')"+
                "or hasAuthority('高级经理')"+
                "or hasAuthority('副经理')"+
                "or hasAuthority('主任')"+
                "or hasAuthority('高级主任')"+
                "or hasAuthority('副主任')"+
                "or hasAuthority('组长')"+
                "or hasAuthority('副组长')"+
                "or hasAuthority('普通员工')"+
                "or hasAuthority('人事专员')"+
                "or hasAuthority('市场专员')"+
                "or hasAuthority('市场主管')"+
                "or hasAuthority('销售主管')"
)
@Api(tags={"客户管理接口"})
@RestController
@RequestMapping("/customer/manager")
public class CustomerManagerController {
    @Resource private CustomerManagerService customerManagerService;
    @Resource private JwtUtil jwtUtil;

    @Operation(description = "客户管理添加")
    @PostMapping
    public Result add(@RequestBody CustomerManager customerManager,@RequestHeader Map<String, String> headers) {
        if(customerManager.getId()!=null){
            customerManager.setId(null);
        }
        String header = jwtUtil.getJwtProperties().getHeader();
        String id= jwtUtil.getId(headers.get(header)).get();
        customerManager.setInputuser(Integer.valueOf(id));
        customerManager.setSeller(Integer.valueOf(id));
        customerManagerService.save(customerManager);
        return ResultGenerator.genOkResult();
    }

//    @Operation(description = "客户管理删除")
//    @DeleteMapping("/{id}")
//    public Result delete(@PathVariable Long id) {
//    customerManagerService.deleteById(id);
//        return ResultGenerator.genOkResult();
//    }

    @Operation(description = "客户管理更新")
    @PutMapping
    public Result update(@RequestBody CustomerManager customerManager) {
    customerManagerService.update(customerManager);
        return ResultGenerator.genOkResult();
    }

    @Operation(description = "客户管理获取详细信息")
    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id) {
    CustomerManager customerManager = customerManagerService.getById(id);
        return ResultGenerator.genOkResult(customerManager);
    }

    @Operation(description = "客户管理分页查询")
    @GetMapping
    @ApiOperation(value="分页查询客户管理", notes="分页查询 ")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "第几页", required = true, dataType = "Integer", paramType="query"),
        @ApiImplicitParam(name = "size", value = "一页有几条", required = true, dataType = "Integer", paramType="query")
    })
    public Result list(@RequestParam(defaultValue = "1") Integer page,
       @RequestParam(defaultValue = "10") Integer size,
       @RequestParam(defaultValue = "",required = false) String keyword,
       @RequestParam(required = false) Integer status) {
        PageHelper.startPage(page, size);
        List<CustomerManagerList> list = customerManagerService.listAllWithDictionary(keyword,status);
        PageInfo<CustomerManagerList> pageInfo = PageInfo.of(list);
        return ResultGenerator.genOkResult(pageInfo);
    }
}
