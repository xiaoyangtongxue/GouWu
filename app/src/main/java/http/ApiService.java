package http;


import java.util.List;
import java.util.Map;

import entity.DataBean;
import entity.MsgBean;
import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;



public interface ApiService {
    //http://120.27.23.105/product/getCarts?uid=3802&source=android
    @GET("product/getCarts")
    Flowable<MsgBean<List<DataBean>>> getGoods(@QueryMap Map<String, String> map);
}
