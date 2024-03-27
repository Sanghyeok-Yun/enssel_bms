package com.enssel.bms.bi.controller;

import com.enssel.bms.bi.entity.CommonCode;
import com.enssel.bms.bi.service.CommonCodeService;
import com.enssel.bms.core.controller.AbstractController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/bi/common_code")
@RequiredArgsConstructor
public class CommonCodeController extends AbstractController {

    private final CommonCodeService commonCodeService;

    @GetMapping
    public List<CommonCode> getListAll(){
        return commonCodeService.getListAll();
    }

    @GetMapping("/{code}")
    public CommonCode getOneByCode(@PathVariable(name="code") String code) throws Exception {
        return commonCodeService.getOneByCode(code);
    }

    @GetMapping("/{pareCode}/childs")
    public List<CommonCode> getListByPare(@PathVariable(name="pareCode") String pareCode) throws Exception {
        return commonCodeService.getListByPareCode(pareCode);
    }

    @PostMapping
    public CommonCode postOne(@RequestBody @Valid CommonCode commonCode) throws BadRequestException {
        return commonCodeService.postOne(commonCode);
    }
}
