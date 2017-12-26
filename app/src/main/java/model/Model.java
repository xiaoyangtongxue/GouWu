package model;


import java.util.List;
import java.util.Map;

import entity.DataBean;
import entity.MsgBean;
import http.RetrofitUtils;
import io.reactivex.Flowable;
import presenter.BasePresenter;


public class Model implements IModel{

    private BasePresenter presenter;

    public Model(BasePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getData(Map<String, String> map) {
        Flowable<MsgBean<List<DataBean>>> flowable = RetrofitUtils.getInstance().getApiService().getGoods(map);
        presenter.get(flowable);
    }
}
