package main

import (
	"encoding/json"
	"fmt"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
)

// claimInsurance claimInsurance
func (t *InsuranceChaincode) claimInsurance(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Printf("claimInsurance=%v\n", args)

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments.")
	}

	// TODO: check duplication
	txID := stub.GetTxID()

	body := []byte(args[0])
	var claim Claim
	err := json.Unmarshal(body, &claim)
	if err != nil {
		return shim.Error(err.Error())
	}
	claim.IDClaim = txID
	claim.Status = "claimed"
	fmt.Printf("claimInsurance: '%v'\n", claim)

	// check bought
	attributes := []string{claim.IDInsurance, claim.IDUser}
	rqi, err := stub.GetStateByPartialCompositeKey("buyInsurance", attributes)
	if err != nil {
		return shim.Error(err.Error())
	} else if !rqi.HasNext() {
		return shim.Error("Have NOT bought")
	}

	// claim
	attributes = []string{claim.IDInsurance, claim.IDUser, txID}
	key, err := stub.CreateCompositeKey("claimInsurance", attributes)
	if err != nil {
		return shim.Error(err.Error())
	}
	err = stub.PutState(txID, []byte(key))
	if err != nil {
		return shim.Error(err.Error())
	}

	claimByte, err := json.Marshal(claim)
	if err != nil {
		return shim.Error(err.Error())
	}
	err = stub.PutState(key, claimByte)
	if err != nil {
		return shim.Error(err.Error())
	}

	return shim.Success(nil)
}

// getClaim getClaim
func (t *InsuranceChaincode) getClaim(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Printf("getClaim=%v\n", args)

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments.")
	}

	key, err := stub.GetState(args[0])
	if err != nil {
		return shim.Error(err.Error())
	}

	value, err := stub.GetState(string(key))
	if err != nil {
		return shim.Error(err.Error())
	}

	return shim.Success(value)
}
