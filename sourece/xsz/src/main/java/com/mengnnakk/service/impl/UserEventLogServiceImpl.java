package  com.mengnnakk.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mengnnakk.entry.User;
import com.mengnnakk.entry.UserEventLog;
import com.mengnnakk.entry.other.KeyValue;
import com.mengnnakk.mapper.UserEventLogMapper;
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
public class UserEventLogServiceImpl extends ServiceImpl<UserEventLogMapper, UserEventLog> implements UserService {

    private final UserEventLogMapper userEventLogMapper;

    @Autowired
    public UserEventLogServiceImpl(UserEventLogMapper userEventLogMapper){
        this.userEventLogMapper = userEventLogMapper;
    }

    @Override
    public List<User> getUsers() {
        return null;
    }

    @Override
    public User getUserById(Integer id) {
        return null;
    }

    @Override
    public User getUserByUserName(String username) {
        return null;
    }

    @Override
    public User getUserByUserNamePwd(String username, String pwd) {
        return null;
    }

    @Override
    public User getUserByUuid(String uuid) {
        return null;
    }

    @Override
    public List<User> userPageList(String name, Integer pageIndex, Integer pageSize) {
        return null;
    }

    @Override
    public Integer userPageCount(String name) {
        return null;
    }

    @Override
    public PageInfo<User> userPage(UserPageRequestVM requestVM) {
        return null;
    }

    @Override
    public void insertUser(User user) {

    }

    @Override
    public void insertUsers(List<User> users) {

    }

    @Override
    public void updateUser(User user) {

    }

    @Override
    public void updateUsersAge(Integer age, List<Integer> ids) {

    }

    @Override
    public void deleteUserByIds(List<Integer> ids) {

    }

    @Override
    public Integer selectAllCount() {
        return null;
    }

    @Override
    public List<KeyValue> selectByUserName(String userName) {
        return null;
    }

    @Override
    public List<User> selectByIds(List<Integer> ids) {
        return null;
    }

    @Override
    public User selectByWxOpenId(String wxOpenId) {
        return null;
    }

    @Override
    public void changePicture(User user, String imagePath) {

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