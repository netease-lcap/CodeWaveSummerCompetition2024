package com.netease.lowcode.lib.officetopdf.web.controller;

import com.netease.lowcode.lib.officetopdf.api.TransferApi;
import com.netease.lowcode.lib.officetopdf.dto.ApiReturn;
import com.netease.lowcode.lib.officetopdf.dto.FileTransferRequest;
import com.netease.lowcode.lib.officetopdf.dto.ResultDTO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/expand/transfer")
public class TransferController {
    @Resource
    private TransferApi transferApi;

    /**
     * 文件转pdf
     *
     * @param request
     * @return 文件异步获取key
     */
    @PostMapping("/to_transfer")
    public ApiReturn<String> pptTransfer(@RequestBody FileTransferRequest request) {
        try {
            if (request.getType() == null) {
                request.setType(1);
            }
            if (request.getType() == 1) {
                return ApiReturn.of(transferApi.pptTransfer(request.getFileUrl()), 200, "success");
            }
            return null;
        } catch (Exception e) {
            return ApiReturn.of(null, -1, e.getMessage());
        }
    }

    /**
     * 获取转换结果
     *
     * @param key 文件异步获取key
     * @return
     */
    @GetMapping("/get_result")
    public ApiReturn<ResultDTO> getResult(@RequestParam("key") String key) {
        try {
            return ApiReturn.of(transferApi.getTransferResult(key));
        } catch (Exception e) {
            return ApiReturn.of(null, -1, e.getMessage());
        }
    }

}