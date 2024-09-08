package org.hzero.imported.api.controller.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.imported.app.service.TemplateManagerService;
import org.hzero.imported.config.ImportSwaggerApiConfig;
import org.hzero.imported.domain.entity.TemplateHeader;
import org.hzero.imported.infra.constant.HimpMessageConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;

/**
 * 模板功能接口
 *
 * @author shuangfei.zhu@hand-china.com 2018-12-7 16:42:53
 */
@Api(tags = ImportSwaggerApiConfig.TEMPLATE_MANAGER)
@RestController("templateManagerController.v1")
@RequestMapping(value = "/v1/{organizationId}/template")
public class TemplateManagerController extends BaseController {

    private final TemplateManagerService templateService;

    @Autowired
    public TemplateManagerController(TemplateManagerService templateService) {
        this.templateService = templateService;
    }

    @ApiOperation(value = "导出Excel模板", produces = "application/octet-stream")
    @GetMapping(value = "/{templateCode}/excel")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public void export(@PathVariable @ApiParam(value = "租户Id", required = true) Long organizationId,
                       @PathVariable @ApiParam(value = "模板编码", required = true) String templateCode,
                       HttpServletResponse response) {
        templateService.exportExcel(organizationId, templateCode, response);
    }

    @ApiOperation(value = "导出Csv模板", produces = "application/octet-stream")
    @GetMapping(value = "/{templateCode}/csv")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public void exportCsv(@PathVariable @ApiParam(value = "租户Id", required = true) Long organizationId,
                          @PathVariable @ApiParam(value = "模板编码", required = true) String templateCode,
                          HttpServletResponse response) {
        templateService.exportCsv(organizationId, templateCode, response);
    }

    @ApiOperation(value = "获取模板头行详细信息")
    @GetMapping(value = "/{templateCode}/info")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue(targetField = {BaseConstants.FIELD_BODY, BaseConstants.FIELD_BODY + ".templateTargetList"})
    public ResponseEntity<TemplateHeader> getTemplateInfo(@PathVariable @ApiParam(value = "租户Id", required = true) Long organizationId,
                                                          @PathVariable @ApiParam(value = "模板编码", required = true) String templateCode) {
        TemplateHeader templateHeader = templateService.getTemplateInfo(organizationId, templateCode);
        Assert.notNull(templateHeader, HimpMessageConstants.TEMPLATE_HEADER_NOT_FOUND);
        return Results.success(templateHeader);
    }

    @ApiOperation(value = "获取模板头行详细信息(无多语言)")
    @GetMapping(value = "/{templateCode}/info/no-multi")
    @Permission(permissionWithin = true)
    @ProcessLovValue(targetField = {BaseConstants.FIELD_BODY, BaseConstants.FIELD_BODY + ".templateTargetList"})
    public ResponseEntity<TemplateHeader> getTemplateInfoNoMulti(@PathVariable @ApiParam(value = "租户Id", required = true) Long organizationId,
                                                                 @PathVariable @ApiParam(value = "模板编码", required = true) String templateCode) {
        TemplateHeader templateHeader = templateService.getTemplateInfoNoMulti(organizationId, templateCode);
        Assert.notNull(templateHeader, HimpMessageConstants.TEMPLATE_HEADER_NOT_FOUND);
        return Results.success(templateHeader);
    }
}
