#!/bin/bash

FABRIC_URL=http://23.91.105.47:7059/v1
#FABRIC_URL=http://120.92.90.145:7059/v1
USERNAME=张三
PASSWORD=123
FABRIC_RESPONSE=.fabric_res

insurance1() {
  echo -e "\033[32m insurance1()\033[0m"
  INSURANCE_POST_DATA='
  {
        "id": "",
        "name": "上海滴滴司机互助",
        "time_founded": "2017-01-01",
        "description": [
          "驾乘意外伤害",
          "驾乘意外医疗",
          "驾驶员，乘客在滴滴接单期间发生事故的意外事故",
          "上海滴滴司机",
          "余额小于0",
          "3天（用于审核资料）",
          "滴滴",
          "d8",
          "d9",
          "d10"
        ],
        "issuer": "由上海滴滴司机联盟发起保障",
        "source": "100000",
        "amount_max": "300000",
        "count_bought": "235000",
        "count_helped": 0,
        "balance": "1615450",
        "amount": 4395450,
        "fee": "0.01",
        "bought": false
      }
  '
  echo $INSURANCE_POST_DATA
  curl --basic -u $MOBILE:$PASSWORD -l -H "Content-type: application/json" -X POST --data "$INSURANCE_POST_DATA" $FABRIC_URL/insurance > $FABRIC_RESPONSE
  res_str=`cat $FABRIC_RESPONSE`
  echo $res_str
  insurance_message=`echo $res_str | sed 's/.*"message":\([^,}]*\).*/\1/' | sed 's/\"//g'`
  echo "insurance_message:"$insurance_message
  echo $insurance_message > .insurance1
}

insurance2() {
  echo -e "\033[32m insurance2()\033[0m"
  INSURANCE_POST_DATA='
  {
        "id": "",
        "name": "中青年抗癌互助",
        "time_founded": "2017-01-01",
        "description": [
          "胃癌、肝癌等各种癌症",
          "18-50岁",
          "胃癌、肝癌等各种癌症",
          "18-50岁，身体健康",
          "余额小于0",
          "90天（防止带病加入）",
          "无",
          "d8",
          "d9",
          "d10"
        ],
        "issuer": "由抗癌公社发起保障",
        "source": "0",
        "amount_max": "300000",
        "count_bought": "1523000",
        "count_helped": 0,
        "balance": "8120890",
        "amount": 11880890,
        "fee": "0.02",
        "bought": false
      }
  '
  echo $INSURANCE_POST_DATA
  curl --basic -u $MOBILE:$PASSWORD -l -H "Content-type: application/json" -X POST --data "$INSURANCE_POST_DATA" $FABRIC_URL/insurance > $FABRIC_RESPONSE
  res_str=`cat $FABRIC_RESPONSE`
  echo $res_str
  insurance_message=`echo $res_str | sed 's/.*"message":\([^,}]*\).*/\1/' | sed 's/\"//g'`
  echo "insurance_message:"$insurance_message
  echo $insurance_message > .insurance2
}

insurance3() {
  echo -e "\033[32m insurance3()\033[0m"
  INSURANCE_POST_DATA='
  {
        "id": "",
        "name": "南航金卡延误互助",
        "time_founded": "2017-01-01",
        "description": [
          "南航金卡会员",
          "当年延误超过15次",
          "购买后1年内南航明珠俱乐部金卡会员在南航乘坐航班累计延误超过15次",
          "南航明珠俱乐部金卡会员",
          "余额小于0",
          "0天",
          "南方航空",
          "d8",
          "d9",
          "d10"
        ],
        "issuer": "由南航明珠俱乐部发起保障",
        "source": "200000",
        "amount_max": "10000",
        "count_bought": "6500",
        "count_helped": 0,
        "balance": "224540",
        "amount": 324540,
        "fee": "0.03",
        "bought": false
      }
  '
  echo $INSURANCE_POST_DATA
  curl --basic -u $MOBILE:$PASSWORD -l -H "Content-type: application/json" -X POST --data "$INSURANCE_POST_DATA" $FABRIC_URL/insurance > $FABRIC_RESPONSE
  res_str=`cat $FABRIC_RESPONSE`
  echo $res_str
  insurance_message=`echo $res_str | sed 's/.*"message":\([^,}]*\).*/\1/' | sed 's/\"//g'`
  echo "insurance_message:"$insurance_message
  echo $insurance_message > .insurance3
}

insuranceget() {
  echo -e "\033[32m insuranceget()\033[0m"
  curl --basic -u $MOBILE:$PASSWORD $FABRIC_URL/insurance > $FABRIC_RESPONSE
  cat $FABRIC_RESPONSE
  res_str=`cat $FABRIC_RESPONSE`
  echo $res_str
  id=`echo $res_str | sed 's/.*"id":\([^,}]*\).*/\1/' | sed 's/\"//g'`
  echo "id:"$id
}


