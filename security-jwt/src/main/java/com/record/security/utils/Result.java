package com.record.security.utils;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 公共返回对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Result",description = "公共返回对象")
public class Result {

    @Schema(description = "状态码")
    private long code;
    @Schema(description = "信息")
    private String message;
    @Schema(description = "数据")
    private Object data;

    /**
     * 成功返回结果
     * @param message
     * @return
     */
    public static Result success(String message){
        return new Result(200,message,null);
    }

    /**
     * 成功返回结果
     * @param message
     * @param obj
     * @return
     */
    public static Result success(String message, Object obj){
        return new Result(200,message,obj);
    }

    /**
     * 失败返回结果
     * @param message
     * @return
     */
    public static Result error(String message){
        return new Result(500,message,null);
    }

    /**
     * 失败返回结果
     * @param message
     * @param obj
     */
    public static Result error(String message, Object obj){
        return new Result(500,message,obj);
    }

}
