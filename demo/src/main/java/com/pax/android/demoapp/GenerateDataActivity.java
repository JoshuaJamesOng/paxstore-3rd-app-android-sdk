package com.pax.android.demoapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.pax.market.android.app.sdk.StoreSdk;
import com.pax.market.api.sdk.java.base.dto.SdkObject;
import com.pax.market.api.sdk.java.base.exception.NotInitException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GenerateDataActivity extends Activity {
    private static final String TAG = GenerateDataActivity.class.getSimpleName();
    private List<DataBean> datas = new ArrayList<>();
    private Button generate;
    private ListView mDataListView;
    private GenerateAdapter mAapter;
    private View headerView;
    private View mLoading;
    private int mQueryCount = 0;
    private List<ChartData> ret = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_data);
        headerView = LayoutInflater.from(this).inflate(R.layout.list_header, null);
        generate = findViewById(R.id.generate);
        mDataListView = findViewById(R.id.data_list);
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadBizData();
            }
        });
        mAapter = new GenerateAdapter(generateFakeData(), this);
        mDataListView.setAdapter(mAapter);
        mDataListView.addHeaderView(headerView);
        mLoading = findViewById(R.id.pro_wrap);
    }


    List<DataBean> generateFakeData() {
        List<DataBean> list = new ArrayList<>();
        int typeCount = 0;
        int amountCount = 0;
        int dataCount = 0;
        for (int i = 0; i < 100; i++) {
            DataBean bean = new DataBean("Best Buy", "New York");
            bean.setOp_Number(randon9());
            bean.setPurchase_ID(randon14());
            list.add(bean);
        }

        for (typeCount = 0; typeCount < 100; typeCount++) {
            if (typeCount >= 0 && typeCount < 45) {
                list.get(typeCount).setAcquirer_Type("Master Card");
            } else if (typeCount >= 45 && typeCount < 69) {
                list.get(typeCount).setAcquirer_Type("VISA");
            } else if (typeCount >= 69 && typeCount < 81) {
                list.get(typeCount).setAcquirer_Type("American Express");
            } else if (typeCount >= 81 && typeCount < 85) {
                list.get(typeCount).setAcquirer_Type("JCB");
            } else if (typeCount >= 85 && typeCount < 87) {
                list.get(typeCount).setAcquirer_Type("Paypal");
            } else if (typeCount >= 87 && typeCount < 89) {
                list.get(typeCount).setAcquirer_Type("Stripe");
            } else if (typeCount >= 89 && typeCount < 93) {
                list.get(typeCount).setAcquirer_Type("UnionPay");
            } else if (typeCount >= 93 && typeCount < 94) {
                list.get(typeCount).setAcquirer_Type("DC");
            } else if (typeCount >= 94 && typeCount < 95) {
                list.get(typeCount).setAcquirer_Type("AE");
            } else if (typeCount >= 95 && typeCount < 98) {
                list.get(typeCount).setAcquirer_Type("Wechat");
            } else if (typeCount >= 98 && typeCount < 100) {
                list.get(typeCount).setAcquirer_Type("AliPay");
            }
        }


        for (amountCount = 0; amountCount < 100; amountCount++) {
            if (amountCount >= 0 && amountCount < 12) {
                list.get(amountCount).setAmount(String.valueOf(randonRange(20, 1)));
            } else if (amountCount >= 12 && amountCount < 29) {
                list.get(amountCount).setAmount(String.valueOf(randonRange(40, 21)));
            } else if (amountCount >= 29 && amountCount < 64) {
                list.get(amountCount).setAmount(String.valueOf(randonRange(60, 41)));
            } else if (amountCount >= 64 && amountCount < 88) {
                list.get(amountCount).setAmount(String.valueOf(randonRange(80, 61)));
            } else if (amountCount >= 88 && amountCount < 96) {
                list.get(amountCount).setAmount(String.valueOf(randonRange(100, 81)));
            } else if (amountCount >= 96 && amountCount < 100) {
                list.get(amountCount).setAmount(String.valueOf(randonRange(1000, 100)));
            }
        }


        for (dataCount = 0; dataCount < 100; dataCount++) {
            if (dataCount >= 0 && dataCount < 10) {
                Date date = randomDate("2019-01-01", "2019-01-31");
                list.get(dataCount).setPurchase_Time(new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(date));
            } else if (dataCount >= 10 && dataCount < 20) {
                Date date = randomDate("2019-02-01", "2019-02-31");
                list.get(dataCount).setPurchase_Time(new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(date));
            } else if (dataCount >= 20 && dataCount < 30) {
                Date date = randomDate("2019-03-01", "2019-03-31");
                list.get(dataCount).setPurchase_Time(new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(date));
            } else if (dataCount >= 30 && dataCount < 40) {
                Date date = randomDate("2019-04-01", "2019-04-31");
                list.get(dataCount).setPurchase_Time(new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(date));
            } else if (dataCount >= 40 && dataCount < 50) {
                Date date = randomDate("2019-05-01", "2019-05-31");
                list.get(dataCount).setPurchase_Time(new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(date));
            } else if (dataCount >= 50 && dataCount < 60) {
                Date date = randomDate("2019-06-01", "2019-06-31");
                list.get(dataCount).setPurchase_Time(new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(date));
            } else if (dataCount >= 60 && dataCount < 70) {
                Date date = randomDate("2019-07-01", "2019-07-31");
                list.get(dataCount).setPurchase_Time(new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(date));
            } else if (dataCount >= 70 && dataCount < 80) {
                Date date = randomDate("2019-08-01", "2019-08-31");
                list.get(dataCount).setPurchase_Time(new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(date));
            } else if (dataCount >= 80 && dataCount < 90) {
                Date date = randomDate("2019-09-01", "2019-09-31");
                list.get(dataCount).setPurchase_Time(new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(date));
            } else if (dataCount >= 90 && dataCount < 100) {
                Date date = randomDate("2019-10-01", "2019-10-31");
                list.get(dataCount).setPurchase_Time(new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(date));
            }
        }

        return list;
    }


    String randon9() {
        Random random = new Random(1000);//指定种子数字
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 9; i++) {
            int item = random.nextInt(10);
            sb.append(item);
        }
        return sb.toString();

    }

    String randon14() {
        Random random = new Random(1000);//指定种子数字
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 13; i++) {
            int item = random.nextInt(10);
            sb.append(item);
        }
        return sb.toString();
    }

    int randonRange(int max, int min) {
        Random random = new Random(1000);//指定种子数字
        int randNumber = random.nextInt(max - min + 1) + min;
        return randNumber;
    }


    private Date randomDate(String beginDate, String endDate) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date start = format.parse(beginDate);
            Date end = format.parse(endDate);
            if (start.getTime() >= end.getTime()) {
                return null;
            }
            long date = random(start.getTime(), end.getTime());
            return new Date(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private static long random(long begin, long end) {
        long rtn = begin + (long) (Math.random() * (end - begin));
        if (rtn == begin || rtn == end) {
            return random(begin, end);
        }
        return rtn;
    }


    private void uploadBizData() {

        mLoading.setVisibility(View.VISIBLE);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    datas = mAapter.getmList();
                    List<Map<String, String>> list = new ArrayList<>();
                    for (int i = 0; i < datas.size(); i++) {
                        Map<String, String> map = new HashMap<>();
                        map.put("merchantname", datas.get(i).getMerchant());
                        map.put("resellername", datas.get(i).getReseller());
                        map.put("acquirertype", datas.get(i).getAcquirer_Type());
                        map.put("amount", datas.get(i).getAmount());
                        map.put("purchasetime", datas.get(i).getPurchase_Time());
                        map.put("opnumber", datas.get(i).getOp_Number());
                        map.put("purchaseid", datas.get(i).getPurchase_ID());
                        list.add(map);
                    }
                    final SdkObject SyncObject = StoreSdk.getInstance().goInsightApi().syncTerminalBizData(list);
                    Log.d(TAG, "code>>" + SyncObject.getBusinessCode() + "   message>>" + SyncObject.getMessage());
                } catch (NotInitException e) {
                    Log.e(TAG, "e:" + e);
                }
            }
        });

        thread.start();
    }

}
