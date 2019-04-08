package com.zq.shop.web.service.impl;

import com.zq.core.restful.ServerResponse;
import com.zq.shop.web.bean.DUser;
import com.zq.shop.web.bean.Sequence;
import com.zq.shop.web.common.Const;
import com.zq.shop.web.mappers.DUserMapper;
import com.zq.shop.web.mappers.SequenceMapper;
import com.zq.shop.web.service.IDUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @Author 张迁-zhangqian
 * @Data 2019/3/27 1:53 PM
 * @Package com.zq.shop.web.service.impl
 **/


@Service("iDUserService")
public class DUserServiceImpl implements IDUserService {

    @Autowired
    private DUserMapper dUserMapper;

    @Autowired
    private SequenceMapper idMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ServerResponse register(DUser dUser) {

        ServerResponse validResponse = this.available(dUser.getEmail(), Const.User.EMAIL);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        validResponse = this.available(dUser.getPhone(), Const.User.PHONE);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }


        dUser.setRole(Const.Role.CUSTOME_USER);
        //MD5加密
        dUser.setPassword(passwordEncoder.encode(dUser.getPassword()));
        //生成 用户ID
        Sequence sequence = idMapper.selectByPrimaryKey(1);
        int uid = sequence.getCurrentValue() + sequence.getIncrement();
        sequence.setCurrentValue(uid);
        idMapper.updateByPrimaryKeySelective(sequence);

        dUser.setUid(uid);
        dUser.setUsername(Const.User.DEFAULT_NAME + dUser.getUid());

        {
            dUser.setAge(0);
            dUser.setEmail("");
            dUser.setSex(0);
            dUser.setType(0);
            dUser.setRoll(0);
            dUser.setImage("http://39.106.46.79/images/product.png");
        }
        int resultCount = dUserMapper.insert(dUser);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }
        dUser.setPassword(null);
        return ServerResponse.createBySuccess("注册成功", dUser);
    }

    private ServerResponse available(String username, String type) {
        if (StringUtils.isNoneBlank(type)) {
            //开始校验
            if (Const.User.EMAIL.equals(type)) {
                if (StringUtils.isBlank(username)) return ServerResponse.createBySuccess();
                DUser resultData = dUserMapper.findOnebyEmail(username);
                if (resultData != null) {
                    return ServerResponse.createByErrorMessage("email已存在");
                }
            }
            if (Const.User.PHONE.equals(type)) {
                DUser resultData = dUserMapper.findOneByPhone(username);
                if (resultData != null) {
                    return ServerResponse.createByErrorMessage("手机号已存在");
                }
            }
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }


    @Override
    public ServerResponse getUserInfo(String username) {
        DUser dUser = dUserMapper.findOneByPhone(username);

        if (dUser == null) {
            return ServerResponse.createByErrorMessage("未获取用户信息");
        }
        dUser.setPassword(null);
        return ServerResponse.createBySuccess(dUser);
    }

    @Override
    public ServerResponse getUserInfo(Integer userId) {
        DUser dUser = dUserMapper.selectByPrimaryKey(userId);
        if (dUser == null) {
            return ServerResponse.createByErrorMessage("未获取用户信息");
        }
        dUser.setPassword(null);
        return ServerResponse.createBySuccess(dUser);
    }
}
