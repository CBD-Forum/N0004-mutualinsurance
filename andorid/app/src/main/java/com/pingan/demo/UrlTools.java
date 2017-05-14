package com.pingan.demo;

public class UrlTools {


    public final static int DEV = 0;

    public final static int STG = 1;

    public final static int PRD = 2;
    /**
     * 测试服务器地址
     */
    private final static String HOSTIP_STG = "http://23.91.105.47:7059/v1";
    //    public static int MODEL = STG;
    //    public static int MODEL = DEV;
    /**
     * 生产服务器地址
     */
    private final static String HOSTIP_PRD = "http://120.92.90.145:7059/v1";
    /**
     * 开发服务器地址
     */
    private final static String HOSTIP_DEV = "http://10.43.47.196:5984/v1";
    /**
     * 模式选择： PRD生产模式，STG测试模式, DEV开发模式
     */
    public static int MODEL = PRD;
    /************
     * User
     ************/
    //入参：basic auth 出参：Error || ProfileRes
    public static String GET_LOGIN = getHost() + "/login";
    //入参：basic auth && id(理赔编号) 出参：Error || ClaimRes
    public static String GET_CLAIM_DETAIL = getHost() + "/claim/";//+{id}理赔详情
    //入参：basic auth 出参：Error || ProfileRes
    public static String GET_ME_DETAIL = getHost() + "/me";//获取用户详情
    //入参：basic auth 出参：Error || ProfileRes
    public static String POST_CHARGE = getHost() + "/charge";//充值个人账户
    /************
     * Insurances
     ************/
    //入参：basic auth 出参：Error || InsurancesRes
    public static String GET_INSURANCE = getHost() + "/insurance";//获取保险列表
    //入参：basic auth && Insurance 出参：OK||Error
    public static String POST_INSURANCE = getHost() + "/insurance";//发布保险
    //入参：basic auth && id(保险编号) 出参：Error || InsuranceRes
    public static String GET_INSURANCE_DETAIL = getHost() + "/insurance/";//+{id}获取保险详情
    //入参：basic auth && InsuranceReq 出参：OK||Error
    public static String POST_REQUEST = getHost() + "/request";//购买保险
    /************
     * Claims
     ************/
    //入参：basic auth && ClaimReq 出参：OK||Error
    public static String POST_CLAIM = getHost() + "/claim";//申请理赔
    //入参：basic auth && ClaimReq 出参：OK||Error
    public static String POST_APPROVE = getHost() + "/approve";//批准理赔
    /************
     * BlockChain
     ************/
    //入参：basic auth 出参：Error || Peers
    public static String GET_PEERS = getHost() + "/network/peers";//获取区块链网络节点信息
    //入参：basic auth 出参：Error || Blockchaininfo
    public static String GET_CHAIN = getHost() + "/chain";//获取区块链信息
    //入参：basic auth && id 出参：Error || Block
    public static String GET_CHAIN_BLOCKS = getHost() + "/chain/blocks/";//+{id}获取区块信息
    //入参：basic auth && id 出参：Error || Transaction
    public static String GET_TRANSACTIONS = getHost() + "/transactions/";//+{id}获取交易信息

    public static String getHost() {
        String result = "";
        switch (MODEL) {
            case DEV: {
                result = HOSTIP_DEV;
            }
            break;
            case STG: {
                result = HOSTIP_STG;
            }
            break;
            case PRD: {
                result = HOSTIP_PRD;
            }
            break;
            default:
                break;
        }
        return result;
    }
}
