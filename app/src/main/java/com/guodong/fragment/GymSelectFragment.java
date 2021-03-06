package com.guodong.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.guodong.R;
import com.guodong.activity.GymSelectActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class GymSelectFragment extends Fragment
{
    private GymSelectActivity gymSelectActivity = null;
    GridView gridView;
    View view;
    MyAdapter myAdapter;
    ArrayList<HashMap<String, Object>> arrayList;

    int[] orderState = null;
    //new int[(num + 1) * timeNum];

    //已选择的场地数目，选择多于4块即弹出提示
    int selectedNum = 0;

    String openTime = "";
    String orderTime = "";
    String reserveDay = "";
    int fieldNum = 0;
    float singlePrice = 0;
    boolean isFirstLoad = true;
    int nowDay = 0;
    int nowHour = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_order_gym, container, false);
        gridView = (GridView) view.findViewById(R.id.order_positon_gridview);

        arrayList = new ArrayList<>();
        getOpenTime();
        getOrderTime();
        getReserveDay();
        getFieldNum();
        getSinglePrice();
        getIsFirstLoad();
        getOrderState();

        int beginTime = Integer.parseInt(openTime.substring(0, 2));
        int endTime = Integer.parseInt(openTime.substring(6, 8));

        if (orderState == null)
        {
            orderState = new int[(fieldNum + 1) * (endTime - beginTime + 2)];
            Arrays.fill(orderState, 1);
        }

        //用来保存每个场地的预订状态0:ordered,1:unselected,2:selected,-1:表示为顶部或左部的文字区域


        //设置场地的预订状态,实际中，根据商家端预订结果和时间过期信息来修改
//        Calendar calendar = Calendar.getInstance();
//        int nowHour = calendar.get(Calendar.HOUR_OF_DAY) + 1;

        Time t = new Time();
        t.setToNow();
        nowDay = t.monthDay;
        nowHour = t.hour;
//        Toast.makeText(getActivity(), "now hour:" + nowHour, Toast.LENGTH_LONG).show();

        int reserveMonthDay = Integer.parseInt(reserveDay.substring(8));
        for (int i = 0; i < orderState.length; i++)
        {
            //顶部和左部的内容是显示字符区域
            if (i / (fieldNum + 1) < 1 || i % (fieldNum + 1) == 0)
            {
                orderState[i] = -1;
            } else if (!isFirstLoad && orderState[i] == 1 && nowDay == reserveMonthDay)
            {
                if (nowHour >= beginTime && (i / (fieldNum + 1) <= (nowHour - beginTime + 1) && i / (fieldNum + 1) >= 1))
                {
                    orderState[i] = 0;
                }
            }
        }
        for (int i = 0; i < orderState.length; i++)
        {
            switch (orderState[i])
            {
                case -1:
                {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("image", R.drawable.order_item_side);
                    if (i / (fieldNum + 1) < 1 & i > 0)
                    {
                        hashMap.put("text", "场地" + (i));
                    } else if (i % (fieldNum + 1) == 0 && i > 0)
                    {
                        hashMap.put("text", (i / (fieldNum + 1) + beginTime - 1) + ":00");
                    }

                    arrayList.add(hashMap);
                    break;
                }
                case 0:
                {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("image", R.drawable.order_item_ordered);
                    hashMap.put("text", "");
                    arrayList.add(hashMap);
                    break;
                }
                case 1:
                {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("image", R.drawable.order_item_unselected);
                    hashMap.put("text", "￥" + singlePrice);
                    arrayList.add(hashMap);
                    break;
                }
                case 2:
                {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("image", R.drawable.order_item_selected);
                    hashMap.put("text", "￥" + singlePrice);
                    arrayList.add(hashMap);
                    break;
                }
                default:
                    break;
            }
        }


        myAdapter = new MyAdapter(arrayList, getActivity());
        gridView.setAdapter(myAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                myAdapter.setSelection(position);


                if (selectedNum > 0 && orderState[position] != 2)
                {
                    Toast.makeText(getActivity(), "选择场地数目太多，请分开下单", Toast.LENGTH_SHORT).show();
                }
                //如果点击ordered的场地，提示该场地已经预订
                if (orderState[position] == 0)
                {
                    Toast.makeText(getActivity(), "该场地已预定", Toast.LENGTH_SHORT).show();
                } else if (orderState[position] == 1 && selectedNum <= 3)
                {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("image", R.drawable.order_item_selected);
                    hashMap.put("text", "￥" + singlePrice);
                    arrayList.set(position, hashMap);

                    orderState[position] = 2;
                    selectedNum++;
                } else if (orderState[position] == 2)
                {

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("image", R.drawable.order_item_unselected);
                    hashMap.put("text", "￥" + singlePrice);
                    arrayList.set(position, hashMap);

                    orderState[position] = 1;
                    selectedNum--;
                }

                myAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    public class MyAdapter extends BaseAdapter
    {
        ArrayList<HashMap<String, Object>> arrayList;
        Context context;
        int selectItem = -1;

        public MyAdapter(ArrayList<HashMap<String, Object>> arrayList, Context context)
        {
            this.arrayList = arrayList;
            this.context = context;
        }

        public void setSelection(int position)
        {
            this.selectItem = position;
        }

        @Override
        public int getCount()
        {
            if (arrayList == null)
            {
                return 0;
            } else
            {
                return arrayList.size();
            }
        }

        @Override
        public Object getItem(int position)
        {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.order_position_item, parent, false);
            ImageView imageView = (ImageView) view.findViewById(R.id.order_position_image);
            TextView textView = (TextView) view.findViewById(R.id.order_position_text);

            HashMap hashMap = (HashMap<String, Object>) getItem(position);

            imageView.setImageResource((Integer) hashMap.get("image"));
            textView.setText((CharSequence) hashMap.get("text"));

            if (selectItem == position)
            {
            }

            return view;
        }
    }

    public GymSelectFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        gymSelectActivity = (GymSelectActivity) activity;
    }

    public void setOrderState()
    {
        gymSelectActivity.setOrderState(orderState);
    }

    public void getSinglePrice()
    {
        singlePrice = gymSelectActivity.getPrice();
    }

    public void getIsFirstLoad()
    {
        isFirstLoad = gymSelectActivity.getIsFirstLoad();
    }

    public void getOpenTime()
    {
        openTime = gymSelectActivity.getOpenTime();
    }

    public void getOrderTime()
    {
        orderTime = gymSelectActivity.getOrderTime();
    }

    public void getReserveDay()
    {
        reserveDay = gymSelectActivity.getReserveDayStr();
    }

    public void getFieldNum()
    {
        fieldNum = gymSelectActivity.getFieldNum();
    }

    public void getOrderState()
    {
        orderState = gymSelectActivity.getOrderState();
    }
}

