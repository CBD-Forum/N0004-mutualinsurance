package main

import (
	"encoding/json"
	"fmt"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
)

// publishInsurance publishInsurance
func (t *InsuranceChaincode) publishInsurance(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Printf("publishInsurance=%v\n", args)

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments.")
	}

	// TODO: check duplication
	txID := stub.GetTxID()

	body := []byte(args[0])
	var insurance Insurance
	err := json.Unmarshal(body, &insurance)
	if err != nil {
		return shim.Error(err.Error())
	}
	insurance.ID = txID
	fmt.Printf("publishInsurance: '%v'\n", insurance)

	attributes := []string{txID}
	key, err := stub.CreateCompositeKey("insurance", attributes)
	if err != nil {
		return shim.Error(err.Error())
	}
	err = stub.PutState(txID, []byte(key))
	if err != nil {
		return shim.Error(err.Error())
	}

	insuranceByte, err := json.Marshal(insurance)
	if err != nil {
		return shim.Error(err.Error())
	}
	err = stub.PutState(key, insuranceByte)
	if err != nil {
		return shim.Error(err.Error())
	}

	return shim.Success(nil)
}

// getInsurances getInsurances
func (t *InsuranceChaincode) getInsurances(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Printf("getInsurances=%v\n", args)

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments.")
	}

	// get profile
	user := args[0]
	mapInsurances := make(map[string]bool, 0)
	attributes := []string{user}
	key, err := stub.CreateCompositeKey("profile", attributes)
	if err != nil {
		return shim.Error(err.Error())
	}

	value, err := stub.GetState(string(key))
	if err != nil {
		return shim.Error(err.Error())
	}

	var profile Profile
	if value != nil {
		err := json.Unmarshal(value, &profile)
		if err != nil {
			return shim.Error(err.Error())
		}
		for _, i := range profile.Insurances {
			mapInsurances[i.ID] = true
		}
	}

	// get insurances
	rqi, err := stub.GetStateByPartialCompositeKey("insurance", nil)
	if err != nil {
		return shim.Error(err.Error())
	}

	var insurances []Insurance
	for rqi.HasNext() {
		_, value, err := rqi.Next()
		if err != nil {
			return shim.Error(err.Error())
		}

		var insurance Insurance
		err = json.Unmarshal(value, &insurance)
		if err != nil {
			return shim.Error(err.Error())
		}

		_, ok := mapInsurances[insurance.ID]
		if ok {
			insurance.Bought = true
		}

		// Claim
		claims := make([]Claim, 0)
		attributes := []string{insurance.ID}
		rqc, err := stub.GetStateByPartialCompositeKey("claimInsurance", attributes)
		if err != nil {
			return shim.Error(err.Error())
		}
		for rqc.HasNext() {
			_, value, err := rqc.Next()
			if err != nil {
				return shim.Error(err.Error())
			}

			var claim Claim
			err = json.Unmarshal(value, &claim)
			if err != nil {
				return shim.Error(err.Error())
			}
			claims = append(claims, claim)
		}
		insurance.Claims = claims

		// statistics
		statistics := make([]InsuranceSta, 0)
		attributes = []string{insurance.ID}
		rqc, err = stub.GetStateByPartialCompositeKey("statistics", attributes)
		if err != nil {
			return shim.Error(err.Error())
		}
		for rqc.HasNext() {
			_, value, err := rqc.Next()
			if err != nil {
				return shim.Error(err.Error())
			}

			var sta InsuranceSta
			err = json.Unmarshal(value, &sta)
			if err != nil {
				return shim.Error(err.Error())
			}
			statistics = append(statistics, sta)
		}
		insurance.Statistics = statistics

		insurances = append(insurances, insurance)
	}

	body, err := json.Marshal(insurances)
	if err != nil {
		return shim.Error(err.Error())
	}

	return shim.Success(body)
}

// getInsurance getInsurance
func (t *InsuranceChaincode) getInsurance(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Printf("getInsurance=%v\n", args)

	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments.")
	}

	// get profile
	mapInsurances := make(map[string]bool, 0)
	attributes := []string{args[0]}
	keyProfile, err := stub.CreateCompositeKey("profile", attributes)
	if err != nil {
		return shim.Error(err.Error())
	}

	value, err := stub.GetState(keyProfile)
	if err != nil {
		return shim.Error(err.Error())
	}

	var profile Profile
	if value != nil {
		err := json.Unmarshal(value, &profile)
		if err != nil {
			return shim.Error(err.Error())
		}
		for _, i := range profile.Insurances {
			if i.ID == args[1] {
				mapInsurances[args[1]] = true
				break
			}
		}
	}

	// get insurance
	key, err := stub.GetState(args[1])
	if err != nil {
		return shim.Error(err.Error())
	}

	value, err = stub.GetState(string(key))
	if err != nil {
		return shim.Error(err.Error())
	}

	if value == nil {
		return shim.Success(nil)
	}

	var insurance Insurance
	err = json.Unmarshal(value, &insurance)
	if err != nil {
		return shim.Error(err.Error())
	}

	_, ok := mapInsurances[insurance.ID]
	if ok {
		insurance.Bought = true
	}

	// Claim
	claims := make([]Claim, 0)
	attributes = []string{insurance.ID}
	rqc, err := stub.GetStateByPartialCompositeKey("claimInsurance", attributes)
	if err != nil {
		return shim.Error(err.Error())
	}
	for rqc.HasNext() {
		_, value, err := rqc.Next()
		if err != nil {
			return shim.Error(err.Error())
		}

		var claim Claim
		err = json.Unmarshal(value, &claim)
		if err != nil {
			return shim.Error(err.Error())
		}
		claims = append(claims, claim)
	}
	insurance.Claims = claims

	// statistics
	statistics := make([]InsuranceSta, 0)
	attributes = []string{insurance.ID}
	rqc, err = stub.GetStateByPartialCompositeKey("statistics", attributes)
	if err != nil {
		return shim.Error(err.Error())
	}
	for rqc.HasNext() {
		_, value, err := rqc.Next()
		if err != nil {
			return shim.Error(err.Error())
		}

		var sta InsuranceSta
		err = json.Unmarshal(value, &sta)
		if err != nil {
			return shim.Error(err.Error())
		}
		statistics = append(statistics, sta)
	}
	insurance.Statistics = statistics

	// Serialize
	value, err = json.Marshal(insurance)
	if err != nil {
		return shim.Error(err.Error())
	}

	return shim.Success(value)
}
