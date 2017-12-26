package com.example.bwie.gouwuche;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.ExpandableAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import entity.ChildBean;
import entity.GroupBean;
import presenter.BasePresenter;
import view.IView;

public class MainActivity extends AppCompatActivity implements IView {



    @BindView(R.id.all_chekbox)
    public CheckBox allCheckbox;
    @BindView(R.id.exListView)
    ExpandableListView exListView;
    @BindView(R.id.total_price)
    TextView totalPrice;
    @BindView(R.id.total_number)
    TextView totalnumber;

    List<GroupBean> groupBeen = new ArrayList<>();
    List<List<ChildBean>> childBeen = new ArrayList<>();

    private ExpandableAdapter expandableAdapter;

    private boolean flagedit = true;
    private BasePresenter basePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //RX  retrofit
        basePresenter = new BasePresenter();
        basePresenter.attachView(this);
        Map<String, String> map1 = new HashMap<>();
        map1.put("uid", "3802");
        basePresenter.getData(map1);


        //获取二级列表适配器
        expandableAdapter = new ExpandableAdapter(MainActivity.this, groupBeen, childBeen);
        exListView.setAdapter(expandableAdapter);

        for (int i = 0; i < expandableAdapter.getGroupCount(); i++) {
            exListView.expandGroup(i);
        }

        exListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
        });

    }

    //计算和数量总价
    public void changesum(List<List<ChildBean>> childBeen) {
        int count = 0;
        double sum = 0;
        for (List<ChildBean> i1 : childBeen) {
            for (int r = 0; r < i1.size(); r++) {
                boolean childCb1 = i1.get(r).isChildCb();
                if (childCb1) {
                    double price = i1.get(r).getPrice();
                    int num = i1.get(r).getNum();
                    sum += price * num;
                    count++;
                }
            }
        }
        totalPrice.setText("￥" + sum);
        totalnumber.setText("共有商品:" + count + "件");
    }

    @OnClick({R.id.edit, R.id.all_chekbox})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.edit:
                for (List<ChildBean> i1 : childBeen) {
                    for (int r = 0; r < i1.size(); r++) {
                        i1.get(r).setBtn(flagedit);
                    }
                }
                flagedit = !flagedit;
                expandableAdapter.notifyDataSetChanged();
                break;
            case R.id.all_chekbox:
                boolean checked = allCheckbox.isChecked();
                for (GroupBean i : groupBeen) {
                    i.setGropuCb(checked);
                }
                for (List<ChildBean> i1 : childBeen) {
                    for (int r = 0; r < i1.size(); r++) {
                        i1.get(r).setChildCb(checked);
                    }
                }
                expandableAdapter.notifyDataSetChanged();
                changesum(childBeen);
                break;
        }
    }


    @Override
    public void OnSuccess(List<GroupBean> groupBeen, List<List<ChildBean>> childBeen) {
        this.groupBeen.addAll(groupBeen);
        this.childBeen.addAll(childBeen);
        for (int i = 0; i < expandableAdapter.getGroupCount(); i++) {
            exListView.expandGroup(i);
        }
        expandableAdapter.notifyDataSetChanged();
    }

    @Override
    public void OnFailed(Exception e) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(basePresenter!=null){
            basePresenter.detachView();
        }
    }
}
