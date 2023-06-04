package com.record.security.service;

import com.record.security.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author OR
 * @since 2023-06-03
 */
public interface UserService extends IService<User> {

    User findByEmail(String email);

}
