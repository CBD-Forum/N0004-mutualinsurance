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

// approveInsurance approveInsurance
func (t *InsuranceChaincode) approveInsurance(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Printf("approveInsurance=%v\n", args)

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments.")
	}

	// get claim
	body := []byte(args[0])
	var claimNew Claim
	err := json.Unmarshal(body, &claimNew)
	if err != nil {
		return shim.Error(err.Error())
	}

	keyByte, err := stub.GetState(claimNew.IDClaim)
	if err != nil {
		return shim.Error(err.Error())
	}

	var claim Claim
	claimByte, err := stub.GetState(string(keyByte))
	if err != nil {
		return shim.Error(err.Error())
	}
	if claimByte == nil {
		return shim.Error("No such insurance")
	}
	err = json.Unmarshal(claimByte, &claim)
	if err != nil {
		return shim.Error(err.Error())
	}

	// update insurance
	attributes := []string{claim.IDInsurance}
	key, err := stub.CreateCompositeKey("insurance", attributes)
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
	insurance.CountHelped++
	amountMax, err := strconv.ParseFloat(insurance.AmountMax, 64)
	if err != nil {
		return shim.Error(err.Error())
	}
	insurance.Amount += float64(amountMax)

	// update user profile: bought fee
	bought, err := strconv.ParseInt(insurance.CountBought, 10, 64)
	if err != nil {
		return shim.Error(err.Error())
	}
	feeAverage, err := strconv.ParseFloat(insurance.Fee, 64)
	if err != nil {
		return shim.Error(err.Error())
	}
	fee := amountMax/float64(bought) + feeAverage
	countQuit := 0
	attributes = []string{}
	rqc, err := stub.GetStateByPartialCompositeKey("profile", attributes)
	if err != nil {
		return shim.Error(err.Error())
	}
	for rqc.HasNext() {
		keyProfile, value, err := rqc.Next()
		if err != nil {
			return shim.Error(err.Error())
		}

		var profile Profile
		err = json.Unmarshal(value, &profile)
		if err != nil {
			return shim.Error(err.Error())
		}

		// distribute claims
		if profile.ID == claim.IDUser {
			// profile.Balance += claim.Amount
		}
		if profile.Balance > 0 && profile.Balance < fee {
			profile.Balance = 0
			balance, err := strconv.ParseFloat(insurance.Balance, 64)
			if err != nil {
				return shim.Error(err.Error())
			}
			balance -= fee - profile.Balance
			insurance.Balance = fmt.Sprintf("%v", balance)

			bought--
			insurance.CountBought = fmt.Sprintf("%v", bought)
			countQuit++
		} else {
			profile.CountHelped++
			profile.Fee += fee
		}

		value, err = json.Marshal(profile)
		if err != nil {
			return shim.Error(err.Error())
		}

		err = stub.PutState(keyProfile, value)
		if err != nil {
			return shim.Error(err.Error())
		}
	}

	value, err = json.Marshal(insurance)
	if err != nil {
		return shim.Error(err.Error())
	}

	err = stub.PutState(string(key), value)
	if err != nil {
		return shim.Error(err.Error())
	}

	// update statistics
	var month string
	timeApproved := claimNew.TimeApproved
	s := strings.Split(timeApproved, "-")
	if len(s) > 1 {
		month = s[0] + "-" + s[1]
	} else {
		location, _ := time.LoadLocation("Asia/Chongqing")
		month = time.Now().In(location).Format("2006-01")
	}
	attributes = []string{insurance.ID, month}
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
	statistics.CountHelped++

	statistics.Amount += amountMax
	statistics.Cost += feeAverage * float64(bought)
	statistics.Fee = fee
	statistics.CountQuit += countQuit
	value, err = json.Marshal(statistics)
	if err != nil {
		return shim.Error(err.Error())
	}

	err = stub.PutState(key, value)
	if err != nil {
		return shim.Error(err.Error())
	}

	// update claim
	claim.Amount = float64(amountMax)
	claim.Status = claimNew.Status
	claim.TimeApproved = claimNew.TimeApproved
	claim.Receipt = claimNew.Receipt
	claim.ReportThird = claimNew.ReportThird
	claim.ReportVisitor = claimNew.ReportVisitor

	claimByte, err = json.Marshal(claim)
	if err != nil {
		return shim.Error(err.Error())
	}
	err = stub.PutState(string(keyByte), claimByte)
	if err != nil {
		return shim.Error(err.Error())
	}
	fmt.Printf("approveInsurance: %v='%v'\n", keyByte, claim)

	return shim.Success(nil)
}
