package com.wallker.framework.tools.io;

import com.wallker.framework.constants.ResourceConstant;
import com.wallker.framework.tools.date.DateUtil;

/**
 * 日志输出基类
 *
 * @Filename: LogUtil.java
 * @Description:
 * @Version: 1.0
 * @Author: paat.com
 * @Email: dee.huang@PAAT.com
 * @History:<br>
 *<li>Author: dee.huang</li>
 *<li>Date: 2017年9月11日</li>
 *<li>Version: 1.0</li>
 *<li>Content: create</li>
 *
 */
public class LogUtil {

    /***
     * 获取格式化后的日志(无异常信息)
     * 可格式化MsgConstant.LOG_TEMPLATE_001 该日志模板
     * @param userId 当前操作人ID
     * @param module 模块
     * @param menu 菜单(页面)名称
     * @param action 操作信息
     * @param params 传入的参数
     * @return
     */
    public static String formatLog(Integer userId, String module, String menu, String action, String params) {
        return String.format(ResourceConstant.LOG_TEMPLATE_001, userId,
                DateUtil.timestamp2String(System.currentTimeMillis(), ResourceConstant.DATE_TIME_FORMAT),
                module, menu, action, params);
    }

    /***
     * 获取格式化后的日志(无异常信息)
     * 可格式化MsgConstant.LOG_TEMPLATE_001 该日志模板
     * @param userId 当前操作人ID
     * @param module 模块
     * @param menu 菜单(页面)名称
     * @param action 操作信息
     * @return
     */
    public static String formatLog(Integer userId, String module, String menu, String action) {
        return String.format(ResourceConstant.LOG_TEMPLATE_002, userId,
                DateUtil.timestamp2String(System.currentTimeMillis(), ResourceConstant.DATE_TIME_FORMAT),
                module, menu, action);
    }

    /***
     * 获取格式化后的日志(有异常信息)
     * 可格式化 ResourceConstant.LOG_TEMPLATE_001 该日志模板
     * @param userId 当前操作人ID
     * @param module 模块
     * @param menu 菜单(页面)名称
     * @param action 操作业务
     * @param e 异常信息
     * @return
     */
    public static String formatLog(Integer userId, String module, String menu, String action, Exception e) {
        return String.format(ResourceConstant.LOG_TEMPLATE_002, userId, DateUtil.timestamp2String(System.currentTimeMillis(), ResourceConstant.DATE_TIME_FORMAT), module, menu, action) + String.format("异常：e=%s, msg=%s", e, e.getMessage());
    }

//    public static  void main(String[]args) {
//        System.out.println(formatLog(1, "111", "11","111", "i=1"));
//        System.out.println(formatLog(1, "111", "11","111", new Exception("报错了")));
//        System.out.println(formatLog(1, "111", "11","111"));
//        System.out.println( LogUtil.formatLog(0,"ss","dd","ac", JSONObject.toJSONString("xxx="+56)));
//    }

}
