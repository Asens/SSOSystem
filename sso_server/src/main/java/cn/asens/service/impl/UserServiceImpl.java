package cn.asens.service.impl;

import cn.asens.dao.UserDao;
import cn.asens.entity.User;
import cn.asens.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

}
