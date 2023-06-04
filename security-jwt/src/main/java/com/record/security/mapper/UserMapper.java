package com.record.security.mapper;

import com.record.security.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author OR
 * @since 2023-06-03
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
