package org.hzero.platform.api.controller.v1;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.platform.api.dto.commontemplate.*;
import org.hzero.platform.app.service.CommonTemplateService;
import org.hzero.platform.config.PlatformSwaggerApiConfig;
import org.hzero.platform.domain.entity.CommonTemplate;
import org.hzero.starter.keyencrypt.core.Encrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import static org.hzero.core.base.BaseConstants.FIELD_BODY;

/**
 * 通用模板 管理 API
 *
 * @author bo.he02@hand-china.com 2020-08-04 09:49:05
 */
@Api(tags = PlatformSwaggerApiConfig.COMMON_TEMPLATE)
@RestController("commonTemplateController.v1")
@RequestMapping("/v1/{organizationId}/common-templates")
public class CommonTemplateController extends BaseController {
    /**
     * 通用模板应用服务对象
     */
    private final CommonTemplateService commonTemplateService;

    @Autowired
    public CommonTemplateController(CommonTemplateService commonTemplateService) {
        this.commonTemplateService = commonTemplateService;
    }

    @ApiOperation(value = "通用模板列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    @ProcessLovValue(targetField = FIELD_BODY)
    public ResponseEntity<Page<CommonTemplateDTO>> list(@PathVariable Long organizationId,
                                                        CommonTemplateQueryDTO queryDTO,
                                                        @ApiIgnore @SortDefault(value = CommonTemplate.FIELD_TEMPLATE_ID,
                                                                direction = Sort.Direction.DESC) PageRequest pageRequest) {
        queryDTO.setTenantId(organizationId);
        return Results.success(this.commonTemplateService.list(queryDTO, pageRequest));
    }

    @ApiOperation(value = "通用模板明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{templateId}")
    @ProcessLovValue(targetField = FIELD_BODY)
    public ResponseEntity<CommonTemplateDTO> detail(@PathVariable Long organizationId,
                                                    @PathVariable @Encrypt Long templateId) {
        return Results.success(this.commonTemplateService.detail(organizationId, templateId));
    }

    @ApiOperation(value = "创建通用模板")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    @ProcessLovValue(targetField = FIELD_BODY)
    public ResponseEntity<CommonTemplateDTO> create(@PathVariable Long organizationId,
                                                    @RequestBody CommonTemplateCreationDTO creationDTO) {
        creationDTO.setTenantId(organizationId);
        return Results.success(this.commonTemplateService.creation(creationDTO));
    }

    @ApiOperation(value = "修改通用模板")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping("/{templateId}")
    @ProcessLovValue(targetField = FIELD_BODY)
    public ResponseEntity<CommonTemplateDTO> update(@PathVariable Long organizationId,
                                                    @PathVariable @Encrypt Long templateId,
                                                    @RequestBody CommonTemplateUpdateDTO updateDTO) {
        return Results.success(this.commonTemplateService.update(organizationId, templateId, updateDTO));
    }

    @ApiOperation(value = "渲染模板")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/render")
    public ResponseEntity<RenderResult> render(@PathVariable Long organizationId,
                                               @RequestBody RenderParameterDTO parameter) {
        parameter.setTenantId(organizationId);
        return Results.success(this.commonTemplateService.render(parameter.getTenantId(), parameter.getTemplateCode(),
                parameter.getLang(), parameter.getArgs()));
    }
}
