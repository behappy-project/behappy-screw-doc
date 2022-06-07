package org.xiaowu.behappy.screw.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.xiaowu.behappy.screw.entity.Dict;
import org.xiaowu.behappy.screw.mapper.DictMapper;

/**
 * @author xiaowu
 */
@Service
public class DictService extends ServiceImpl<DictMapper,Dict> implements IService<Dict> {
}
