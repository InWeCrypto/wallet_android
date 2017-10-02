package capital.fbg.wallet.base;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

public abstract class BasePresenter<M extends BaseModel,V extends BaseView> {

    protected M mModel;

    protected V mView;

    protected Context mContext;

    public BasePresenter(M m,V v){

        this.mModel=m;
        this.mView = v;

        mView.setPresenter(this);

        initContext();

    }

    private void initContext(){

        if(mView instanceof Fragment){
            mContext = ((Fragment)mView).getActivity();
        }
        else {
            mContext = (Activity) mView;
        }
    }

    public abstract void start();

    public void onDestroy(){
        mView = null;
        if(mModel != null) {
            mModel.cancleTasks();
            mModel = null;
        }
    }

}