login() {
  echo -e "\033[32m login()\033[0m"
  curl --basic -u $MOBILE:$PASSWORD $FABRIC_URL/login > $FABRIC_RESPONSE
  cat $FABRIC_RESPONSE
}

me() {
  echo -e "\033[32m me()\033[0m"
  curl --basic -u $MOBILE:$PASSWORD $FABRIC_URL/me > $FABRIC_RESPONSE
  res_str=`cat $FABRIC_RESPONSE`
  echo $res_str
  id_insurance=`echo $res_str | sed 's/.*"id_insurance":\([^,}]*\).*/\1/' | sed 's/\"//g'`
  echo "id_insurance:"$id_insurance
  id_claim=`echo $res_str | sed 's/.*"id_claim":\([^,}]*\).*/\1/' | sed 's/\"//g'`
  echo "id_claim:"$id_claim
}

charge() {
  echo -e "\033[32m charge()\033[0m"
  CHARGE_POST_DATA='
  {
    "id": "'${MOBILE}'",
    "charge": "22"
  }
  '
  echo $CHARGE_POST_DATA
  curl --basic -u $MOBILE:$PASSWORD -l -H "Content-type: application/json" -X POST --data "$CHARGE_POST_DATA" $FABRIC_URL/charge > $FABRIC_RESPONSE
  res_str=`cat $FABRIC_RESPONSE`
  echo $res_str
  charge_message=`echo $res_str | sed 's/.*"message":\([^,}]*\).*/\1/' | sed 's/\"//g'`
  echo "charge_message:"$charge_message
}

#1 id_insurance
#2 time_bought
request() {
  echo -e "\033[32m request()\033[0m"
  REQUEST_POST_DATA='
  {
    "id_insurance": "'$1'",
    "id_user": "'${MOBILE}'",
    "name": "'${USERNAME}'",
    "idcard": "string",
    "mobile": "'${MOBILE}'",
    "amount": "88",
    "medical": "string",
    "id_driver": "string",
    "id_driving": "string",
    "id_didi": "string",
    "id_csa": "string",
    "time_bought": "'$2'",
    "reserved": "string"
  }
  '
  echo $REQUEST_POST_DATA
  curl --basic -u $MOBILE:$PASSWORD -l -H "Content-type: application/json" -X POST --data "$REQUEST_POST_DATA" $FABRIC_URL/request > $FABRIC_RESPONSE
  res_str=`cat $FABRIC_RESPONSE`
  echo $res_str
  request_message=`echo $res_str | sed 's/.*"message":\([^,}]*\).*/\1/' | sed 's/\"//g'`
  echo "request_message:"$request_message
}

#1 id_insurance
#2 time_claimed
claim() {
  echo -e "\033[32m claim()\033[0m"
  CLAIM_POST_DATA='
  {
    "id_insurance": "'$1'",
    "id_user": "'${MOBILE}'",
    "name": "'${USERNAME}'",
    "mobile": "'${MOBILE}'",
    "cardnum": "string",
    "time_claimed": "'$2'"
  }
  '
  echo $CLAIM_POST_DATA
  curl --basic -u $MOBILE:$PASSWORD -l -H "Content-type: application/json" -X POST --data "$CLAIM_POST_DATA" $FABRIC_URL/claim > $FABRIC_RESPONSE
  res_str=`cat $FABRIC_RESPONSE`
  echo $res_str
  claim_message=`echo $res_str | sed 's/.*"message":\([^,}]*\).*/\1/' | sed 's/\"//g'`
  echo "claim_message:"$claim_message
}

#1 id_insurance
#2 time_approved
approve() {
  echo -e "\033[32m approve()\033[0m"
  APPROVE_POST_DATA='
  {
    "id_insurance": "'$1'",
    "id_user": "'${MOBILE}'",
    "id_claim": "'$claim_message'",
    "status": "approved",
    "amount": '${amount_max}',
    "report_third": "由上海张三保险评估公司调查",
    "report_visitor": "由会员李四调查，确认属实",
    "receipt": "string",
    "time_approved": "'$2'"
  }
  '
  echo $APPROVE_POST_DATA
  curl --basic -u $MOBILE:$PASSWORD -l -H "Content-type: application/json" -X POST --data "$APPROVE_POST_DATA" $FABRIC_URL/approve > $FABRIC_RESPONSE
  res_str=`cat $FABRIC_RESPONSE`
  echo $res_str
  approve_message=`echo $res_str | sed 's/.*"message":\([^,}]*\).*/\1/' | sed 's/\"//g'`
  echo "approve_message:"$approve_message
}

#1 id_insurance
#2 time_claimed
#3 time_bought
#4 time_approved
buyclaimsapprove(){
 sleep 5
 request $1 $3
 sleep 5
 claim $1 $2
 sleep 5
 approve $1 $4
}

addinsurance(){
  insurance1
  insurance2
  insurance3
}

