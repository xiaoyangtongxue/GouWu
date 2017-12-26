package presenter;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import entity.ChildBean;
import entity.DataBean;
import entity.GroupBean;
import entity.MsgBean;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import model.Model;
import view.IView;


public class BasePresenter implements IPresenter{

    private IView iView;
    private DisposableSubscriber<MsgBean<List<DataBean>>> subscriber;

    public void attachView(IView iView){
        this.iView=iView;
    }

    @Override
    public void getData(Map<String, String> map) {
        Model model = new Model(this);
        model.getData(map);
    }

    public void detachView(){
        if(subscriber!=null){
            //如果该资源已被处理，则可一次性返回true。如果没有被处理返回false
            if(!subscriber.isDisposed()){
                subscriber.dispose();
            }
        }

    }

    public void get(Flowable<MsgBean<List<DataBean>>> flowable){
        subscriber = flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<MsgBean<List<DataBean>>>() {
                    @Override
                    public void onNext(MsgBean<List<DataBean>> dataBeanMsgBean) {
                        List<GroupBean> groupBeen = new ArrayList<>();
                        List<List<ChildBean>> childBeen = new ArrayList<>();

                        List<DataBean> data = dataBeanMsgBean.getData();
                        for (DataBean i : data) {
                            GroupBean groupBean = new GroupBean(i.getSellerName(), false);
                            groupBeen.add(groupBean);
                            List<DataBean.ListBean> list = i.getList();
                            List<ChildBean> ls = new ArrayList<>();
                            for (DataBean.ListBean w : list) {
                                String[] split = w.getImages().split("\\|");
                                ChildBean childBean = new ChildBean(w.getTitle(), split[0], w.getPrice(), 1, false, false);
                                ls.add(childBean);
                            }
                            childBeen.add(ls);

                        }
                        iView.OnSuccess(groupBeen,childBeen);
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });




    }

}
