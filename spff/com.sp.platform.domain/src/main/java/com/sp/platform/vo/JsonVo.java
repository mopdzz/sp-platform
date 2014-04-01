package com.sp.platform.vo;

/**
 * Created with IntelliJ IDEA.
 * User: mopdgg
 * Date: 13-3-30
 * Time: 下午11:12
 * To change this template use File | Settings | File Templates.
 */
public class JsonVo{
    private boolean flag;
    private String msg;
    private Object result;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
