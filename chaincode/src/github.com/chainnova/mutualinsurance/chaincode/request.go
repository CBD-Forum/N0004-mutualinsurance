package main

import (
	"encoding/json"
	"fmt"
	"strconv"
	"strings"
	"time"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
)

// buyInsurance buyInsurance
func (t *InsuranceChaincode) buyInsurance(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Printf("buyInsurance=%v\n", args)

	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments.")
	}

	txID := stub.GetTxID()

	user := args[0]
	body := []byte(args[1])
	var insuranceReq InsuranceReq
	err := json.Unmarshal(body, &insuranceReq)
	if err != nil {
		return shim.Error(err.Error())
	}
	fmt.Printf("buyInsurance: '%v'\n", insuranceReq)

	attributes := []string{insuranceReq.IDInsurance, insuranceReq.IDUser, txID}
	key, err := stub.CreateCompositeKey("buyInsurance", attributes)
	if err != nil {
		return shim.Error(err.Error())
	}

	err = stub.PutState(key, body)
	if err != nil {
		return shim.Error(err.Error())
	}

	// update insurance
	attributes = []string{insuranceReq.IDInsurance}
	key, err = stub.CreateCompositeKey("insurance", attributes)
	if err != nil {
		return shim.Error(err.Error())
	}
	value, err := stub.GetState(string(key))
	if err != nil {
		return shim.Error(err.Error())
	}
	if value == nil {
		return shim.Error("No such insurance")
	}
	var insurance Insurance
	err = json.Unmarshal(value, &insurance)
	if err != nil {
		return shim.Error(err.Error())
	}
	bought, err := strconv.ParseInt(insurance.CountBought, 10, 64)
	if err != nil {
		return shim.Error(err.Error())
	}
	insurance.CountBought = fmt.Sprintf("%v", bought+1)

	value, err = json.Marshal(insurance)
	if err != nil {
		return shim.Error(err.Error())
	}

	err = stub.PutState(string(key), value)
	if err != nil {
		return shim.Error(err.Error())
	}

	fmt.Printf("buyInsurance: updated insurance\n")

	// update user profile for cache
	attributes = []string{insuranceReq.IDUser}
	key, err = stub.CreateCompositeKey("profile", attributes)
	if err != nil {
		return shim.Error(err.Error())
	}

	value, err = stub.GetState(string(key))
	if err != nil {
		return shim.Error(err.Error())
	}

	var profile Profile
	if value != nil {
		err := json.Unmarshal(value, &profile)
		if err != nil {
			return shim.Error(err.Error())
		}
		// for _, i := range profile.Insurances {
		// 	if i.ID == insuranceReq.IDInsurance {
		// 		// TODO: update
		// 		return shim.Success(nil)
		// 	}
		// }
	}
	profile.ID = user
	amount, err := strconv.ParseFloat(insuranceReq.Amount, 64)
	if err != nil {
		return shim.Error(err.Error())
	}
	profile.Balance += amount
	profile.Insurances = append(profile.Insurances, insurance)
	value, err = json.Marshal(profile)
	if err != nil {
		return shim.Error(err.Error())
	}

	err = stub.PutState(string(key), value)
	if err != nil {
		return shim.Error(err.Error())
	}

	fmt.Printf("buyInsurance: updated profile '%v'\n", profile)

	// update statistics
	var month string
	timeBought := insuranceReq.TimeBought
	s := strings.Split(timeBought, "-")
	if len(s) > 1 {
		month = s[0] + "-" + s[1]
	} else {
		location, _ := time.LoadLocation("Asia/Chongqing")
		month = time.Now().In(location).Format("2006-01")
	}
	attributes = []string{insuranceReq.IDInsurance, month}
	key, err = stub.CreateCompositeKey("statistics", attributes)
	if err != nil {
		return shim.Error(err.Error())
	}

	value, err = stub.GetState(key)
	if err != nil {
		return shim.Error(err.Error())
	}

	var statistics InsuranceSta
	if value != nil {
		err := json.Unmarshal(value, &statistics)
		if err != nil {
			return shim.Error(err.Error())
		}
	}
	statistics.Month = month
	statistics.CountNew++
	value, err = json.Marshal(statistics)
	if err != nil {
		return shim.Error(err.Error())
	}

	err = stub.PutState(key, value)
	if err != nil {
		return shim.Error(err.Error())
	}

	fmt.Printf("buyInsurance: updated statistics '%v'\n", statistics)

	return shim.Success(nil)
}
