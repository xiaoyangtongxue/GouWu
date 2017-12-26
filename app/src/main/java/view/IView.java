package view;


import java.util.List;

import entity.ChildBean;
import entity.GroupBean;

public interface IView {

    void OnSuccess(List<GroupBean> groupBeen, List<List<ChildBean>> childBeen);
    void OnFailed(Exception e);
}
