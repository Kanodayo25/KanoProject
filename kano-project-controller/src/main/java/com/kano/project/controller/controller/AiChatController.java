package com.kano.project.controller.controller;

import com.kano.project.common.model.Result;
import com.kano.project.controller.controller.vo.RagChatReqVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.AiService;

/**
 * AI 问答接口：普通对话 + RAG 知识库问答
 */
@Api(tags = "AI 问答", value = "ai")
@RestController
@RequestMapping("/ai")
@Validated
@Slf4j
public class AiChatController {

    @Reference
    private AiService aiService;

    @ApiOperation("知识库问答（RAG），可附带图片 imageUrl")
    @PostMapping("/chat/rag")
    public Result<String> chatRag(@RequestBody @Valid RagChatReqVO reqVO) {
        Result<String> result = aiService.doChatWithRag(
                reqVO.getMemoryId(), reqVO.getMessage(), reqVO.getCollectionName(), reqVO.getTopK(), reqVO.getImageUrl());
        if (result.isSuccess()) {
            return Result.success(result.getData());
        }
        return Result.fail(result.getMsg());
    }

    @ApiOperation("普通对话")
    @PostMapping("/chat")
    public Result<String> chat(@RequestParam("memoryId") Long memoryId,
                               @RequestParam("message") String message) {
        Result<String> result = aiService.doChat(memoryId, message, null);
        if (result.isSuccess()) {
            return Result.success(result.getData());
        }
        return Result.fail(result.getMsg());
    }
}
