package capital.fbg.wallet.ui.wallet.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.zxing.WriterException;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.math.BigInteger;

import butterknife.BindView;
import capital.fbg.wallet.R;
import capital.fbg.wallet.base.BaseActivity;
import capital.fbg.wallet.bean.CountBean;
import capital.fbg.wallet.bean.TokenBean;
import capital.fbg.wallet.bean.TransferABIBean;
import capital.fbg.wallet.bean.TransferBean;
import capital.fbg.wallet.bean.WalletBean;
import capital.fbg.wallet.common.Constant;
import capital.fbg.wallet.common.http.LzyResponse;
import capital.fbg.wallet.common.http.api.WalletApi;
import capital.fbg.wallet.common.http.callback.JsonCallback;
import capital.fbg.wallet.common.util.QuanCodeUtils;
import capital.fbg.wallet.common.util.ToastUtil;
import capital.fbg.wallet.event.BaseEventBusBean;
import capital.fbg.wallet.event.KeyEvent;
import capital.fbg.wallet.ui.ScanActivity;
import me.drakeet.materialdialog.MaterialDialog;
import me.grantland.widget.AutofitTextView;

/**
 * Created by Administrator on 2017/7/27.
 * 功能描述：
 * 版本：@version
 */

public class WatchTokenTransferAccountsConfirmActivity extends BaseActivity {
    @BindView(R.id.txt_left_title)
    TextView txtLeftTitle;
    @BindView(R.id.txt_main_title)
    TextView txtMainTitle;
    @BindView(R.id.txt_right_title)
    TextView txtRightTitle;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_service_charge)
    TextView tvServiceCharge;
    @BindView(R.id.et_address)
    AutofitTextView etAddress;
    @BindView(R.id.et_hit)
    TextView etHit;
    @BindView(R.id.tv_transfer)
    TextView tvTransfer;

    private WalletBean wallet;
    private TokenBean.ListBean gnt;
    private String address;
    private String price;
    private String gas;
    private String hit;
    private MaterialDialog mMaterialDialog;

    private String oxPrice;
    private String oxGas;
    private BigDecimal pEther = new BigDecimal("1000000000000000000");

    @Override
    protected void getBundleExtras(Bundle extras) {
        gnt= (TokenBean.ListBean) extras.getSerializable("gnt");
        address = extras.getString("address");
        price = extras.getString("price");
        oxPrice = "0x" + new BigInteger(new BigDecimal(price).multiply(pEther).setScale(0,BigDecimal.ROUND_HALF_UP).toPlainString(),10).toString(16);
        gas = extras.getString("gas");
        oxGas = "0x" + new BigInteger(new BigDecimal(gas).multiply(pEther).divide(new BigDecimal(gnt.getGnt_category().getGas()), 0,BigDecimal.ROUND_HALF_UP).toPlainString(),10).toString(16);
        hit = extras.getString("hit");
        wallet = (WalletBean) extras.getSerializable("wallet");
    }

    @Override
    protected int setLayoutID() {
        return R.layout.wallet_activity_transfer_accounts_confirm;
    }

    @Override
    protected void initView() {
        txtLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtMainTitle.setText(R.string.transfer_confirm);
        txtRightTitle.setVisibility(View.GONE);
        tvTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();
                transferWatch();
            }
        });
    }

    @Override
    protected void initData() {
        tvPrice.setText(new BigDecimal(price).setScale(4,BigDecimal.ROUND_HALF_UP).toPlainString());
        tvServiceCharge.setText(getString(R.string.transfer_hit1) + gas);
        etAddress.setText(address);
        etHit.setText(hit);
    }

    private String nonce;
    private String hash;
    private Bitmap bitmap;

    private void transferWatch() {

        WalletApi.transactionCount(mActivity,wallet.getAddress(), new JsonCallback<LzyResponse<CountBean>>() {
            @Override
            public void onSuccess(Response<LzyResponse<CountBean>> response) {
                hideLoading();
                nonce=response.body().data.getCount();
                WalletApi.transferABI(mActivity, gnt.getGnt_category().getAddress(), address, oxPrice, new JsonCallback<LzyResponse<TransferABIBean>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<TransferABIBean>> response) {
                        transfer(response.body().data.getData());
                    }

                    @Override
                    public void onError(Response<LzyResponse<TransferABIBean>> response) {
                        super.onError(response);
                        ToastUtil.show("转账失败请重试");
                    }
                });
            }

            @Override
            public void onError(Response<LzyResponse<CountBean>> response) {
                super.onError(response);
                ToastUtil.show(getString(R.string.load_error));
                hideLoading();
            }
        });
    }

    private void transfer(String data) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.view_dialog_watch_code, null, false);
        final ImageView code= (ImageView) view.findViewById(R.id.code);
        TextView ok= (TextView) view.findViewById(R.id.ok);
        code.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) code.getLayoutParams();
                params.height=code.getMeasuredWidth();
                code.setLayoutParams(params);
            }
        });
        final TransferBean codeJson=new TransferBean(wallet.getAddress()
                ,nonce
                ,oxGas
                ,gnt.getGnt_category().getAddress()
                ,data
                ,price
                ,gas
                ,"0x"+new BigInteger(gnt.getGnt_category().getGas(),10).toString(16)
                ,hit
                ,2);
        new Thread(new Runnable() {
            @Override
            public void run() {
                //文本类型
                try {
                    bitmap = QuanCodeUtils.createQuanCode(mActivity,new Gson().toJson(codeJson));
                    code.post(new Runnable() {
                        @Override
                        public void run() {
                            code.setImageBitmap(bitmap);
                        }
                    });
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keepTogo(ScanActivity.class);
            }
        });

        mMaterialDialog = new MaterialDialog(mActivity).setView(view);
        mMaterialDialog.setBackgroundResource(R.drawable.trans_bg);
        mMaterialDialog.setCanceledOnTouchOutside(true);
        mMaterialDialog.show();
    }

    @Override
    protected void EventBean(BaseEventBusBean event) {
        if (event.getEventCode()== Constant.EVENT_KEY){
            KeyEvent key= (KeyEvent) event.getData();
                if (null==key.getKey()||key.getKey().length()==0||!key.getKey().contains("0x")){
                    ToastUtil.show("请扫描正确的二维码");
                    return;
                }
                mMaterialDialog.dismiss();
            Intent intent=new Intent(this,WatchTransferAccountsConfirmActivity.class);
            intent.putExtra("isGnt",true);
            intent.putExtra("wallet",wallet);
            intent.putExtra("data",key.getKey());
            intent.putExtra("payAddress",gnt.getGnt_category().getAddress());
            intent.putExtra("address",address);
            intent.putExtra("hint",hit);
            intent.putExtra("price",price);
            intent.putExtra("gas",gas);
            intent.putExtra("gnt",gnt.getName());
            finshTogo(intent);
        }
    }
}
