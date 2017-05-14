package main

import (
	"encoding/json"
	"fmt"
	"strconv"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
)

// getProfile getProfile
func (t *InsuranceChaincode) getProfile(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Printf("getProfile=%v\n", args)

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments.")
	}

	user := args[0]
	fmt.Printf("getProfile: user=%v\n", user)

	attributes := []string{user}
	keyProfile, err := stub.CreateCompositeKey("profile", attributes)
	if err != nil {
		return shim.Error(err.Error())
	}

	value, err := stub.GetState(keyProfile)
	if err != nil {
		return shim.Error(err.Error())
	}
	if value == nil {
		return shim.Success(nil)
	}

	// update insurances according to insurance id
	var profile Profile
	err = json.Unmarshal(value, &profile)
	if err != nil {
		return shim.Error(err.Error())
	}
	var insurances []Insurance
	for _, i := range profile.Insurances {
		attributes = []string{i.ID}
		key, err := stub.CreateCompositeKey("insurance", attributes)
		if err != nil {
			return shim.Error(err.Error())
		}
		value, err = stub.GetState(key)
		if err != nil {
			return shim.Error(err.Error())
		}
		var insurance Insurance
		err = json.Unmarshal(value, &insurance)
		if err != nil {
			return shim.Error(err.Error())
		}
		insurance.Bought = true
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
	profile.Insurances = insurances
	value, err = json.Marshal(profile)
	if err != nil {
		return shim.Error(err.Error())
	}

	err = stub.PutState(keyProfile, value)
	if err != nil {
		return shim.Error(err.Error())
	}

	return shim.Success(value)
}

func (t *InsuranceChaincode) charge(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Printf("charge=%v\n", args)

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments.")
	}

	body := []byte(args[0])
	var charge Charge
	err := json.Unmarshal(body, &charge)
	if err != nil {
		return shim.Error(err.Error())
	}

	attributes := []string{charge.IDUser}
	key, err := stub.CreateCompositeKey("profile", attributes)
	if err != nil {
		return shim.Error(err.Error())
	}
	value, err := stub.GetState(key)
	if err != nil {
		return shim.Error(err.Error())
	}
	if value == nil {
		return shim.Success(nil)
	}
	// update profile balance according to id
	var profile Profile
	err = json.Unmarshal(value, &profile)
	if err != nil {
		return shim.Error(err.Error())
	}

	amount, err := strconv.ParseFloat(charge.Amount, 64)
	if err != nil {
		return shim.Error(err.Error())
	}
	profile.Balance += amount
	value, err = json.Marshal(profile)
	if err != nil {
		return shim.Error(err.Error())
	}

	err = stub.PutState(key, value)
	if err != nil {
		return shim.Error(err.Error())
	}

	return shim.Success(value)
}
