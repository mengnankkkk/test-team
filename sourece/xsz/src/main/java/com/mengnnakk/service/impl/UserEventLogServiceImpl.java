package  com.mengnnakk.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mengnnakk.entry.User;
import com.mengnnakk.entry.UserEventLog;
import com.mengnnakk.entry.other.KeyValue;
import com.mengnnakk.mapper.UserEventLogMapper;
import com.mengnnakk.service.UserEventLogService;
import com.mengnnakk.service.UserService;
import com.mengnnakk.utility.DateTimeUtil;
import com.mengnnakk.viewmodel.admin.user.UserEventPageRequestVM;
import com.mengnnakk.viewmodel.admin.user.UserPageRequestVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserEventLogServiceImpl extends ServiceImpl<UserEventLogMapper, UserEventLog> implements UserEventLogService {

    private final UserEventLogMapper userEventLogMapper;

    @Autowired
    public UserEventLogServiceImpl(UserEventLogMapper userEventLogMapper){
        this.userEventLogMapper = userEventLogMapper;
    }

    @Override
    public List<UserEventLog> getUserEventLogByUserId(Integer id) {
        return userEventLogMapper.getUserEventLogByUserId(id);
    }

    @Override
    public PageInfo<UserEventLog> page(UserEventPageRequestVM requestVM){
        return PageHelper.startPage(requestVM.getPageIndex(),requestVM.getPageSize(),"id desc").doSelectPageInfo(()->
                userEventLogMapper.page(requestVM)
        );
    }
    @Override
    public List<Integer> selectMothCount(){
        Date startTime = DateTimeUtil.getMonthStartDay();
        Date endTime = DateTimeUtil.getMonthEndDay();
        List<KeyValue> monthCount = userEventLogMapper.selectCountByDate(startTime,endTime);
        List<String> mothStartToNowFormat = DateTimeUtil.MothStartToNowFormat();
        return mothStartToNowFormat.stream().map(md->{
            KeyValue keyValue = monthCount.stream().filter(kv->kv.getName().equals(md)).findAny().orElse(null);
            return null == keyValue ? 0 : keyValue.getValue();
        }).collect(Collectors.toList());

    }

}