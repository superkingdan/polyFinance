package com.jnshu.entity;


import com.jnshu.utils.payutils.ConfigReader;

public class Constants
{
	/**商户号 */
	public static final String H5_MCHNT_CD = ConfigReader.getString("h5_mchnt_cd");
	/**商户密钥 */
	public static final String H5_MCHNT_KEY = ConfigReader.getString("h5_mchnt_key");
	/**商户号 */
	public static final String API_MCHNT_CD = ConfigReader.getString("api_mchnt_cd");
	/**商户密钥 */
	public static final String API_MCHNT_KEY = ConfigReader.getString("api_mchnt_key");
	/**响应吗：成功 */
	public static final String RESP_CODE_SUCCESS = "0000";
	/**响应吗：已支付 */
	public static final String RESP_CODE_PAIED = "5185";
	/** 省份证验证请求地址*/
	public static final String CHECK_ID_REQ_URL = ConfigReader.getString("check_id_req_url");
	
	public static final String CHECK_CARD_1_REQ_URL = ConfigReader.getString("check_card_req_url");
	
	public static final String CHECK_CARD_2_REQ_URL = ConfigReader.getString("check_card_2_req_url");
	
	public static final String CHECK_CARD_3_REQ_URL = ConfigReader.getString("check_card_3_req_url");
	
	public static final String CREATE_ORDER_REQ_URL_V15 = ConfigReader.getString("create_order_req_url_v15");
	public static final String PAY_ORDER_REQ_URL_V15 = ConfigReader.getString("pay_order_req_url_v15");
	
	public static final String PAY_BACKURL_V15 = ConfigReader.getString("pay_backurl_v15");
	public static final String PAY_REURL_V15 = ConfigReader.getString("pay_reurl_v15");
	public static final String PAY_HOMEURL_V15 = ConfigReader.getString("pay_homeurl_v15");
	public static final String PAY_COMMON_REQ_URL = ConfigReader.getString("h5.pay_common_req_url");
	public static final String FUIOU_PUB_KEY= ConfigReader.getString("fuiou_pub_key");

}