baoxian1() {
  id_insurance=`cat .insurance1`
  amount_max=300000
  echo "id_insurance:"$id_insurance
  echo "amount_max:"$amount_max
  USERNAME=张先生
  MOBILE=13344445555
  buyclaimsapprove $id_insurance "2017-02-03" "2017-01-03" "2017-02-28"
  USERNAME=张女士
  MOBILE=14455556666
  buyclaimsapprove $id_insurance "2017-03-10" "2017-02-10" "2017-03-31"
  USERNAME=张先生
  MOBILE=13344445555
  buyclaimsapprove $id_insurance "2017-03-11" "2017-02-11" "2017-03-31"
  USERNAME=张女士
  MOBILE=14455556666
  buyclaimsapprove $id_insurance "2017-03-15" "2017-02-15" "2017-03-31"
  USERNAME=张先生
  MOBILE=13344445555
  buyclaimsapprove $id_insurance "2017-04-07" "2017-03-07" "2017-04-30"
  USERNAME=张女士
  MOBILE=14455556666
  buyclaimsapprove $id_insurance "2017-04-11" "2017-03-11" "2017-04-30"
  USERNAME=张先生
  MOBILE=13344445555
  buyclaimsapprove $id_insurance "2017-04-13" "2017-03-13" "2017-04-30"
  USERNAME=张女士
  MOBILE=14455556666
  buyclaimsapprove $id_insurance "2017-04-15" "2017-03-15" "2017-04-30"
  USERNAME=张先生
  MOBILE=13344445555
  buyclaimsapprove $id_insurance "2017-04-21" "2017-03-21" "2017-04-30"
}

baoxian2() {
  id_insurance=`cat .insurance2`
  amount_max=300000
  echo "id_insurance:"$id_insurance
  echo "amount_max:"$amount_max
  USERNAME=李先生
  MOBILE=15566667777
  buyclaimsapprove $id_insurance "2017-02-10" "2017-01-10" "2017-04-30"
  USERNAME=李女士
  MOBILE=16677778888
  buyclaimsapprove $id_insurance "2017-02-10" "2017-01-10" "2017-04-30"
  USERNAME=李先生
  MOBILE=15566667777
  buyclaimsapprove $id_insurance "2017-02-11" "2017-01-11" "2017-04-30"
  USERNAME=李女士
  MOBILE=16677778888
  buyclaimsapprove $id_insurance "2017-02-13" "2017-02-13" "2017-04-30"
  USERNAME=李先生
  MOBILE=15566667777
  buyclaimsapprove $id_insurance "2017-03-17" "2017-02-17" "2017-04-30"
  USERNAME=李女士
  MOBILE=16677778888
  buyclaimsapprove $id_insurance "2017-03-17" "2017-02-17" "2017-04-30"
  USERNAME=李先生
  MOBILE=15566667777
  buyclaimsapprove $id_insurance "2017-03-18" "2017-02-18" "2017-04-30"
  USERNAME=李女士
  MOBILE=16677778888
  buyclaimsapprove $id_insurance "2017-03-19" "2017-02-19" "2017-04-30"
  USERNAME=李先生
  MOBILE=15566667777
  buyclaimsapprove $id_insurance "2017-04-20" "2017-03-20" "2017-04-30"
  USERNAME=李女士
  MOBILE=16677778888
  buyclaimsapprove $id_insurance "2017-04-22" "2017-03-22" "2017-04-30"
  USERNAME=李先生
  MOBILE=15566667777
  buyclaimsapprove $id_insurance "2017-04-23" "2017-03-23" "2017-04-30"
  USERNAME=李女士
  MOBILE=16677778888
  buyclaimsapprove $id_insurance "2017-04-24" "2017-03-24" "2017-04-30"
}

baoxian3() {
  id_insurance=`cat .insurance3`
  amount_max=10000
  echo "id_insurance:"$id_insurance
  echo "amount_max:"$amount_max
  USERNAME=王先生
  MOBILE=17788889999
  buyclaimsapprove $id_insurance "2017-02-27" "2017-01-27" "2017-02-28"
  USERNAME=王女士
  MOBILE=18899990000
  buyclaimsapprove $id_insurance "2017-03-10" "2017-02-10" "2017-03-31"
  USERNAME=王先生
  MOBILE=17788889999
  buyclaimsapprove $id_insurance "2017-03-11" "2017-02-11" "2017-03-13"
  USERNAME=王女士
  MOBILE=18899990000
  buyclaimsapprove $id_insurance "2017-03-23" "2017-02-23" "2017-03-31"
  USERNAME=王先生
  MOBILE=17788889999
  buyclaimsapprove $id_insurance "2017-04-17" "2017-03-17" "2017-04-30"
  USERNAME=王女士
  MOBILE=18899990000
  buyclaimsapprove $id_insurance "2017-04-17" "2017-03-17" "2017-04-30"
}

if [ "$1" = "init" ]; then
  MOBILE=18812345678
  addinsurance
elif [ "$1" = "run" ]; then
  baoxian1
  baoxian2
  baoxian3
else
  echo "Usage: <init|run>"
fi