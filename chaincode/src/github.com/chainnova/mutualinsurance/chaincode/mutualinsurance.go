package main

import (
	"fmt"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
)

type Profile struct {
	ID          string      `protobuf:"bytes,1,opt,name=id" json:"id"`
	Balance     float64     `protobuf:"bytes,2,opt,name=balance" json:"balance"`
	CountHelped int         `protobuf:"bytes,3,opt,name=count_helped" json:"count_helped"`
	Fee         float64     `protobuf:"bytes,4,opt,name=fee" json:"fee"`
	Insurances  []Insurance `protobuf:"bytes,5,opt,name=insurances" json:"insurances"`
}

type Charge struct {
	IDUser string `protobuf:"bytes,1,opt,name=id_user" json:"id_user"`
	Amount string `protobuf:"bytes,2,opt,name=amount" json:"amount"`
}

type Insurance struct {
	ID          string         `protobuf:"bytes,1,opt,name=id" json:"id"`
	Name        string         `protobuf:"bytes,2,opt,name=name" json:"name"`
	TimeFounded string         `protobuf:"bytes,3,opt,name=time_founded" json:"time_founded"`
	Description []string       `protobuf:"bytes,4,opt,name=description" json:"description"`
	Issuer      string         `protobuf:"bytes,5,opt,name=issuer" json:"issuer"`
	Source      string         `protobuf:"bytes,6,opt,name=source" json:"source"`
	AmountMax   string         `protobuf:"bytes,7,opt,name=amount_max" json:"amount_max"`
	CountBought string         `protobuf:"bytes,8,opt,name=count_bought" json:"count_bought"`
	CountHelped int            `protobuf:"bytes,9,opt,name=count_helped" json:"count_helped"`
	Balance     string         `protobuf:"bytes,10,opt,name=balance" json:"balance"`
	Amount      float64        `protobuf:"bytes,11,opt,name=amount" json:"amount"`
	Fee         string         `protobuf:"bytes,12,opt,name=fee" json:"fee"`
	Bought      bool           `protobuf:"bytes,13,opt,name=bought" json:"bought"`
	Claims      []Claim        `protobuf:"bytes,14,opt,name=claims" json:"claims"`
	Statistics  []InsuranceSta `protobuf:"bytes,15,opt,name=statistics" json:"statistics"`
}

type InsuranceSta struct {
	Month       string  `protobuf:"bytes,1,opt,name=month" json:"month"`
	CountNew    int     `protobuf:"bytes,2,opt,name=count_new" json:"count_new"`
	CountQuit   int     `protobuf:"bytes,3,opt,name=count_quit" json:"count_quit"`
	CountHelped int     `protobuf:"bytes,4,opt,name=count_helped" json:"count_helped"`
	Amount      float64 `protobuf:"bytes,5,opt,name=amount" json:"amount"`
	Cost        float64 `protobuf:"bytes,6,opt,name=cost" json:"cost"`
	Fee         float64 `protobuf:"bytes,7,opt,name=fee" json:"fee"`
}

type InsuranceReq struct {
	IDInsurance string `protobuf:"bytes,1,opt,name=id_insurance" json:"id_insurance,omitempty"`
	IDUser      string `protobuf:"bytes,2,opt,name=id_user" json:"id_user,omitempty"`
	Name        string `protobuf:"bytes,3,opt,name=name" json:"name,omitempty"`
	IDCard      string `protobuf:"bytes,4,opt,name=id_card" json:"id_card,omitempty"`
	Mobile      string `protobuf:"bytes,5,opt,name=mobile" json:"mobile,omitempty"`
	Amount      string `protobuf:"bytes,6,opt,name=amount" json:"amount,omitempty"` //float64
	Medical     string `protobuf:"bytes,7,opt,name=medical" json:"medical,omitempty"`
	IDDriver    string `protobuf:"bytes,8,opt,name=id_driver" json:"id_driver,omitempty"`
	IDDriving   string `protobuf:"bytes,9,opt,name=id_driving" json:"id_driving,omitempty"`
	IDDidi      string `protobuf:"bytes,10,opt,name=id_didi" json:"id_didi,omitempty"`
	IDCsa       string `protobuf:"bytes,11,opt,name=id_csa" json:"id_csa,omitempty"`
	TimeBought  string `protobuf:"bytes,12,opt,name=time_bought" json:"time_bought,omitempty"`
	Reserved    string `protobuf:"bytes,13,opt,name=reserved" json:"reserved,omitempty"`
}

type Claim struct {
	IDInsurance   string  `protobuf:"bytes,1,opt,name=id_insurance" json:"id_insurance"`
	IDUser        string  `protobuf:"bytes,2,opt,name=id_user" json:"id_user"`
	IDClaim       string  `protobuf:"bytes,3,opt,name=id_claim" json:"id_claim"`
	Name          string  `protobuf:"bytes,4,opt,name=name" json:"name"`
	Mobile        string  `protobuf:"bytes,5,opt,name=mobile" json:"mobile"`
	CardNum       string  `protobuf:"bytes,6,opt,name=cardnum" json:"cardnum"`
	Status        string  `protobuf:"bytes,7,opt,name=status" json:"status"`
	Amount        float64 `protobuf:"bytes,8,opt,name=amount" json:"amount"`
	ReportThird   string  `protobuf:"bytes,9,opt,name=report_third" json:"report_third"`
	ReportVisitor string  `protobuf:"bytes,10,opt,name=report_visitor" json:"report_visitor"`
	Receipt       string  `protobuf:"bytes,11,opt,name=receipt" json:"receipt"`
	TimeClaimed   string  `protobuf:"bytes,12,opt,name=time_claimed" json:"time_claimed"`
	TimeApproved  string  `protobuf:"bytes,13,opt,name=time_approved" json:"time_approved"`
}

// InsuranceChaincode mutual insurance Chaincode implementation
type InsuranceChaincode struct {
}

// Init ...
func (t *InsuranceChaincode) Init(stub shim.ChaincodeStubInterface) pb.Response {
	fmt.Println("########### insurance Init ###########")
	_, _ = stub.GetFunctionAndParameters()

	if transientMap, err := stub.GetTransient(); err == nil {
		if transientData, ok := transientMap["result"]; ok {
			fmt.Printf("Transient data in 'init' : %s\n", transientData)
			return shim.Success(transientData)
		}
	}
	return shim.Success(nil)
}

// Invoke ...
func (t *InsuranceChaincode) Invoke(stub shim.ChaincodeStubInterface) pb.Response {
	fmt.Println("########### insurance Invoke ###########")
	function, args := stub.GetFunctionAndParameters()

	switch function {
	case "publishInsurance":
		return t.publishInsurance(stub, args)
	case "getInsurances":
		return t.getInsurances(stub, args)
	case "getInsurance":
		return t.getInsurance(stub, args)
	case "buyInsurance":
		return t.buyInsurance(stub, args)
	case "claimInsurance":
		return t.claimInsurance(stub, args)
	case "approveInsurance":
		return t.approveInsurance(stub, args)
	case "getClaim":
		return t.getClaim(stub, args)
	case "getProfile":
		return t.getProfile(stub, args)
	case "charge":
		return t.charge(stub, args)
	default:
		return shim.Error("Unknown function: " + function)
	}
}

func main() {
	err := shim.Start(new(InsuranceChaincode))
	if err != nil {
		fmt.Printf("Error starting Simple chaincode: %s", err)
	}
}
